package org.rp.rest.auth;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.rp.json.GzResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/a")
public class AuthRestController {

	private static final Logger log = Logger.getLogger(AuthRestController.class);

	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value="/authorize", method=RequestMethod.POST)
	public GzResultJson authorize(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request
			){
		
		log.info("authorize : " + username);
		GzResultJson result = new GzResultJson();
		String path;
		path = "http://localhost:8080/Tan/rp/oauth/token";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "password");
			map.add("client_id", "barClientIdPassword");
			map.add("client_secret", "secret");
			map.add("username", username);
			map.add("password", password);
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			ResponseEntity<Object> responseEntity = restTemplate.exchange(path, HttpMethod.POST, entity, Object.class);
			log.info("authorize success");
			result.success(responseEntity.getBody());
			return result;
		}
		catch(HttpClientErrorException e){
			e.printStackTrace();
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)){
				result.setMessage("username or password incorrect");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		log.info("authorize fail");

		return result;
	}

	@RequestMapping(value="/refreshToken", method=RequestMethod.POST)
	public GzResultJson refreshToken(
			@RequestParam("refreshToken") String refreshToken,
			HttpServletRequest request
			){
		GzResultJson result = new GzResultJson();
		String path;
		path = "http://localhost:8080/Tan/rp/oauth/token";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("grant_type", "refresh_token");
			map.add("refresh_token", refreshToken);
			map.add("client_id", "barClientIdPassword");
			map.add("client_secret", "secret");
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			ResponseEntity<Object> responseEntity = restTemplate.exchange(path, HttpMethod.POST, entity, Object.class);
			result.success(responseEntity.getBody());
		}
		catch(Exception e){
			e.printStackTrace();
			result.fail("Bad credentials");
		}
		return result;
	}

	@RequestMapping(value="/revokeRefreshToken", method=RequestMethod.POST)
	public GzResultJson revokeRefreshToken(
			@RequestParam("refreshToken") String refreshToken
			){
		GzResultJson result = new GzResultJson();
		((JdbcTokenStore) tokenStore).removeAccessTokenUsingRefreshToken(refreshToken);
		((JdbcTokenStore) tokenStore).removeRefreshToken(refreshToken);
		result.success();
		return result;
	}
}