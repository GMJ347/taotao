package cn.gmj.taotao.search.client;

import cn.gmj.taotao.item.common.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
