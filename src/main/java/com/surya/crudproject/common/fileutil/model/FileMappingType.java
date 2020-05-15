package com.surya.crudproject.common.fileutil.model;

import com.surya.crudproject.common.fileutil.constants.FileMappingConstants;

import java.util.Arrays;
import java.util.List;


public enum FileMappingType {
  STORED_DATA(FileMappingConstants.STORED_DATA, FileMappingConstants.STORED_DATA_FILE_PATH),
  LOCATION_DATA(FileMappingConstants.LOCATION_DATA, FileMappingConstants.LOCATION_DATA_FILE_PATH),
  USER_DATA(FileMappingConstants.LOCATION_DATA, FileMappingConstants.LOCATION_DATA_FILE_PATH);

  public String workflow;
  public String filePath;

  FileMappingType(String indicesName, String filePath) {
    this.workflow = indicesName;
    this.filePath = filePath;
  }

  public static List<FileMappingType> getFileMappingList() {
    return Arrays.asList(FileMappingType.values());
  }

  public static FileMappingType findFileMappingType(String docType) {
    return Arrays.stream(values()).filter(value -> value.workflow.equals(docType)).findFirst()
        .orElse(null);
  }
}
