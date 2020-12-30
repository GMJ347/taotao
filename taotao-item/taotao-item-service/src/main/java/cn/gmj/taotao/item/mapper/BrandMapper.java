package cn.gmj.taotao.item.mapper;

import cn.gmj.taotao.item.common.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 新增tb_category_brand
     * @param cid categoryId
     * @param bid BrandId
     * @return
     */
    @Insert("insert into tb_category_brand(category_id, brand_id) values(#{cid}, #{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据brandId删除中间表数据
     */
    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    void deleteByBrandIdInCategoryBrand(@Param("bid") Long bid);
}
