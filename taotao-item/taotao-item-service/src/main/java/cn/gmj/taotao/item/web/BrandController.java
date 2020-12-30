package cn.gmj.taotao.item.web;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Brand;
import cn.gmj.taotao.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品牌相关
 */
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 分页条件查询品牌
     * @param page 当前页
     * @param rows 每页行数
     * @param desc 是否降序
     * @param sortBy 排序字段
     * @param key 搜索关键字
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value="page", defaultValue="1") Integer page,
            @RequestParam(value="rows", defaultValue="8") Integer rows,
            @RequestParam(value="desc", defaultValue="1") Boolean desc,
            @RequestParam(value="sortBy", required=false) String sortBy,
            @RequestParam(value="key", required=false) String key
    ) {
        return ResponseEntity.ok(brandService.queryBrandByPage(page, rows, desc, sortBy, key));
    }

    /**
     * 新增品牌，将品牌加入tb_brand，并更新tb_category_brand
     * @param brand 品牌pojo
     * @param cids 对应的categoryId
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 删除品牌，包括tb_brand，tb_category_brand
     * @param bid 品牌id
     * @return
     */
    @DeleteMapping("/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") String bid) {
        brandService.deleteBrand(Long.parseLong(bid));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
