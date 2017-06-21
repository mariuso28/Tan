package org.rp.tester;


import org.apache.log4j.Logger;
import org.rp.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCsvImport 
{
	private static Logger log = Logger.getLogger(TestCsvImport.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"rp-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");

		try
		{
			gzServices.doPerformImport();
			log.info("Done");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
