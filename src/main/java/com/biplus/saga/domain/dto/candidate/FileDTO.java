package com.biplus.saga.domain.dto.candidate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FileDTO {

    @JsonProperty("file_id")
    private Long id;

    @JsonProperty("full_path")
    private String fullPath;
    private String content;
    private String name;
    @JsonProperty("display_name")
    private String displayName;
    private String extension;
    private String type;
}
