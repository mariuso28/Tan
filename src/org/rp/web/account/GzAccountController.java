package org.rp.web.account;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.rp.account.GzInvoice;
import org.rp.account.persistence.GzXactionRowMapperPaginated;
import org.rp.agent.GzAgent;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.util.ErrorMsgReport;
import org.rp.util.StackDump;
import org.rp.web.GzFormValidationException;
import org.rp.web.account.pdf.PDFStoreOpenInvoices;
import org.rp.web.member.GzMemberForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser","currAccountUser","codeQue","currInvoice", "firstInQueUser", "currChecked", "currXtrmp", "currBaseUser"})

@RequestMapping("/acc")
public class GzAccountController {


	private static Logger log = Logger.getLogger(GzAccountController.class);	
	private GzServices gzServices;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.setAutoGrowCollectionLimit(2048);
	}
	
	private boolean integrityCheck(GzBaseUser currUser,GzBaseUser currAccountUser)
	{
		
		if (currUser.getRole().equals(GzRole.ROLE_COMP) && currUser.getCode().equals(currAccountUser.getCode()))
			return true;
		
		if (!currUser.getCode().equals(currAccountUser.getParentCode()))
		{
			log.fatal("maintainExistAccount - PARENT/CHILD CODE MISMATCH :" + 
					currUser.getCode() +"/" + currAccountUser.getParentCode());
			return false;
		}
		
	 return true;
	}
	
	private boolean integrityCheck2(GzBaseUser currUser,GzBaseUser currAccountUser)
	{
		
		if (currUser.getRole().equals(GzRole.ROLE_COMP) && currUser.getCode().equals(currAccountUser.getCode()))
			return true;
		
		if (!currUser.getCode().equals(currAccountUser.getParentCode()) && !currAccountUser.getCode().equals(currUser.getParentCode()))
		{
			log.fatal("maintainExistAccount - PARENT/CHILD CODE MISMATCH :" + 
					currUser.getCode() +"/" + currAccountUser.getParentCode());
			return false;
		}
		
	 return true;
	}
	
	@RequestMapping(value = "/processAccount", params=("saveAccount"), method = RequestMethod.POST)
	public Object saveAccount(ModelMap model,@ModelAttribute("accountDetailsForm") AccountDetailsForm accountDetailsForm,
										HttpServletRequest request) 
	{
		HttpSession session = request.getSession(false);	
		GzBaseUser currUser = (GzBaseUser) session.getAttribute("sCurrUser");		// as the thing maybe GzAgent or GzAdmin
		
		GzBaseUser currAccountUser = (GzBaseUser) session.getAttribute("sCurrAccountUser");	// maybe GzAgent or GzPlayer
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		try
		{
			accountDetailsForm.validate(gzServices,currAccountUser,currUser);				// test against some parent values
		}
		catch (GzFormValidationException e){
			accountDetailsForm = new AccountDetailsForm(gzServices.getGzHome(),currAccountUser,currUser,accountDetailsForm);
			accountDetailsForm.setMessage("Validate of Account failed : " + e.getMessage());
			if (currAccountUser.getRole().equals(GzRole.ROLE_COMP))
				return new ModelAndView("accountCompEdit" , "accountDetailsForm", accountDetailsForm);
			else
				return new ModelAndView("accountEdit" , "accountDetailsForm", accountDetailsForm);
		}
		
		AccountCommand command = accountDetailsForm.getCommand();
		
		GzMemberForm memberForm = new GzMemberForm();
		command.getNewAccount().populateAccount(currAccountUser.getAccount());
		
		
		memberForm = new GzMemberForm();
		try {
			gzServices.getGzHome().updateAccount(currAccountUser.getAccount());
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
			memberForm.setErrMsg(ErrorMsgReport.createMessage("Account could not be updated - contact support"));
		}
		gzServices.getGzHome().getDownstreamForParent(currUser);
		model.addAttribute("currUser",currUser);
		
		return new ModelAndView("memberHome", "memberForm", memberForm );
	}
	
	@RequestMapping(value = "/maintainAccount", method = RequestMethod.GET)
	public Object maintainAccount(ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		GzBaseUser currUser = (GzBaseUser) session.getAttribute("sCurrUser");				// as the thing comes from AgentController
		GzBaseUser currAccountUser = (GzBaseUser) session.getAttribute("sCurrAccountUser");				// the owner of the account
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(gzServices.getGzHome(),
															currAccountUser,currUser);
		
	    return new ModelAndView("maintainAccount" , "accountDetailsForm", accountDetailsForm);
    }
	
	@RequestMapping(value = "/processAccount", params=("method=updateComp"), method = RequestMethod.GET)
	public Object updateCompAccount(ModelMap model,HttpServletRequest request) 
	{
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		GzBaseUser currUser = (GzBaseUser) session.getAttribute("sCurrUser");				
		log.trace("got session attribute : currUser : " +  currUser );
		
		GzBaseUser currAccountUser = currUser;		// same user/owner
		log.trace("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		session.setAttribute("sCurrAccountUser",currAccountUser);	// the same uses this
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(gzServices.getGzHome(),currAccountUser,currUser);
		
		return new ModelAndView("accountCompEdit" , "accountDetailsForm", accountDetailsForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("method=updateDistributions"), method = RequestMethod.GET)
	public Object updateDistributions(ModelMap model,String code,HttpServletRequest request) 
	{
		HttpSession session = request.getSession(false);	
		log.trace("getting session attribute : sCurrUser ");
		GzBaseUser currUser = (GzBaseUser) session.getAttribute("sCurrUser");				
		log.trace("got session attribute : currUser : " +  currUser );
		
		try {
			// refresh
			currUser = gzServices.getGzHome().getBaseUserByCode(currUser.getCode());
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
			return "redirect:/rp/agnt/backtoMemberHomeErr?errMsg=Distributions could not be updated - contact support".replace(" ","%20");
		}
		
		GzBaseUser currAccountUser = currUser;		// same user/owner
		log.trace("got session attribute : currUser : " +  currUser );
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		session.setAttribute("sCurrAccountUser",currAccountUser);	// the same uses this
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		AccountDistributionForm accountDistributionForm = new AccountDistributionForm(currAccountUser.getAccount());
		
		return new ModelAndView("accountDistributionEdit" , "accountDistributionForm", accountDistributionForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("saveDistribution"), method = RequestMethod.POST)
	public Object saveDistribution(ModelMap model,@ModelAttribute("accountDistributionForm") AccountDistributionForm accountDistributionForm,
										HttpServletRequest request) 
	{
		HttpSession session = request.getSession(false);	
		GzBaseUser currUser = (GzBaseUser) session.getAttribute("sCurrUser");		// as the thing maybe GzAgent or GzAdmin
		
		GzBaseUser currAccountUser = (GzBaseUser) session.getAttribute("sCurrAccountUser");	// maybe GzAgent or GzPlayer
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		try
		{
			accountDistributionForm.getCommand().populateAccount(currAccountUser.getAccount());
		}
		catch (GzFormValidationException e){
			accountDistributionForm = new AccountDistributionForm(currUser.getAccount());
			accountDistributionForm.setMessage("Validate of Distributions failed : " + e.getMessage());
			return new ModelAndView("accountDistributionEdit" , "accountDistributionForm", accountDistributionForm);
		}
		
		try {
			gzServices.getGzHome().updateDistributions(currAccountUser.getAccount());
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
			return "redirect:/rp/agnt/backtoMemberHomeErr?errMsg=Distributions could not be updated - contact support".replace(" ","%20");
		}
		gzServices.getGzHome().getDownstreamForParent(currUser);
		model.addAttribute("currUser",currUser);
		
		return "redirect:/rp/agnt/backtoMemberHome";
	}
	
	
	// processAccount?method=update&code=c59
	@RequestMapping(value = "/processAccount", params=("method=update"), method = RequestMethod.GET)
	public Object updateAccount(String code,ModelMap model,HttpServletRequest request) {
       
		HttpSession session = request.getSession(false);	
		GzBaseUser currUser = (GzBaseUser) session.getAttribute("sCurrUser");				
		
		GzBaseUser currAccountUser = null;
		try {
			currAccountUser = (GzBaseUser) gzServices.getGzHome().getBaseUserByCode(code);
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
			return null;
		}			
		
		model.addAttribute("currChecked",new Boolean(false));
		model.addAttribute("currUser",currUser);
		model.addAttribute("currAccountUser",currAccountUser);
		session.setAttribute("sCurrAccountUser",currAccountUser);	// the same uses this
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		Deque<String> codeQue = new ArrayDeque<String>();
		codeQue.addFirst(code);							// head contains the code of currAccountUser
		model.addAttribute("codeQue",codeQue);
		
		model.addAttribute("currBaseUser",currUser);
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(gzServices.getGzHome(),currAccountUser,currUser);
		
		log.trace("updateAccount : " + accountDetailsForm);
        return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
    }
	
	
	@RequestMapping(value = "/processAccount", params=("modifyAccount"), method = RequestMethod.POST)
	public Object createAccount(ModelMap model) 
	{
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		GzBaseUser currAccountUser = (GzBaseUser) model.get("currAccountUser");
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(gzServices.getGzHome(),
														currAccountUser,currUser);
		accountDetailsForm.setModify(true);
		log.trace("createAccount : " + accountDetailsForm);
		return new ModelAndView("accountEdit" , "accountDetailsForm", accountDetailsForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("enableMember"), method = RequestMethod.POST)
	public Object enableMember(ModelMap model) 
	{
		return setEnabled(model,true);
	}
	
	@RequestMapping(value = "/processAccount", params=("disableMember"), method = RequestMethod.POST)
	public Object disableMember(ModelMap model) 
	{
		return setEnabled(model,false);
	}
	
	private Object setEnabled(ModelMap model,boolean flag)
	{
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		GzBaseUser currAccountUser = (GzBaseUser) model.get("currAccountUser");
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		String message = "";
		try {
			gzServices.updateEnabled(currAccountUser,flag);
			currAccountUser = (GzBaseUser) gzServices.getGzHome().getBaseUserByCode(currAccountUser.getCode());
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
			message = "Update enabled failed";
		}
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(gzServices.getGzHome(),currAccountUser,currUser);
		accountDetailsForm.setMessage(message);
		
		return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("invoiceDetails"), method = RequestMethod.GET)
	public Object viewInvoiceDetails(ModelMap model,String invoiceId)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		codeQue.addFirst(invoiceId);
		model.addAttribute("codeQue", codeQue);
		
		long invoiceNum = new Long(invoiceId);
		try
		{
			GzInvoice currInvoice = gzServices.getGzHome().getInvoiceForId(invoiceNum);
			GzBaseUser currAccountUser = gzServices.getGzHome().getBaseUserByEmail(currInvoice.getPayee());
			GzBaseUser currUser = gzServices.getGzHome().getBaseUserByEmail(currInvoice.getPayer());
			model.addAttribute("currUser",currUser);
			model.addAttribute("currAccountUser",currAccountUser);
			model.addAttribute("currInvoice",currInvoice);
			
			if (!integrityCheck2(currAccountUser,currUser))				// bidirectional checking
			{
				return "redirect:../logon/access_denied";
			}
					
			if (currAccountUser.getRole().equals(GzRole.ROLE_PLAY))
			{
				TransactionListForm transactionListForm = new TransactionListForm(currAccountUser,currInvoice,gzServices.getGzHome());
				return new ModelAndView("transactionList" , "transactionListForm", transactionListForm);
			}
			if (currUser.getRole().equals(GzRole.ROLE_PLAY))
			{
				TransactionListForm transactionListForm = new TransactionListForm(currUser,currInvoice,gzServices.getGzHome());
				return new ModelAndView("transactionList" , "transactionListForm", transactionListForm);
			}
			
			InvoiceListForm invoiceListForm = new InvoiceListForm(currAccountUser,currInvoice,gzServices.getGzHome());
			return new ModelAndView("invoiceSubList" , "invoiceListForm", invoiceListForm);
		}
		catch (GzPersistenceException e)
		{
			log.error(StackDump.toString(e));
			return null;
		}
	}
	
	private Object backViewInvoiceDetails(ModelMap model,String infoMsg,String errMsg)
	{
		try
		{
			GzInvoice currInvoice = (GzInvoice) model.get("currInvoice");
			GzBaseUser currAccountUser = (GzBaseUser) model.get("currAccountUser");
			GzBaseUser currUser = (GzBaseUser) model.get("currUser");
					
			if (currAccountUser.getRole().equals(GzRole.ROLE_PLAY))
			{
				TransactionListForm transactionListForm = new TransactionListForm(currAccountUser,currInvoice,gzServices.getGzHome());
				return new ModelAndView("transactionList" , "transactionListForm", transactionListForm);
			}
			if (currUser.getRole().equals(GzRole.ROLE_PLAY))
			{
				TransactionListForm transactionListForm = new TransactionListForm(currUser,currInvoice,gzServices.getGzHome());
				return new ModelAndView("transactionList" , "transactionListForm", transactionListForm);
			}
			
			InvoiceListForm invoiceListForm = new InvoiceListForm(currAccountUser,currInvoice,gzServices.getGzHome());
			invoiceListForm.setInfoMsg(infoMsg);
			invoiceListForm.setErrMsg(errMsg);
			
			return new ModelAndView("invoiceSubList" , "invoiceListForm", invoiceListForm);
		}
		catch (GzPersistenceException e)
		{
			log.error(StackDump.toString(e));
			return null;
		}
	}
	
	@RequestMapping(value = "/processAccount", params=("cancelInvoiceDetails"), method = RequestMethod.GET)
	public Object cancelInvoiceDetails(ModelMap model,String invoiceId,HttpServletRequest request)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		
		log.trace("cancelInvoiceDetails : " + codeQue);
		if (codeQue.isEmpty())
		{
			return "redirect:../logon/signin";
		}
		
		String code = codeQue.removeFirst();
		if (codeQue.size()==1)
		{
			// last must be the code of the currAccountUset
			code = codeQue.removeFirst();
			return updateAccount(code,model,request);
		}
		
		if (codeQue.isEmpty())
		{
			return "redirect:../logon/signin";
		}
		
		code = codeQue.removeFirst();
		
		model.addAttribute("codeQue", codeQue);
		return viewInvoiceDetails(model,code);
	}
	
	
	
	/*
	
	@RequestMapping(value = "/processAccount", params=("cancelInvoiceDetails"), method = RequestMethod.GET)
	public ModelAndView cancelInvoiceDetails(ModelMap model,String invoiceId,HttpServletRequest request)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		
		log.trace("cancelInvoiceDetails : " + codeQue);
		String code = codeQue.removeFirst();
		if (codeQue.size()==1)
		{
			// last must be the code of the currAccountUset
			code = codeQue.removeFirst();
			return updateAccount(code,model,request);
		}
		
		code = codeQue.removeFirst();
		model.addAttribute("codeQue", codeQue);
		return viewInvoiceDetails(model,code);
	}
	*/
	
	@RequestMapping(value = "/processAccount", params=("invoicePay"), method = RequestMethod.POST)
	public Object payInvoice(AccountDetailsForm accountDetailsForm,ModelMap model,HttpServletRequest request)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		
		log.trace("payInvoice : " + codeQue);
		if (codeQue.size()!=1)
		{
			// last must be the code of the currAccountUset
			log.error("payInvoice : SOMETHING REAL DODGY GOING ON WITH CODEQUE");
			return null;
		}
		
		String userCode = codeQue.removeFirst();
		model.addAttribute("codeQue", codeQue);
		
		AccountCommand command = accountDetailsForm.getCommand();
		try
		{
			payInvoices(command);
		}
		catch (GzExceptionFatal | GzPersistenceException e)
		{
			log.error(StackDump.toString(e));
			log.fatal("payInvoice : SOMETHING REAL DODGY GOING ON WITH PAY INVOICE " + e.getMessage(), e);
			return null;
		}
		
		return updateAccount(userCode,model,request);
	}
	
	@RequestMapping(value = "/processAccount", params=("checkAll"), method = RequestMethod.POST)
	public Object checkAll(ModelMap model)
	{
		Boolean currChecked = (Boolean) model.get("currChecked");
		GzBaseUser currAccountUser = (GzBaseUser) model.get("currAccountUser");
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		currChecked = !currChecked;
		model.addAttribute("currChecked", currChecked);
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(gzServices.getGzHome(),currAccountUser,currUser);
		accountDetailsForm.setPayFlagsOn(currChecked);
		
		log.trace("updateAccount : " + accountDetailsForm);
        return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
		
	}
	
	@RequestMapping(value = "/processAccount", params=("invoicePDF"), method = RequestMethod.GET)
	public Object invoicePDF(ModelMap model,HttpServletRequest request)
	{
		GzBaseUser currAccountUser = (GzBaseUser) model.get("currAccountUser");
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		List<GzInvoice> outstandingInvoices = null;
		try {
			outstandingInvoices = gzServices.getGzHome().getOutstandingInvoices(currAccountUser,currUser);
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.put("outStandingInvoices",outstandingInvoices);
		return new ModelAndView("pdfView", model );
	}
	
	@RequestMapping(value = "/processAccount", params=("invoiceEmailPDF"), method = RequestMethod.GET)
	public Object invoiceEmailPDF(ModelMap model,HttpServletRequest request)
	{
		GzBaseUser currAccountUser = (GzBaseUser) model.get("currAccountUser");
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		if (!integrityCheck(currUser,currAccountUser))
		{
			return "redirect:../logon/access_denied";
		}
		
		List<GzInvoice> outstandingInvoices = null;
		try {
			outstandingInvoices = gzServices.getGzHome().getOutstandingInvoices(currAccountUser,currUser);
		} catch (GzPersistenceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		model.put("outStandingInvoices",outstandingInvoices);
		
		String msg = "";
		try
		{
			PDFStoreOpenInvoices pdfSOI = new PDFStoreOpenInvoices(gzServices,model,currUser,currAccountUser);
			String subject = "Dear " + currAccountUser.getContact() + ", please review outstanding invoices.";
			String content = "Dear " + currAccountUser.getContact() + ", please review outstanding invoices attached.\n"
					+ "Details can be reviewed by logging into your account. \nKind regards - Goldmine Gaming Support Team.";
			
			List<String> attachments = new ArrayList<String>();
			attachments.add(pdfSOI.getPdfPath());
			gzServices.getMail().sendMail(currAccountUser,subject, content, attachments );
			log.info("Sucessfully sent open invoices");
		}
		catch (Exception e)
		{
			log.error("Sending PDF of invoice failed : " + e.getMessage());
			msg = "Sending PDF of Open Invoice failed - please try later";
		}
		AccountDetailsForm accountDetailsForm = new AccountDetailsForm(gzServices.getGzHome(),
															currAccountUser,currUser);
		if (!msg.isEmpty())
			accountDetailsForm.setMessage(msg);
		else
			accountDetailsForm.setInfo("Invoice PDF successfully sent");
		
		log.trace("invoiceEmailPDF : " + accountDetailsForm);
        return new ModelAndView("accountMaintain" , "accountDetailsForm", accountDetailsForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("emailXls"), method = RequestMethod.GET)
	public Object emailXls(ModelMap model,String invoiceId) {
       
		log.info("emailXls for : " + invoiceId);
		
		GzBaseUser currBaseUser =  (GzBaseUser) model.get("currBaseUser");

		String errMsg = "";
		String infoMsg = "";
		try
		{
			long id = Long.parseLong(invoiceId);
			gzServices.generateAndSendXls(id,currBaseUser);
			infoMsg = "Xls workbook for invoice : " + invoiceId + " successfully sent to : " + currBaseUser.getEmail() + ". Please check your inbox.";
			log.info(infoMsg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			errMsg = "Xls workbook could not be sent - please try later.";
			log.error("Xls workbook could not be sent - please try later. - " + e.getMessage());
		}	
				
        return backViewInvoiceDetails(model,infoMsg,errMsg);
	}

	
	private void payInvoices(AccountCommand command) throws GzExceptionFatal, GzPersistenceException
	{
		List<GzInvoice> invoices = new ArrayList<GzInvoice>();
		int cnt = 0;
		for (String flag : command.getPayFlags())
		{
			if (!flag.equals("off"))	
			{
				GzInvoice invoice = gzServices.getGzHome().getInvoiceForId(new Long(command.getInvoiceIds().get(cnt)));
				invoices.add(invoice);
			}
			cnt++;
		}
		gzServices.payInvoices(invoices);
	}
	
	@RequestMapping(value = "/processAccount", params=("method=rollup"), method = RequestMethod.GET)
	public ModelAndView processRollup(ModelMap model)
	{
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		if (codeQue==null)
			codeQue = new ArrayDeque<String>();
		else
			codeQue.clear();
		codeQue.addFirst(currUser.getCode());
		model.addAttribute("codeQue", codeQue);
		model.addAttribute("currUser",currUser);
		
		model.addAttribute("firstInQueUser",currUser);
		RollupForm rollupForm = null;
		try {
			rollupForm = new RollupForm(gzServices,currUser);
		} catch (GzPersistenceException e) {
			e.printStackTrace();
			log.error(StackDump.toString(e));
		}
		return new ModelAndView("rollup" , "rollupForm", rollupForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("method=subRollup"), method = RequestMethod.GET)
	public ModelAndView processSubRollup(ModelMap model,String code)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		codeQue.addFirst(code);
		model.addAttribute("codeQue", codeQue);
		
		RollupForm rollupForm = null;
		GzAgent currUser = null;
		try {
			currUser = (GzAgent) gzServices.getGzHome().getAgentByCode(code);
			rollupForm = new RollupForm(gzServices,currUser);
		} catch (GzPersistenceException e) {
			log.info(StackDump.toString(e));
		}
		
		log.trace("processAccount : " + rollupForm);
		return new ModelAndView("rollup" , "rollupForm", rollupForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("method=cancelRollup"), method = RequestMethod.GET)
	public Object cancelRollup(ModelMap model)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		String code = codeQue.removeFirst();
		if (codeQue.isEmpty())
		{
			GzBaseUser currUser = (GzBaseUser) model.get("firstInQueUser");
			model.addAttribute("currUser",currUser);
			return "redirect:../agnt/backtoMemberHome";
		}
		code = codeQue.removeFirst();
		model.addAttribute("codeQue", codeQue);
		return processSubRollup(model,code);
	}
		
	@RequestMapping(value = "/processAccount", params=("method=cancelRollupDetails"), method = RequestMethod.GET)
	public ModelAndView cancelRollupDetails(ModelMap model)
	{
		return processRollup( model );
	}
	
	@RequestMapping(value = "/processAccount", params=("method=backToLastRollup"), method = RequestMethod.GET)
	public Object backToLastRollup(ModelMap model)
	{
		@SuppressWarnings("unchecked")
		Deque<String> codeQue = (Deque<String>) model.get("codeQue");
		
		if (codeQue.isEmpty())
		{
			GzBaseUser currUser = (GzBaseUser) model.get("firstInQueUser");
			model.addAttribute("currUser",currUser);
			return "redirect:../agnt/backtoMemberHome";
		}
		
		RollupForm rollupForm = null;
		GzAgent currUser = null;
		try {
			String code = codeQue.peekFirst();
			currUser = (GzAgent) gzServices.getGzHome().getAgentByCode(code);
			rollupForm = new RollupForm(gzServices,currUser);
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
		}
		
		log.trace("processAccount : " + rollupForm);
		return new ModelAndView("rollup" , "rollupForm", rollupForm);
	}
	
	@RequestMapping(value = "/processAccount", params=("method=accDetails"), method = RequestMethod.GET)
	public ModelAndView accDetails(ModelMap model,String code)
	{
		GzBaseUser currUser = null;
		try {
			currUser = (GzBaseUser) gzServices.getGzHome().getAgentByCode(code);
		} catch (GzPersistenceException e) {
			log.equals(StackDump.toString(e));
		}
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		int pageSize = 16;
		GzXactionRowMapperPaginated xtrmp = gzServices.getGzHome().getGzInvoiceRowMapperPaginated(currUser,pageSize);
		transactionForm.createTxList(currUser,gzServices.getGzHome(),xtrmp.getNextPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		model.addAttribute("currUser",currUser);
		model.addAttribute("currXtrmp",xtrmp);
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	@RequestMapping(value = "/processAccount", params=("method=accDetailsNext"), method = RequestMethod.GET)
	public ModelAndView accDetailsNext(ModelMap model)
	{
		GzAgent currUser = (GzAgent) model.get("currUser");
		GzXactionRowMapperPaginated xtrmp = (GzXactionRowMapperPaginated) model.get("currXtrmp");
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		transactionForm.createTxList(currUser,gzServices.getGzHome(),xtrmp.getNextPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	@RequestMapping(value = "/processAccount", params=("method=accDetailsLast"), method = RequestMethod.GET)
	public ModelAndView accDetailsLast(ModelMap model)
	{
		GzAgent currUser = (GzAgent) model.get("currUser");
		GzXactionRowMapperPaginated xtrmp = (GzXactionRowMapperPaginated) model.get("currXtrmp");
		TransactionForm transactionForm = new TransactionForm(currUser.getContact(),currUser.getRole(),currUser.getCode());
		transactionForm.createTxList(currUser,gzServices.getGzHome(),xtrmp.getPrevPage(), xtrmp.getCurrentPage(), xtrmp.getLastPage());
		
		return new ModelAndView("accDetails", "transactionForm", transactionForm ); 
	}
	
	/*
	@RequestMapping(value = "/processAccount", params=("emailXls"), method = RequestMethod.GET)
	public Object emailXls(ModelMap model,String invoiceId) {
       
		log.info("emailXls for : " + invoiceId);
		
		GzBaseUser currBaseUser =  (GzBaseUser) model.get("currBaseUser");

		String errMsg = "";
		String infoMsg = "";
		try
		{
			long id = Long.parseLong(invoiceId);
			gzServices.generateAndSendXls(id,currBaseUser);
			infoMsg = "Xls workbook for invoice : " + invoiceId + " successfully sent to : " + currBaseUser.getEmail() + ". Please check your inbox.";
			log.info(infoMsg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			errMsg = "Xls workbook could not be sent - please try later.";
			log.error("Xls workbook could not be sent - please try later. - " + e.getMessage());
		}	
				
        return backViewInvoiceDetails(model,infoMsg,errMsg);
	}
*/
	
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
