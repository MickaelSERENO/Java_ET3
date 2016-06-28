package com.et3.Engine;

import com.et3.Engine.Company;
import com.et3.Engine.BankAccount;

import java.util.ArrayList;

public class Bank extends Company
{
	private ArrayList<BankAccount> m_listBankAccount;

	public Bank(BankAccount c, String name, Country co, Owner o) throws AssociationException
	{
		super(c, name, co, o);
		m_listBankAccount = new ArrayList<BankAccount>();
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
		BankAccount b = new BankAccount(this, money);
		m_listBankAccount.add(b);
		return b;
	}

	public ArrayList<BankAccount> getListBankAccount()
	{
		return m_listBankAccount;
	}
}
