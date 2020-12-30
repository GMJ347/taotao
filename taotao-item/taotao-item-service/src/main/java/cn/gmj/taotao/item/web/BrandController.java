package cn.gmj.taotao.item.web;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Brand;
import cn.gmj.taotao.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

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

}
