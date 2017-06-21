package org.rp.tester;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TestSetBaseUserPassword 
{
	private static Logger log = Logger.getLogger(TestSetBaseUserPassword.class);
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"rp-persistence.xml");

	//	GzServices gzServices = (GzServices) context.getBean("gzServices");
		
		GzHome home = (GzHome) context.getBean("gzHome"); // gzServices.getGzHome();
		if (args.length > 0)
		{
			log.info(("Running with datasource: " + args[0]));
			home.overrideDataSourceUrl(args[0]);
		}
		
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		
		String encoded = encoder.encode("88888888");
		
		home.setDefaultPasswordForAll(encoded);
		
		try {
			GzBaseUser hb = home.getBaseUserByEmail("c1@rpco.com");
			log.info("matches : " + encoder.matches("88888888", hb.getPassword()));
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.info("All passwords successfully reset to 88888888");
	}
}
