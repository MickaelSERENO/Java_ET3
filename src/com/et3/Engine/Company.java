package com.et3.Engine;

public class Company extends Owner
{
	private Owner    m_owner;
	private Country  m_country;

	public Company(BankAccount account, String name, Country c, Owner owner) throws AssociationException
	{
		super(account, name);
		m_country = c;
		m_owner   = owner;
		if(m_owner != null)
			m_owner.addCompany(this);
		if(CentralRegister.centralRegister != null)
		{
			CentralRegister.centralRegister.addCompany(this);
		}
	}

	public Owner getOwner()
	{
		return m_owner;
	}

	public Country getCountry()
	{
		return m_country;
	}
}
