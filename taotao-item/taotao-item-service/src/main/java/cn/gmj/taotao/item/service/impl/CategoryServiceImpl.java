package cn.gmj.taotao.item.service.impl;

import cn.gmj.taotao.item.common.pojo.Category;
import cn.gmj.taotao.item.mapper.CategoryMapper;
import cn.gmj.taotao.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryCategoriesByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }
}
