package com.axelor.apps.uni.web;

import com.axelor.apps.uni.db.Definition;
import com.axelor.apps.uni.service.DefinitionService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class DefinitionController {

  public void sendEmail(ActionRequest request, ActionResponse response) {
    Definition definition = request.getContext().asType(Definition.class);
    try {
      Beans.get(DefinitionService.class).sendEmail(definition);
    } catch (Exception e) {
      response.setError(e.toString());
    }
  }
}
