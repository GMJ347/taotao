package cn.gmj.taotao.service;

import cn.gmj.taotao.pojo.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 根据父节点查询子节点
     * @param pid 父节点id，即category.parentId
     * @return 子节点的列表
     */
    List<Category> queryCategoriesByPid(Long pid);
}
