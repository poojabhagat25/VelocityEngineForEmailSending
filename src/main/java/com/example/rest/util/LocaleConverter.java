package com.example.rest.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

public class LocaleConverter
{

	public static Locale getLocaleFromRequest(HttpServletRequest request)
	{
		String acceptLanguage = request.getHeader("Accept-Language");
		String[] lang = acceptLanguage.split(",");
		String language = lang[0];
		
		//String[] CountryLanguage=language1.split("-");
		//Locale locale = new Locale(CountryLanguage[0],CountryLanguage[1]);
		
		Locale locale = new Locale(language.trim());
		return locale;

	}

}
