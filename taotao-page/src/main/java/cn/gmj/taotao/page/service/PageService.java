package cn.gmj.taotao.page.service;

import java.util.Map;

public interface PageService {
    Map<String, Object> loadModel(Long spuId);

    void createHtml(Long l);
}
