package cn.gmj.taotao.search.pojo;

import cn.gmj.taotao.common.view.PageResult;
import cn.gmj.taotao.item.common.pojo.Brand;
import cn.gmj.taotao.item.common.pojo.Category;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {
    private List<Category> categories;
    private List<Brand> brands;
    private List<Map<String, Object>> specs;

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }

    public SearchResult(Long total, List<Goods> items, List<Category> categories, List<Brand> brands) {
        super(total, items);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(List<Category> categories, List<Brand> brands) {
        this.categories = categories;
        this.brands = brands;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}
