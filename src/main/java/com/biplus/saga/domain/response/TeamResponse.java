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
public class TeamResponse {
    private static final long serialVersionUID = 4590066832132670826L;

    @JsonProperty("team_id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("team_type_id")
    private Long teamTypeId;

    @JsonProperty("team_lead_id")
    private Long teamLeadId;

    @JsonProperty("po_id")
    private Long poId;

    @JsonProperty("projects")
    private String projects;

    @JsonProperty("customers")
    private String customers;

    @JsonProperty("note")
    private String note;

    @JsonProperty("salary")
    private Double salary;

    @JsonProperty("productivity")
    private Double productivity;

    @JsonProperty("headcount")
    private Integer headcount;

    @JsonProperty("average_productivity")
    private Double averageProductivity;

    @JsonProperty(value = "deleted", defaultValue = "false")
    private boolean deleted;
}
