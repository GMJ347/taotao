package cn.gmj.taotao.page.client;

import cn.gmj.taotao.item.common.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
