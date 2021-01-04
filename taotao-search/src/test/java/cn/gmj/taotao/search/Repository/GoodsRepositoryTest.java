package cn.gmj.taotao.search.Repository;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Spu;
import cn.gmj.taotao.search.client.GoodsClient;
import cn.gmj.taotao.search.pojo.Goods;
import cn.gmj.taotao.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Test
    public void testCreateIndex() {
        template.createIndex(Goods.class);
        template.putMapping(Goods.class);
    }

    @Test
    public void loadData() {
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询spu信息
            PageResult<Spu> spuPageResult = goodsClient.querySpuByPage(page, rows, true, null);
            List<Spu> spuList = spuPageResult.getItems();
            if (CollectionUtils.isEmpty(spuList)) break;
            // 构建为goods
            List<Goods> collect = spuList.stream().map(searchService::buildGoods).collect(Collectors.toList());
            // 存入索引库
            goodsRepository.saveAll(collect);
            page++;
            size = spuList.size();
        } while (size == 100);
    }

    @Test
    public void deleteData() {
        goodsRepository.deleteAll();
    }
}