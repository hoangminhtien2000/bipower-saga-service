package com.biplus.saga.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Embedded;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupMember implements Serializable {
    private Long groupMemberId;

    private Long groupLineId;

    private Long productId;

    private Long isMain;

    private Long status;

    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    @Embedded
    private UpdateInfo updateInfo;
}
