package cn.gmj.taotao.search.service;

import cn.gmj.taotao.common.Utils.JsonUtils;
import cn.gmj.taotao.item.common.pojo.*;
import cn.gmj.taotao.search.Repository.GoodsRepository;
import cn.gmj.taotao.search.client.BrandClient;
import cn.gmj.taotao.search.client.CategoryClient;
import cn.gmj.taotao.search.client.GoodsClient;
import cn.gmj.taotao.search.client.SpecificationClient;
import cn.gmj.taotao.search.pojo.Goods;
import cn.gmj.taotao.search.pojo.SearchRequest;
import cn.gmj.taotao.search.pojo.SearchResult;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchService {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private SpecificationClient specClient;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public Goods buildGoods(Spu spu) {
        // 查询分类
        List<Category> categories = categoryClient.queryCategoryByCids(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        if (CollectionUtils.isEmpty(categories))
            throw new RuntimeException("查询分类名称失败");
        List<String> categoryNames = categories.stream().map(Category::getName).collect(Collectors.toList());
        // 查询品牌
        Brand brand = brandClient.queryBrandByBid(spu.getBrandId());
        if (brand == null)
            throw new RuntimeException("查询品牌名称失败");
        // 搜索字段
        String all = spu.getTitle() + StringUtils.join(categoryNames, " ") + brand.getName();

        // 查询sku
        List<Sku> skus = goodsClient.querySkusBySpuId(spu.getId());
        if (CollectionUtils.isEmpty(skus))
            throw new RuntimeException("查询Spu失败");
        Set<Long> prices = new HashSet<>();
        List<Map<String, Object>> simpleSkus = new ArrayList<>();
        for (Sku sku : skus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", sku.getId());
            map.put("title", sku.getTitle());
            map.put("price", sku.getPrice());
            map.put("images", StringUtils.substringBefore(sku.getImages(), ", "));
            prices.add(sku.getPrice());
            simpleSkus.add(map);
        }

        // 查询参数规格
        List<SpecParam> specParams = specificationClient.queryParamList(null, spu.getCid3(), true);
        if (CollectionUtils.isEmpty(specParams))
            throw new RuntimeException("规格参数名称查询失败");
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
        // 获取通用规格参数
        Map<Long, String> genericSpec = JsonUtils.parseMap(spuDetail.getGenericSpec(), Long.class, String.class);
        // 获取特有参数
        Map<Long, List<String>> specialSpec = JsonUtils.nativeRead(
                spuDetail.getSpecialSpec(),
                new TypeReference<Map<Long, List<String>>>() {
        });
        Map<String, Object> specs = new HashMap<>();
        for (SpecParam specParam : specParams) {
            String key = specParam.getName();
            Object value = "";
            if (specParam.getGeneric()) {
                value = genericSpec.get(specParam.getId());
                if (specParam.getNumeric())
                {
                    value = chooseSegment(value.toString(), specParam);
                }

            } else {
                value = specialSpec.get(specParam.getId());
            }
            specs.put(key, value);
        }

        Goods goods = new Goods();
        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setAll(all);
        goods.setPrice(prices);
        goods.setSkus(JsonUtils.serialize(simpleSkus));
        goods.setSpecs(specs);
        goods.setSubTitle(spu.getSubTitle());

        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult search(SearchRequest searchRequest) {
        int page = searchRequest.getPage();
        int size = SearchRequest.getDefaultRows();
        // 创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 分页
        queryBuilder.withPageable(PageRequest.of(page-1, size));
        // 过滤
        QueryBuilder basicQuery = buildBasicQuery(searchRequest);
        queryBuilder.withQuery(basicQuery);

        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        // 聚合
        String categoryAgg = "category_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAgg).field("cid3"));
        String brandAgg = "brand_agg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAgg).field("brandId"));
        // 查询
//        Page<Goods> result = goodsRepository.search(queryBuilder.build());
        AggregatedPage<Goods> result = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);
        // 解析结果
        long total = result.getTotalElements();
        int totalPages = result.getTotalPages();
        List<Goods> goodsList = result.getContent();
        Aggregations aggregations = result.getAggregations();
        List<Category> categories = parseCategoryAgg(aggregations.get(categoryAgg));
        List<Brand> brands = parseBrandAgg(aggregations.get(brandAgg));

        List<Map<String, Object>> specs = null;
        if (categories != null && categories.size()==1) {
            specs = buildSpecificationAgg(categories.get(0).getId(), basicQuery);
        }
        return new SearchResult(total, totalPages, goodsList, categories, brands, specs);
    }

    private QueryBuilder buildBasicQuery(SearchRequest searchRequest) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()));
        // 过滤条件
        Map<String, String> filter = searchRequest.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            key = (key == "品牌") ? "brandId" : key;
            key = (key == "分类") ? "cid3" : key;
            if (!"cid3".equals(key) && !"brandId".equals(key)) {
                key = "specs." + key + ".keyword";
            }
            queryBuilder.filter(QueryBuilders.termQuery(key, entry.getValue()));
        }
        return queryBuilder;
    }

    private List<Map<String, Object>> buildSpecificationAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> specs = new ArrayList<>();
        List<SpecParam> params = specClient.queryParamList(null, cid, true);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withQuery(basicQuery);
        for (SpecParam param : params) {
            String name = param.getName();
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs."+name+".keyword"));
        }
        AggregatedPage<Goods> results = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), Goods.class);
        Aggregations aggregations = results.getAggregations();
        for (SpecParam param : params) {
            String name = param.getName() ;
            StringTerms stringTerms = aggregations.get(name);
            Map<String, Object> map = new HashMap<>();
            map.put("k", name);
            map.put("options", stringTerms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString).collect(Collectors.toList()));
            specs.add(map);
        }

        return specs;
    }

    private List<Brand> parseBrandAgg(LongTerms aggregation) {
        try {
            List<Long> ids = aggregation.getBuckets().stream()
                    .map(b -> b.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByBids(ids);
            return brands;
        } catch (Exception e) {
            log.error("brand聚合失败");
            return null;
        }
    }

    private List<Category> parseCategoryAgg(LongTerms aggregation) {
        try {
            List<Long> ids = aggregation.getBuckets().stream()
                    .map(c -> c.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryByCids(ids);
            return categories;
        } catch (Exception e) {
            return null;
        }
    }
}
