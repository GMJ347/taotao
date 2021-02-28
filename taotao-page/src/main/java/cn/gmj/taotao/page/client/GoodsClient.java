package cn.gmj.taotao.page.client;

import cn.gmj.taotao.item.common.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
