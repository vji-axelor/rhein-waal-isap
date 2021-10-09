package com.axelor.apps.uni.web;

import com.axelor.apps.uni.db.StudentDeparture;
import com.axelor.apps.uni.service.StudentDepartureService;
import com.axelor.inject.Beans;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class StudentDepartureController {

  public void acceptDocumentNotify(ActionRequest request, ActionResponse response) {
    StudentDeparture studentDeparture = request.getContext().asType(StudentDeparture.class);

    if (!studentDeparture.getIsStudentLa()) {
      response.setError("The learning agreement process still not completed.");
    }

    if (!studentDeparture.getIsSignCoordinator() || !studentDeparture.getIsSignHod()) {
      response.setError("The learning agreement process from university still not completed.");
    }

    if (!studentDeparture.getIsAdmissionLetter()) {
      response.setError("The admission letter not attached.");
    }

    if (!studentDeparture.getIsStipendAcceptance() || !studentDeparture.getIsSafetyBriefing()) {
      response.setError("The acquisition of ISAP stipend process still not completed.");
    }

    if (!studentDeparture.getIsFlightBooking()
        || !studentDeparture.getIsValidVisa()
        || !studentDeparture.getIsValidInsurance()) {
      response.setError("The travel preparation documents process still not completed.");
    }

    Beans.get(StudentDepartureService.class).acceptDocEmail(studentDeparture);
    response.setValue("statusSelect", 5);
  }
  
  public void startAbroadArrivalProcess(ActionRequest request, ActionResponse response) {
	  StudentDeparture studentDeparture = request.getContext().asType(StudentDeparture.class);
	  Beans.get(StudentDepartureService.class).createAbroadArrival(studentDeparture);
	  
  }
}
