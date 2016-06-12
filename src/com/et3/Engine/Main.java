package com.et3.Engine;

import java.util.*;

public class Main
{
	private static final int maxLevel = 5;
	private ArrayList<Investigator> m_investigators;
	private ArrayList<Bank>         m_banks;
	private ArrayList<String>       m_companyNames;
	private ArrayList<String>       m_taxPayerNames;

	private int m_nbFraudulent = 0;

	Main()
	{
		m_investigators = new ArrayList<Investigator>();
		m_banks         = new ArrayList<Bank>();
	}

	void addInvestigator(Investigator i)
	{
		m_investigators.add(i);
	}

	void addBank(Bank b)
	{
		m_bank.add(b);
	}

	void addCompanyName(String name)
	{
		m_companyNames.add(name);
	}

	void addTaxPayerName(String name)
	{
		m_taxPayerNames.add(name);
	}

	void init(int nbFraudulent)
	{
		m_nbFraudulent = nbFraudulent;

		CentralRegister.initCentralRegister();

		//Pour chaque fraude, on créé un contribuable.
		for(int i=0; i < nbFraudulent; i++)
		{
			Bank bank         = bankList.get((int)(Math.random()*1000)%bankList.length());
			BankAccount ba    = bank.createAccount((int)Math.random()*100000);
			TaxPayer taxPayer = new TaxPayer(ba);
			taxPayers.add(TaxPayer);
			//Et l'ajoute au registre central
			CentralRegister.centralRegister.addTaxPayer(taxPayer);
		}

		//On leur créer ensuite un réseau d'entreprises
		ArrayList<String> companyNames = new ArrayList<String>(m_companyNames);
		for(int i=0; i < nbFraudulent; i++)
		{
			//Ajoute la première entreprise (nécessaire)

			//Appelle recursivement createTreeCompany qui va créer un arbre d'entreprise aléatoirement.
		}
	}

	//Boucle principale du jeu
	void launch()
	{
	}

	//Créer récursivement un arbre d'entreprise pour o.Le level aide à ne pas dépasser une certaine taille
	void createTreeCompany(Owner o, int level)
	{
		//5 niveaux maximum
		for(int i=0; i < (int)(Math.random() * 1000)%((int)Math.random() * 10 * (maxLevel-level)); i++)
		{
			//Créer l'entreprise et la ratache à o
			
			//Lui créer à son tour un réseau d'entreprise
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
			propBuf = Files.newBufferedReader(file);
		}
		catch(IOException e)
		{
			return;
		}

		prop.load(propBuf);
		String countryProp = prop.getProperty("countryName");
		String[] countrySplit = countryProp.split(",");

		for(String s : countrySplit)
			countryList.add(s);

		Country.initCountries(countryList);

		//Récupérer la liste des banques
		ArrayList<Bank>   bankList    = new ArrayList<Bank>();
		String[] bankSplit = prop.getProperty("bankName").split(",");

		for(String s : bankSplit)
		{
			Bank b = Bank.createDefaultBank(s, Country.getCountry(countrySplit[(int)(Math.random() * 1000) % countrySplit.length()]), null);
			bankList.add(b);
			m.addBank(b);
		}

		//Recupérer la liste des noms d'entreprises valides
		String[]  companyNames = prop.getProperty("companyNames").split(",");
		for(String s : companyNames)
			m.addCompanyName(s);

		//Récupérer la liste de joueurs
		System.out.println("Bonjour, la chasse aux fraudes est ouverte.\n Vous jouerez le rôle d'enquêteurs afin de trouver ces fraudes. Un enquếteur est désigné par pays, dont voici la liste : ");

		CentralRegister r = new CentralRegister();
		Scanner sc        = new Scanner(System.in);

		String invName;

		while(!countryList.empty())
		{
			System.out.println("Qu'un joueur se désigne. N'entrez rien pour cloturer");
			invName = sc.nextLine();
			if(invName.equals(""))
				break;

			String countryName;
			do
			{
				System.out.println("Choisissez un pays que vous représenterez");
				String countryName = sc.nextLine();
			}while(!countryList.contains(countryName))

			//Associe le pays au joueur
			Country c = Country.getCountry(countryName);

			//Lui créer un compte bancaire
			Bank invBank = bankList.get((int)(Math.random()*1000)%bankList.length());
			BankAccount ba = invBank.createAccount(0);

			//Et l'ajoute au moteur
			m.addInvestigator(new Investigator(ba, c, invName));

			//Ce pays n'est plus disponible
			countryList.remove(countryName);
		}

		//On récupère ensuite le nombre de compte frauduleux
		int nbFraudulent = Integer.parseInt(prop.getProperty("nbFraudulent"));
		m.init(nbFraudulent);
	}
}
