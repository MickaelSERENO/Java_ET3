package com.et3.Engine;

import java.util.*;

public class Main
{
	public static void main(String[] argv)
	{
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
			bankList.add(Bank.createDefaultBank(s, Country.getCountry(countrySplit[(int)(Math.random() * 1000) % countrySplit.length()]), null));

		ArrayList<Investigator> m_investigator;

		//Récupérer la liste de joueurs
		System.out.println("Bonjour, la chasse aux fraudes est ouverte.\n Vous jouerez le rôle d'enquêteurs afin de trouver ces fraudes. Un enquếteur est désigné par pays, dont voici la liste : ");

		CentralRegister r = new CentralRegister();
		Scanner sc        = new Scanner(System.in);

		String reponse;

		while(!countryList.empty())
		{
			System.out.println("Qu'un joueur se désigne");
			reponse = sc.nextLine();
			if(reponse.equals(""))
				break;

			String countryName;
			do
			{
				System.out.println("Choisissez un pays que vous représenterez");
				String countryName = sc.nextLine();
			}while(!countryList.contains(countryName))

			//Associe le pays au joueur
			Country c = Country.getCountry(countryName);
			m_investigator.add(new Investigator());

			countryList.remove(countryName);
		}
	}
}
