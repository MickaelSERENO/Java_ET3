package com.et3.Engine;

import com.et3.Engine.Company;
import com.et3.Engine.BankAccount;

public class Bank extends Company
{
	public Bank(BankAccount c, String name, Country co, Owner o) throws AssociationException
	{
		super(c, name, co, o);
	}

	public static Bank createDefaultBank(String name, Country c, Owner o) throws AssociationException
	{
		BankAccount account = new BankAccount(null, 0);
		Bank bank = new Bank(account, name, c, o);
		account.setBank(bank);

		return bank;
	}

	public BankAccount createAccount(int money)
	{
		return new BankAccount(this, money);
	}
}
