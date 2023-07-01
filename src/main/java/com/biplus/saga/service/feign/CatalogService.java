package com.biplus.saga.service.feign;

import com.biplus.core.config.OAuth2FeignAutoConfiguration;
import com.biplus.saga.domain.response.ApiResponse;
import com.biplus.saga.domain.response.ItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "CatalogService", url = "${spring.rest.catalog.url}", configuration = {OAuth2FeignAutoConfiguration.class})
public interface CatalogService {
    @GetMapping("/catalog/item/get")
    ApiResponse<List<ItemResponse>> get(@RequestParam("catalog_code") String catalogCode, @RequestParam(name = "parent_id", required = false) Long parentId);
}
