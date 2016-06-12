package com.et3.Engine;

public class Company extends Owner
{
	private Owner    m_owner;
	private String   m_name;
	private Country  m_country;

	public Company(BankAccount account, String name, Country c, Owner owner) throws AssociationException
	{
		super(account);
		m_name    = name;
		m_country = c;
		m_owner   = owner;
		m_owner.addCompany(this);
	}
}
