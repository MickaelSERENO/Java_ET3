package com.et3.Engine;

import com.et3.Engine.BankAccount;

public class Transaction
{
	private static int transID=0;
	private BankAccount m_source;
	private BankAccount m_dest;
	private int         m_id;

	public Transaction(BankAccount source, BankAccount dest)
	{
		m_source = source;
		m_dest   = dest;
		m_id     = Transaction.transID;
		Transaction.transID = Transaction.transID+1;
	}

	public BankAccount getSource()
	{
		return m_source;
	}

	public BankAccount getDest()
	{
		return m_dest;
	}

	public int getID()
	{
		return m_id;
	}
}
