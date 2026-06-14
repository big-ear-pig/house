package org.bigearpig.base.feign.client.sys.website;

import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(name = "server3sys", contextId = "website", path = "/website")
public interface WebsiteFeignClient {

}
