package org.rp.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Semaphore;

import org.apache.log4j.Logger;
import org.rp.account.GzAccountMgr;
import org.rp.account.GzInvoice;
import org.rp.account.ss.InvoiceAnalytic;
import org.rp.baseuser.GzBaseUser;
import org.rp.framework.Mail;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.importer.CsvImportMgr;
import org.rp.util.NumberUtil;
import org.rp.util.StackDump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


public class GzServices 
{
	private static final Logger log = Logger.getLogger(GzServices.class);	
	private GzHome gzHome;
	private ThreadPoolTaskScheduler scheduler;
	private PlatformTransactionManager transactionManager;
	private GzAccountMgr gzAccountMgr;
	@Autowired
	private PasswordEncoder passwordEncoder;
	private Mail mail;
	private String gzProperties;
	private Properties properties;
	private Semaphore updateInvoiceSem = new Semaphore(1);
	
	public GzServices()
	{
		scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);
		scheduler.setThreadNamePrefix("faces async scheduler- ");
		scheduler.initialize();
	}
	
	public void initServices()
	{
		gzAccountMgr.setServices(this);
		gzAccountMgr.setHome(gzHome);
		
		properties = new Properties();
		try {
			properties.load(new FileInputStream(gzProperties));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(5);
		} 
		
		mail.setMailCcNotifications(properties.getProperty("mailCcNotifications"));
		mail.setMailSendFilter(properties.getProperty("mailSendFilter"));
		mail.setMailDisabled(properties.getProperty("mailDisabled"));
		
//		scheduleCloseOpenInvoices();
	}
	

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	
	public GzHome getGzHome() {
		return gzHome;
	}

	public void setGzHome(GzHome gzHome) {
		this.gzHome = gzHome;
	}

	public ThreadPoolTaskScheduler getScheduler() {
		return scheduler;
	}

	
	@SuppressWarnings("unused")
	private void scheduleCloseOpenInvoices() 
	{	
		gzHome.setCloseInvoiceAfter(Integer.parseInt(gzHome.getCloseInvoiceAfterMins()));
		if (gzHome.getCloseInvoiceAfter() < 0)
			return;
		
		Date startAt = getCloseInvoiceStartAt();
		
		long interval = gzHome.getCloseInvoiceAfter() * 60 * 1000L;
		
		log.info("SCHEDULING CLOSE OPEN INVOICES AT INTERVAL : " + gzHome.getCloseInvoiceAfter() + " MINS " + interval + " MS FROM : " + startAt);
		scheduler.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						
						log.info("Running scheduled event");
						try {
							GregorianCalendar gc = new GregorianCalendar();
							log.info("CLOSING OPEN INVOICES AT : " + gc.getTime());
							closeOpenInvoices();
						} catch (GzPersistenceException e) {
							e.printStackTrace();
							log.error("GzPersistenceException in scheduled CLOSE OPEN INVOICES " + e.getMessage());
						}
					}
				}, startAt, interval);
	}

	public synchronized void closeOpenInvoices() throws GzPersistenceException
	{
		try {
			updateInvoiceSem.acquire();
//			String baseComp = getProperties().getProperty("baseCompany", "c1@rpco.com");
//			gzAccountMgr.createRoyalties(baseComp);
			gzHome.closeOpenInvoices();
			updateInvoiceSem.release();
			
		} catch (InterruptedException e1) {
			log.error(e1.getMessage());
			e1.printStackTrace();
		} 
	}
	
	private Date getCloseInvoiceStartAt() {
		String startAt = gzHome.getCloseInvoiceStartAt();
		GregorianCalendar gc = new GregorianCalendar();
		GregorianCalendar now = new GregorianCalendar();
		if (startAt.equals("-1"))
			return now.getTime();
		
		String[] hhmmss = startAt.split(":");
		if (hhmmss.length != 3)
		{
			log.fatal(" Invalid format for persistent property closeInvoiceStartAt :" + startAt + " must be HH:MM:SS");
			System.exit(3);
		}
		try
		{
			int hour = NumberUtil.parseIntInRange(hhmmss[0], 0, 23);
			int min = NumberUtil.parseIntInRange(hhmmss[1], 0, 59);
			int sec = NumberUtil.parseIntInRange(hhmmss[2], 0, 59);
			
			log.info(gc.getTime());
			
			gc.set(Calendar.HOUR_OF_DAY, hour); 
			gc.set(Calendar.MINUTE, min);
			gc.set(Calendar.SECOND, sec);
			
			if (gc.before(now))
				gc.add(Calendar.HOUR, 24);
			return gc.getTime();
		}
		catch (NumberFormatException e)
		{
			log.fatal(" Invalid format for persistent property closeInvoiceStartAt :" + startAt + " must be HH:MM:SS");
			System.exit(3);
		}
		return null;
	}

	
	public GzAccountMgr getGzAccountMgr() {
		return gzAccountMgr;
	}

	public void setGzAccountMgr(GzAccountMgr gzAccountMgr) {
		this.gzAccountMgr = gzAccountMgr;
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount, String comment) throws GzPersistenceException {
		gzHome.performWithdrawlDeposit(currAccountUser,dwType,dwAmount);
	}
	
	TransactionStatus ts;
	public synchronized void performImport() throws GzServicesException {

		try {
			updateInvoiceSem.acquire();
			
			new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus arg0) {
					
					doPerformImport();
					ts = arg0;
				}
				
			});
			
			updateInvoiceSem.release();
			
			if (gzHome.getCloseInvoiceAfter()<0)
				closeOpenInvoices();
			
		} catch (InterruptedException | GzPersistenceException e1) {
			log.error(e1.getMessage());
			e1.printStackTrace();
		} 
		
		if (ts.isCompleted()==false)
			throw new GzServicesException("performImport failed");
			
	}

	public void doPerformImport() throws RuntimeException{
		
		CsvImportMgr cim = new CsvImportMgr(this);
		try {
			cim.importFiles();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public synchronized void updateEnabled(final GzBaseUser user,final boolean flag)		 // looks wrong
	{
		log.trace("%%%perform updateEnabled for:" + user.getEmail());
		new TransactionTemplate(getTransactionManager()).execute(new TransactionCallbackWithoutResult() {
		@Override
		protected void doInTransactionWithoutResult(TransactionStatus arg0) {
			try {
				doUpdateEnabled(user,flag);
			} catch (GzPersistenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		});
	}
	
	private void doUpdateEnabled(GzBaseUser user,boolean enable) throws GzPersistenceException
	{
		user.setEnabled(enable);
		getGzHome().updateEnabled(user);
		if (enable == true)
			return;
		getGzHome().getDownstreamForParent(user);
		for (GzBaseUser member : user.getMembers())
		{
			doUpdateEnabled(member,enable);
		}
	}

	public void payInvoices(List<GzInvoice> invoices) throws GzPersistenceException {
		if (invoices.isEmpty())
			return;
		
		GzBaseUser cp1 = gzHome.getBaseUserByEmail(invoices.get(0).getPayer());
		GzBaseUser cp2 = gzHome.getBaseUserByEmail(invoices.get(0).getPayee());
		
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		for (GzInvoice invoice : invoices)
		{
			if (cp1.getEmail().equals(invoices.get(0).getPayer()))
				payInvoice(cp1,cp2,invoice,now);
			else
				payInvoice(cp1,cp2,invoice,now);
		}
	}

	private void payInvoice(GzBaseUser payer, GzBaseUser payee, GzInvoice invoice, Date now) throws GzPersistenceException {
	
		invoice.setPaymentDate(now);
		gzHome.updatePayment(invoice);					
	}
	
	public void generateAndSendXls(long id,GzBaseUser currUser) throws Exception{
		
		InvoiceAnalytic ia = new InvoiceAnalytic(this);
		ia.createWorkBook(id);
		String subject = "Dear " + currUser.getContact() + ", please find xls workbook for invoice : #" + id;
		String content = "Dear " + currUser.getContact() + ", please find xls workbook for invoice : #" + id 
				+ " for you to review. \nKind regards - Goldmine Gaming Support Team.";
		
		List<String> attachments = new ArrayList<String>();
		attachments.add(ia.getXlsPath());
		getMail().sendMail(currUser,subject, content, attachments );
		log.info("Sucessfully sent xls path");
	}
	
	public void resetPassword(GzBaseUser baseUser,boolean first) throws Exception {
		int cnt=0;
		StringBuilder pw = new StringBuilder();
		while (cnt<10)
		{
			Random rand = new Random();
			
			switch (rand.nextInt(3))
			{
				case 0: int lim = rand.nextInt(25);
						char ch = 'A';
						for (; lim>0; ch++,lim--)
							;
						pw.append(ch); break;
				case 1: lim = rand.nextInt(8);
						ch = '1';
						for (; lim>0; ch++,lim--)
							;
						pw.append(ch); break;
				case 2: lim = rand.nextInt(25);
						ch = 'a';
						for (; lim>0; ch++,lim--)
							;
						pw.append(ch); break;
			}
			cnt++;
		}
		
		log.info("New password for : " + baseUser.getEmail() + " is : " + pw);
		baseUser.setPassword(passwordEncoder.encode(pw.toString()));

		gzHome.updateBaseUserProfile(baseUser);
		if (first)
			mail.notifyRegistration(baseUser, null,pw.toString());
		else
			mail.notifyPasswordReset(baseUser,null,pw.toString());
	}
	
	public String tryResetPassword(String email)
	{
		log.info("Received request to password ");
		GzBaseUser baseUser;
		try {
			baseUser = getGzHome().getBaseUserByEmail(email);
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
			return "Error on reset, please try later.";
		}
		if (baseUser==null)
		{
			return "User : " + email + " has not been registered - please consult your agent";
		}
		
		try {
			resetPassword(baseUser, false);
		} catch (Exception e) {
			log.error(StackDump.toString(e));
			return "Error on mail server, please try later.";
		}
		return "";
	}
	
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public Mail getMail() {
		return mail;
	}

	public void setMail(Mail mail) {
		this.mail = mail;
	}

	public String getGzProperties() {
		return gzProperties;
	}

	public void setGzProperties(String gzProperties) {
		this.gzProperties = gzProperties;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	

}
