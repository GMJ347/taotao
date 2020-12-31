package cn.gmj.taotao.item.service;

import cn.gmj.taotao.item.common.pojo.SpecGroup;
import cn.gmj.taotao.item.common.pojo.SpecParam;

import java.util.List;

public interface SpecificationService {

    List<SpecGroup> queryGroupByCid(Long cid);

    List<SpecParam> queryParamByGid(Long gid);
}
