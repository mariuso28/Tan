package org.rp.tester;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGetAdmin 
{
	private static Logger log = Logger.getLogger(TestGetAdmin.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"rp-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHome gzHome = gzServices.getGzHome();
		
		try {
			
				GzAdmin bu = gzHome.getAdminByEmail("rpadmin@test.com");
				gzHome.getDownstreamForParent(bu);
				log.info("DONE : " + bu.getCode() + " " + bu.getClass());
			} catch (GzPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
