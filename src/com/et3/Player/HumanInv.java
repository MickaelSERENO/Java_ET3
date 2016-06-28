package com.et3.Player;

import com.et3.Engine.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HumanInv extends Investigator
{
	public HumanInv(BankAccount ba, Country c, String name) throws AssociationException
	{
		super(ba, c, name);
	}

	public int play()
	{
		System.out.println("Vos messages : \n");
		for(Message m : m_messages)
			if(m.getDay() == CentralRegister.centralRegister.getCurrentDay())
				System.out.println(m);

		System.out.println("Au tour de " + m_name + " de jouer. Que souhaitez-vous faire ?");
		int action=0;
		do
		{
			System.out.println("0) Ne rien faire 1) Afficher les données du registre.\n2) Denoncer quelqu'un\n3) Demander à un autre enquêteur le proprietaire d'une societé\n4) Demander à un enquêteur la liste des sociétés d'un propriétaire\n5) Demander à une banque le proprietaire d'un compte banquaire");
			Scanner sc = new Scanner(System.in);
			action = Integer.parseInt(sc.nextLine());
			if(action < 0 || action > 5)
				System.out.println("Merci de selectionner une entrée valide !");
		}while(action < 0 || action > 5);
		return action;
	}

	public void denonciate(CentralRegister c)
	{
		CentralRegister cr = CentralRegister.centralRegister;
		System.out.println("Veuillez nommé le contribuable a dénoncé");
		Scanner sc  = new Scanner(System.in);
		String name = sc.nextLine();

		System.out.println("Vous avez choisi le contribuable " + name);

		Taxpayer taxpayer = cr.getTaxpayer(name);

		ArrayList<String> sourceString = new ArrayList<String>();
		String line = null;
		System.out.println("Veuillez donné la chaine de société du compte source en partant du contribuable " + name);
		do
		{
			line = sc.nextLine();
			if(!line.equals(""))
				sourceString.add(line);
		}while(!line.equals(""));

		ArrayList<String> destString = new ArrayList<String>();

		System.out.println("Veuillez donné la chaine de société du compte destination en partant du contribuable " + name);
		do
		{
			line = sc.nextLine();
			if(!line.equals(""))
				destString.add(line);
		}while(!line.equals(""));
		
		cr.addDenonciation(this, taxpayer, sourceString, destString);
	}

	public void promptInvOwner(ArrayList<Investigator> invs)
	{
		CentralRegister cr = CentralRegister.centralRegister;
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez choisir un enquêteur à qui poser la question");
		String invName = sc.nextLine();

		Investigator inv = null;
		do
		{
			inv = cr.getInv(invName);
			if(inv == null)
			{
				System.out.println("Veuillez choisir un enqueteur valide. Voici la liste des enquêteurs disponible");
				cr.printInvestigator();
				inv = cr.getInv(sc.nextLine());
			}
		}while(inv == null);

		System.out.println("Veuillez choisir une entreprise dont vous voulez connaître le propriétaire");
		String soc = sc.nextLine();

		addMessage(new Message(cr.getCurrentDay()+1, inv.getOwner(soc)));
		inv.addMessage(new Message(cr.getCurrentDay()+1,inv.getOwner(soc)));
	}

	public void promptInvList(ArrayList<Investigator> invs)
	{
		CentralRegister cr = CentralRegister.centralRegister;
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez choisir un enquêter à qui demander la liste des sociétés d'un contribuable");
		String invName = sc.nextLine();

		Investigator inv = null;
		do
		{
			inv = cr.getInv(invName);
			if(inv == null)
			{
				System.out.println("Veuillez choisir un enqueteur valide. Voici la liste des enquêteurs disponible");
				cr.printInvestigator();
				inv = cr.getInv(sc.nextLine());
			}
		}while(inv == null);

		System.out.println("Veuillez choisir le propriétaire à qui demandé sa liste de sociétées");
		String ownerName = sc.nextLine();

		addMessage(new Message(cr.getCurrentDay()+1, inv.getCompanyList(ownerName)));
		inv.addMessage(new Message(cr.getCurrentDay()+1, inv.getCompanyList(ownerName)));
	}

	public void promptBank(ArrayList<Bank> banks)
	{
		CentralRegister cr = CentralRegister.centralRegister;
		System.out.println("Veuillez préciser le numéro du compte bancaire");
		Scanner sc = new Scanner(System.in);
		int id = Integer.parseInt(sc.nextLine());
		System.out.println("Vous recevrez votre message sous peu");

		boolean found = false;
		Owner o=null;

		for(Bank bank : banks)
		{
			ArrayList<BankAccount> bas = bank.getListBankAccount();
			for(BankAccount ba : bas)
			{
				if(ba.getID() == id)
				{
					o = ba.getOwner();
					found = true;
				}		
			}
			if(found)
				break;
		}

		if(o!=null)
			addMessage(new Message(cr.getCurrentDay()+1, o.getName()));
		else
			addMessage(new Message(cr.getCurrentDay()+1, "Compte non existant"));
	}
}
