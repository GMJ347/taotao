package cn.gmj.taotao.item.common.api;

import cn.gmj.taotao.item.common.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BrandApi {
    /**
     * 根据品牌id查询品牌
     * @param bid
     * @return
     */
    @GetMapping("/brand/{bid}")
    Brand queryBrandByBid(@PathVariable("bid") Long bid);

    @GetMapping("/brand/ids")
    public List<Brand> queryBrandByBids(@RequestParam("ids") List<Long> bids);

}
