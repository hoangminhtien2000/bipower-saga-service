package com.biplus.saga.domain.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
abstract class Auditable implements Serializable {

    @CreatedBy
    @Column(name = "create_user")
    private String createUser;

    @CreatedDate
    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "update_user")
    @LastModifiedBy
    private String updateUser;

    @Column(name = "update_datetime")
    @LastModifiedDate
    private LocalDateTime updateDatetime;

}
