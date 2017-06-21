package org.rp.importer;

import org.rp.baseuser.GzBaseUser;
import org.rp.util.EmailValidator;

public class PlayerRec{
	
	static EmailValidator ev = new EmailValidator();
	
	private String email;
	private String contact;
	private double bankerTurnover;
	private double turnover;
	private double rollingBet;
	private double fees;
	private GzBaseUser player;
	
	public PlayerRec(String contact,double turnover,double rollingBet,double bankerTurnover,double fees)
	{
		setContact(contact);
		setBankerTurnover(bankerTurnover);
		setTurnover(turnover);
		setRollingBet(rollingBet);
		setFees(fees);
	}
	
	public void massageContactCreateEmail(String suffix) throws CsvImporterException
	{
		// contacts can be YH LAU or 陈晨
		//	pkim khor<img src="/images/emoji/D83DDC30.png"/><img src="/images/emoji/D83DDC30.png"/><img src="/images/emoji/D83DDC30.png"/>
		// need tp normalize and create meaningful email
		
		while (stripOutImageRefs())
			;
		createEmail(suffix);
	}

	private void createEmail(String suffix) throws CsvImporterException{
		String EMAIl_PATTERN = "[^a-zA-Z0-9!#$%&@'*+-/=?^_`{|}~.]+";
		email = contact.replaceAll(EMAIl_PATTERN, "") + suffix;
		email = email.toLowerCase();
		if (!ev.validate(email))
			throw new CsvImporterException("Could not create valid email from : " + contact);
	}

	private boolean stripOutImageRefs() {
		int pos = contact.indexOf("<img src=");
		if (pos<0)
			return false;
		int end = contact.indexOf("/>",pos+"<img src=".length());
		if (end<-1)
			contact = contact.substring(0,pos);
		else
			contact = contact.substring(0,pos) + contact.substring(end+"/>".length());
		return true;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public double getBankerTurnover() {
		return bankerTurnover;
	}

	public void setBankerTurnover(double bankerTurnover) {
		this.bankerTurnover = bankerTurnover;
	}

	public double getTurnover() {
		return turnover;
	}

	public void setTurnover(double turnover) {
		this.turnover = turnover;
	}

	public double getRollingBet() {
		return rollingBet;
	}

	public void setRollingBet(double rollingBet) {
		this.rollingBet = rollingBet;
	}

	public double getFees() {
		return fees;
	}

	public void setFees(double fees) {
		this.fees = fees;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "PlayerRec [email=" + email + ", contact=" + contact + ", bankerTurnover=" + bankerTurnover
				+ ", turnover=" + turnover + ", rollingBet=" + rollingBet + ", fees=" + fees + "]";
	}

	public GzBaseUser getPlayer() {
		return player;
	}

	public void setPlayer(GzBaseUser player) {
		this.player = player;
	}
}
