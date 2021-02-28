package cn.gmj.taotao.item.service.impl;

import cn.gmj.taotao.item.common.pojo.SpecGroup;
import cn.gmj.taotao.item.common.pojo.SpecParam;
import cn.gmj.taotao.item.mapper.SpecGroupMapper;
import cn.gmj.taotao.item.mapper.SpecParamMapper;
import cn.gmj.taotao.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<SpecParam> queryParamList(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        return specParamMapper.select(specParam);
    }

    @Override
    public List<SpecGroup> queryListByCid(Long cid) {
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        List<SpecParam> params = queryParamList(null, cid, null);
        Map<Long, List<SpecParam>> map = new HashMap<>();
        for (SpecParam param : params) {
            if (!map.containsKey(param.getGroupId())) {
                map.put(param.getGroupId(), new ArrayList<>());
            }
            map.get(param.getGroupId()).add(param);
        }
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }
        return specGroups;
    }
}
