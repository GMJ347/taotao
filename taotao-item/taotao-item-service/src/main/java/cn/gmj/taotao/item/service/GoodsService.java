package cn.gmj.taotao.item.service;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Sku;
import cn.gmj.taotao.item.common.pojo.Spu;
import cn.gmj.taotao.item.common.pojo.SpuDetail;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface GoodsService {

    /**
     * 分页查询商品，包括关键字搜索、条件搜索
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key);

    /**
     * 保存商品SPU，并自动创建其SKU
     * @param spu
     */
    void saveGoods(Spu spu);

    /**
     * 根据SPUID查询SpuDetail
     * @param spuId
     * @return
     */
    SpuDetail querySpuDetailBySpuId(Long spuId);

    /**
     * 根据SPUID查询sku
     * @param spuId
     * @return
     */
    List<Sku> querySkusBySpuId(Long spuId);

    /**
     * 更新SPU
     * @param spu
     */
    void updateGoods(Spu spu);
}
