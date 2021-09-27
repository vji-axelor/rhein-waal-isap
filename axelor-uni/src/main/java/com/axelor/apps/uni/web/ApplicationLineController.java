package com.axelor.apps.uni.web;

import com.axelor.apps.uni.db.ApplicationLine;
import com.axelor.apps.uni.service.ApplicationLineService;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import java.time.LocalDateTime;
import java.util.Map;

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

  public void sendInterviewEmail(ActionRequest request, ActionResponse response)
      throws AxelorException {
    ApplicationLine applicationLine = request.getContext().asType(ApplicationLine.class);
    LocalDateTime dateTime = applicationLine.getInterviewDate();
    String interViewLink = applicationLine.getInterviewLink();

    if (dateTime == null || interViewLink == null) {
      response.setError("Please select the date and give the interview link.");
      return;
    }

    Map<String, Object> definitionMap = request.getContext().getParent();
    Beans.get(ApplicationLineService.class).sendInterviewEmail(applicationLine, definitionMap);
  }

  public void sendAcceptEmail(ActionRequest request, ActionResponse response)
      throws AxelorException {
    ApplicationLine applicationLine = request.getContext().asType(ApplicationLine.class);
    Beans.get(ApplicationLineService.class).sendAcceptEmail(applicationLine);
  }

  public void sendRejectEmail(ActionRequest request, ActionResponse response)
      throws AxelorException {
    ApplicationLine applicationLine = request.getContext().asType(ApplicationLine.class);
    Beans.get(ApplicationLineService.class).sendRejectEmail(applicationLine);
  }

  public void sendSelectionEmail(ActionRequest request, ActionResponse response)
      throws AxelorException {

    ApplicationLine applicationLine = request.getContext().asType(ApplicationLine.class);

    Map<String, Object> definitionMap = request.getContext().getParent();
    Beans.get(ApplicationLineService.class).sendSelectionEmail(applicationLine, definitionMap);
  }
}
