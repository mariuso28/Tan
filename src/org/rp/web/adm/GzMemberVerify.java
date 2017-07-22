package org.rp.web.adm;

import org.rp.baseuser.GzBaseUser;
import org.rp.home.GzHome;

public class GzMemberVerify {

	public GzMemberVerify()
	{
	}
	
	String verify(GzMemberCommand command,GzHome home,GzBaseUser superior)
	{
		String msg = "";
		home.getDownstreamForParent(superior);
		msg = verifyUserName(command.getUsername(),home);
		if (!msg.isEmpty())
			return msg;
		msg = verifyPassword(command.getPassword(),command.getvPassword());
		if (!msg.isEmpty())
			return msg;
		msg = verifyWeChat(command.getWeChatName(),superior);
		if (!msg.isEmpty())
			return msg;
		return msg;
	}

	public String verifyPassword(String password,String vPassword) {
		if (!vPassword.equals(password))
		{
			return "Password/Verify Password mismatch - please fix";
		}
		
		if (password.length()<8)
			return "Password should be 8 chars or more - please fix";
		
		return "";
	}

	private String verifyWeChat(String weChatName, GzBaseUser superior) {
		for (GzBaseUser bu : superior.getMembers())
			if (bu.getContact().equals(weChatName))
				return "Member with WeChat name : " + weChatName + " - exists for superior : " + superior.getContact();
		return "";
	}

	private String verifyUserName(String username,GzHome home) {
		
		if (username==null || username.length()<3)
			return "Invalid Username - must be >= 3 chars";
		GzBaseUser bu = home.getBaseUserByEmail(username);
		if (bu != null)
		{
			String sa = suggestAlternative(home,username);
			return "Username : " + username + " - already taken - suggest : " + sa;
		}
		return "";
	}

	private String suggestAlternative(GzHome home, String username) {
		int i=1;
		String num="";
		for (int pos = username.length()-1; pos>=0; pos--)
		{
			char ch = username.charAt(pos);
			if (!Character.isDigit(ch))
				break;
			num = Character.toString(ch) + num;
		}
		if (!num.isEmpty())
		{
			username = username.substring(0,username.length()-num.length());
			i = Integer.parseInt(num) + 1;
		}
		while (true)
		{
			String alt = Integer.toString(i++);
			if (alt.contains("4"))
				continue;
			String AltUsername = username + alt;
			GzBaseUser bu = home.getBaseUserByEmail(AltUsername);
			if (bu == null)
				return AltUsername;
		}
	}
}
