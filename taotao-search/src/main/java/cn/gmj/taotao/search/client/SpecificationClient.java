package cn.gmj.taotao.search.client;

import cn.gmj.taotao.item.common.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
