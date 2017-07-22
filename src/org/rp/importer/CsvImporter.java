package org.rp.importer;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.rp.agent.GzAgent;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzProfile;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;

import com.opencsv.CSVReader;

public class CsvImporter {

	private static Logger log = Logger.getLogger(CsvImporter.class);
	private String path;
	private GzBaseUser agent;
	private GzServices gzServices;
	private String source;
	
	public CsvImporter(String path, GzServices gzServices,String source)
	{
		setPath(path);
		setGzServices(gzServices);
		setSource(source);
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void readRecs(AgentRec agentRec) throws IOException
	{
		CSVReader reader = new CSVReader(new FileReader(path));
		
	    List<PlayerRec> list = new ArrayList<PlayerRec>();
	    List<String[]> lines;
	    try
	    {
	    	lines = reader.readAll();
		    reader.close();
	    }
	    catch (Exception e)
	    {
	    	log.error("Error parsing : " + path + " file ignored");
	    	reader.close();
	    	return;
	    }
	    
	    int cnt=0;
	    
	    for (String[] line : lines)
	    {
	    	if (cnt < 3)
	    	{
	    		cnt++;
	    		continue;
	    	}
	    	
	    	PlayerRec ir;
			try {
				ir = createImportRec(line);
				log.info(ir);
				list.add(ir);
				cnt++;
			} catch (Exception e) {
				// e.printStackTrace();
				log.error("Could not create record from : " + line.toString() + " Ignored");
			}
	    }
	   
	    log.info("Read "  + cnt + " Recs");
	    
	    importTransactions(list,agentRec);
	    createTransactionBet(list,agentRec);
	}
	
	private void importTransactions(List<PlayerRec> list, AgentRec agentRec) {
		log.info("%%% Importing transactions %%%");
		for (PlayerRec pr : list)
		{
			try {
				pr.massageContactCreateEmail(agentRec.getSuffix());
				log.info(pr);
				GzBaseUser player = gzServices.getGzHome().getBaseUserByEmail(pr.getEmail());
				if (player==null)
					player = createPlayer(pr,agentRec.getAgent(),40,40);
				
				if (player != null)
					pr.setPlayer(player);
				
			} catch (CsvImporterException e) {
				log.error(e.getMessage() + " Contact : " + pr.getContact() + " ignored");
			} catch (GzPersistenceException e) {
				e.printStackTrace();
				log.error(e.getMessage() + " Could not create Player for : " + pr.getEmail());
			}
		}
	}
	
	private void createTransactionBet(List<PlayerRec> list, AgentRec agentRec)
	{
		for (PlayerRec pr : list)
		{
			if (pr.getPlayer()==null)
				continue;
			
			try {
				@SuppressWarnings("unused")
				GzBaseUser player = gzServices.getGzHome().getBaseUserByEmail(pr.getPlayer().getEmail());
		//		gzServices.getGzHome().getParentForUser(player);
//				gzServices.getGzAccountMgr().createTransactionsForTurnovers(player, pr.getTurnover(), pr.getBankerTurnover(), source);
			} catch (GzPersistenceException e) {
				e.printStackTrace();
			}
			
			// break;					// USE FOR 1 TRANSACTION ONLY
		}
	}

	private GzBaseUser createPlayer(PlayerRec pr, GzAgent agent,double playerRoyalty,double bankerRoyalty) {
		log.info("Creating player : " + pr.getEmail() + " for : " + agent.getEmail());
		
		GzProfile profile = new GzProfile(pr.getContact(),pr.getEmail(),"8888888");
		GzBaseUser newMember = new GzBaseUser();
		newMember.setEmail(pr.getEmail());
		newMember.copyProfileValues(profile);
		newMember.setParent(agent);
		newMember.setParentCode(agent.getCode());
		newMember.setRole(GzRole.ROLE_PLAY);
		newMember.setEnabled(true);
		
		try {
			gzServices.getGzHome().storeBaseUser(newMember);
			GzBaseUser bu = gzServices.getGzHome().getBaseUserByEmail(pr.getEmail());
			return bu;
		} catch (GzPersistenceException e) {
			e.printStackTrace();
			log.error("Could not create : " + pr.getEmail() + " for : " + agent.getEmail() + " - " + e.getMessage());
			return null;
		}
	}

	private PlayerRec createImportRec(String[] line) throws Exception{
	
		return new PlayerRec(line[0],Double.parseDouble(line['H'-'A']),Double.parseDouble(line['K'-'A']),
								Double.parseDouble(line['O'-'A']),Double.parseDouble(line['P'-'A']),Double.parseDouble(line['U'-'A']));
	}
	
	public GzBaseUser getAgent() {
		return agent;
	}

	public void setAgent(GzBaseUser agent) {
		this.agent = agent;
	}

	public GzServices getGzServices() {
		return gzServices;
	}

	public void setGzServices(GzServices gzServices) {
		this.gzServices = gzServices;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}