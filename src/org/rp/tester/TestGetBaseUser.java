package org.rp.tester;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGetBaseUser 
{
	private static Logger log = Logger.getLogger(TestGetBaseUser.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"gz-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHome home = gzServices.getGzHome();
		
		try {		
			GzBaseUser bu = home.getBaseUserByEmail("pmk@test.com");
			log.info(bu.getEmail() + " : " + bu.getContact());
			
			
		//	home.updateBaseUserProfile(bu);
		//	bu = home.getBaseUserByEmail("pmk@test.com");
			log.info(bu.getEmail() + " : " + bu.getContact());
			
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
