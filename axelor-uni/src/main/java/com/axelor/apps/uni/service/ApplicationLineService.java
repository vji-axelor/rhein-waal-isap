package com.axelor.apps.uni.service;

import com.axelor.apps.uni.db.ApplicationLine;
import com.axelor.exception.AxelorException;
import java.util.Map;

public interface ApplicationLineService {

  String setUniqueApplicant(ApplicationLine line);

  void sendInterviewEmail(ApplicationLine line, Map<String, Object> map) throws AxelorException;

  void sendSelectionEmail(ApplicationLine line, Map<String, Object> map) throws AxelorException;

  void sendAcceptEmail(ApplicationLine line) throws AxelorException;

  void sendRejectEmail(ApplicationLine line) throws AxelorException;
}
