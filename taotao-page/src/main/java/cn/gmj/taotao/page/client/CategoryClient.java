package cn.gmj.taotao.page.client;

import cn.gmj.taotao.item.common.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
