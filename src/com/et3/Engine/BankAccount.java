package com.et3.Engine;

import com.et3.Engine.Taxpayer;

public class BankAccount
{
	private static int accountID=0;

	private Bank     m_bank;
	private int      m_id;
	private int      m_money;
    private Taxpayer m_taxPayer;

	public BankAccount(Bank bank, int money)
	{
		m_bank                = bank;
		m_id                  = BankAccount.accountID;
        m_money               = money;
		BankAccount.accountID = BankAccount.accountID+1;
	}

	public void setBank(Bank b)
	{
		m_bank = b;
	}
}
