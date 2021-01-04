package cn.gmj.taotao.item.web;

import cn.gmj.taotao.item.common.pojo.Category;
import cn.gmj.taotao.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父节点查询子节点
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        // 400 参数不合法
        if (pid == null || pid < 0) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            return ResponseEntity.badRequest().build();

        }
        List<Category> categories = this.categoryService.queryCategoriesByPid(pid);
        // 404 资源未找到
        if (CollectionUtils.isEmpty(categories)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.notFound().build();
        }
        // 200 查询成功
        return ResponseEntity.ok(categories);
    }

    /**
     * 根据分类id查询分类
     */
    @GetMapping("/list/cids")
    public ResponseEntity<List<Category>> queryCategoryByCids(@RequestParam("cids")List<Long> cids) {
        return ResponseEntity.ok(categoryService.queryCategoriesByCids(cids));
    }

}
