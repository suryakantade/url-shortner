package com.surya.crudproject.v1.repository;

import com.surya.crudproject.v1.entity.CrudProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("com.surya.crudproject.v1.repository.CrudProjectEntity")
public interface CrudProjectRepository extends JpaRepository<CrudProjectEntity, Integer> {


}

