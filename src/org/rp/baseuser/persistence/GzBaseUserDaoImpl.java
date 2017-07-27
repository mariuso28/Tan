package org.rp.baseuser.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.rp.account.persistence.GzAccountDaoImpl;
import org.rp.admin.GzAdmin;
import org.rp.agent.GzAgent;
import org.rp.agent.GzComp;
import org.rp.agent.GzMA;
import org.rp.agent.GzSMA;
import org.rp.agent.GzZMA;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.util.GetNextNumberNo4s;
import org.rp.util.StackDump;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Transactional
public class GzBaseUserDaoImpl extends GzAccountDaoImpl implements GzBaseUserDao {
	
	private static Logger log = Logger.getLogger(GzBaseUserDaoImpl.class);
	
	@Override
	public List<GzBaseUserStub> getUpstreamPossibleParents(GzRole role,String type,String term) throws GzPersistenceException
	{
		try
		{
			if (!type.isEmpty() && !term.isEmpty())
			{
				if (type.equals("contact"))
					return getUpstreamPossibleParentsByContact(role,term);
				if (type.equals("email"))
					return getUpstreamPossibleParentsByEmail(role,term);
			}
			
			String sql = "select bu.email as email,bu.contact as contact,bu.role as role,bu.enabled as enabled,pu.email as parentemail,pu.contact as parentcontact,pu.role as parentrole,ac.commission as commission" + 
					" from baseuser as bu join baseuser as pu on bu.parentcode = pu.code" +
					" join role as r on r.role = bu.role join account as ac on ac.baseuserid = bu.id " +
					" where level < (select level from role where role = ?) and level<>1";
			List<GzBaseUserStub> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
		        public void setValues(PreparedStatement preparedStatement) throws SQLException {
		          preparedStatement.setString(1, role.name());
		        }
		      }, BeanPropertyRowMapper.newInstance(GzBaseUserStub.class));
			return bus;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	private List<GzBaseUserStub> getUpstreamPossibleParentsByEmail(GzRole role,String term) throws GzPersistenceException
	{
		String sql = "select bu.email as email,bu.contact as contact,bu.role as role,bu.enabled as enabled,pu.email as parentemail,pu.contact as parentcontact,pu.role as parentrole,ac.commission as commission" + 
					" from baseuser as bu join baseuser as pu on bu.parentcode = pu.code" +
					" join role as r on r.role = bu.role join account as ac on ac.baseuserid = bu.id " +
					" where level < (select level from role where role = '" + role.name() + "') and level<>1 and lower(bu.email) like '%"+ term.toLowerCase() + "%'";
		List<GzBaseUserStub> bus = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(GzBaseUserStub.class));
		return bus;
	}
	
	private List<GzBaseUserStub> getUpstreamPossibleParentsByContact(GzRole role,String term) throws GzPersistenceException
	{
		String sql = "select bu.email as email,bu.contact as contact,bu.role as role,bu.enabled as enabled,pu.email as parentemail,pu.contact as parentcontact,pu.role as parentrole,ac.commission as commission" + 
					" from baseuser as bu join baseuser as pu on bu.parentcode = pu.code" +
					" join role as r on r.role = bu.role join account as ac on ac.baseuserid = bu.id " +
					" where level < (select level from role where role = '" + role.name() + "') and level<>1 and lower(bu.contact) like '%"+ term.toLowerCase() + "%'";
		List<GzBaseUserStub> bus = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(GzBaseUserStub.class));
		return bus;
	}
	
	@Override
	public void reassignMemberRole(GzBaseUser baseUser, GzRole newRole)
	{
		baseUser.setRole(newRole);
		try
		{
			getJdbcTemplate().update("UPDATE baseuser SET role = ? WHERE email = ?"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, baseUser.getRole().name());
						ps.setString(2, baseUser.getEmail().toLowerCase());
			      }
			    });
			getJdbcTemplate().update("DELETE FROM authority WHERE baseuserid=?", new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setObject(1, baseUser.getId());
				}
			});
			baseUser.setAuthorities(baseUser.getRole().getAllRoles());
			for (final GzRole role : baseUser.getAuthorities())
			{
				getJdbcTemplate().update("INSERT INTO authority (baseuserid,role) VALUES (?,?)"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setObject(1, baseUser.getId());
							ps.setString(2,role.name());
			      }
			    });
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void storeBaseUser(final GzBaseUser baseUser) throws GzPersistenceException {
		
		baseUser.setId(UUID.randomUUID());
		
		setNextUserCode(baseUser);
		
		try
		{
			getJdbcTemplate().update("INSERT INTO baseuser (id,email,contact,phone,nickname,code,parentcode,role,icon,enabled,password,leafinstance) "
										+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
			    	  	ps.setObject(1, baseUser.getId());
						ps.setString(2, baseUser.getEmail().toLowerCase());
						ps.setString(3, baseUser.getContact());
						ps.setString(4, baseUser.getPhone());
						ps.setString(5, baseUser.getNickname());
						ps.setString(6, baseUser.getCode());
						ps.setString(7, baseUser.getParentCode());
						ps.setString(8, baseUser.getRole().name());
						ps.setString(9, baseUser.getIcon());
						ps.setBoolean(10, baseUser.isEnabled());
						ps.setString(11, baseUser.getPassword());
						ps.setLong(12, baseUser.getLeafInstance());
			      }
			    });
			baseUser.setAuthorities(baseUser.getRole().getAllRoles());
			storeAuthorities(baseUser);
			storeAccount(baseUser.getId());
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public void updateBaseUserProfile(final GzBaseUser baseUser) throws GzPersistenceException {
		
		try
		{
			getJdbcTemplate().update("UPDATE baseuser SET email=?,contact=?,phone=?,nickname=?,icon =?,enabled=?,password=? WHERE id=?"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, baseUser.getEmail());
						ps.setString(2, baseUser.getContact());
						ps.setString(3, baseUser.getPhone());
						ps.setString(4, baseUser.getNickname());
						ps.setString(5, baseUser.getIcon());
						ps.setBoolean(6, baseUser.isEnabled());
						ps.setString(7, baseUser.getPassword());
						ps.setObject(8, baseUser.getId());
			      }
			    });
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	private synchronized void setNextUserCode(GzBaseUser user) throws GzPersistenceException {
		
		if (user.getRole().equals(GzRole.ROLE_ADMIN))
		{
			user.setCode(GzAdmin.getDefaultCode());
			user.setSystemMember(true);
			return;
		}
		
		String parentCode = user.getParentCode();
		Long nextCode = getNextCode(user.getRole(),parentCode);
		if (user.getParent().getRole().equals(GzRole.ROLE_ADMIN))
			parentCode = "";
		user.setLeafInstance(nextCode);
		user.setCode( parentCode + user.getRole().getCode() + nextCode.toString() );
	}
	
	@Override
	public String getEmailForId(UUID id) throws GzPersistenceException
	{
		String email;	
		try
		{
			String sql = "SELECT email FROM baseuser WHERE id=?";
			email = getJdbcTemplate().queryForObject(sql,new Object[] { id }, String.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
		return email;
	}
	
	private Long getNextCode(GzRole role,String parentCode) throws GzPersistenceException
	{
		Long leafInstance;	
		try
		{
			String sql = "SELECT MAX(leafinstance) FROM baseuser WHERE role = ? AND PARENTCODE=?";
			log.info(sql);
			leafInstance = getJdbcTemplate().queryForObject(sql,new Object[] { role.name(), parentCode }, Long.class);
			log.info("Got next leaf instance : " + leafInstance);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	

		if (leafInstance==null)
			return 0L;
		
		long lf = GetNextNumberNo4s.next(leafInstance);
		log.info("Using leaf instance : " + lf);
		return lf;
	}
	
	private void storeAuthorities(final GzBaseUser baseUser) throws GzPersistenceException
	{
		try
		{
			for (final GzRole role : baseUser.getAuthorities())
			{
				getJdbcTemplate().update("INSERT INTO authority (baseuserid,role) VALUES (?,?)"
			        , new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setObject(1, baseUser.getId());
							ps.setString(2,role.name());
			      }
			    });
			}
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public GzBaseUser getBaseUserByEmail(final String email,@SuppressWarnings("rawtypes") Class clazz) throws GzPersistenceException 
	{	
		try
		{
			final String sql = "SELECT * FROM baseUser WHERE email=?";
			List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, email);
				        }
				      }, new GzBaseUserRowMapper1(clazz));
			if (bus.isEmpty())
				return null;
			populateUser(bus.get(0));
			return bus.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	@Override
	public GzBaseUser getBaseUserByCode(final String code) throws GzPersistenceException {
		
		try
		{
			GzRole role = GzRole.getRoleForCode(code);
			@SuppressWarnings("rawtypes")
			Class clazz = role.getCorrespondingClass();
			
			final String sql = "SELECT * FROM baseUser WHERE code=?";
			List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, code);
				        }
				      }, new GzBaseUserRowMapper1(clazz));
			if (bus.isEmpty())
				return null;
			populateUser(bus.get(0));
			return bus.get(0);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public boolean contactExists(String contact)
	{
		try
		{
			final String sql = "SELECT count(*) FROM baseUser WHERE contact=?";
			int num = getJdbcTemplate().queryForObject(sql,new Object[] { contact }, Integer.class);
			return num != 0;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}
	
	/*
	private GzRole getRole(GzBaseUser user) throws GzPersistenceException {
		
		String sql = "SELECT role FROM baseuser WHERE id = ?";
		try
		{
			PreparedStatement ps = getConnection().prepareStatement(sql);	
			ps.setObject(1,user.getId());
			ResultSet rs = ps.executeQuery();
			rs.next();
			String role = rs.getString(1);
			return GzRole.valueOf(role);
		}
		catch (Exception e)
		{
			log.error("Could not execute : PS : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException("PS : " + sql + " - " + e.getMessage());
		}	
	}
	*/
	
	@Override
	public List<String> getMemberCodes(final GzBaseUser baseUser) throws GzPersistenceException
	{
		try
		{
			final String sql = "SELECT code FROM baseUser WHERE parentcode = ?";
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List<String> memberCodes = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setString(1, baseUser.getCode());
				        }
				      }, new RowMapper() {
				          public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				              return resultSet.getString(1);
				            }});
			
			return memberCodes;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	protected String getCodeForEmail(String email) throws GzPersistenceException {
		
		String sql = "SELECT code FROM baseUser WHERE email = ?";
		try
		{
			return getJdbcTemplate().queryForObject(sql,new Object[] { email.toLowerCase() }, String.class);
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}

	@Override
	public void getDownstreamForParent(GzBaseUser parent) {
		
		try {
			getMembersForUser(parent);
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
		}
	}
	
	protected void getMembersForUser(GzBaseUser user) throws GzPersistenceException
	{
		user.setMembers(new ArrayList<GzBaseUser>());
		
		if (user.getClass().equals(GzAdmin.class))					// admin only has smas
		{
			GzAdmin admin = (GzAdmin) user;
			admin.setSmas(getSmasForParent(user));
			return;
		}
		/*
		if (user.getClass().equals(GzComp.class))
		{
			GzComp comp = (GzComp) user;
			comp.setZmas(getZmasForParent(user));
			comp.setSmas(getSmasForParent(user));
			comp.setMas(getMasForParent(user));
			comp.setAgents(getAgentsForParent(user));
			comp.setPlayers(getParticipantsForParent(user));
			return;
		}
		
		if (user.getClass().equals(GzZMA.class))
		{
			GzZMA zma = (GzZMA) user;
			zma.setSmas(getSmasForParent(user));
			zma.setMas(getMasForParent(user));
			zma.setAgents(getAgentsForParent(user));
			zma.setPlayers(getParticipantsForParent(user));
			return;
		}
		*/
		
		if (user.getClass().equals(GzSMA.class))
		{
			GzSMA sma = (GzSMA) user;
			sma.setMas(getMasForParent(user));
			sma.setAgents(getAgentsForParent(user));
			sma.setPlayers(getParticipantsForParent(user));
			return;
		}
		
		if (user.getClass().equals(GzMA.class))
		{
			GzMA ma = (GzMA) user;
			ma.setAgents(getAgentsForParent(user));
			ma.setPlayers(getParticipantsForParent(user));
			return;
		}
		
		if (user.getClass().equals(GzAgent.class))
		{
			GzAgent agent = (GzAgent) user;
			agent.setPlayers(getParticipantsForParent(user));
			return;
		}
		
		log.error("getMembersForUser : illegal class for user :" + user.getClass());
	}
	
	@SuppressWarnings("unused")
	private List<GzComp> getCompsForParent(GzBaseUser parent) throws GzPersistenceException
	{
		List<GzBaseUser> users = getUsersForParent(parent,GzComp.class,GzRole.ROLE_COMP);
		List<GzComp> comps = new ArrayList<GzComp>();
		for (GzBaseUser user : users)
		{
			parent.getMembers().add(user);
			comps.add((GzComp) user);
		}
		return comps;
	}
	
	@SuppressWarnings("unused")
	private List<GzZMA> getZmasForParent(GzBaseUser parent) throws GzPersistenceException
	{
		List<GzBaseUser> users = getUsersForParent(parent,GzZMA.class,GzRole.ROLE_ZMA); 
		List<GzZMA> zmas = new ArrayList<GzZMA>();
		for (GzBaseUser user : users)
		{
			parent.getMembers().add(user);
			zmas.add((GzZMA) user);
		}
		return zmas;
	}
	
	private List<GzSMA> getSmasForParent(GzBaseUser parent) throws GzPersistenceException
	{
		List<GzBaseUser> users = getUsersForParent(parent,GzSMA.class,GzRole.ROLE_SMA); 
		List<GzSMA> zmas = new ArrayList<GzSMA>();
		for (GzBaseUser user : users)
		{
			parent.getMembers().add(user);
			zmas.add((GzSMA) user);
		}
		return zmas;
	}
	
	private List<GzMA> getMasForParent(GzBaseUser parent) throws GzPersistenceException
	{
		List<GzBaseUser> users = getUsersForParent(parent,GzMA.class,GzRole.ROLE_MA); 
		List<GzMA> mas = new ArrayList<GzMA>();
		for (GzBaseUser user : users)
		{
			parent.getMembers().add(user);
			mas.add((GzMA) user);
		}
		return mas;
	}
	
	private List<GzAgent> getAgentsForParent(GzBaseUser parent) throws GzPersistenceException
	{
		List<GzBaseUser> users = getUsersForParent(parent,GzAgent.class,GzRole.ROLE_AGENT); 
		List<GzAgent> agents = new ArrayList<GzAgent>();
		for (GzBaseUser user : users)
		{
			parent.getMembers().add(user);
			agents.add((GzAgent) user);
		}
		return agents;
	}


	private List<GzBaseUser> getParticipantsForParent(GzBaseUser parent) throws GzPersistenceException
	{
		List<GzBaseUser> users = getUsersForParent(parent,GzBaseUser.class,GzRole.ROLE_PLAY); 
		for (GzBaseUser user : users)
		{
			parent.getMembers().add(user);
		}
		return users;
	}
	
	private List<GzBaseUser> getUsersForParent(final GzBaseUser parent,
			@SuppressWarnings("rawtypes") Class userClass,final GzRole role) throws GzPersistenceException {

		try
		{
			@SuppressWarnings("rawtypes")
			Class clazz = role.getCorrespondingClass();
			final String sql = "SELECT * FROM baseUser WHERE parentcode = ? AND role = ?";
			List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement ps) throws SQLException {
				        	ps.setString(1,parent.getCode());
							ps.setString(2,role.name());
				        }
				      }, new GzBaseUserRowMapper1(clazz));
			for (GzBaseUser bu : bus)
				populateUser(bu);
			return bus;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
		
	}

	@Override
	public List<GzBaseUser> getUpstreaMembers(final GzRole role) {

		List<GzBaseUser> members = new ArrayList<GzBaseUser>();
		if (role.getRank()==6)
			return members;
		if (role.getRank()==5)
		{
			members.addAll(getUpstreaMembers(GzAdmin.class,GzRole.ROLE_ADMIN));
			return members;
		}
		if (role.getRank()==4)  // ZMA
		{
			members.addAll(getUpstreaMembers(GzAdmin.class,GzRole.ROLE_COMP));
			return members;
		}
		if (role.getRank()<=3)	// SMA
		{
			members.addAll(getUpstreaMembers(GzAdmin.class,GzRole.ROLE_COMP));
			members.addAll(getUpstreaMembers(GzAdmin.class,GzRole.ROLE_ZMA));
		}
		if (role.getRank()<=2)	// MA
		{
			members.addAll(getUpstreaMembers(GzAdmin.class,GzRole.ROLE_SMA));
		}
		if (role.getRank()<=1)	// Agent
		{
			members.addAll(getUpstreaMembers(GzAdmin.class,GzRole.ROLE_MA));
		}
		if (role.getRank()<=0)	// Player
		{
			members.addAll(getUpstreaMembers(GzAdmin.class,GzRole.ROLE_AGENT));
		}
		return members;
	}

	private List<GzBaseUser> getUpstreaMembers(@SuppressWarnings("rawtypes") Class userClass,final GzRole role) throws GzPersistenceException {

		try
		{
			@SuppressWarnings("rawtypes")
			Class clazz = role.getCorrespondingClass();
			final String sql = "SELECT * FROM baseUser WHERE role = ? and enabled=true";
			List<GzBaseUser> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement ps) throws SQLException {
				        	ps.setString(1,role.name());
				        }
				      }, new GzBaseUserRowMapper1(clazz));
			return bus;
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	
	private void populateUser(GzBaseUser user) throws GzPersistenceException {
		user.setAccount(getAccount(user));
		user.setAuthorities(getAuthorities(user));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<GzRole> getAuthorities(final GzBaseUser user) throws GzPersistenceException {
		
		List<String> roleList;
		try
		{
			final String sql = "SELECT role FROM authority WHERE baseuserid= ?";
			roleList = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
				        public void setValues(PreparedStatement preparedStatement) throws SQLException {
				          preparedStatement.setObject(1,user.getId());
				        }
				      }, new RowMapper() {
				          public Object mapRow(ResultSet resultSet, int i) throws SQLException {
				              return resultSet.getString(1);
				            }});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
		
		List<GzRole> roles = new ArrayList<GzRole>();
		for (String r : roleList)
		{
			roles.add(GzRole.valueOf(r));
		}
		
		return roles;
	}

	@Override
	public Double getDownStreamCreditAsPlayer(GzBaseUser user,GzBaseUser parent)
	{
		String sql = "SELECT SUM(creditasplayer) FROM account WHERE baseuserid IN " +
					"(SELECT id FROM baseuser WHERE parentcode='" + parent.getCode() +"' AND enabled=TRUE) AND " +
					"baseuserid <> '" + user.getId() +"'";
		try
		{
			log.trace("sql = "  + sql );
			Double total = getJdbcTemplate().queryForObject(sql,Double.class);
			if (total == null)
				total = 0.0;
			return total;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("getDownStreamCreditLimit : " + e + " - " + e.getMessage());
			return 0.0;
		}
	}

	@Override
	public Double getDownStreamCreditAsBanker(GzBaseUser user,GzBaseUser parent)
	{
		String sql = "SELECT SUM(creditasbanker) FROM account WHERE baseuserid IN " +
					"(SELECT id FROM baseuser WHERE parentcode='" + parent.getCode() +"' AND enabled=TRUE) AND " +
					"baseuserid <> '" + user.getId() +"'";
		try
		{
			log.trace("sql = "  + sql );
			Double total = getJdbcTemplate().queryForObject(sql,Double.class);
			if (total == null)
				total = 0.0;
			return total;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("getDownStreamCreditLimit : " + e + " - " + e.getMessage());
			return 0.0;
		}
	}

	@Override
	public void setAsSystemMember(GzBaseUser user) throws GzPersistenceException  {
		String sql = "UPDATE baseuser SET systemmember = TRUE WHERE id = '" + user.getId() + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException(sql + " - " + e.getMessage());
		}		
	}

	@Override
	public void storeImage(final String email, MultipartFile data, final String contentType) throws GzPersistenceException {
		
		final InputStream is;
		try {
			is = data.getInputStream();
		} catch (IOException e) {
			
			e.printStackTrace();
			log.error("Could not convert : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
		try
		{
			String sql = "DELETE FROM image WHERE email = '" + email + "'";
			getJdbcTemplate().update(sql);
			getJdbcTemplate().update("INSERT INTO image (email,contenttype,data) VALUES (?, ?, ?)"
					, new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, email);
							ps.setString(2, contentType);
							ps.setBinaryStream(3, is);
						}
					});
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}	
	}
	
	@Override
	public byte[] getImage(final String email) throws GzPersistenceException {
		
		byte[] imgBytes = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = getConnection().prepareStatement("SELECT data FROM image WHERE email = ?");
			ps.setString(1, email);
			rs = ps.executeQuery();
			if (rs != null) {
			    while (rs.next()) {
			        imgBytes = rs.getBytes(1);
			    }
			    rs.close();
			}
			ps.close();
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
		finally
		{
			if (rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (ps!=null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return imgBytes;
	}

	@Override
	public void setDefaultPasswordForAll(String encoded) {
		String sql = "UPDATE baseuser SET password = '" + encoded + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
		}
	}
	
	@Override
	public void updateLeafInstance(GzBaseUser bu) {
		String sql = "UPDATE baseuser SET leafinstance = " + bu.getLeafInstance() + " WHERE id = '" + bu.getId() + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
		}
	}

	@Override
	public void updateBaseUserParentCode(String code, String parentCode) {
		String sql = "UPDATE baseuser SET parentcode='" + parentCode + "' WHERE code = '" + code + "'";
		try
		{
			log.info(sql);
			getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			log.error("Could not execute : " + sql + " - " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	@Override
	public List<GzBaseUserStub> search(String term,String type) {
		try
		{
			if (type.equals("email"))
				return searchByEmail(term);
			if (type.equals("contact"))
				return searchByContact(term);
			return new ArrayList<GzBaseUserStub>();
		}
		catch (DataAccessException e)
		{
			log.error("Could not execute : " + e.getMessage());
			throw new GzPersistenceException("Could not execute : " + e.getMessage());
		}
	}

	private List<GzBaseUserStub> searchByContact(String term)
	{
		String sql = "select bu.email as email,bu.contact as contact,bu.role as role,pu.email as parentemail,pu.contact as parentcontact,pu.role as parentrole" + 
				" from baseuser as bu join baseuser as pu on bu.parentcode = pu.code" +
				" where lower(bu.contact)=?";
		List<GzBaseUserStub> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
	        public void setValues(PreparedStatement preparedStatement) throws SQLException {
	          preparedStatement.setString(1, term.toLowerCase());
	        }
	      }, BeanPropertyRowMapper.newInstance(GzBaseUserStub.class));
		if (!bus.isEmpty())
			return bus;
		sql = "select bu.email as email,bu.contact as contact,bu.role as role,pu.email as parentemail,pu.contact as parentcontact,pu.role as parentrole" + 
				" from baseuser as bu join baseuser as pu on bu.parentcode = pu.code" +
				" where lower(bu.contact) like '%"+ term.toLowerCase() + "%'";
		bus = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(GzBaseUserStub.class));
		return bus;
	}
	
	private List<GzBaseUserStub> searchByEmail(String term)
	{
		String sql = "select bu.email as email,bu.contact as contact,bu.role as role,pu.email as parentemail,pu.contact as parentcontact,pu.role as parentrole" + 
					" from baseuser as bu join baseuser as pu on bu.parentcode = pu.code" +
					" where lower(bu.email)=?";
		List<GzBaseUserStub> bus = getJdbcTemplate().query(sql,new PreparedStatementSetter() {
	        public void setValues(PreparedStatement preparedStatement) throws SQLException {
	          preparedStatement.setString(1, term.toLowerCase());
	        }
	      }, BeanPropertyRowMapper.newInstance(GzBaseUserStub.class));
		if (!bus.isEmpty())
			return bus;
		sql = "select bu.email as email,bu.contact as contact,bu.role as role,pu.email as parentemail,pu.contact as parentcontact,pu.role as parentrole" + 
				" from baseuser as bu join baseuser as pu on bu.parentcode = pu.code" +
				" where lower(bu.email) like '%"+ term.toLowerCase() + "%'";
		log.info(sql);
		bus = getJdbcTemplate().query(sql,BeanPropertyRowMapper.newInstance(GzBaseUserStub.class));
		return bus;
	}
}
