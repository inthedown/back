package com.example.boe.Repository;


import com.example.boe.Entity.Classes;
import com.example.boe.Form.ClassesParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ClassesRepository extends JpaRepository<Classes, Integer>, JpaSpecificationExecutor<Classes> {
    @Query(value = "select * from classes where class_name = ?1", nativeQuery = true)
    Classes findByClassName(String className);
    @Query(value = "select * from classes left join student on classes.id = student.classes_id where classes.id = ?1", nativeQuery = true)
    List<Map<String,Object>> findDetailById(int id);

    //动态查询
    default Page<Classes> findByParam(ClassesParam classesParam, Pageable pageable) {
        return findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (classesParam!= null) {
                if (classesParam.getClassName() != null) {
                    predicates.add(criteriaBuilder.equal(root.get("className"), classesParam.getClassName()));
                }
            }

            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, pageable);
    }
}
