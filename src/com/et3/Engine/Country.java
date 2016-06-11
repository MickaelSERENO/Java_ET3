package com.et3.Engine;

import java.util.ArrayList;

public class Country
{
	//Because we can't create twice the same country, use a static function to create them (and be sure it aren't created yet)
	//
	//List of countries
	private static ArrayList<Country> countryList;

	//Init the countries
	private static void initCountries(ArrayList<String> countryNameList)
	{
		Country.countryList.clear();
		for(String name : countryNameList)
			Country.countryList.add(new Country(name));
	}

	private static int getCooperation(Country c1, Country c2)
	{
		if(c1 == c2)
			return -1;

		return c1.getCooperation(c2);
	}

	private static Country getCountry(String name)
	{
		for(Country c : Country.countryList)
			if(c.getName().equals(name))
				return c;
		return null;
	}

	//Class itself
	private String m_name;

	//Singleton makes the Constructor private
	private Country(String name)
	{
		m_name = name;
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
}
