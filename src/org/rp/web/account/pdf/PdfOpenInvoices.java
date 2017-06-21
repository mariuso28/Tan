package org.rp.web.account.pdf;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.rp.account.GzInvoice;
import org.rp.baseuser.GzBaseUser;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PdfOpenInvoices {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PdfOpenInvoices.class);

	public static void buildPdfDocument(Map<String, Object> model, Document doc)
				throws Exception {
		{
			GzBaseUser currUser = (GzBaseUser) model.get("currUser");
			GzBaseUser currAccountUser = (GzBaseUser) model.get("currAccountUser");
			@SuppressWarnings("unchecked")
			List<GzInvoice> outstandingInvoices = (List<GzInvoice>) model.get("outStandingInvoices");
			
			String title = "Outstanding Invoices for : " + currUser.getRole().getDesc() +  " - " 
				+ currUser.getContact() + " and " + currAccountUser.getRole().getDesc() +  " - " 
				+ currAccountUser.getContact();
			
			doc.add(new Paragraph(title));
			doc.add(new Paragraph("Amount Due:"));
			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100.0f);
			table.setWidths(new float[] {1.0f, 1.0f, 3.0f, 2.0f, 1.0f, 2.0f});
			table.setSpacingBefore(10);

			// define font for table header row
			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setSize(8);
			font.setColor(BaseColor.WHITE);

			// define table header cell
			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.BLUE);
			cell.setPadding(5);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			// write table header
			cell.setPhrase(new Phrase("Invoice#", font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Issue Date", font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Pay To", font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Amount", font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Royalty", font));
			table.addCell(cell);
			cell.setPhrase(new Phrase("Due", font));
			table.addCell(cell);

			DecimalFormat df = new DecimalFormat("#0.00");
			SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yy");
			
			double totalAmountCu = 0.0;
			double totalCommssionCu = 0.0;
			double totalDueCu = 0.0;
			double totalAmountCau = 0.0;
			double totalCommssionCau = 0.0;
			double totalDueCau = 0.0;
			int cnt=0;
			
			font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setSize(8);
			font.setColor(BaseColor.BLACK);
			cell = getRightCell();
			
			for (GzInvoice invoice : outstandingInvoices) {
				cnt++;
				
				cell.setPhrase(new Phrase(String.valueOf(invoice.getId()), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df1.format(invoice.getTimestamp()), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(invoice.getPayee(), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(invoice.getAmount()), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(invoice.getRoyalty()), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(invoice.getNetAmount()), font));
				table.addCell(cell);
			
				if (invoice.getPayee().equals(currUser.getEmail()))
				{
					totalAmountCu += invoice.getAmount();
					totalCommssionCu += invoice.getRoyalty();
					totalDueCu += invoice.getNetAmount();
				}
				else
				{
					totalAmountCau += invoice.getAmount();
					totalCommssionCau += invoice.getRoyalty();
					totalDueCau += invoice.getNetAmount();
				}
			}
			if (cnt>0)
			{
				cell.setPhrase(new Phrase("Total ",font));
				table.addCell(cell);
				table.addCell(new Phrase(" ",font));
				table.addCell(new Phrase(" ",font));
				cell.setPhrase(new Phrase(currUser.getEmail(),font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(totalAmountCu), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(totalCommssionCu), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(totalDueCu), font));
				table.addCell(cell);
				
				cell.setPhrase(new Phrase("Total ",font));
				table.addCell(cell);
				table.addCell(new Phrase(" ",font));
				table.addCell(new Phrase(" ",font));
				cell.setPhrase(new Phrase(currAccountUser.getEmail(),font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(totalAmountCau), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(totalCommssionCau), font));
				table.addCell(cell);
				cell.setPhrase(new Phrase(df.format(totalDueCau), font));
				table.addCell(cell);
				
				doc.add(table);			
			}
			else 
			{
				doc.add(new Paragraph(" "));
				title = "No Current Outstanding Invoices";
				doc.add(new Paragraph(title));
			}
		}
		
	}

	static PdfPCell getRightCell()
	{
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		return cell;
	}
	
}
