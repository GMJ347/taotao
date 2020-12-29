package cn.gmj.taotao.service.impl;

import cn.gmj.taotao.mapper.CategoryMapper;
import cn.gmj.taotao.pojo.Category;
import cn.gmj.taotao.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryCategoriesByPid(Long pid) {
        return categoryMapper.queryCategoriesByPid(pid);
    }
}
