package com.et3.Engine;

import com.et3.Engine.BankAccount;

public class Taxpayer extends Owner
{
	public Taxpayer(BankAccount account, String name) throws AssociationException
	{
		super(account, name);
	}
}
