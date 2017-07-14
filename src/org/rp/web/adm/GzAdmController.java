package org.rp.web.adm;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.agent.GzAgent;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.web.admin.GzAdminController;
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

@RequestMapping("/adm")
public class GzAdmController {

	private static Logger log = Logger.getLogger(GzAdminController.class);
	@Autowired
	private GzServices gzServices;
	
	// /rp/adm/logon?user&email=
		@RequestMapping(value = "/logon", params="user", method = RequestMethod.GET)
		public Object signin(String email,ModelMap model,HttpServletRequest request) {
			
			log.info("Received request to logon");
				
			GzAdmin currUser = null;
		
			try {
				currUser = gzServices.getGzHome().getAdminByEmail(email);
			} catch (GzPersistenceException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return backToSignin(model,"Unxepected error on Admin - contact support");
			}
		
			model.addAttribute("currUser",currUser);
			
			
			HttpSession session = request.getSession();						// have to set in the session as end up in other controllers
			log.trace("Setting session attribute : sCurrUser : " +  currUser );
			session.setAttribute("sCurrUser", currUser);	
			
			return goAdminHome("","",model);
		}
	
		private Object goAdminHome(String errMsg,String infoMsg,ModelMap model){
			
			GzMemberForm admForm = new GzMemberForm();
			admForm.setInfoMsg(infoMsg);
			return new ModelAndView("admHome","admForm", admForm);
		}
		
		@RequestMapping(value = "/registerMember", method = RequestMethod.GET)
		public ModelAndView registerMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			
			return new ModelAndView("admMemberRegister","memberForm", memberForm);
		}
		
		private GzMemberForm createMemberForm(GzRole newMemberRole,ModelMap model)
		{
			GzMemberForm memberForm = new GzMemberForm();
			memberForm.setInCompleteCommand(new GzMemberCommand());
			if (newMemberRole.equals(GzRole.ROLE_SMA))
			{
				GzAdmin admin = (GzAdmin) model.get("currUser");
				memberForm.getUpstreamMembers().add(admin);
				gzServices.getGzHome().getDownstreamForParent(admin);
				if (admin.getMembers().isEmpty())
					memberForm.setAdminOnly(true);
			}
			else
				memberForm.getUpstreamMembers().addAll(gzServices.getGzHome().getUpstreaMembers(newMemberRole));		
			
			memberForm.getInCompleteCommand().setMemberRank(newMemberRole.getShortCode().toUpperCase());
			memberForm.getInCompleteCommand().setSuperiorCode(memberForm.getUpstreamMembers().get(0).getContact());
			return memberForm;
		}
		
		private ModelAndView backToSignin(ModelMap model,String errMsg) {
			
			log.info("Received request to signin");
				
			HashMap<String,String> logon = new HashMap<String,String>();
			logon.put("errMsg", errMsg);
			logon.put("infoMsg", "");
			logon.put("email", "");
			
			return new ModelAndView("logon","logon", logon);
		}
		
		@RequestMapping(value="/processAdm", params="changeMemberRank", method = RequestMethod.POST)
		public ModelAndView changeMemberRank(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model,String role)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("changeMemberRank: " + command);
			GzRole newRole = GzRole.valueOf(command.getMemberRank());
			
			memberForm = createMemberForm(newRole,model);
			memberForm.setInCompleteCommand(command);
			
			return new ModelAndView("admMemberRegister","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="memberCancel", method = RequestMethod.POST)
		public Object memberRegister(ModelMap model)
		{
			return goAdminHome("","",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberRegister", method = RequestMethod.POST)
		public Object memberRegister(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();	
			log.info("memberRegister: " + command);
			GzRole role = GzRole.valueOf(command.getMemberRank());
			GzBaseUser superior = gzServices.getGzHome().getBaseUserByCode(command.getSuperiorCode());
			
			GzMemberVerify verify = new GzMemberVerify();
			String errMsg = verify.verify(command, gzServices.getGzHome(),superior);
			if (!errMsg.isEmpty())
			{
				memberForm = createMemberForm(role,model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg(errMsg);
				return new ModelAndView("admMemberRegister","memberForm", memberForm);
			}
			
			try
			{
				GzBaseUser newMember;
				if (role.equals(GzRole.ROLE_PLAY))
					newMember = new GzBaseUser();
				else
					newMember = (GzAgent) role.getCorrespondingClass().newInstance();
								
				newMember.setEmail(command.getUsername());
				newMember.setContact(command.getWeChatName());
				newMember.setParent(superior);
				newMember.setParentCode(superior.getCode());
				newMember.setRole(role);
				newMember.setEnabled(true);
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				newMember.setPassword(encoder.encode(command.getPassword()));
				
				if (role.equals(GzRole.ROLE_PLAY))
					gzServices.getGzHome().storeBaseUser(newMember);
				else
					gzServices.getGzHome().storeAgent((GzAgent) newMember);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm = createMemberForm(role,model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg("Error registering member - contact support.");
				return new ModelAndView("admMemberRegister","memberForm", memberForm);
			}
			
			return goAdminHome("","Member : " +  command.getUsername() + " successfully registered",model);
		}
}
