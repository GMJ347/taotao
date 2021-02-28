package cn.gmj.taotao.item.service.impl;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.*;
import cn.gmj.taotao.item.mapper.*;
import cn.gmj.taotao.item.service.BrandService;
import cn.gmj.taotao.item.service.CategoryService;
import cn.gmj.taotao.item.service.GoodsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang.StringUtils;
import org.omg.SendingContext.RunTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Override
    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        // 分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("title", "%"+key+"%");
        }
        if (saleable != null)
            criteria.andEqualTo("saleable", saleable);
        example.setOrderByClause("last_update_time DESC");
        // 查询
        List<Spu> spus = spuMapper.selectByExample(example);
        // 解析分类和品牌的名称
        loadCategoryAndBrandName(spus);
        // 解析分页结果
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);
        return new PageResult<>(spuPageInfo.getTotal(), spus);
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            // 处理分类名称
            List<String> names = categoryService.queryCategoriesByCids(Arrays.asList(
                    spu.getCid1(), spu.getCid2(), spu.getCid3()
            )).stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names, "/"));
            // 处理品牌名称
            spu.setBname(brandService.queryById(spu.getBrandId()).getName());
        }
    }

    @Override
    @Transactional
    public void saveGoods(Spu spu) {
        // 新增spu
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);

        int count = spuMapper.insert(spu);
        if (count != 1) {
            throw new RuntimeException("商品保存失败");
        }
        // 新增detail
        SpuDetail detail = spu.getSpuDetail();
        detail.setSpuId(spu.getId());
        count = spuDetailMapper.insert(detail);
        if (count != 1) {
            throw new RuntimeException("商品详情创建失败");
        }
        saveSkuAndStock(spu);
    }

    private void saveSkuAndStock(Spu spu) {
        int count;// 新增sku
        ArrayList<Stock> stocks = new ArrayList<>();
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(spu.getCreateTime());
            sku.setLastUpdateTime(spu.getCreateTime());
            sku.setSpuId(spu.getId());
            count = skuMapper.insert(sku);
            if (count != 1) {
                throw new RuntimeException("商品SKU创建失败");
            }
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stocks.add(stock);
        }
        // 新增库存
        count = stockMapper.insertList(stocks);
        if (count != stocks.size()) {
            throw new RuntimeException("商品SKU库存创建失败");
        }
    }

    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = skuMapper.select(record);
        skus.stream().forEach(sku -> sku.setStock(stockMapper.selectByPrimaryKey(sku.getId()).getStock()));
        return skus;
    }

    @Override
    @Transactional
    public void updateGoods(Spu spu) {
        List<Sku> skus = querySkusBySpuId(spu.getId());
        if (!CollectionUtils.isEmpty(skus)) {
            List<Long> skuIds = skus.stream().map(sku -> sku.getId()).collect(Collectors.toList());
            // 删除旧库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", skuIds);
            stockMapper.deleteByExample(example);
            // 删除旧Sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            skuMapper.delete(record);
        }
        // 新增Sku和Stock
        saveSkuAndStock(spu);
        // 更新Spu和SpuDetail
        spu.setCreateTime(null);
        spu.setLastUpdateTime(new Date());
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
    }

    @Override
    public Spu querySpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        spu.setSkus(querySkusBySpuId(id));
        spu.setSpuDetail(querySpuDetailBySpuId(id));
        return spu;
    }
}
