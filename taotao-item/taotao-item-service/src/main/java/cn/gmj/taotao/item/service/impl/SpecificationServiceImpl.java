package cn.gmj.taotao.item.service.impl;

import cn.gmj.taotao.item.common.pojo.SpecGroup;
import cn.gmj.taotao.item.common.pojo.SpecParam;
import cn.gmj.taotao.item.mapper.SpecGroupMapper;
import cn.gmj.taotao.item.mapper.SpecParamMapper;
import cn.gmj.taotao.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    @Override
    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup group = new SpecGroup();
        group.setCid(cid);
        return specGroupMapper.select(group);
    }

    @Override
    public List<SpecParam> queryParamByGid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        return specParamMapper.select(specParam);
    }
}
