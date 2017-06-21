package org.rp.account.ss;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.rp.account.GzInvoice;
import org.rp.account.GzTransaction;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzInvoiceAnalyticException;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class InvoiceAnalytic {
	private static final Logger log = Logger.getLogger(InvoiceAnalytic.class);	
	
	private  Workbook wb;
	private GzServices services;
	private GzHome home;
	private GzInvoice rootInvoice;
	private List<InvoiceSheet> sheets;
	private Map<String, CellStyle> styles;
	private String xlsPath = null;
	private TransactionSheet transSheet;
	
	
	public InvoiceAnalytic(GzServices gzServices) throws Exception {
			services = gzServices;
			home = services.getGzHome();
	        wb = new XSSFWorkbook();
	        styles = WbStyles.createStyles(wb);
	}
	
	public void createWorkBook(long rootInvoiceId) throws GzPersistenceException, GzInvoiceAnalyticException
	{
		rootInvoice = home.getInvoiceForId(rootInvoiceId);
		GzBaseUser payer = home.getBaseUserByEmail(rootInvoice.getPayer());
		GzBaseUser payee = home.getBaseUserByEmail(rootInvoice.getPayee());
		
		GzRole topRole;
		if (payer.getRole().getRank()<payee.getRole().getRank())
			topRole = payer.getRole();
		else
			topRole = payee.getRole();
		
		sheets = new ArrayList<InvoiceSheet>();
		List<GzRole> roles = topRole.getAllRoles();
		for (GzRole role : roles)
		{
			if (role.equals(GzRole.ROLE_PLAY))
				break;
			
			Sheet sheet = wb.createSheet("#"+Long.toString(rootInvoiceId) + " - " + role.getShortCode() );
			sheets.add(new InvoiceSheet(sheet,role,styles));
		}
		Sheet sheet = wb.createSheet("Player Transactions");
		transSheet = new TransactionSheet(sheet,styles);
		
		addSubInvoices(rootInvoice);
			
		String path = (String) services.getProperties().get("xlsFolder");
		if (path==null)
		{
			log.error("xlsPath not found");
			throw new GzInvoiceAnalyticException("xlsPath not found");
		}
		if (!path.endsWith("/"))
			path += "/";
		
		path += "Invoice #" + rootInvoiceId + ".xls";
        if (wb instanceof XSSFWorkbook) 
        	path += "x";
        
        FileOutputStream out;
		try {
			out = new FileOutputStream(path);
			wb.write(out);
		    out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new GzInvoiceAnalyticException("creation of xls file : " + path + " failed - " + e.getMessage());
		}
      
        setXlsPath(path);
		
        log.info("Sheet : " + path + " created..");
	}
	
	private void addSubInvoices(GzInvoice parentInvoice) throws GzPersistenceException
	{
		GzRole useRole;
		GzBaseUser payer = home.getBaseUserByEmail(parentInvoice.getPayer());
		GzBaseUser payee = home.getBaseUserByEmail(parentInvoice.getPayee());
		if (payer.getRole().getRank()<payee.getRole().getRank())
			useRole = payer.getRole();
		else
			useRole = payee.getRole();
		
		InvoiceSheet useSheet = null;
		for (InvoiceSheet sheet : sheets)
		{
			if (sheet.getRole().equals(useRole))
			{
				useSheet = sheet;
				break;
			}
		}
		
		if (payer.getRole().equals(GzRole.ROLE_PLAY))
		{
			addTransactionsForInvoice(parentInvoice,payer);
		}
		else
		if (payee.getRole().equals(GzRole.ROLE_PLAY))
		{
			addTransactionsForInvoice(parentInvoice,payee);
		}
		else
		{
			useSheet.addParentInvoice(parentInvoice,true);
			List<GzInvoice> subs = home.getInvoicesForInvoice(parentInvoice);
			for (GzInvoice invoice : subs)
			{
				useSheet.addInvoice(invoice);
				addSubInvoices(invoice);
			}
		}
	}
	
	
	private void addTransactionsForInvoice(GzInvoice parentInvoice, GzBaseUser payee) throws GzPersistenceException {
		
		List<GzTransaction> trans = home.getTransactionsForInvoice(parentInvoice);
		transSheet.addInvoiceId(parentInvoice.getId());
		for (GzTransaction tx : trans)
			transSheet.addTransaction(tx);
	}
	
	public String getXlsPath() {
		return xlsPath;
	}

	public void setXlsPath(String xlsPath) {
		this.xlsPath = xlsPath;
	}
	
	public static void main(String[] args)
	{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"rp-service.xml");

		GzServices services =  (GzServices) context.getBean("gzServices");
		
		InvoiceAnalytic ia;
		try {
			ia = new InvoiceAnalytic(services);
			ia.createWorkBook(186);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

}
