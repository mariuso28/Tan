package org.rp.web.adm.member;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.web.adm.GzMemberVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser"})

@RequestMapping("/admMember")
public class GzAdmMemberController {

	private static Logger log = Logger.getLogger(GzAdmMemberController.class);
	@Autowired
	private GzServices gzServices;

	@RequestMapping(value="/accountDetails", method = RequestMethod.GET)
	public ModelAndView accountDetails(ModelMap model)
	{
		log.info("in accountDetails");
		AdmMemberForm admMemberForm = new AdmMemberForm();
		return new ModelAndView("admMemberDetails","admMemberForm",admMemberForm);
	}
	
	@RequestMapping(value="/processAdmMember", params="memberCancel", method = RequestMethod.POST)
	public Object memberCancel(ModelMap model)
	{
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		if (currUser.getRole().equals(GzRole.ROLE_ADMIN))
			return "redirect:/rp/adm/backToAdm";
		return null;
	}
	
	@RequestMapping(value="/processAdmMember", params="updatePassword", method = RequestMethod.POST)
	public Object updatePassword(@ModelAttribute("admMemberForm") AdmMemberForm admMemberForm,ModelMap model)
	{
		log.info("updating password");
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		GzMemberVerify verify = new GzMemberVerify();
		String errMsg = verify.verifyPassword(admMemberForm.getPassword(),admMemberForm.getvPassword());
		if (!errMsg.isEmpty())
		{
			log.info("error updating password : " + errMsg);
			admMemberForm.setErrMsg(errMsg);
			return new ModelAndView("admMemberDetails","admMemberForm",admMemberForm);
		}
		
		try
		{
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			currUser.setPassword(encoder.encode(admMemberForm.getPassword()));
			gzServices.getGzHome().updateBaseUserProfile(currUser);
		}
		catch (GzPersistenceException e)
		{
			e.printStackTrace();
			log.error("Updating password");
			admMemberForm.setErrMsg("Unexpected error updating password - contact support");
			return new ModelAndView("admMemberDetails","admMemberForm",admMemberForm);
		}
		
		log.info("Password successfully updated.");
		admMemberForm.setInfoMsg("Password successfully updated.");
		return new ModelAndView("admMemberDetails","admMemberForm",admMemberForm);
	}
}
