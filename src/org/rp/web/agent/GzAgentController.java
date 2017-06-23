package org.rp.web.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.agent.GzAgent;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzProfile;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.util.StackDump;
import org.rp.web.member.GzMemberEntryForm;
import org.rp.web.member.GzMemberForm;
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
@SessionAttributes({"currUser","currCreateRole","currStackDump","currOIM","currExpandedMembers"})

@RequestMapping("/agnt")
public class GzAgentController {

	private static Logger log = Logger.getLogger(GzAgentController.class);	
	private GzServices gzServices;
	
	
	@RequestMapping(value = "/viewCompanies", method = RequestMethod.GET)
    public ModelAndView viewCompanies(ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		GzAdmin currUser = (GzAdmin) session.getAttribute("sCurrUser");				// as the thing comes from AdminController
		log.trace("got session attribute : currUser : " +  currUser );
		
		model.addAttribute("currUser",currUser);
		HashMap<String, GzBaseUser> expandedMembers = new HashMap<String, GzBaseUser>();
		model.addAttribute("currExpandedMembers", expandedMembers);
	
		GzMemberForm memberForm = new GzMemberForm();
		return new ModelAndView("memberHome","memberForm",memberForm);
    }
	
	@RequestMapping(value = "/processAgent", params = "goMemberHome", method = RequestMethod.GET)
    public Object goMemberHome(ModelMap model,HttpServletRequest request,RedirectAttributes stack) {
       
		HttpSession session = request.getSession(false);	
		String email = (String) session.getAttribute("email");
		
		GzAgent agent;
		try {
			agent = gzServices.getGzHome().getAgentByEmail(email);
		} catch (GzPersistenceException e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			stack.addFlashAttribute("errMsg",stackDump);
			return "redirect:/rp/logon/errStackDump";
		}
		model.addAttribute("currUser",agent);
		HashMap<String, GzBaseUser> expandedMembers = new HashMap<String, GzBaseUser>();
		model.addAttribute("currExpandedMembers", expandedMembers);
	
		return "redirect:backtoMemberHome";
    }
	
	// backtoMemberHome&errMes=
	@RequestMapping(value = "/backtoMemberHomeErr", method = RequestMethod.GET)
	public ModelAndView backtoMemberHomeErr(String errMsg,ModelMap model,HttpServletRequest request)
	{
		model.addAttribute("errMsg",errMsg);
		return backtoMemberHome(model,request);
	}
	
	@RequestMapping(value = "/backtoMemberHome", method = RequestMethod.GET)
	public ModelAndView backtoMemberHome(ModelMap model,HttpServletRequest request)
	{
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		try {
			GzBaseUser user = gzServices.getGzHome().getBaseUserByCode(currUser.getCode());
			gzServices.getGzHome().getAccount(user);
			gzServices.getGzHome().getDownstreamForParent(user);
			model.addAttribute("currUser",user);
		} catch (GzPersistenceException e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
		}
		
		HttpSession session = request.getSession(false);
		session.setAttribute("sCurrUser",currUser);
		
		HashMap<String, GzBaseUser> expandedMembers = new HashMap<String, GzBaseUser>();
		model.addAttribute("currExpandedMembers", expandedMembers);

		setUpOutstandingInvoiceMap(currUser,model);
		
		GzMemberForm memberForm = new GzMemberForm();
		
		String errMsg = (String) model.get("errMsg");
		if (errMsg != null)
		{
			memberForm.setErrMsg(errMsg);
			model.remove("errMsg");
		}
		
		return new ModelAndView("memberHome","memberForm",memberForm);
	}
	
	// processAgent?method=expand&code
	@RequestMapping(value = "/processAgent", params = "method=expand", method = RequestMethod.GET)
	public ModelAndView expandMember(String code,ModelMap model) {
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");


		@SuppressWarnings("unchecked")
		HashMap<String, GzBaseUser> expandedMembers = (HashMap<String, GzBaseUser>) model.get("currExpandedMembers");

		if (expandedMembers.containsKey(code))
		{
			// behaves like a toggle but remove sub ones as well
			log.trace("expandMember: Removing code : " + code);
			Iterator<String> iter = expandedMembers.keySet().iterator();
			List<String> removeKeys = new ArrayList<String>();
			while (iter.hasNext())
			{
				String key = iter.next();
				log.trace("expandMember: testing : " + key);
				if (key.startsWith(code))
					removeKeys.add(key);
			}
			for (String key:removeKeys)
			{
				log.trace("expandMember: Removing key : " + key);
				expandedMembers.remove(key);	
			}
		}
		else
		{
			try {
				GzBaseUser user = gzServices.getGzHome().getBaseUserByCode(code);
				gzServices.getGzHome().getDownstreamForParent(user);
				expandedMembers.put(code,user);
			} catch (GzPersistenceException e) {
				
				log.error(StackDump.toString(e));
				return null;
			}
			
		}

		model.addAttribute("currExpandedMembers",expandedMembers);	
		model.addAttribute("currUser",currUser);

		log.trace("expandMembers : " + expandedMembers);

		return new ModelAndView("memberHome","memberForm", new GzMemberForm());
	}
	
	
	@RequestMapping(value = "/processAgent", params = "memberCreate", method = RequestMethod.POST)
    public Object memberCreate(@ModelAttribute("gzMemberForm") GzMemberForm gzMemberForm,ModelMap model,
    		HttpServletRequest request) {
       
		GzRole role = GzRole.valueOf(gzMemberForm.getCreateRole());
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		if (role.getRank()>=currUser.getRole().getRank())
		{
			return "redirect:../logon/access_denied";
		}
		
		model.addAttribute("currCreateRole", role);
		
		GzMemberEntryForm memberCreateForm = new GzMemberEntryForm();
		return new ModelAndView("memberCreate","memberCreateForm",memberCreateForm);
    }
	
	@RequestMapping(value = "/processAgent", params = "memberSave", method = RequestMethod.POST)
    public Object memberSave(@ModelAttribute("gzMemberCreateForm") GzMemberEntryForm gzMemberCreateForm,
    										ModelMap model,RedirectAttributes stack,HttpServletRequest request) {
       
		GzProfile profile = gzMemberCreateForm.getProfile();
		
		log.info("Storing member : " + profile);
		
		GzRole role = (GzRole) model.get("currCreateRole");
		String errMsg = profile.validate("",false,role);
		if (!errMsg.isEmpty())
		{
			GzMemberEntryForm memberCreateForm = new GzMemberEntryForm();
			memberCreateForm.setInCompleteProfile(profile);
			memberCreateForm.setErrMsg(errMsg);
			log.info("Error on member profile : " + errMsg);
			return new ModelAndView("memberCreate","memberCreateForm",memberCreateForm);
		}
		
		GzBaseUser parent = (GzBaseUser) model.get("currUser");
		GzBaseUser newMember = null; 
		
		try {
			if (role.equals(GzRole.ROLE_PLAY))
				newMember = new GzBaseUser();
			else
				newMember = (GzAgent) role.getCorrespondingClass().newInstance();
				
			// String pw = GzUserPasswordService.autoGeneratePassword();
		
			String pw = "88888888";
			profile.setPassword(pw);
					
			newMember.setEmail(profile.getEmail());
			newMember.copyProfileValues(profile);
			newMember.setParent(parent);
			newMember.setParentCode(parent.getCode());
			newMember.setRole(role);
			newMember.setEnabled(true);
			
			if (role.equals(GzRole.ROLE_PLAY))
				gzServices.getGzHome().storeBaseUser(newMember);
			else
				gzServices.getGzHome().storeAgent((GzAgent) newMember);
		
			if (gzServices.getProperties().get("passwordGeneration").equals("on"))
			{
			// this is where the mail of password happens
				gzServices.resetPassword(newMember, true);
			}
			
		} catch (Exception e) {
			
			if (e.getMessage().contains("duplicate key value violates unique constraint \"ad_uq_email\""))
			{
				GzMemberEntryForm memberCreateForm = new GzMemberEntryForm();
				memberCreateForm.getInCompleteProfile().setContact(profile.getContact());
				memberCreateForm.getInCompleteProfile().setPhone(profile.getPhone());
				memberCreateForm.getInCompleteProfile().setNickname(profile.getNickname());
				memberCreateForm.setErrMsg("Member with email : " + profile.getEmail() + " - already exists on platform - please choose another.");
				return new ModelAndView("memberCreate","memberCreateForm",memberCreateForm);
			}
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			stack.addFlashAttribute("errMsg",stackDump);
			return "redirect:/rp/logon/errStackDump";
		}
		
		
		return "redirect:backtoMemberHome";
    }
	
	@RequestMapping(value = "/processAgent", params = "memberCancel", method = RequestMethod.POST)
    public Object memberCancel(ModelMap model)
    {
		return "redirect:backtoMemberHome";
    }
	
	/*
	// from external controller
	@RequestMapping(value = "/processAgent", params = "editAgentRedirect", method = RequestMethod.GET)
    public ModelAndView editAgentRedirect(ModelMap model,HttpServletRequest request)
    {
		HttpSession session = request.getSession(false);	
		GzBaseUser currUser = (GzBaseUser) session.getAttribute("sCurrUser");
		
		model.addAttribute("currUser",currUser);
		
		return editAgent(model);
    }
	*/
	
	@RequestMapping(value = "/processAgent", params = "editAgent", method = RequestMethod.GET)
    public ModelAndView editAgent(ModelMap model)
    {
		GzMemberEntryForm memberEditForm = new GzMemberEntryForm();
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		memberEditForm.setInCompleteProfile(currUser.createProfile());
		
		return new ModelAndView("agentEdit","agentEditForm",memberEditForm);
    }
	
	@RequestMapping(value = "/processAgent", params = "saveAgent", method = RequestMethod.POST)
    public Object saveAgent(@ModelAttribute("gzMemberCreateForm") GzMemberEntryForm gzMemberCreateForm,
			ModelMap model,RedirectAttributes stack)
    {
		GzProfile profile = gzMemberCreateForm.getProfile();
		
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		String errMsg = profile.validate(gzMemberCreateForm.getvPassword(),true,currUser.getRole());
		if (!errMsg.isEmpty())
		{
			GzMemberEntryForm memberEditForm = new GzMemberEntryForm();
			memberEditForm.setInCompleteProfile(profile);
			memberEditForm.setErrMsg(errMsg);
			return new ModelAndView("agentEdit","agentEditForm",memberEditForm);
		}
		
		currUser.copyProfileValues(profile);
		try {
			gzServices.getGzHome().updateBaseUserProfile(currUser);
			
		} catch (Exception e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			stack.addFlashAttribute("errMsg",stackDump);
			return "redirect:/rp/logon/errStackDump";
		}
		
		return "redirect:backtoMemberHome";
    }
	
	private void setUpOutstandingInvoiceMap(GzBaseUser currUser,ModelMap model)
	{
		log.trace("SETTING UP OUTSTANDING INVOICE MAP");
		Map<UUID,Double> currOIM = new HashMap<UUID,Double>();
		try {
			currOIM = gzServices.getGzHome().getOutstandingInvoiceAmounts(currUser);
		} catch (GzPersistenceException e) {
			e.getStackTrace();
			log.error("Could not getOutstandingInvoiceAmounts " + e.getMessage() );
		}
		model.addAttribute("currOIM",currOIM);
	}
	
	@RequestMapping(value = "/processAgent", params = "updateInvoices", method = RequestMethod.GET)
    public Object updateInvoices(ModelMap model)
    {
		try {
			gzServices.closeOpenInvoices();
		} catch (Exception e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			return "redirect:backtoMemberHomeErr?errMsg=Update Invoices Failed please contact support".replace(" ","%20");
		}
		
		return "redirect:backtoMemberHome";
    }
	
	@RequestMapping(value = "/processAgent", params = "importCsvs", method = RequestMethod.GET)
    public Object importCsvs(ModelMap model,HttpServletRequest request)
    {
		try {
			gzServices.performImport();
		} catch (Exception e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			return "redirect:backtoMemberHomeErr?errMsg=Import Cvs Failed please contact support".replace(" ","%20");
		}
		
		return "redirect:backtoMemberHome?";
    }
	
	@RequestMapping(value = "/processAgent", params = "clearAllTurnovers", method = RequestMethod.GET)
    public Object clearAllTurnovers(ModelMap model)
    {
		try {
			gzServices.getGzHome().clearTurnoversAndDistributions();
		} catch (Exception e) {
			String stackDump = StackDump.toString(e);
			log.error(stackDump);
			return "redirect:backtoMemberHomeErr?errMsg=Clear All Turnovers Failed please contact support".replace(" ","%20");
		}
		
		return "redirect:backtoMemberHome?";
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
