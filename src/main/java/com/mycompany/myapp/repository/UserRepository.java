package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SysUser;
import com.mycompany.myapp.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<SysUser, Long> {
    //    Optional<User> findOneByActivationKey(String activationKey);
    //    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    //    Optional<User> findOneByResetKey(String resetKey);
    Optional<SysUser> findOneByEmailIgnoreCase(String email);

    //    Optional<User> findOneByLogin(String login);

    //    @EntityGraph(attributePaths = "authorities")
    //    Optional<SysUser> findOneWithAuthoritiesByUserName(String login);
    @EntityGraph(attributePaths = { "roles" })
    Optional<SysUser> findOneWithAuthoritiesByUserName(String login);

    @EntityGraph(attributePaths = "authorities")
    Optional<SysUser> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndIsActiveIsTrue(Pageable pageable);

    Optional<SysUser> findOneByUserName(String userName);
}
