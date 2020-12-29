package cn.gmj.taotao.item.mapper;

import cn.gmj.taotao.item.common.pojo.Category;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    @Select("select * from tb_category where parent_id = #{pid}")
    List<Category> queryCategoriesByPid(Long pid);
}
