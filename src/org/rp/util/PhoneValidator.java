package org.rp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator {

	private Pattern pattern;
	private Matcher matcher;

	private static final String PHONE_PATTERN = 
		"^(([(]?(\\d{2,4})[)]?)|(\\d{2,4})|([+1-9]+\\d{1,2}))?[-\\s]?(\\d{2,3})?[-\\s]?((\\d{7,8})|(\\d{3,4}[-\\s]\\d{3,4}))$";

	public PhoneValidator() {
		pattern = Pattern.compile(PHONE_PATTERN);
	}

	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex) {

		matcher = pattern.matcher(hex);
		return matcher.matches();

	}
}