package com.et3.Game;

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.et3.Engine.*;
import com.et3.Player.*;

public class Main
{
	private static final int MAX_LEVEL = 5;
	private static final int NB_MAX_ACTION = 2;
	private ArrayList<Investigator> m_investigators;
	private ArrayList<Bank>         m_banks;
	private ArrayList<String>       m_companyNames;
	private ArrayList<String>       m_taxpayerNames;

	private int m_nbFraudulent = 0;

	Main()
	{
		m_investigators = new ArrayList<Investigator>();
		m_banks         = new ArrayList<Bank>();
		m_companyNames  = new ArrayList<String>();
		m_taxpayerNames = new ArrayList<String>();
	}

	public void addInvestigator(Investigator i)
	{
		m_investigators.add(i);
		CentralRegister.centralRegister.addInvestigator(i);
	}

	public void addBank(Bank b)
	{
		m_banks.add(b);
	}

	public void addCompanyName(String name)
	{
		m_companyNames.add(name);
	}

	public void addTaxpayerName(String name)
	{
		m_taxpayerNames.add(name);
	}

	public void init(int nbFraudulent)
	{
		m_nbFraudulent = nbFraudulent;
		Owner.initAccountAssociation();
		ArrayList<String> taxpayersName = new ArrayList(m_taxpayerNames);

		//Pour chaque fraude, on créé un contribuable.
		for(int i=0; i < nbFraudulent; i++)
		{
			Bank bank         = m_banks.get((int)(Math.random()*1000)%m_banks.size());
			BankAccount ba    = bank.createAccount((int)(Math.random()*100000));
			try
			{
				String n = taxpayersName.get((int)(Math.random()*taxpayersName.size())%taxpayersName.size());
				Taxpayer taxpayer = new Taxpayer(ba, n);
				taxpayersName.remove(n);	
				//Et l'ajoute au registre central
				CentralRegister.centralRegister.addTaxpayer(taxpayer);

				//Liste d'entreprise toujours valide
				ArrayList<String> companyNames = new ArrayList<String>(m_companyNames);

				for(int j=0; j < 2; j++)
				{
					//On leur créer ensuite un réseau d'entreprises
					//Ajoute la première entreprise (nécessaire)
					Bank b                  = m_banks.get((int)(Math.random()*1000)%m_banks.size());
					BankAccount bankAccount = b.createAccount((int)Math.random()*100000);
					Country co              = Country.getRandomCountry();
					String name             = companyNames.get((int)(Math.random()*1000)%companyNames.size());
					try
					{
						Company company = new Company(bankAccount, name, co, taxpayer);
						companyNames.remove(name);
						createTreeCompany(company, companyNames, 1); //Don't forget the company created
					}
					catch(Exception e)
					{}
				}

				//Initialise les fraudes
				Owner[] fraud = new Owner[2];
				fraud[0] = fraud[1] = null;

				for(int j=0; j < 2; j++)
				{
					//Selectionne des niveaux d'entreprise (profondeur) du contribuable
					int level = 5;
					Owner owner = taxpayer;
					int k;

					for(k=0; k < level; k++)
					{
						ArrayList<Company> companyList = owner.getCompanyOwned();

						//Si la liste est vide, on choisit le dernier proprietaire
						if(companyList.isEmpty())
							break;

						else if((int)(Math.random()*1000)%100 < 10 && owner != taxpayer)
							break;
						
						owner = companyList.get((int)(Math.random()*1000)%companyList.size());
					}

					if(fraud[0] == null)
						fraud[0] = owner;
					//Verifie que le proprietaire n'avais pas déjà été sélectionné
					else if(fraud[0] != owner)
					{
						fraud[1] = owner;
						CentralRegister.centralRegister.addFraudulentTrans(new Transaction(fraud[0].getAccount(), fraud[1].getAccount()));
					}
					else
					{
						j--;
						continue;
					}
				}
			}

			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}

	//Boucle principale du jeu
	public void launch()
	{
		while(!CentralRegister.centralRegister.isEnd())
		{
			System.out.println(CentralRegister.centralRegister);
			for(Investigator inv : m_investigators)
			{
				int nbAction = 0;
				while(nbAction < Main.NB_MAX_ACTION)
				{
					int action = inv.play();
					switch(action)
					{
						case Investigator.PUBLISH_REGISTER:
							System.out.println(CentralRegister.centralRegister);
							break;
						case Investigator.DENONCIATE:
							inv.denonciate(CentralRegister.centralRegister);
							break;
						case Investigator.PROMPT_INV_OWNER:
							inv.promptInvOwner(m_investigators);
							break;
						case Investigator.PROMPT_INV_LIST:
							inv.promptInvList(m_investigators);
							break;
						case Investigator.PROMPT_BANK_OWNER:
							inv.promptBank(m_banks);
							break;
					}

					if(action != Investigator.PUBLISH_REGISTER)
						nbAction++;
				}
			}
			CentralRegister.centralRegister.nextDay();
		}
	}

	//Créer récursivement un arbre d'entreprise pour o.Le level aide à ne pas dépasser une certaine taille
	public void createTreeCompany(Owner o, ArrayList<String> companyNames, int level)
	{
		//5 niveaux maximum
		if(MAX_LEVEL == level)
			return;
		else if(companyNames.size() == 0)
			return;

		for(int i=0; i < (int)(Math.random() * 10000)%((int)(Math.random() * 3 * (MAX_LEVEL-level))+1); i++)
		{
			//Créer l'entreprise et la ratache à o
			Bank bank       = m_banks.get((int)(Math.random()*1000)%m_banks.size());
			BankAccount ba  = bank.createAccount((int)Math.random()*100000);
			Country co      = Country.getRandomCountry();
			String name     = companyNames.get((int)(Math.random()*1000)%companyNames.size());
			try
			{
				Company company = new Company(ba, name, co, o);
				companyNames.remove(name);
				
				//Lui créer à son tour un réseau d'entreprise
				createTreeCompany(company, companyNames, level+1);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
	}

	public static void main(String[] argv)
	{
		Main m = new Main();

		//Récupérer la liste des pays
		ArrayList<String> countryList = new ArrayList<String>();
		Properties prop = new Properties();
		BufferedReader propBuf;
		try
		{
			propBuf = Files.newBufferedReader(Paths.get("res/config.properties"), StandardCharsets.UTF_8);
		}

		catch(IOException e)
		{
			return;
		}

		try
		{
			prop.load(propBuf);
		}
		catch(Exception e)
		{}

		String countryProp = prop.getProperty("countryName");
		String[] countrySplit = countryProp.split(",");

		for(String s : countrySplit)
			countryList.add(s);

		Country.initCountries(countryList);

		//Récupérer la liste des banques
		ArrayList<Bank> bankList = new ArrayList<Bank>();
		String bankProp = prop.getProperty("bankName");
		String[] bankSplit = bankProp.split(",");

		ArrayList<String> cSplit = new ArrayList<String>(Arrays.asList(countrySplit));

		for(String s : bankSplit)
		{
			try
			{
				int indice = (int)(Math.random() * 1000) % cSplit.size();
				Bank b = Bank.createDefaultBank(s, Country.getCountry(cSplit.get(indice)), null);
				bankList.add(b);
				m.addBank(b);
				cSplit.remove(indice);
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}

		String[] taxpayerNames = prop.getProperty("taxpayer").split(",");
		for(String s : taxpayerNames)
			m.addTaxpayerName(s);

		//Recupérer la liste des noms d'entreprises valides
		String[]  companyNames = prop.getProperty("companyNames").split(",");
		for(String s : companyNames)
			m.addCompanyName(s);

		//Récupérer la liste de joueurs
		System.out.println("Bonjour, la chasse aux fraudes est ouverte.\nVous jouerez le rôle d'enquêteurs afin de trouver ces fraudes. Un enquếteur est désigné par pays, dont voici la liste : ");

		CentralRegister.initCentralRegister();
		for(String s : countryList)
			System.out.println(s);
		Scanner sc        = new Scanner(System.in);

		String invName;

		while(!countryList.isEmpty())
		{
			System.out.println("Qu'un joueur se désigne. N'entrez rien pour cloturer");
			invName = sc.nextLine();
			if(invName.equals(""))
				break;

			String countryName;
			do
			{
				System.out.println("Choisissez un pays que vous représenterez");
				countryName = sc.nextLine();
			}while(!countryList.contains(countryName));

			//Associe le pays au joueur
			Country c = Country.getCountry(countryName);

			//Lui créer un compte bancaire
			Bank invBank = bankList.get((int)(Math.random()*1000)%bankList.size());
			BankAccount ba = invBank.createAccount(0);

			//Et l'ajoute au moteur
			try
			{
				m.addInvestigator(new HumanInv(ba, c, invName));
			}
			catch(Exception e)
			{
				System.out.println(e);
			}

			//Ce pays n'est plus disponible
			countryList.remove(countryName);
		}

		for(int i=countryList.size()-1; countryList.size() > 0; i--)
		{
			String cName = countryList.get((int)(Math.random()*1000) % countryList.size());
			Country c = Country.getCountry(cName);
			//Lui créer un compte bancaire
			Bank invBank = bankList.get((int)(Math.random()*1000)%bankList.size());
			BankAccount ba = invBank.createAccount(0);

			String name = "Investigator" + i;
			System.out.println("L'enquêteur " + name + " du pays " + cName + " sera un personnage non jouable. Posez lui vos questions !");
			try
			{
				m.addInvestigator(new PNJ(ba, c, name));
			}
			catch(Exception e)
			{}
			countryList.remove(cName);
		}

		//On récupère ensuite le nombre de compte frauduleux
		int nbFraudulent = Integer.parseInt(prop.getProperty("nbFraudulent"));

		m.init(nbFraudulent);
		m.launch();
	}
}
