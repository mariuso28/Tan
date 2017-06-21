package org.rp.config.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private static final Logger log = Logger.getLogger(CustomUserDetailsService.class);
	@Autowired
	private GzServices gzServices;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("loadUserByUsername email : " + email);
		
		email = email.toLowerCase();
		EmailValidator ev = new EmailValidator();
		if (!ev.validate(email))
		{
			log.error("Email " + email + " Invalid");
			throw new UsernameNotFoundException("Invalid email: " + email);
		}
		
		GzBaseUser baseUser;
		try {
			baseUser = gzServices.getGzHome().getBaseUserByEmail(email);
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("Error finding User: " + email + " not found");
			throw new UsernameNotFoundException("Error finding User: " + email);
		}
		if (baseUser==null){
			log.error("User : " + email + " not found");
			throw new UsernameNotFoundException("User: " + email);
		}
		log.info("User : " + baseUser.getEmail() + " found with role :" + baseUser.getRole());
		
		Collection<GrantedAuthority> authorities = getAuthorities(baseUser);
		
		boolean enabled = baseUser.isEnabled();
		User user = new User(baseUser.getEmail(), baseUser.getPassword(), enabled, true, true, true, authorities);
		
		log.info("Using User : " + user.getUsername() + " with authorities :" + authorities);
		return user;
	}

	private Collection<GrantedAuthority> getAuthorities(GzBaseUser baseUser) {
		// Create a list of grants for this user
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();

		authList.add(new SimpleGrantedAuthority(baseUser.getRole().name()));

		return authList;
	}
}
