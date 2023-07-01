package com.biplus.saga.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UpdateInfo implements Serializable {
    @Column(name = "create_user")
    private String createUser;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;
}
