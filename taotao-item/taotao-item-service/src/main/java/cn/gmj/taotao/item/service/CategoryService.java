package cn.gmj.taotao.item.service;

import cn.gmj.taotao.item.common.pojo.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 根据父节点查询子节点
     * @param pid 父节点id，即category.parentId
     * @return 子节点的列表
     */
    List<Category> queryCategoriesByPid(Long pid);

    /**
     * 根据分类id查询分类名称
     * @param cids 分类id
     * @return
     */
    List<Category> queryCategoriesByCids(List<Long> cids);
}
