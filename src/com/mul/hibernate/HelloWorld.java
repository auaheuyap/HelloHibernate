package com.mul.hibernate;

import java.util.*;
import org.hibernate.*;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HelloWorld {
	public static void main(String[] args) {
		// First unit of work
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		Message message = new Message("Hello World");
		Long msgId = (Long) session.save(message);
		tx.commit();
		session.close();
		
		// Second unit of work
		Session newSession = HibernateUtil.getSessionFactory().openSession();
		Transaction newTransaction = newSession.beginTransaction();
		List messages = newSession.createQuery("from Message m order by m.text asc").list();
		System.out.println( messages.size() +" message(s) found:" );
		
		for ( Iterator iter = messages.iterator(); iter.hasNext(); ) {
				Message loadedMsg = (Message) iter.next();
				System.out.println( loadedMsg.getText() );
				}
		newTransaction.commit();
		newSession.close();
		
		// Third unit of work
		Session thirdSession = HibernateUtil.getSessionFactory().openSession();
		Transaction thirdTransaction = thirdSession.beginTransaction();
		// msgId holds the identifier value of the first message
		message = (Message) thirdSession.get( Message.class, msgId );
		message.setText( "Greetings Earthling" );
		message.setNextMessage(new Message( "Take me to your leader (please)" ));
		
		messages = thirdSession.createQuery("from Message m order by m.text asc").list();
		System.out.println( messages.size() +" message(s) found:" );
		for ( Iterator iter = messages.iterator(); iter.hasNext(); ) {
			Message loadedMsg = (Message) iter.next();
			System.out.println( loadedMsg.getText() );
			}
		
		thirdTransaction.commit();
		thirdSession.close();
		
		// Shutting down the application
		HibernateUtil.shutdown();
	}
}