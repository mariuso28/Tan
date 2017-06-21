package org.rp.account.ss;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.rp.account.GzTransaction;

public class TransactionSheet{

	private Sheet sheet;
	private static String[] titles = {"transid", "made", "pay from","pay to","type","amount","source"};
	private Row row;
    private Cell cell;
    private int rownum = 1;
    private Map<String, CellStyle> styles;
    private int sheetNum;
	
    TransactionSheet(Sheet sheet,Map<String, CellStyle> styles)
	{
		setSheet(sheet);
		setStyles(styles);
		setSheetNum(sheetNum);
		initializeSheet();
	}

    void addInvoiceId(long id) {
		row = sheet.createRow(rownum);
		
		int col=0;
		
		cell = row.createCell(col);
        cell.setCellValue("Transactions for Inv:" + id);
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*20);
        
        rownum++;
    }
    
    // {"transid", "made", "pay from","pay to","type","amount","source"};
    void addTransaction(GzTransaction tx) {
		row = sheet.createRow(rownum);
		
		int col=0;
		
		cell = row.createCell(col);
        cell.setCellValue(tx.getId());
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*20);
        
        cell = row.createCell(col);
        cell.setCellValue(tx.getTimestamp());
        cell.setCellStyle(styles.get("cell_normal_date"));
        sheet.setColumnWidth(col++, 256*20);
        
        cell = row.createCell(col);
        cell.setCellValue(tx.getPayer());
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*20);
        
        cell = row.createCell(col);
        cell.setCellValue(tx.getPayee());
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*20);
        
        cell = row.createCell(col);
        if (tx.getType().equals(GzTransaction.PLAYERTURNOVER))
        	cell.setCellValue("Turnover");
        else
        	cell.setCellValue("Banker Turnover");
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*20);
        
        cell = row.createCell(col);
        cell.setCellValue(tx.getAmount());
        cell.setCellStyle(styles.get("cell_double"));
        sheet.setColumnWidth(col++, 256*20);
        
        cell = row.createCell(col);
        cell.setCellValue(tx.getSource());
        cell.setCellStyle(styles.get("cell_normal"));
        sheet.setColumnWidth(col++, 256*40);
        
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
	            
	            if (i==titles.length-1)
	            	sheet.setColumnWidth(col++, 256*40);
	            else
	            	sheet.setColumnWidth(col++, 256*20);
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

	public int getSheetNum() {
		return sheetNum;
	}

	public void setSheetNum(int sheetNum) {
		this.sheetNum = sheetNum;
	}

	
	
}
