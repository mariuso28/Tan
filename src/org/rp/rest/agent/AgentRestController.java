package org.rp.rest.agent;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;
import org.rp.json.GzProfileJson;
import org.rp.json.GzResultJson;
import org.rp.services.GzServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agnt")
public class AgentRestController {

	private static final Logger log = Logger.getLogger(AgentRestController.class);
	
	@Autowired
	private GzServices gzServices;
	
	@RequestMapping(value="/getProfile", method=RequestMethod.POST)
	public GzResultJson getProfile()
	{
		String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		log.info(email);
		
		try {
			GzBaseUser bu = gzServices.getGzHome().getBaseUserByEmail(email);
			GzProfileJson prof = new GzProfileJson();
			prof.setEmail(bu.getEmail());
			prof.setPhone(bu.getPhone());
			prof.setContact(bu.getContact());
			
			GzResultJson rj = new GzResultJson();
			rj.success(prof);
			return rj;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			GzResultJson rj = new GzResultJson();
			rj.fail("Broke : " + e.getMessage());
			return rj;
		}
	
		
	}
}