package com.axelor.apps.uni.service;

import com.axelor.apps.uni.db.ApplicationLine;

public class ApplicationLineServiceImpl implements ApplicationLineService {

  @Override
  public String setUniqueApplicant(ApplicationLine line) {
    String mtNumber = line.getStudent().getMatriculationNumber();
    String topic = line.getDefinition().getTopic();

    String uniqueName = mtNumber + "_" + topic;

    return uniqueName;
  }
}
