package cn.gmj.taotao.item.common.api;

import cn.gmj.taotao.item.common.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {

    @GetMapping("/category/list/cids")
    List<Category> queryCategoryByCids(@RequestParam("cids") List<Long> cids);
}
