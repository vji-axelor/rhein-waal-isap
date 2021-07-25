package com.axelor.apps.uni.service;

import java.util.HashSet;
import java.util.Set;

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

public class DefinitionServiceImpl implements DefinitionService {

	@Override
	@Transactional
	public void sendEmail(Definition definition) {
		System.err.println(definition.getTopic());
		
		Message emailmessage = new Message();
		Set<EmailAddress> emailSet = new HashSet<EmailAddress>();
//		emailSet.add(Beans.get(EmailAddressRepository.class).find(new Long(4)));
		
		emailmessage.setMediaTypeSelect(2);
		emailmessage.setStatusSelect(1);
		emailmessage.setSenderUser(AuthUtils.getUser());
		emailmessage.setSubject("Hello");
		emailmessage.setContent("hello world");
		Message m = Beans.get(MessageRepository.class).save(emailmessage);
		
		
		for(ApplicationLine def : definition.getApplicanStudents()) {
			if(def.getStudent() != null) {
				if(def.getStudent().getEmailAddress() != null) {
					EmailAddress studentEmail = def.getStudent().getEmailAddress();
					emailSet.add(studentEmail);
				}
			}
		}
		
		emailmessage.setMailAccount(Beans.get(EmailAccountRepository.class).find(new Long(1)));
		emailmessage.setToEmailAddressSet(emailSet);
		
		try {
			Beans.get(MessageService.class)
			.sendMessage(Beans.get(MessageRepository.class).find(m.getId()));
		} catch (AxelorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
