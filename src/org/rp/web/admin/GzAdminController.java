package org.rp.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.baseuser.GzProfile;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.util.StackDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes({"currUser","currStackDump"})

@RequestMapping("/admin")
public class GzAdminController {

	private static Logger log = Logger.getLogger(GzAdminController.class);	
	private GzServices gzServices;
	
	
	// /rp/admin/logon?user&email=
	@RequestMapping(value = "/logon", params="user", method = RequestMethod.GET)
	public Object signin(String email,ModelMap model,HttpServletRequest request,RedirectAttributes stack) {
		
		log.info("Received request to logon");
			
		GzAdmin currUser = null;
	
		try {
			currUser = gzServices.getGzHome().getAdminByEmail(email);
		} catch (GzPersistenceException e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			stack.addFlashAttribute("errMsg",stackDump);
			return "redirect:/rp/logon/errStackDump";
		}
	
		model.addAttribute("currUser",currUser);
		
		
		HttpSession session = request.getSession();						// have to set in the session as end up in other controllers
		log.trace("Setting session attribute : sCurrUser : " +  currUser );
		session.setAttribute("sCurrUser", currUser);	
		
		return goAdminHome("",model,stack);
	}
	
	// exec?returnAdmin
	@RequestMapping(value = "/exec", params = "returnAdmin", method = RequestMethod.GET)
    public Object returnAdmin(ModelMap model,HttpServletRequest request,RedirectAttributes stack)
    {
		HttpSession session = request.getSession(false);	
		GzAdmin currUser = (GzAdmin) session.getAttribute("sCurrUser");				
		model.addAttribute("currUser",currUser);
		
		return goAdminHome("",model,stack);
    }
	
	
	@RequestMapping(value = "/exec", params="modifyAdmin", method = RequestMethod.POST)
	public Object modifyAdmin(@ModelAttribute("gzAdminForm") GzAdminForm gzAdminForm,ModelMap model,RedirectAttributes stack)
	{
		GzAdminCommand command = gzAdminForm.getCommand();
		GzProfile profile = command.getProfile();
		
		log.info("Modify profile");
		
		String errMsg = profile.validate(command.getVerifyPassword(),true,GzRole.ROLE_ADMIN); 
		if (errMsg.isEmpty())
		{
			GzAdmin currUser = (GzAdmin) model.get("currUser");
			currUser.copyProfileValues(profile);
			try {
				gzServices.getGzHome().updateBaseUserProfile(currUser);
			} catch (GzPersistenceException e) {
				String stackDump = StackDump.toString(e);
				log.error(stackDump);
				stack.addFlashAttribute("errMsg",stackDump);
				return "redirect:/rp/logon/errStackDump";
			}
		}
		
		return goAdminHome(errMsg,model,stack);
	}
	
	private Object goAdminHome(String errMsg,ModelMap model,RedirectAttributes stack){
		
		GzAdminForm adminForm = new GzAdminForm();
		
		return new ModelAndView("adminHome","adminForm", adminForm);
	}
	
	
	@Autowired
	public void setServices(GzServices gzServices)
	{
		this.setGzServices(gzServices);
	}

	public GzServices getGzServices() {
		return gzServices;
	}

	public void setGzServices(GzServices gzServices) {
		this.gzServices = gzServices;
	}
}
