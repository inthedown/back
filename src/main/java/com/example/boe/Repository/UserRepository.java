package com.example.boe.Repository;

import com.example.boe.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    @Query(value = "select u from User u where u.userName = ?1")
    User findUserByUsername(String username);

    default Page<User> findByParam(String userName, Integer role, Pageable pageable) {
        return findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (userName != null) {
                predicates.add(criteriaBuilder.equal(root.get("userName"), userName));
            }
            if (role != null) {
                predicates.add( criteriaBuilder.equal(root.get("roleId"), role));
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageable);
    }
    @Modifying
    @Transactional
    @Query(value = "delete from User u where u.id in ?1")
    void deleteByIdIn(Collection<Integer> id);
}
