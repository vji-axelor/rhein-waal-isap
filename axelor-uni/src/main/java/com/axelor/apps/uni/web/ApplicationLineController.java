package com.axelor.apps.uni.web;

import com.axelor.apps.uni.db.ApplicationLine;
import com.axelor.apps.uni.service.ApplicationLineService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class ApplicationLineController {

  public void setUniqueApplicant(ActionRequest request, ActionResponse response) {
    ApplicationLine ApplicationLine = request.getContext().asType(ApplicationLine.class);
    if (ApplicationLine.getStudent() == null || ApplicationLine.getDefinition() == null) {
      response.setError("Please add student and definition.");
      return;
    }
    String uniqueName = Beans.get(ApplicationLineService.class).setUniqueApplicant(ApplicationLine);
    response.setValue("uniqueName", uniqueName);
  }
}
