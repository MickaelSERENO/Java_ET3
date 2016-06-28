package com.et3.Engine;

import java.util.ArrayList;

public abstract class Investigator extends Taxpayer
{
	public final static int NO_ACTION         = 0;
	public final static int PUBLISH_REGISTER  = 1;
	public final static int DENONCIATE        = 2;
	public final static int PROMPT_INV_OWNER  = 3;
    public final static int PROMPT_INV_LIST   = 4;
	public final static int PROMPT_BANK_OWNER = 5;

	private static ArrayList<Country> associatedCountry;

	public static ArrayList<Country> getAssociatedCountry()
	{
		return Investigator.associatedCountry;
	}

	protected Country m_country;
	protected int    m_score;
	protected ArrayList<Message> m_messages;

	public Investigator(BankAccount ba, Country c, String name) throws AssociationException
	{
		super(ba, name);
		m_country  = c;
		m_score    = 0;
		m_messages = new ArrayList<Message>();
	}

	public void addScore(int a)
	{
		m_score += a;
	}

	public void addMessage(Message m)
	{
		m_messages.add(m);
	}
	
	public Country getCountry()
	{
		return m_country;
	}

	public String getOwner(String companyName)
	{
		CentralRegister cr = CentralRegister.centralRegister;
		Company comp = cr.getCompany(companyName);

		if(comp == null)
			return "La société " + companyName + " n'existe pas.";

		else if(comp.getCountry() != getCountry())
			return "Le pays de la société " + companyName + " n'est pas le même que celui de l'enquêteur " + getName();

		else
			return "Le propriétaire de la société " + companyName + " est " + comp.getOwner().getName();
	}

	public String getCompanyList(String ownerName)
	{
		CentralRegister cr = CentralRegister.centralRegister;
		Owner o = cr.getOwner(ownerName);
		String value = "";
		for(Company comp : o.getCompanyOwned())
		{
			value = value + comp.getName() + "\n";
		}
		return value;
	}

	public abstract int  play();
	public abstract void denonciate(CentralRegister c);
	public abstract void promptInvOwner(ArrayList<Investigator> invs);
	public abstract void promptInvList(ArrayList<Investigator> invs);
	public abstract void promptBank(ArrayList<Bank> banks);
}
