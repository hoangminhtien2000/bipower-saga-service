package com.biplus.saga.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "temp_data")
public class TempData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temp_data_id")
    @JsonIgnoreProperties
    private Long tempDataId;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "tenant")
    @JsonIgnoreProperties
    private String tenant;

    @Column(name = "function_code")
    private String functionCode;

    @Column(name = "data")
    private String data;
}
