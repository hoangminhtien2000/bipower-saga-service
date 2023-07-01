package com.biplus.saga.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "saga_offline")
@NoArgsConstructor
@AllArgsConstructor
public class SagaOffline extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saga_offline_id")
    private Long sagaOfflineId;
    @Column(name = "order_id")
    private Long orderId;
    @Column(name = "saga_type")
    private String sagaType;
    @Column(name = "saga_id")
    private String sagaId;
    @Column(name = "telecom_service_id")
    private Long telecomServiceId;
    @Column(name = "batch_code")
    private String batchCode;
    @Column(name = "data")
    private String data;
    @Column(name = "locale")
    private String locale;
    @Column(name = "is_registered")
    private Integer isRegistered;
    @Column(name = "retry")
    private int retry;
    @Column(name = "status")
    private Integer status;
    @Column(name = "result")
    private String result;
    @Column(name = "description")
    private String description;
    @Column(name = "account")
    private String account;
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "agreement_id")
    private Long agreementId;

}
