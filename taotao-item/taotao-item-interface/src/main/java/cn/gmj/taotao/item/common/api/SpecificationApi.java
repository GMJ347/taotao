package cn.gmj.taotao.item.common.api;

import cn.gmj.taotao.item.common.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {
    /**
     * 查询规格参数
     * @param gid
     * @param cid
     * @Param searching
     * @return
     */
    @GetMapping("/spec/params")
    List<SpecParam> queryParamList(
            @RequestParam(value="gid", required=false) Long gid,
            @RequestParam(value="cid", required=false) Long cid,
            @RequestParam(value="searching", required=false) Boolean searching
    );
}
