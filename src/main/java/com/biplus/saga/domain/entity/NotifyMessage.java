package com.biplus.saga.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "notify_message")
@NoArgsConstructor
@AllArgsConstructor
public class NotifyMessage extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notify_message_id")
    private Long notifyMessageId;
    @Column(name = "saga_offline_id")
    private Long sagaOfflineId;
    @Column(name = "batch_code")
    private String batchCode;
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "message")
    private String message;
    @Column(name = "status")
    private Integer status;
}
