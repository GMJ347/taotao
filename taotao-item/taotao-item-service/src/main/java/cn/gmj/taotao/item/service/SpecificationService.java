package cn.gmj.taotao.item.service;

import cn.gmj.taotao.item.common.pojo.SpecGroup;
import cn.gmj.taotao.item.common.pojo.SpecParam;

import java.util.List;

public interface SpecificationService {

    /**
     * 根据分类id查询规格组
     * @param cid
     * @return
     */
    List<SpecGroup> queryGroupByCid(Long cid);

    /**
     * 根据规格组id查询规格参数
     * @param gid
     * @return
     */
    List<SpecParam> queryParamList(Long gid, Long cid, Boolean searching);
}
