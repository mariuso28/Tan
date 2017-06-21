package org.rp.tester;


import java.util.List;

import org.apache.log4j.Logger;
import org.rp.account.GzInvoice;
import org.rp.baseuser.GzBaseUser;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInvoices 
{
	private static Logger log = Logger.getLogger(TestInvoices.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"gz-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHome gzHome = gzServices.getGzHome();
		
		try
		{
		GzBaseUser bu = gzHome.getBaseUserByEmail("pmk@test.com");
		GzBaseUser bu1 = gzHome.getBaseUserByEmail("ringo@test.com");
		List<GzInvoice> invoices = gzHome.getOutstandingInvoices(bu,bu1);
		for (GzInvoice inv : invoices)
		{
			log.info(inv.getTimestamp());
		}
		
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
