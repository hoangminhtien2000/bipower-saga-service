package com.biplus.saga.repo;

import com.biplus.saga.domain.entity.ExchangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author anhnv174
 **/
public interface ExchangeLogRepo extends JpaRepository<ExchangeLog, Long> {
}
