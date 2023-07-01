package com.biplus.saga.domain.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ItemDTO {

    @JsonProperty("item_id")
    private Long itemId;

    private String code;

    private String name;

    @JsonProperty("parent_id")
    private Long parentId;
}
