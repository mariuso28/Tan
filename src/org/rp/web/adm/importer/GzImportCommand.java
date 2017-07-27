package org.rp.web.adm.importer;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class GzImportCommand implements Serializable{

	private static final long serialVersionUID = 5168551159585929874L;
	private MultipartFile importFile;
	
	public GzImportCommand()
	{
	}

	public MultipartFile getImportFile() {
		return importFile;
	}

	public void setImportFile(MultipartFile importFile) {
		this.importFile = importFile;
	}

}
