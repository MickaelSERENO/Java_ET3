package com.et3.Engine;

import java.util.ArrayList;

import com.et3.Engine.Investigator;
import com.et3.Engine.Transaction;

public class CentralRegister
{
	//Singleton statement
	private static CentralRegister centralRegister=null;
	public  static void initCentralRegister()
	{
		CentralRegister.centralRegister = new CentralRegister();
	}

	private ArrayList<Denonciation> m_denonciation;
	private ArrayList<Transaction>  m_fraudulentTrans;

	private CentralRegister()
	{
		m_denonciation    = new ArrayList<Denonciation>();
		m_fraudulentTrans = new ArrayList<Transaction>();
	}

	public void addDenonciation(int day, Investigator inv, Transaction trans)
	{
		for(Denonciation deno : m_denonciation)
		{
			if(deno.getTransaction() == trans)
			{
				if(deno.getDay() == day)
					if(deno.getInvestigator() != inv)
						inv.addScore(100);
				return;
			}
		}

		m_denonciation.add(new Denonciation(day, inv, trans));
		inv.addScore(100);
	}

	public void addFraudulentTrans(Transaction trans)
	{
		if(!m_fraudulentTrans.contains(trans))
			m_fraudulentTrans.add(trans);
	}

	public String toString()
	{
		String s = String.format("%10s %10s %10s", "Transaction", "Source", "Destination");
		for(Transaction trans : m_fraudulentTrans)
			s = s + String.format("\n %10d %10d %10d", trans.getID());
		return s;
	}
}

class Denonciation
{
	private int m_day;
	private Investigator m_inv;
	private Transaction m_trans;

	public Denonciation(int day, Investigator inv, Transaction trans)
	{
		m_day   = day;
		m_inv   = inv;
		m_trans = trans;
	}

	public Investigator getInvestigator()
	{
		return m_inv;
	}

	public Transaction getTransaction()
	{
		return m_trans;
	}

	public int getDay()
	{
		return m_day;
	}
}
