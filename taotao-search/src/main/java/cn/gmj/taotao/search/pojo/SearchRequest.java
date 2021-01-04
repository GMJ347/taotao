package cn.gmj.taotao.search.pojo;

import java.util.Map;

public class SearchRequest {
    private String key;
    private Integer page;

    private Map<String, String> filter;

    private static final int DEFAULT_ROWS = 20;
    private static final int DEFAULT_PAGE = 1;

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getPage() {
        if (page == null)
            return DEFAULT_PAGE;
        return Math.max(page, DEFAULT_PAGE);
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public static int getDefaultRows() {
        return DEFAULT_ROWS;
    }

    public static int getDefaultPage() {
        return DEFAULT_PAGE;
    }
}
