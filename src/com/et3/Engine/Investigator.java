package com.et3.Engine;

import java.util.ArrayList;

public abstract class Investigator extends Taxpayer
{
	private static ArrayList<Country> associatedCountry;

	public static ArrayList<Country> getAssociatedCountry()
	{
		return Investigator.associatedCountry;
	}

	public Country m_country;
	public String m_name;
	public int    m_score;

	public Investigator(BankAccount ba, Country c, String name) throws AssociationException
	{
		super(ba);
		m_name    = name;
		m_country = c;
		m_score   = 0;
	}

	public void addScore(int a)
	{
		m_score += a;
	}

	public abstract int play();
}
