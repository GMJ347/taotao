package cn.gmj.taotao.item.service.impl;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Brand;
import cn.gmj.taotao.item.mapper.BrandMapper;
import cn.gmj.taotao.item.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, Boolean desc, String sortBy, String key) {
        // 分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().orLike("name", "%"+key+"%");
        }
        // 排序
        if (StringUtils.isNotBlank(sortBy)) {
            String orderClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderClause);
        }
        // 查询
        List<Brand> list = brandMapper.selectByExample(example);

        // 解析分页结果
        PageInfo<Brand> info = new PageInfo<>(list);

        return new PageResult<Brand>(info.getTotal(), list);
    }
}
