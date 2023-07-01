package com.biplus.saga.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "action_detail")
public class ActionDetail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_detail_id")
    private Long actionDetailId;

    @Column(name = "action_audit_id")
    private Long actionAuditId;

    @Column(name = "issue_datetime")
    private LocalDateTime issueDatetime;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "column_name")
    private String columnName;
    
    @Column(name = "pk_id")
    private Long pkId;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @Column(name = "col_name")
    private String colName;

}
