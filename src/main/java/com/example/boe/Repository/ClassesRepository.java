package com.example.boe.Repository;


import com.example.boe.Entity.Classes;
import com.example.boe.Form.ClassesParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public interface ClassesRepository extends JpaRepository<Classes, Integer>, JpaSpecificationExecutor<Classes> {

    @Query(value = "select * from classes where class_name = ?1", nativeQuery = true)
    Classes findByClassName(String className);
    @Query("SELECT c FROM Classes c  WHERE c.id = :classId")
    Classes findDetailById(@Param("classId") int id);

    //动态查询
    default Page<Classes> findByParam(ClassesParam classesParam, Pageable pageable) {
        return findAll((root, query, criteriaBuilder) -> {
            // Join students and courses entities
            root.join("students", JoinType.LEFT);
            root.join("courses", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();
            if (classesParam!= null) {
                if (classesParam.getClassName() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("className"), classesParam.getClassName()));
                }
            }
            // Apply the predicates to the query
            query.where(predicates.toArray(new Predicate[predicates.size()]));

            // Group by the root entity to avoid duplicates in the result set
            query.groupBy(root.get("id"));

            return query.getRestriction();

        }, pageable);
    }
}
