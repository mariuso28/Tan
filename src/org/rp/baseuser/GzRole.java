package org.rp.baseuser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.agent.GzAgent;
import org.rp.agent.GzComp;
import org.rp.agent.GzMA;
import org.rp.agent.GzSMA;
import org.rp.agent.GzZMA;

public enum GzRole implements Serializable{
	
		ROLE_PLAY("Player",0,'p',"Player",GzBaseUser.class,"FFD6D6"),
		ROLE_AGENT("Agent",1,'a',"Agent",GzAgent.class,"FFF7D6"),
		ROLE_MA("Master Agent",2,'m',"MA",GzMA.class,"E9FFD6"),
		ROLE_SMA("Super Master Agent",3,'s',"SMA",GzSMA.class,"D6FFEE"),
		ROLE_ZMA("Corporate Master Agent",4,'z',"ZMA",GzZMA.class,"D6ECFF"),
		ROLE_COMP("Company",5,'c',"Company",GzComp.class,"E3D6FF"),
		ROLE_ADMIN("Admin",6,'x',"Admin",GzAdmin.class,"F7D6FF");
		
		private static final Logger log = Logger.getLogger(GzRole.class);
		private String desc;
		private int rank;					// don't use ordinal cos we might mess with the enum order
		private Character code;
		private String shortCode;
		@SuppressWarnings("rawtypes")
		private Class correspondingClass;
		private String color;
		private static HashMap<Character,GzRole> codeMap;
		
		@SuppressWarnings("rawtypes")
		private GzRole(String desc,int rank,char code,String shortCode,Class correspondingClass,String color)
		{
			setRank(rank);
			setDesc(desc);
			setCode(code);
			setColor(color);
			setCorrespondingClass(correspondingClass);
			setShortCode(shortCode);
			GzRole.addCodeMap(this,code);
		}
		
		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		private static void addCodeMap(GzRole role,Character code)
		{
			if (codeMap==null)									// static initialization dont work
				codeMap=new HashMap<Character,GzRole>();
			codeMap.put(code, role);
		}
		
		public static GzRole getRoleForCode(String code)
		{
	//		log.info(code);
			Character ch = '?';
			for (int index=code.length()-1; index>=0; index--)
			{
				ch = code.charAt(index);
				if (!Character.isDigit(ch))
					break;
			}
			return codeMap.get(ch);
		}
		
		@SuppressWarnings("rawtypes")
		public static Class getRoleClassForCode(String code)
		{
			GzRole role = getRoleForCode(code);
			return role.getCorrespondingClass();
		}
		
		private void setDesc(String desc)
		{
			this.desc = desc;
		}
		
		public void setRank(int rank) {
			this.rank = rank;
		}

		public int getRank() {
			return rank;
		}

		public String getDesc()
		{
			return desc;
		}
		
		public boolean isAgentRole()
		{
			return this.equals(ROLE_COMP) || this.equals(ROLE_ZMA) || this.equals(ROLE_SMA)
				|| this.equals(ROLE_MA) || this.equals(ROLE_AGENT);
		}
		
		public List<GzRole> getAllRoles()
		{
			List<GzRole> roles = new ArrayList<GzRole>();
			roles.add(this);
			if (rank==0)
				return roles;
			
			if (rank>0)
			{
				roles.add(1,ROLE_PLAY);
			}
			if (rank<2)
				return roles;
			if (rank>1)
			{
				roles.add(1,ROLE_AGENT);
			}
			if (rank==2)
				return roles;
			if (rank>2)
			{
				roles.add(1,ROLE_MA);
			}
			if (rank==3)
				return roles;
			if (rank>3)
			{
				roles.add(1,ROLE_SMA);
			}
			if (rank==4)
				return roles;
			if (rank>4)
			{
				roles.add(1,ROLE_ZMA);
			}
			if (rank==5)
				return roles;
			if (rank>5)
			{
				roles.add(1,ROLE_COMP);
			}
			if (rank==6)
				return roles;
			
			log.error("getRoles : unknown high level role : " + this);
			
			return new ArrayList<GzRole>();
		}
		
		public static GzRole getRoleForShortCode(String shortCode)
		{
			if (shortCode.equalsIgnoreCase("Admin"))
				return GzRole.ROLE_ADMIN;
			if (shortCode.equalsIgnoreCase("Company"))
				return GzRole.ROLE_COMP;
			if (shortCode.equalsIgnoreCase("ZMA"))
				return GzRole.ROLE_ZMA;
			if (shortCode.equalsIgnoreCase("SMA"))
				return GzRole.ROLE_SMA;
			if (shortCode.equalsIgnoreCase("MA"))
				return GzRole.ROLE_MA;
			if (shortCode.equalsIgnoreCase("Agent"))
				return GzRole.ROLE_AGENT;
			if (shortCode.toLowerCase().startsWith("play"))
				return GzRole.ROLE_PLAY;
			return null;
		}

		public void setCode(Character code) {
			this.code = code;
		}

		public Character getCode() {
			return code;
		}

		public void setShortCode(String shortCode) {
			this.shortCode = shortCode;
		}

		public String getShortCode() {
			return shortCode;
		}

		public void setCorrespondingClass(@SuppressWarnings("rawtypes") Class correspondingClass) {
			this.correspondingClass = correspondingClass;
		}

		@SuppressWarnings("rawtypes")
		public Class getCorrespondingClass() {
			return correspondingClass;
		}

		
}
