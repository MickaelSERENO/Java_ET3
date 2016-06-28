package com.et3.Engine;

import java.util.ArrayList;
import java.util.HashMap;

public class Country
{
	//Because we can't create twice the same country, use a static function to create them (and be sure it aren't created yet)
	//
	//List of countries
	private static ArrayList<Country> countryList=null;

	//Init the countries
	public static void initCountries(ArrayList<String> countryNameList)
	{
		if(countryList == null)
			countryList = new ArrayList<Country>();
		else
			Country.countryList.clear();
		for(String name : countryNameList)
			Country.countryList.add(new Country(name));
	}

	public static int getCooperation(Country c1, Country c2)
	{
		if(c1 == c2)
			return -1;

		return c1.getCooperation(c2);
	}

	public static Country getCountry(String name)
	{
		for(Country c : Country.countryList)
			if(c.getName().equals(name))
				return c;
		return null;
	}

	public static Country getRandomCountry()
	{
		return countryList.get((int)(Math.random()*1000)%countryList.size());
	}

	//Class itself
	private String m_name;
	private HashMap<Country, Integer> m_alliance;

	//Singleton makes the Constructor private
	private Country(String name)
	{
		m_name = name;
		m_alliance = new HashMap<Country, Integer>();
	}

	//Make accusation
	public void makeAccusation()
	{}

	//Get information
	public void getInformation()
	{}

	//Get the value of the cooperation between countries
	public int getCooperation(Country c)
	{
		return 0;
	}

	public String getName()
	{
		return m_name;
	}

	public void addAlliance(Country c)
	{
		m_alliance.put(c, (int)(Math.random()*1000 % 4));
	}
}
