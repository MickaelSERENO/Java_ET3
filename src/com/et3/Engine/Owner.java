package com.et3.Engine;
import java.util.*;

public class Owner
{
	private static HashMap<BankAccount, Owner> accountAssociation=new HashMap<BankAccount, Owner>();

	protected BankAccount m_account;
	protected ArrayList<Company> m_companyOwned;
	protected String m_name;

	public static void initAccountAssociation()
	{
		Owner.accountAssociation = new HashMap<BankAccount, Owner>();
	}

	public Owner(BankAccount account, String name) throws AssociationException
	{
		if(Owner.accountAssociation == null)
			System.out.println("it is null !!!! sniff");
		if(Owner.accountAssociation.containsKey(account))
			throw new AssociationException("The account is already bind to another taxpayer");

		Owner.accountAssociation.put(account, this);
		m_account = account;
		m_companyOwned = new ArrayList<Company>();
		account.setOwner(this);
		m_name = name;
	}

	public void addCompany(Company c)
	{
		m_companyOwned.add(c);
	}

	public ArrayList<Company> getCompanyOwned()
	{
		return m_companyOwned;
	}

	public BankAccount getAccount()
	{
		return m_account;
	}

	public String getName()
	{
		return m_name;
	}
}
