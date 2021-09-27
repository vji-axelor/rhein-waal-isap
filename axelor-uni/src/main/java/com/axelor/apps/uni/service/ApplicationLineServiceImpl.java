package com.axelor.apps.uni.service;

import com.axelor.apps.message.db.EmailAddress;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.EmailAccountRepository;
import com.axelor.apps.message.db.repo.MessageRepository;
import com.axelor.apps.message.service.MessageService;
import com.axelor.apps.uni.db.ApplicationLine;
import com.axelor.auth.AuthUtils;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApplicationLineServiceImpl implements ApplicationLineService {

  @Override
  public String setUniqueApplicant(ApplicationLine line) {
    String mtNumber = line.getStudent().getMatriculationNumber();
    String topic = line.getDefinition().getTopic();

    String uniqueName = mtNumber + "_" + topic;

    return uniqueName;
  }

  @Override
  @Transactional
  public void sendInterviewEmail(ApplicationLine line, Map<String, Object> map)
      throws AxelorException {
    Message rejectedStudentEmailmessage = new Message();

    rejectedStudentEmailmessage.setMediaTypeSelect(2);
    rejectedStudentEmailmessage.setStatusSelect(1);
    rejectedStudentEmailmessage.setSenderUser(AuthUtils.getUser());
    rejectedStudentEmailmessage.setMailAccount(
        Beans.get(EmailAccountRepository.class).all().fetchOne());
    rejectedStudentEmailmessage.setSubject("Rhein Waal Student Exchange Program");

    String defTopic = (String) map.get("topic");
    String content =
        "Dear Student,<br>You are invited for&nbsp;ISAP&nbsp;interview form Rhein waal.&nbsp;&nbsp;<br><br>Topic: <span style=\\\"font-weight: bold;\\\"> "
            + defTopic
            + "</span><br>Date:<span style=\"font-weight: 700;\">"
            + line.getInterviewDate()
            + "</span><br>Link:&nbsp;<span style=\"font-weight: 700;\">"
            + line.getInterviewLink()
            + "</span>";

    rejectedStudentEmailmessage.setContent(content);

    Set<EmailAddress> applicant = new HashSet<EmailAddress>();

    if (line.getStudent().getEmailAddress() == null) {
      throw new AxelorException(0, "Student does not have email address...");
    }
    applicant.add(line.getStudent().getEmailAddress());

    rejectedStudentEmailmessage.setToEmailAddressSet(applicant);
    System.err.println(rejectedStudentEmailmessage);
    Message emailMessage = Beans.get(MessageRepository.class).save(rejectedStudentEmailmessage);
    Beans.get(MessageService.class)
        .sendMessage(Beans.get(MessageRepository.class).find(emailMessage.getId()));
  }

  @Override
  @Transactional
  public void sendSelectionEmail(ApplicationLine line, Map<String, Object> map)
      throws AxelorException {
    Message rejectedStudentEmailmessage = new Message();

    rejectedStudentEmailmessage.setMediaTypeSelect(2);
    rejectedStudentEmailmessage.setStatusSelect(1);
    rejectedStudentEmailmessage.setSenderUser(AuthUtils.getUser());
    rejectedStudentEmailmessage.setMailAccount(
        Beans.get(EmailAccountRepository.class).all().fetchOne());
    rejectedStudentEmailmessage.setSubject("Rhein Waal Student Exchange Program: Selected Student");

    String defTopic = (String) map.get("topic");
    String content =
        "Dear Student,<br>You are Selected for&nbsp;ISAP&nbsp;student exchange program, Please accept this offer from application platform.&nbsp;&nbsp;<br><br>Topic: <span style=\\\"font-weight: bold;\\\"> "
            + defTopic
            + "</span><br>Matriculation Number :<span style=\"font-weight: 700;\">"
            + line.getStudent().getMatriculationNumber()
            + "</span>";

    rejectedStudentEmailmessage.setContent(content);

    Set<EmailAddress> applicant = new HashSet<EmailAddress>();

    if (line.getStudent().getEmailAddress() == null) {
      throw new AxelorException(0, "Student does not have email address...");
    }
    applicant.add(line.getStudent().getEmailAddress());

    rejectedStudentEmailmessage.setToEmailAddressSet(applicant);
    System.err.println(rejectedStudentEmailmessage);
    Message emailMessage = Beans.get(MessageRepository.class).save(rejectedStudentEmailmessage);
    Beans.get(MessageService.class)
        .sendMessage(Beans.get(MessageRepository.class).find(emailMessage.getId()));
  }

  @Override
  @Transactional
  public void sendAcceptEmail(ApplicationLine line) throws AxelorException {
    Message acceptOffer = new Message();

    acceptOffer.setMediaTypeSelect(2);
    acceptOffer.setStatusSelect(1);
    acceptOffer.setSenderUser(AuthUtils.getUser());
    acceptOffer.setMailAccount(Beans.get(EmailAccountRepository.class).all().fetchOne());
    acceptOffer.setSubject("Rhein Waal Student Exchange Program: Accept Offer");
    String defTopic = line.getDefinition().getTopic();

    String content =
        "Student has accepted offer for exchange semester.&nbsp;&nbsp;<br><br>Topic: <span style=\\\"font-weight: bold;\\\"> "
            + defTopic
            + "</span><br>Status:<span style=\"font-weight: 700;\">"
            + "Accepted"
            + "</span><br>Matriculation Number :<span style=\"font-weight: 700;\">"
            + line.getStudent().getMatriculationNumber()
            + "</span>";

    acceptOffer.setContent(content);

    Set<EmailAddress> applicant = new HashSet<EmailAddress>();

    if (line.getStudent().getEmailAddress() == null) {
      throw new AxelorException(0, "Student does not have email address...");
    }
    if (line.getDefinition().getProjectLeader().getEmailAddress() == null) {
      throw new AxelorException(
          0, "Project leader does not have email address please contect head of department...");
    }
    applicant.add(line.getStudent().getEmailAddress());
    applicant.add(line.getDefinition().getProjectLeader().getEmailAddress());

    acceptOffer.setToEmailAddressSet(applicant);
    Message emailMessage = Beans.get(MessageRepository.class).save(acceptOffer);
    Beans.get(MessageService.class)
        .sendMessage(Beans.get(MessageRepository.class).find(emailMessage.getId()));
  }

  @Transactional
  public void sendRejectEmail(ApplicationLine line) throws AxelorException {
    Message acceptOffer = new Message();

    acceptOffer.setMediaTypeSelect(2);
    acceptOffer.setStatusSelect(1);
    acceptOffer.setSenderUser(AuthUtils.getUser());
    acceptOffer.setMailAccount(Beans.get(EmailAccountRepository.class).all().fetchOne());
    acceptOffer.setSubject("Rhein Waal Student Exchange Program: Reject Offer");
    String defTopic = line.getDefinition().getTopic();

    String content =
        "Student has rejected offer for exchange semester.&nbsp;&nbsp;<br><br>Topic: <span style=\\\"font-weight: bold;\\\"> "
            + defTopic
            + "</span><br>Status:<span style=\"font-weight: 700;\">"
            + "Rejected"
            + "</span><br>Matriculation Number :<span style=\"font-weight: 700;\">"
            + line.getStudent().getMatriculationNumber()
            + "</span>";

    acceptOffer.setContent(content);

    Set<EmailAddress> applicant = new HashSet<EmailAddress>();

    if (line.getStudent().getEmailAddress() == null) {
      throw new AxelorException(0, "Student does not have email address...");
    }
    if (line.getDefinition().getProjectLeader().getEmailAddress() == null) {
      throw new AxelorException(
          0, "Project leader does not have email address please contect head of department...");
    }
    applicant.add(line.getStudent().getEmailAddress());
    applicant.add(line.getDefinition().getProjectLeader().getEmailAddress());

    acceptOffer.setToEmailAddressSet(applicant);
    Message emailMessage = Beans.get(MessageRepository.class).save(acceptOffer);
    Beans.get(MessageService.class)
        .sendMessage(Beans.get(MessageRepository.class).find(emailMessage.getId()));
  }
}
