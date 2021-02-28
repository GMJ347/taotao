package cn.gmj.taotao.item.common.api;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Sku;
import cn.gmj.taotao.item.common.pojo.Spu;
import cn.gmj.taotao.item.common.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    /**
     * 分页查询Spu
     * @param page 当前页
     * @param rows 每页行数
     * @param saleable 是否可售
     * @param key 查询关键字
     * @return
     */
    @GetMapping("/spu/page")
    public PageResult<Spu> querySpuByPage(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="rows", defaultValue="8") Integer rows,
            @RequestParam(value="saleable", required=false) Boolean saleable,
            @RequestParam(value="key", required=false) String key
    );

    /**
     * 根据SpuID查询SpuDetail
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId") Long spuId);

    /**
     * 根据SpuId查询Sku
     * @param spuId
     * @return
     */
    @GetMapping("/sku/list")
    public List<Sku> querySkusBySpuId(@RequestParam("id") Long spuId);

    /**
     * 根据SpuId查询Spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    public Spu querySpuById(@PathVariable("id") Long id);
}
