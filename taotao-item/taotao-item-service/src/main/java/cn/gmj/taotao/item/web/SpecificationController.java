package cn.gmj.taotao.item.web;

import cn.gmj.taotao.item.common.pojo.SpecGroup;
import cn.gmj.taotao.item.common.pojo.SpecParam;
import cn.gmj.taotao.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询规格组
     * @param cid 分类id
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(specificationService.queryGroupByCid(cid));
    }

    /**
     * 根据组id查询规格参数
     * @param gid 组id
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParamByGid(@RequestParam("gid") Long gid) {
        return ResponseEntity.ok(specificationService.queryParamByGid(gid));
    }
}
