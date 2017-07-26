package org.rp.web.adm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.agent.GzAgent;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.baseuser.persistence.GzBaseUserStub;
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
			
			return goAdmHome("","",model);
		}
	
		private Object goAdmHome(String errMsg,String infoMsg,ModelMap model){
			
			GzMemberForm admForm = createMemberForm(GzRole.ROLE_SMA,model);
			admForm.setInfoMsg(infoMsg);
			return new ModelAndView("admHome","admForm", admForm);
		}
		
		@RequestMapping(value = "/registerMember", method = RequestMethod.GET)
		public ModelAndView registerMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			
			return new ModelAndView("admMemberRegister","memberForm", memberForm);
		}
		
		@RequestMapping(value = "/manageMember", method = RequestMethod.GET)
		public ModelAndView manageMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			
			return new ModelAndView("admMemberManage","memberForm", memberForm);
		}
		
		@RequestMapping(value = "/activateMember", method = RequestMethod.GET)
		public ModelAndView activateMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			return setActivated(memberForm);
		}
		
		private ModelAndView setActivated(GzMemberForm memberForm) {
			try
			{
				GzBaseUser bu = gzServices.getGzHome().getBaseUserByEmail(memberForm.getInCompleteCommand().getMemberToChangeCode());
				if (bu == null)
				{
					throw new GzPersistenceException("Member does not exist");
				}
				memberForm.getInCompleteCommand().setEnabled(bu.isEnabled());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm.setErrMsg("Error activating member - contact support.");
				return new ModelAndView("admMemberRegister","memberForm", memberForm);
			}
			return new ModelAndView("admMemberActivate","memberForm", memberForm);
		}

		@RequestMapping(value = "/processAdm", params="changeMemberActive", method = RequestMethod.POST)
		public ModelAndView changeMemberActive(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			memberForm.setInCompleteCommand(command);
			return setActivated(memberForm);
		}
		
		private GzMemberForm createMemberForm(GzRole newMemberRole,ModelMap model)
		{
			GzMemberForm memberForm = new GzMemberForm();
			GzBaseUser user = (GzBaseUser) model.get("currUser");
			
			memberForm.setMemberSummary(populateMemberSummary(user,null,memberForm.getFlatMembers()));
			memberForm.setChooseMembers(memberForm.getFlatMembers());
			memberForm.setUplineMembers(memberForm.getFlatMembers());
			memberForm.setInCompleteCommand(new GzMemberCommand());
			if (newMemberRole.equals(GzRole.ROLE_SMA))
			{
				memberForm.getUpstreamMembers().add(new GzMemberSummary(user));
				if (user.getMembers().isEmpty())
					memberForm.setAdminOnly(true);
				memberForm.getPossibleSuperiors().add(new GzMemberSummary(user));
			}
			else
			{
				for (GzBaseUser bu : gzServices.getGzHome().getUpstreaMembers(newMemberRole))
					memberForm.getUpstreamMembers().add(new GzMemberSummary(bu));
			}
			memberForm.getInCompleteCommand().setMemberRank(newMemberRole.getShortCode().toUpperCase());
			memberForm.getInCompleteCommand().setMemberToChangeCode(user.getMembers().get(0).getEmail());
			memberForm.getInCompleteCommand().setEnabled(user.getMembers().get(0).isEnabled());
			memberForm.getInCompleteCommand().setMemberToChangeUpline(user.getMembers().get(0).getEmail());
			memberForm.getInCompleteCommand().setSuperiorCode(memberForm.getUpstreamMembers().get(0).getWeChatName());
			return memberForm;
		}
		
		private GzMemberSummary populateMemberSummary(GzBaseUser user, GzMemberSummary parent, List<GzMemberSummary> flatMembers) {
			GzMemberSummary ms = new GzMemberSummary(user);
			ms.setParent(parent);
			
			if (!user.getRole().equals(GzRole.ROLE_ADMIN))
				flatMembers.add(ms);
			if (user.getRole().equals(GzRole.ROLE_PLAY))
				return ms;
			
			gzServices.getGzHome().getDownstreamForParent(user);
			for (GzBaseUser bu : user.getMembers())
				ms.getMembers().add(populateMemberSummary(bu,ms,flatMembers));
			return ms;
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
		public ModelAndView changeMemberRank(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("changeMemberRank: " + command);
			GzRole newRole = GzRole.getRoleForShortCode(command.getMemberRank());
			
			memberForm = createMemberForm(newRole,model);
			memberForm.setInCompleteCommand(command);
			
			return new ModelAndView("admMemberRegister","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="activateMember", method = RequestMethod.POST)
		public ModelAndView activateMember(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("activateMember: " + command);
			return acivate(true,model,command);
		}
		
		@RequestMapping(value="/processAdm", params="deactivateMember", method = RequestMethod.POST)
		public ModelAndView deactivateMember(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("deactivateMember: " + command);
			return acivate(false,model,command);
		}
		
		private ModelAndView acivate(boolean activate,ModelMap model,GzMemberCommand command)
		{
			GzMemberForm memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			memberForm.setInCompleteCommand(command);
			try
			{
				GzBaseUser bu = gzServices.getGzHome().getBaseUserByEmail(command.getMemberToChangeCode());
				if (bu == null)
				{
					throw new GzPersistenceException("Member does not exist");
				}
				bu.setEnabled(activate);
				gzServices.getGzHome().updateBaseUserProfile(bu);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm.setErrMsg("Error activating/deactivating member - contact support.");
				return new ModelAndView("admMemberRegister","memberForm", memberForm);
			}
			memberForm.getInCompleteCommand().setEnabled(activate);
			memberForm.setInfoMsg("Member : " + command.getMemberToChangeCode() + " - Successfully " + (activate ? "activated." : " deactivated."));
			return new ModelAndView("admMemberActivate","memberForm", memberForm);
		}
		
		@RequestMapping(value="/backToAdm", method = RequestMethod.GET)
		public Object backToAdm(ModelMap model)
		{
			return goAdmHome("","",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberCancel", method = RequestMethod.POST)
		public Object memberCancel(ModelMap model)
		{
			return goAdmHome("","",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberRegister", method = RequestMethod.POST)
		public Object memberRegister(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();	
			log.info("memberRegister: " + command);
			GzRole role = GzRole.getRoleForShortCode(command.getMemberRank());
			GzBaseUser superior = gzServices.getGzHome().getBaseUserByEmail(command.getSuperiorCode());
			
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
			
			return goAdmHome("","Member : " +  command.getUsername() + " successfully registered",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberChangePw", method = RequestMethod.POST)
		public ModelAndView memberChangePw(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();	
			if (command.getPassword().length()<8)
			{
				memberForm = createMemberForm(GzRole.ROLE_SMA,model);
				memberForm.setErrMsg("Member password must be >= 8 chars");
				return new ModelAndView("admMemberManage","memberForm", memberForm);
			}
			return memberChange(memberForm,model,"Password");
		}
		
		@RequestMapping(value="/processAdm", params="memberChangeUsername", method = RequestMethod.POST)
		public ModelAndView memberChangeUsername(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			return memberChange(memberForm,model,"User Name");
		}
	
		@RequestMapping(value="/processAdm", params="memberChangeWeChat", method = RequestMethod.POST)
		public ModelAndView memberChangeWeChat(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			return memberChange(memberForm,model,"WeChat Name");
		}
		
		private ModelAndView memberChange(GzMemberForm memberForm,ModelMap model,String field)
		{
			GzMemberCommand command = memberForm.getCommand();	
			log.info("memberChange: " + command);
			try
			{
				GzBaseUser member = gzServices.getGzHome().getBaseUserByEmail(command.getMemberToChangeCode());
				if (member == null)
				{
					memberForm = createMemberForm(GzRole.ROLE_SMA,model);
					memberForm.setErrMsg("Member : " + command.getMemberToChangeCode() + " doesn't exist. Please retry.");
					return new ModelAndView("admMemberManage","memberForm", memberForm);
				}
				if (field.equals("Password"))
				{
					PasswordEncoder encoder = new BCryptPasswordEncoder();
					member.setPassword(encoder.encode(command.getPassword()));
					gzServices.getGzHome().updateBaseUserProfile(member);
				}
				if (field.equals("User Name") && !command.getUsername().equals(member.getEmail()))
				{
					GzBaseUser exists = gzServices.getGzHome().getBaseUserByEmail(command.getUsername());
					if (exists!=null)
					{
						memberForm = createMemberForm(GzRole.ROLE_SMA,model);
						memberForm.setErrMsg("Member : " + command.getUsername() + " already exists");
						return new ModelAndView("admMemberManage","memberForm", memberForm);
					}	
					member.setEmail(command.getUsername());
					gzServices.getGzHome().updateBaseUserProfile(member);
				}
				if (field.equals("WeChat Name") && !command.getWeChatName().equals(member.getContact()))
				{
					if (gzServices.getGzHome().contactExists(command.getWeChatName()))
					{
						memberForm = createMemberForm(GzRole.ROLE_SMA,model);
						memberForm.setErrMsg("Member with WeChat name : " + command.getWeChatName() + " already exists");
						return new ModelAndView("admMemberManage","memberForm", memberForm);
					}	
					member.setContact(command.getWeChatName());
					gzServices.getGzHome().updateBaseUserProfile(member);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm = createMemberForm(GzRole.ROLE_SMA,model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg("Error managing member - contact support.");
				return new ModelAndView("admMemberManage","memberForm", memberForm);
			}
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			memberForm.setInfoMsg(field + " successfully updated.");
			return new ModelAndView("admMemberManage","memberForm", memberForm);
		}
		
		@RequestMapping(value = "/placementMember", method = RequestMethod.GET)
		public ModelAndView placementMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="changeMemberUpline", method = RequestMethod.POST)
		public ModelAndView changeMemberUpline(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("changeMemberUpline: " + command);
			GzBaseUser member = gzServices.getGzHome().getBaseUserByEmail(command.getMemberToChangeUpline());
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			memberForm.setInCompleteCommand(command);
			
			setPossibleSupers(member.getRole(),memberForm,"","");
			
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="filterSuperiorsWeChat", method = RequestMethod.POST)
		public ModelAndView filterSuperiorsWeChat(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("filterSuperiorsWeChat: " + command);
			GzBaseUser member = gzServices.getGzHome().getBaseUserByEmail(command.getMemberToChangeUpline());
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			memberForm.setInCompleteCommand(command);
			
			setPossibleSupers(member.getRole(),memberForm,"contact",command.getSearch2());
			
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="filterSuperiorsUserName", method = RequestMethod.POST)
		public ModelAndView filterSuperiorsUserName(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("filterSuperiorsWeChat: " + command);
			GzBaseUser member = gzServices.getGzHome().getBaseUserByEmail(command.getMemberToChangeUpline());
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			memberForm.setInCompleteCommand(command);
			
			setPossibleSupers(member.getRole(),memberForm,"email",command.getSearch2());
			
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		private void setPossibleSupers(GzRole role,GzMemberForm memberForm,String type,String term)
		{
			if (!role.equals(GzRole.ROLE_SMA))
			{	
				List<GzBaseUserStub> possibleSupers = gzServices.getGzHome().getUpstreamPossibleParents(role,type,term);
				memberForm.getPossibleSuperiors().clear();
				for (GzBaseUserStub bu : possibleSupers)
					memberForm.getPossibleSuperiors().add(new GzMemberSummary(bu));
			}
		}
		
		@RequestMapping(value="/processAdm", params="changeMemberLevel", method = RequestMethod.POST)
		public ModelAndView changeMemberLevel(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("changeMemberLevel: " + command);
			GzRole role = GzRole.getRoleForShortCode(command.getMemberRank());
			GzBaseUser member = gzServices.getGzHome().getBaseUserByEmail(command.getMemberToChangeCode());
			
			if (!role.equals(member.getRole()))
				gzServices.getGzHome().reassignMemberRole(member,role);
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			memberForm.setInCompleteCommand(command);
			
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="submitMemberUpline", method = RequestMethod.POST)
		public Object submitMemberUpline(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();	
			log.info("submitMemberUpline: " + command);
			GzBaseUser superior = gzServices.getGzHome().getBaseUserByEmail(command.getSuperiorCode());
			GzBaseUser member = gzServices.getGzHome().getBaseUserByEmail(command.getMemberToChangeUpline());
			
			if (!member.getParentCode().equals(superior.getCode()))
			{
				gzServices.getGzHome().updateBaseUserParentCode(member.getCode(),superior.getCode());
			}
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="searchWeChat", method = RequestMethod.POST)
		public ModelAndView searchWeChat(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchWeChatP(memberForm,model);
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="searchWeChat2", method = RequestMethod.POST)
		public ModelAndView searchWeChat2(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchWeChatP(memberForm,model);
			return new ModelAndView("admMemberManage","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="searchWeChat3", method = RequestMethod.POST)
		public ModelAndView searchWeChat3(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchWeChatP(memberForm,model);
			return setActivated(memberForm);
		}
		
		private GzMemberForm searchWeChatP(GzMemberForm memberForm,ModelMap model)
		{
			log.info("Searching wechat name");
			
			GzMemberCommand command = memberForm.getCommand();
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			List<GzMemberSummary> mss = searchMembers("contact",command.getSearch());
			memberForm.setInCompleteCommand(command);
			if (!mss.isEmpty())
			{
				memberForm.getInCompleteCommand().setMemberToChangeCode(mss.get(0).getUserName());
				memberForm.setChooseMembers(mss);
			}
			else
				memberForm.setErrMsg("No weChat names like this term exist");
			
			return memberForm;
		}
		
		@RequestMapping(value="/processAdm", params="searchUserName", method = RequestMethod.POST)
		public ModelAndView searchUserName(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchUserNameP(memberForm,model);
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="searchUserName2", method = RequestMethod.POST)
		public ModelAndView searchUserName2(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchUserNameP(memberForm,model);
			return new ModelAndView("admMemberManage","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="searchUserName3", method = RequestMethod.POST)
		public ModelAndView searchUserName3(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			memberForm = searchUserNameP(memberForm,model);
			return setActivated(memberForm);
		}
		
		private GzMemberForm searchUserNameP(GzMemberForm memberForm,ModelMap model)
		{
			log.info("Seraching username");
			GzMemberCommand command = memberForm.getCommand();
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			List<GzMemberSummary> mss = searchMembers("email",command.getSearch());
			memberForm.setInCompleteCommand(command);
			if (!mss.isEmpty())
			{
				memberForm.getInCompleteCommand().setMemberToChangeCode(mss.get(0).getUserName());
				memberForm.setChooseMembers(mss);
			}
			else
				memberForm.setErrMsg("No User names like this term exist");
			
			return memberForm;
		}
		
		@RequestMapping(value="/processAdm", params="searchWeChat1", method = RequestMethod.POST)
		public ModelAndView searchWeChat1(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			List<GzMemberSummary> mss = searchMembers("contact",command.getSearch1());
			memberForm.setInCompleteCommand(command);
			if (!mss.isEmpty())
			{
				memberForm.getInCompleteCommand().setMemberToChangeUpline(mss.get(0).getUserName());
				memberForm.setUplineMembers(mss);
				setPossibleSupers(GzRole.getRoleForShortCode(mss.get(0).getRank()),memberForm,"","");
			}
			else
				memberForm.setErrMsg("No weChat names like this term exist");
			
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		@RequestMapping(value="/processAdm", params="searchUserName1", method = RequestMethod.POST)
		public ModelAndView searchUserName1(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			memberForm = createMemberForm(GzRole.ROLE_SMA,model);
			List<GzMemberSummary> mss = searchMembers("email",command.getSearch1());
			memberForm.setInCompleteCommand(command);
			if (!mss.isEmpty())
			{
				memberForm.getInCompleteCommand().setMemberToChangeUpline(mss.get(0).getUserName());
				memberForm.setUplineMembers(mss);
				setPossibleSupers(GzRole.getRoleForShortCode(mss.get(0).getRank()),memberForm,"","");
			}
			else
				memberForm.setErrMsg("No User names like this term exist");
			
			return new ModelAndView("admMemberChangeLevel","memberForm", memberForm);
		}
		
		private List<GzMemberSummary> searchMembers(String type, String term) {
			List<GzBaseUserStub> bus = gzServices.getGzHome().search(term, type);
			List<GzMemberSummary> mss = new ArrayList<GzMemberSummary>();
			for (GzBaseUserStub bu : bus)
			{
				mss.add(new GzMemberSummary(bu));
			}
			return mss;
		}

		@RequestMapping(value="/processAdm", params="memberTree", method = RequestMethod.POST)
		public ModelAndView memberTree(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("changeMemberRank: " + command);
			GzRole newRole = GzRole.valueOf(command.getMemberRank());
			
			memberForm = createMemberForm(newRole,model);
			memberForm.setInCompleteCommand(command);
			
			return new ModelAndView("admMemberTree","memberForm", memberForm);
		}
}
