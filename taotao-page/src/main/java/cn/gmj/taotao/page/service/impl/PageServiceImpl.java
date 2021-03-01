package cn.gmj.taotao.page.service.impl;

import cn.gmj.taotao.item.common.pojo.*;
import cn.gmj.taotao.page.client.BrandClient;
import cn.gmj.taotao.page.client.CategoryClient;
import cn.gmj.taotao.page.client.GoodsClient;
import cn.gmj.taotao.page.client.SpecificationClient;
import cn.gmj.taotao.page.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class PageServiceImpl implements PageService {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> map = new HashMap<>();
        Spu spu = goodsClient.querySpuById(spuId);
        List<Sku> skus = spu.getSkus();
        SpuDetail detail = spu.getSpuDetail();
        Brand brand = brandClient.queryBrandByBid(spu.getBrandId());
        List<Category> categories = categoryClient.queryCategoryByCids(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<Map<String, Object>> categoriesName = new ArrayList<>();
        for (Category category : categories) {
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", category.getId());
            categoryMap.put("name", category.getName());
            categoriesName.add(categoryMap);
        }
        List<SpecGroup> groups = specificationClient.queryListByCid(spu.getCid3());
        Map<Long, String> paramMap = new HashMap<>();
        groups.forEach(group-> {
            group.getParams().forEach(specParam -> {
                paramMap.put(specParam.getId(), specParam.getName());
            });
        });
        map.put("spu", spu);
        map.put("spuDetail", detail);
        map.put("skus", skus);
        map.put("categories", categoriesName);
        map.put("brand", brand);
        map.put("groups", groups);
        map.put("paramMap", paramMap);
        return map;
    }

    public void createHtml(Long spuId) {
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        File dest = new File("G:\\shoppingWeb\\code\\upload", spuId + ".html");
        if (dest.exists()) {
            dest.delete();
        }
        try {
            PrintWriter writer = new PrintWriter(dest, "utf-8");
            templateEngine.process("item", context, writer);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("生成静态页面异常", e);
        }
    }

    @Override
    public void deleteHtml(Long spuId) {
        File dest = new File("G:\\shoppingWeb\\code\\upload", spuId + ".html");
        if (dest.exists()) dest.delete();
    }
}
