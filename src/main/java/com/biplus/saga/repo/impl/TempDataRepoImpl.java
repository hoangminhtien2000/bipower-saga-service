package com.biplus.saga.repo.impl;

import com.biplus.saga.domain.entity.TempData;
import com.biplus.saga.repo.TempDataRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class TempDataRepoImpl implements TempDataRepoCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public TempData findByOrderIdAndFunctionCode(Long orderId, String functionCode) {

        StringBuilder strQuery = new StringBuilder();
        strQuery.append(" select * from temp_data where request_id like '%\\_");
        strQuery.append(orderId.toString());
        strQuery.append("'");
        strQuery.append(" and function_code = :functionCode ");
        strQuery.append(" limit 1 ");

        Query query = em.createNativeQuery(strQuery.toString(), TempData.class);
        query.setParameter("functionCode", functionCode);

        List<TempData> rs =  query.getResultList();
        return rs.size() == 0 ? null : rs.get(0);
    }
}
