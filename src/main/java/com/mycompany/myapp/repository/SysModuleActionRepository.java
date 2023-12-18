package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SysAction;
import com.mycompany.myapp.domain.SysModuleAction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SysModuleActionRepository extends JpaRepository<SysModuleAction, Long> {
    List<SysModuleAction> findByModuleId(@Param("moduleId") Long moduleId);

    void deleteAllByModuleId(@Param("moduleId") Long moduleId);

    @Query(
        value = "select sa from SysAction sa " +
        " where 1=1 " +
        " and sa.id in ( " +
        " select sma.actionId from SysModuleAction sma " +
        " where sma.moduleId = (" +
        " select sm.id from SysModule sm where sm.code = :code ) " +
        ")"
    )
    List<SysAction> getListAction(@Param("code") String code);
}
