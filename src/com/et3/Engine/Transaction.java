package com.et3.Engine;

import com.et3.Engine.BankAccount;

public class Transaction
{
	private static int transID=0;
	private BankAccount m_source;
	private BankAccount m_dest;
	private int         m_id;
	private int         m_day = 0;
	private boolean     m_found;

	public Transaction(BankAccount source, BankAccount dest)
	{
		m_source = source;
		m_dest   = dest;
		m_id     = Transaction.transID;
		m_found  = false;
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

	public boolean isFound()
	{
		return m_found;
	}

	public void setFound(boolean b, int day)
	{
		m_found = b;
		m_day = day;
	}

	public int getDay()
	{
		return m_day;
	}
}
