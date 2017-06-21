package org.rp.web.account.pdf;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFInvoiceBuilder extends AbstractITextPdfView {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PDFInvoiceBuilder.class);

	public void buildPdfDocument(Map<String, Object> model, Document doc,
			PdfWriter writer, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		{
			PdfOpenInvoices.buildPdfDocument(model, doc);
		}
		
	}
}