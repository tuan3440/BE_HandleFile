package com.mycompany.myapp.repository.impl;

import com.mycompany.myapp.repository.SysRoleModuleCustomRepository;
import com.mycompany.myapp.service.dto.SysRoleModuleDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class SysRoleModuleCustomRepositoryImpl implements SysRoleModuleCustomRepository {

    @Autowired
    private EntityManager em;

    @Override
    public List<SysRoleModuleDTO> findByUserId(Long id) {
        Session session = this.em.unwrap(Session.class);
        String sql =
            "SELECT " +
            " module_code as moduleCode, action_code as actionCode " +
            " FROM " +
            " sys_role_module " +
            "WHERE " +
            " role_id IN ( SELECT role_id FROM sys_user_role WHERE user_id = :userId ) ";

        Query<SysRoleModuleDTO> query = session
            .createSQLQuery(sql)
            .addScalar("moduleCode", new StringType())
            .addScalar("actionCode", new StringType())
            .setParameter("userId", id)
            .setResultTransformer(Transformers.aliasToBean(SysRoleModuleDTO.class));
        return query.getResultList();
    }
}
