package com.surya.crudproject.v1.repository;


import com.surya.crudproject.common.fileutil.model.FileMappingType;
import com.surya.crudproject.common.fileutil.util.MappingFileUtil;
import com.surya.crudproject.v1.entity.CrudProjectEntity;
import com.surya.crudproject.v1.model.Location;
import com.surya.crudproject.v1.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component("com.surya.crudproject.v1.repository.DummyRepository")
public class DummyRepository {

  @Autowired
  @Qualifier("com.peoplehum.reportsync.common.util.MappingFileUtil")
  private MappingFileUtil mappingFileUtil;


  List<CrudProjectEntity> findAll() {
    log.info("fetching all dummy data");
    return mappingFileUtil.getDummyDataForCrudOperation(FileMappingType.STORED_DATA);
  }

  public List<Location> getAllDummyLocation(){
    return mappingFileUtil.getAllDummyLocation(FileMappingType.LOCATION_DATA);
  }

  public List<User> getAllDummyUser(){
    return mappingFileUtil.getAllDummyUser(FileMappingType.USER_DATA);
  }
  void findAggregate(){
    /*Map<String, List<CrudProjectEntity>> output = list.stream()
        .filter(e->e.getSalary() > 2000)
        .collect(Collectors.groupingBy(Employee::getDepartment, HashMap::new, toList()));*/
  }
}
