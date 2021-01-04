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
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        return brandMapper.queryByCategoryId(cid);
    }

    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        this.brandMapper.insert(brand);
        for (Long cid : cids) {
            // 这里brand在写入数据库后，自增长的主键会自动回写入brand opjo
            this.brandMapper.insertCategoryBrand(cid, brand.getId());
        }
    }

    @Override
    @Transactional
    public void deleteBrand(Long bid) {
        this.brandMapper.deleteByPrimaryKey(bid);
        this.brandMapper.deleteByBrandIdInCategoryBrand(bid);
    }

    @Override
    public Brand queryById(Long bid) {
        return brandMapper.selectByPrimaryKey(bid);
    }

    @Override
    public List<Brand> queryByIds(List<Long> bids) {
        return brandMapper.selectByIdList(bids);
    }
}
