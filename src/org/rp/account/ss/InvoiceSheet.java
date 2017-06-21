package org.rp.account.ss;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.rp.account.GzInvoice;
import org.rp.baseuser.GzRole;

public class InvoiceSheet{

	private GzRole role;
	private Sheet sheet;
	private static String[] titles = {"id","pay from","pay to","amount","comm","net amnt","issued","paid"};
	private Row row;
    private Cell cell;
    private int rownum = 1;
    private Map<String, CellStyle> styles;
	
	InvoiceSheet(Sheet sheet,GzRole role,Map<String, CellStyle> styles)
	{
		setSheet(sheet);
		setRole(role);
		setStyles(styles);
		initializeSheet();
	}

	public void addParentInvoice(GzInvoice parentInvoice, boolean hasSubInvoices) {
		
		row = sheet.createRow(rownum);
		cell = row.createCell(0);
        cell.setCellValue("Invoice");
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(0, 256*10);
		rownum++;
		addInvoice(parentInvoice);
		
		if (!hasSubInvoices)
			return;
		
		row = sheet.createRow(rownum);
		cell = row.createCell(0);
        cell.setCellValue("Sub Invoices");
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(0, 256*20);
		rownum++;
	}
	
	void addInvoice(GzInvoice invoice) {
		row = sheet.createRow(rownum);
		
		int col=0;
		
		cell = row.createCell(col);
        cell.setCellValue(invoice.getId());
        cell.setCellStyle(styles.get("cell_num"));
        sheet.setColumnWidth(col++, 256*10);
        
        cell = row.createCell(col);
        cell.setCellValue(invoice.getPayer());
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*20);
        
        cell = row.createCell(col);
        cell.setCellValue(invoice.getPayee());
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*20);
		
        cell = row.createCell(col);
        cell.setCellValue(invoice.getAmount());
        cell.setCellStyle(styles.get("cell_double"));
        sheet.setColumnWidth(col++, 256*10);
        
        cell = row.createCell(col);
        cell.setCellValue(invoice.getRoyalty());
        cell.setCellStyle(styles.get("cell_double"));
        sheet.setColumnWidth(col++, 256*10);
        
        cell = row.createCell(col);
        cell.setCellValue(invoice.getNetAmount());
        cell.setCellStyle(styles.get("cell_double"));
        sheet.setColumnWidth(col++, 256*10);
        	
		cell = row.createCell(col);
        cell.setCellValue(invoice.getTimestamp());
        cell.setCellStyle(styles.get("cell_normal_date"));
        sheet.setColumnWidth(col++, 256*10);
        
        
        if (invoice.getPaymentDate()!=null)
        {
        	cell = row.createCell(col);
	        cell.setCellValue(invoice.getPaymentDate().getTime());
	        cell.setCellStyle(styles.get("cell_normal_date"));
	        sheet.setColumnWidth(col++, 256*10);
		}
        else
        {
        	cell = row.createCell(col);
            cell.setCellValue("Outstanding");
            cell.setCellStyle(styles.get("cell_normal"));
            sheet.setColumnWidth(col++, 256*10);
	    
            cell = row.createCell(col);
            cell.setCellValue("");
            cell.setCellStyle(styles.get("cell_normal"));
            sheet.setColumnWidth(col++, 256*10);
		}
        
        rownum++;
	}
	
	private void initializeSheet()
	 {
		//turn off gridlines
	        sheet.setDisplayGridlines(false);
	        sheet.setPrintGridlines(false);
	        sheet.setFitToPage(true);
	        sheet.setHorizontallyCenter(true);
	        PrintSetup printSetup = sheet.getPrintSetup();
	        printSetup.setLandscape(true);

	        //the following three statements are required only for HSSF
	        sheet.setAutobreaks(true);
	        printSetup.setFitHeight((short)1);
	        printSetup.setFitWidth((short)1);

	        //the header row: centered text in 48pt font
	        Row headerRow = sheet.createRow(0);
	        headerRow.setHeightInPoints(12.75f);
	        int col = 0;
	        for (int i = 0; i < titles.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(titles[i]);
	            cell.setCellStyle(styles.get("header"));
	            if (col==1 || col==2)
	            	sheet.setColumnWidth(col++, 256*16);
	            else
	            	sheet.setColumnWidth(col++, 256*10);
	        }
	       
	        //freeze the first row
	        sheet.createFreezePane(0, 1);
	 }
	
	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public GzRole getRole() {
		return role;
	}

	public void setRole(GzRole role) {
		this.role = role;
	}

	public static String[] getTitles() {
		return titles;
	}

	public static void setTitles(String[] titles) {
		InvoiceSheet.titles = titles;
	}

	public Row getRow() {
		return row;
	}

	public void setRow(Row row) {
		this.row = row;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}

	public int getRownum() {
		return rownum;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public Map<String, CellStyle> getStyles() {
		return styles;
	}

	public void setStyles(Map<String, CellStyle> styles) {
		this.styles = styles;
	}

	
	
}
