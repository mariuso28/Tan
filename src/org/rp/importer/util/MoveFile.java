package org.rp.importer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

public class MoveFile
{
	private static Logger log = Logger.getLogger(MoveFile.class);
	
    public static void move(String source,String dest)
    {
    	InputStream inStream = null;
    	OutputStream outStream = null;

    	log.info("Moving file : " + source + " to: " + dest);
    	try{

    	    File afile =new File(source);
    	    File bfile =new File(dest);

    	    inStream = new FileInputStream(afile);
    	    outStream = new FileOutputStream(bfile);

    	    byte[] buffer = new byte[1024];
    	    int length;
    	    //copy the file content in bytes
    	    while ((length = inStream.read(buffer)) > 0){
    	    	outStream.write(buffer, 0, length);
    	    }

    	    inStream.close();
    	    outStream.close();

    	    //delete the original file
    	    afile.delete();

        	log.info("File moved successful");

    	}catch(IOException e){
    		e.printStackTrace();
    		log.error("Error moving file " + e.getMessage());
    	}
    }

	public static void deleteFile(String absolutePath) {
		
		File afile = new File(absolutePath);
	    afile.delete();
      	log.info("File deleted successful");
	}
}

