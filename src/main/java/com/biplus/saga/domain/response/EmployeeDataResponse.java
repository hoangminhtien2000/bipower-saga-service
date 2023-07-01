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
public class EmployeeDataResponse {
    private static final long serialVersionUID = 4590066832132670826L;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("employeeCode")
    private String employeeCode;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("fullName")
    private String fullName;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("companyEmail")
    private String companyEmail;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("status")
    private String status;
    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("role")
    private String role;
    @JsonProperty("numberOfMonthStackExperience")
    private Integer numberOfMonthStackExperience;
    @JsonProperty("cvEnLink")
    private String cvEnLink;
    @JsonProperty("cvViLink")
    private String cvViLink;
    @JsonProperty("fullSalary")
    private Double fullSalary;
    @JsonProperty("effort")
    private Double effort;
    @JsonProperty("working")
    private Boolean working;
    @JsonProperty(defaultValue = "false")
    private boolean isSendEmail;
}
