package com.biplus.saga.domain.request.employee;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author congpham
 * @since on 30/05/2022
 */
@Getter
@Setter
@ToString
public class CreateEmployeeRequest implements Serializable {

    private String firstName;

    private String lastName;

    private String individualEmail;

    private String phone;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthday;

    private String gender;

    private List<String> position;

    private String stackTech;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate workingTimeFrom;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate workingTimeWithStackFrom;

    private String avatarPath;

    private String cvPath;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate onboardDate;

    private String nationality;

    private String currentAddress;

    private Boolean isCollaborator = Boolean.FALSE;

    private Long candidateId;

    private Long netSalary;

    private Double effort;

}
