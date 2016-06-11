package com.et3.Engine;
import java.util.Map;

class AssociationException extends Exception
{
	public AssociationException(String msg)
	{
		super(msg);
	}
}

public class Owner
{
	private static Map<BankAccount, Owner> accountAssociation;

	private BankAccount m_account;

	public Owner(BankAccount account) throws AssociationException
	{
		if(Owner.accountAssociation.containsKey(account))
			throw new AssociationException("The account is already bind to another taxpayer");

		Owner.accountAssociation.put(account, this);
		m_account = account;
	}
}
