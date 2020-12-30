package cn.gmj.taotao.item.service;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Brand;
import org.springframework.http.ResponseEntity;

public interface BrandService {
    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, Boolean desc, String sortBy, String key);
}
