package com.axelor.apps.uni.service;

import com.axelor.apps.message.db.EmailAddress;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.EmailAccountRepository;
import com.axelor.apps.message.db.repo.MessageRepository;
import com.axelor.apps.message.service.MessageService;
import com.axelor.apps.uni.db.ApplicationLine;
import com.axelor.apps.uni.db.Definition;
import com.axelor.auth.AuthUtils;
import com.axelor.exception.AxelorException;
import com.axelor.inject.Beans;
import com.google.inject.persist.Transactional;
import java.util.HashSet;
import java.util.Set;

public class DefinitionServiceImpl implements DefinitionService {

  @Override
  @Transactional
  public void sendEmail(Definition definition) {
    System.err.println(definition.getTopic());

    Message selectedStudentEmailmessage = new Message();

    selectedStudentEmailmessage.setMediaTypeSelect(2);
    selectedStudentEmailmessage.setStatusSelect(1);
    selectedStudentEmailmessage.setSenderUser(AuthUtils.getUser());
    selectedStudentEmailmessage.setMailAccount(
        Beans.get(EmailAccountRepository.class).all().fetchOne());
    selectedStudentEmailmessage.setSubject(definition.getTopic() + " : Result Announcement");
    selectedStudentEmailmessage.setContent(
        "Congratulation..!! You are selected for student exchange program.");

    Message rejectedStudentEmailmessage = new Message();

    rejectedStudentEmailmessage.setMediaTypeSelect(2);
    rejectedStudentEmailmessage.setStatusSelect(1);
    rejectedStudentEmailmessage.setSenderUser(AuthUtils.getUser());
    rejectedStudentEmailmessage.setMailAccount(
        Beans.get(EmailAccountRepository.class).all().fetchOne());
    rejectedStudentEmailmessage.setSubject(definition.getTopic() + " : Result Announcement");
    rejectedStudentEmailmessage.setContent(
        "Sorry to inform you that you are not full fill the requirement for student exchange program.");

    Set<EmailAddress> selectedStudentEmailSet = new HashSet<EmailAddress>();
    Set<EmailAddress> rejectedStudentEmailSet = new HashSet<EmailAddress>();

    for (ApplicationLine def : definition.getApplicanStudents()) {
      if (def.getStudent() != null && def.getApplicantSelect() == 3) {
        if (def.getStudent().getEmailAddress() != null) {
          // notify
          EmailAddress studentEmail = def.getStudent().getEmailAddress();
          selectedStudentEmailSet.add(studentEmail);
        }
      }

      if (def.getStudent() != null && def.getApplicantSelect() != 3) {
        if (def.getStudent().getEmailAddress() != null) {
          EmailAddress studentEmail = def.getStudent().getEmailAddress();
          rejectedStudentEmailSet.add(studentEmail);
        }
      }
    }

    selectedStudentEmailmessage.setToEmailAddressSet(selectedStudentEmailSet);
    rejectedStudentEmailmessage.setToEmailAddressSet(rejectedStudentEmailSet);

    Message positiveMessage = Beans.get(MessageRepository.class).save(selectedStudentEmailmessage);
    Message nagativeMessage = Beans.get(MessageRepository.class).save(rejectedStudentEmailmessage);

    try {
      Beans.get(MessageService.class)
          .sendMessage(Beans.get(MessageRepository.class).find(positiveMessage.getId()));
      Beans.get(MessageService.class)
          .sendMessage(Beans.get(MessageRepository.class).find(nagativeMessage.getId()));
    } catch (AxelorException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
