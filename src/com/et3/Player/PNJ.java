package com.et3.Player;

import java.util.ArrayList;
import com.et3.Engine.*;

public class PNJ extends Investigator
{
	public PNJ(BankAccount ba, Country c, String name) throws AssociationException
	{
		super(ba, c, name);
	}

	public int  play()
	{
		return Investigator.NO_ACTION;
	}

	public void denonciate(CentralRegister c)
	{}

	public void promptInvOwner(ArrayList<Investigator> invs)
	{}

	public void promptInvList(ArrayList<Investigator> invs)
	{}

	public void promptBank(ArrayList<Bank> banks)
	{}
}
