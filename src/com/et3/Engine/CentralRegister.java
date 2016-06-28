package com.et3.Engine;

import java.util.ArrayList;

import com.et3.Engine.Investigator;
import com.et3.Engine.Transaction;

public class CentralRegister
{
	//Singleton statement
	public static CentralRegister centralRegister=null;
	public static void initCentralRegister()
	{
		CentralRegister.centralRegister = new CentralRegister();
	}

	private ArrayList<Denonciation> m_denonciation;
	private ArrayList<Transaction>  m_fraudulentTrans;
	private ArrayList<Taxpayer>     m_taxpayers;
	private ArrayList<Investigator> m_investigators;
	private ArrayList<Company>      m_companies;
	private ArrayList<String>       m_errors;
	private int                     m_currentDay;

	private CentralRegister()
	{
		m_denonciation    = new ArrayList<Denonciation>();
		m_fraudulentTrans = new ArrayList<Transaction>();
		m_taxpayers       = new ArrayList<Taxpayer>();
		m_companies       = new ArrayList<Company>();
		m_errors          = new ArrayList<String>();
		m_investigators   = new ArrayList<Investigator>();
		m_currentDay      = 0;
	}

	public void addDenonciation(Investigator inv, Taxpayer taxpayer, ArrayList<String> source, ArrayList<String> dest)
	{
		Owner owner = taxpayer;
		for(int i=0; i < source.size(); i++)
		{
			boolean found = false;
			for(Company comp : owner.getCompanyOwned())
			{
				if(comp.getName().equals(source.get(i)))
				{
					owner = comp;
					found = true;
				}
			}

			if(found == false)
			{
				m_errors.add("L'enqueteur " + inv.getName() + " s'est trompé. Malus de 500 points");
				return;
			}
		}

		Transaction trans = null;
		for(Transaction t : m_fraudulentTrans)
			if(owner.getAccount() == t.getSource())
			{
				trans = t;
				break;
			}

		owner = taxpayer;
		for(int i=0; i < dest.size(); i++)
		{
			boolean found = false;
			for(Company comp : owner.getCompanyOwned())
			{
				if(comp.getName().equals(dest.get(i)))
				{
					owner = comp;
					found = true;
				}
			}

			if(found == false)
			{
				m_errors.add("L'enqueteur " + inv.getName() + " s'est trompé. Malus de 500 points");
				return;
			}
		}

		if(trans.getDest() == owner.getAccount())
		{
			System.out.println("Bien joué !");
			if(trans.isFound())
			{
				if(trans.getDay() == m_currentDay)
					inv.addScore(100);
				else
					m_errors.add("La transaction " + trans.getSource().getID() + " vers " + trans.getDest().getID() + " avait déjà été trouvé le jour " + trans.getDay());
			}

			else
			{
				trans.setFound(true, m_currentDay);
				inv.addScore(100);
			}
		}

		System.out.println("Désoler, le contribuable %s n'est pas en faute. Malus de 500 points");
		inv.addScore(-500);
	}

	public void addTaxpayer(Taxpayer t)
	{
		m_taxpayers.add(t);
	}

	public void addCompany(Company c)
	{
		m_companies.add(c);
	}

	public void addFraudulentTrans(Transaction trans)
	{
		if(!m_fraudulentTrans.contains(trans))
			m_fraudulentTrans.add(trans);
	}

	public void addInvestigator(Investigator i)
	{
		m_investigators.add(i);
	}

	public int getCurrentDay()
	{
		return m_currentDay;
	}

	public String toString()
	{
		String s = String.format("%10s %10s %10s", "Transaction", "Source", "Destination");
		for(Transaction trans : m_fraudulentTrans)
			s = s + String.format("\n %10d %10d %10d", trans.getID(), trans.getDest().getID(), trans.getSource().getID());
		return s;
	}

	public boolean isEnd()
	{
		for(Transaction t : m_fraudulentTrans)
		{
			if(!t.isFound())
				return false;
		}
		return true;
	}

	public void nextDay()
	{
		for(Transaction t : m_fraudulentTrans)
		{
			if(t.isFound() && t.getDay() == m_currentDay)
			{
				System.out.println("La transaction de " + t.getSource().getID() + " vers " + t.getDest().getID() + "a été trouvé");
			}
		}

		for(String s : m_errors)
		{
			System.out.println(s);
		}

		m_errors.clear();
		m_currentDay++;
	}

	public Company getCompany(String compName)
	{
		Company comp = null;
		for(Taxpayer t : m_taxpayers)
		{
			for(Company c : t.getCompanyOwned())
			{
				comp = getCompanyRec(c, compName);
				if(comp != null)
					return comp;
			}
		}

		return comp;
	}

	public void printInvestigator()
	{
		System.out.println(String.format("%15s %15s" + m_investigators.size(), "Enqueteur", "Pays"));
		for(Investigator inv : m_investigators)
		{
			System.out.println(String.format("%15s %15s", inv.getName(), inv.getCountry().getName()));
		}
	}

	public Owner getOwner(String ownerName)
	{
		for(Taxpayer taxpayer : m_taxpayers)
		{
			if(taxpayer.getName().equals(ownerName))
				return taxpayer;
		}

		for(Company comp : m_companies)
		{
			if(comp.getName().equals(ownerName))
				return comp;
		}

		return null;
	}

	public Company getCompanyRec(Company o, String compName)
	{
		Company comp = null;
		if(o.getName().equals(compName))
			return o;

		for(Company c : o.getCompanyOwned())
		{
			comp = getCompanyRec(c, compName);
			if(comp != null)
				return comp;
		}

		return comp;
	}

	public Investigator getInv(String name)
	{
		for(Investigator inv : m_investigators)
		{
			if(inv.getName().equals(name))
				return inv;
		}
		return null;
	}

	public Taxpayer getTaxpayer(String name)
	{
		for(Taxpayer t : m_investigators)
		{
			if(t.getName().equals(name))
				return t;
		}
		return null;
	}
}

class Denonciation
{
	private int m_day;
	private Investigator m_inv;
	private Taxpayer m_taxpayer;

	public Denonciation(int day, Investigator inv, Taxpayer t)
	{
		m_day   = day;
		m_inv   = inv;
		m_taxpayer = t;
	}

	public Investigator getInvestigator()
	{
		return m_inv;
	}

	public String getTaxpayerName()
	{
		return m_taxpayer.getName();
	}

	public int getDay()
	{
		return m_day;
	}
}
