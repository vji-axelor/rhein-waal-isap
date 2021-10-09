package com.axelor.apps.uni.service;

import com.axelor.apps.message.db.EmailAddress;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.EmailAccountRepository;
import com.axelor.apps.message.db.repo.MessageRepository;
import com.axelor.apps.message.service.MessageService;
import com.axelor.apps.uni.db.StudentAbroadArrival;
import com.axelor.apps.uni.db.StudentDeparture;
import com.axelor.apps.uni.db.repo.StudentAbroadArrivalRepository;
import com.axelor.auth.AuthUtils;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;
import java.util.HashSet;
import java.util.Set;

public class StudentDepartureServiceImpl implements StudentDepartureService {

  @Override
  @Transactional
  public void acceptDocEmail(StudentDeparture studentDeparture) {
    Message acceptDocEmail = new Message();

    acceptDocEmail.setMediaTypeSelect(2);
    acceptDocEmail.setStatusSelect(1);
    acceptDocEmail.setSenderUser(AuthUtils.getUser());
    acceptDocEmail.setMailAccount(Beans.get(EmailAccountRepository.class).all().fetchOne());
    acceptDocEmail.setSubject(
        studentDeparture.getDefinition().getTopic() + " : Completed departure documents");

    String content =
        "Dear Student,<br>all student departure documents accepted.&nbsp;&nbsp;<br><br>Topic: <span style=\\\"font-weight: bold;\\\"> "
            + studentDeparture.getDefinition().getTopic()
            + "</span><br>Document Status:<span style=\"font-weight: 700;\">"
            + "Accepted"
            + "</span>";

    acceptDocEmail.setContent(content);

    Set<EmailAddress> selectedStudentEmailSet = new HashSet<EmailAddress>();
    EmailAddress studentEmail = studentDeparture.getApplicant().getStudent().getEmailAddress();
    selectedStudentEmailSet.add(studentEmail);

    acceptDocEmail.setToEmailAddressSet(selectedStudentEmailSet);

    Message positiveMessage = Beans.get(MessageRepository.class).save(acceptDocEmail);

    try {
      Beans.get(MessageService.class)
          .sendMessage(Beans.get(MessageRepository.class).find(positiveMessage.getId()));
    } catch (AxelorException e) {
      e.printStackTrace();
    }
  }

	@Override
	@Transactional
	public void createAbroadArrival(StudentDeparture studentDeparture) {
		StudentAbroadArrival abroadArrival = new StudentAbroadArrival();

		if(studentDeparture.getProjectLeader() != null) {
			abroadArrival.setProjectLeader(studentDeparture.getProjectLeader());
		}
		if(studentDeparture.getApplicant() != null) {
			abroadArrival.setApplicant(studentDeparture.getApplicant());
		}
		if(studentDeparture.getUserStudent() != null) {
			abroadArrival.setUserStudent(studentDeparture.getUserStudent());
		}
		if(studentDeparture.getDefinition() != null) {
			abroadArrival.setDefinition(studentDeparture.getDefinition());
		}
		
		Beans.get(StudentAbroadArrivalRepository.class).save(abroadArrival);
	}
}
