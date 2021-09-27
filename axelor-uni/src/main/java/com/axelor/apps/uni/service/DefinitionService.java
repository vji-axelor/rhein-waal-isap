package com.axelor.apps.uni.service;

import com.axelor.apps.uni.db.Definition;

public interface DefinitionService {

  void sendEmail(Definition description);

  void setupStuDeparture(Definition definition);
}
