package org.rp.web.adm.importer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.web.adm.GzMemberVerify;
import org.rp.web.adm.member.GzImportForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser"})

@RequestMapping("/admImport")
public class GzAdmImporterController {

	private static Logger log = Logger.getLogger(GzAdmImporterController.class);
	@Autowired
	private GzServices gzServices;

	@RequestMapping(value="/importData", method = RequestMethod.GET)
	public ModelAndView importData(ModelMap model)
	{
		log.info("in importData");
		GzImportForm importForm = new GzImportForm();
		return new ModelAndView("admImport","importForm",importForm);
	}
	
	@RequestMapping(value = "/cancelImportData", method = RequestMethod.GET)
    public Object cancelImportData(ModelMap model)
    {
        return "redirect:/rp/adm/backToAdm";
    }
	
	@RequestMapping(value = "/processImport", params = "importData", method = RequestMethod.POST)
    public ModelAndView importData(@ModelAttribute("importForm") GzImportForm importForm,ModelMap model) {
       			
		log.info("In importData");
		
		GzImportCommand command = importForm.getCommand();
		
		importForm = new GzImportForm();
		try {
			importData(command.getImportFile());
		} catch (Exception e) {
			e.printStackTrace();
			importForm.setErrMsg("Could not import data - check with support");
			return new ModelAndView("admImport","admImportForm",admImportForm);
		}
		
		return new ModelAndView("admImport","admImportForm",admImportForm);
	}
	
	private String importData(MultipartFile importFile) throws Exception 
	{	
		String saveDirectory = gzServices.getProperties().getProperty("importFolder");
		if (saveDirectory==null)
			throw new Exception("importFolder not in config");
		
		log.info("importData to " + saveDirectory);
			
         String fileName = importFile.getOriginalFilename();
         log.info("adding apk : " + fileName + " to : " + saveDirectory );
         if (!"".equalsIgnoreCase(fileName)) {
             // Handle file content - multipartFile.getInputStream()
        	 
        	 String path = saveDirectory + fileName;
        	 importFile.transferTo(new File(path));
            return path;
         }
         throw new Exception("Empty File Name");    
	 }
	
	
}
