package com.biplus.saga.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author anhnv174
 **/
@Entity
@Data
@Table(name = "EXCHANGE_LOG")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "exchange_log_id")
    private Long exchangeLogId;
    @Column(name = "pk_type")
    private Long pkType;
    @Column(name = "pk_id")
    private Long pkId;
    @Column(name = "isdn")
    private String isdn;
    @Column(name = "action_code")
    private String actionCode;
    @Column(name = "src_system")
    private String srcSystem;
    @Column(name = "des_system")
    private String desSystem;
    @Column(name = "request")
    private String request;
    @Column(name = "response")
    private String response;
    @Column(name = "response_code")
    private String responseCode;
    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;
    @Column(name = "create_user")
    private String createUser;

}
