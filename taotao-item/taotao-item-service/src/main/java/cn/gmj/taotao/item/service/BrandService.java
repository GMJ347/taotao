package cn.gmj.taotao.item.service;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Brand;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BrandService {

    /**
     * 分页条件查询品牌
     * @param page 当前页
     * @param rows 每页的行数
     * @param desc 是都降序
     * @param sortBy 排序字段
     * @param key 搜索关键字
     * @return
     */
    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, Boolean desc, String sortBy, String key);

    /**
     * 新增品牌
     * @param brand 品牌
     * @param cids 该品牌相关categoryId
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 通过BrandId删除brand，并更新中间表category_brand
     * @param bid
     */
    void deleteBrand(Long bid);

    /**
     * 根据BrandId查询Brand
     * @param bid
     * @return
     */
    Brand queryById(Long bid);

    /**
     * 根据CategoryId查询brand
     * @param cid
     * @return
     */
    List<Brand> queryBrandByCid(Long cid);
}
