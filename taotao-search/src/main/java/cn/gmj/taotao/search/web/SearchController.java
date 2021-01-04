package cn.gmj.taotao.search.web;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.search.pojo.Goods;
import cn.gmj.taotao.search.pojo.SearchRequest;
import cn.gmj.taotao.search.pojo.SearchResult;
import cn.gmj.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Action;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest searchRequest){
        return ResponseEntity.ok(searchService.search(searchRequest));
    }
}
