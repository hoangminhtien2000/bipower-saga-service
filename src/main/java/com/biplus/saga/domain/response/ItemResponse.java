package com.biplus.saga.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ItemResponse {
    private static final long serialVersionUID = 4590066832132670826L;

    @JsonProperty("item_id")
    private Long id;

    @JsonProperty("catalog_id")
    private Long catalogId;

    @JsonProperty("parent_id")
    private Long parentId;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("note")
    private String note;

    @JsonProperty("deleted")
    private boolean deleted;

}