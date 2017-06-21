package org.rp.tester;

import org.apache.log4j.Logger;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.baseuser.GzBaseUser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSetBaseUserLeafInstance 
{
	private static Logger log = Logger.getLogger(TestSetBaseUserLeafInstance.class);
	
	static void setLeafInstance(GzBaseUser user,GzHome home)
	{
		String code = user.getCode();
		int index = code.length()-1;
		if (!Character.isDigit(code.charAt(index)))
			return;
		while (Character.isDigit(code.charAt(index)))
			index--;
		String str = "";
		index++;
		while (index<code.length())
		{
			str += code.charAt(index);
			index++;
		}
		
		long leafInstance = Long.parseLong(str);
		log.info("setting leaf instance for code : " + code + " to : " + leafInstance);
		user.setLeafInstance(leafInstance);
		home.updateLeafInstance(user);
		
		for (GzBaseUser member : user.getMembers())
			setLeafInstance(member,home);
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"gz-service.xml");

		GzServices gzServices = (GzServices) context.getBean("gzServices");
		GzHome home = gzServices.getGzHome();
	
		try {
			GzBaseUser comp = home.getAgentByCode("c0");
			
			home.getDownstreamForParent(comp);
			
			setLeafInstance(comp,home);
			
			
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
