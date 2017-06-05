package polito.sdp2017.Tests;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import  polito.sdp2017.Components.*;
import  polito.sdp2017.HardwareInterface.*; 

public class Test01 {

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, SAXException, ParserConfigurationException {
		
		final String dbpath = "./src/polito/sdp2017/Tests/DB01.db";
		
		int a; 
		
		Scanner scannerIO = new Scanner(System.in);
		DBManager DBM = new SQLiteManager();
		DBM.setDBName("System Design Project Database");
		DBM.openConnection(dbpath);
		//printMenu();
		//a = selectChoice(scannerIO);
		a =5; 
		
		switch (a) {
		case 1:	//addIP
			DBM.resetDatabase();
			fillDatabase(DBM);
			break;
		case 2:	//removeIP
				boolean tmp;
				fillDatabase(DBM);
				//DBM.removeIP("Encoder", "$", true);
				tmp = DBM.removeIP("$", "VHDL_lfsr", true);
				//tmp = DBM.removeIP("$", "VHDL_lfsr", false); //it doesn't exist in IPManager Lib
				//DBM.removeIP("", "", true);
				
				if(tmp == true)
				{
					System.out.println("Rimosso con successo");
				}
				else
				{
					System.out.println("Oggetto non presente oppure non rimosso");
				}
			break;
		case 3: //searchIP
			//fillDatabase(DBM);
			LinkedList<IP> lip = new LinkedList<IP>();
			String isIPCore = new String("false");
			String idIP = new String("$");
			String name = new String("$");
			String maxLUTs = new String("$");
			String maxFFs = new String("$");
			String maxLatency = new String("$");
			String maxPowerConsuption = new String("$");
			String maxClockFrequency = new String("$");
			String idAuthor = new String("$");
			String nameAuthor = new String("Emanuele Garolla");
			String company = new String("$");
			
			LinkedList<String> l = new LinkedList<String>();
			
			l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
			l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
		    l.add(maxClockFrequency); l.add(idAuthor);
			l.add(nameAuthor); l.add(company);
			
			lip = DBM.searchIP(l);
			
			for(IP i : lip)
			{
				System.out.println(i.getName());
				
				if(i.getHwInterface().getGenerics() != null) {
					for(Generic g : i.getHwInterface().getGenerics())
					{
						System.out.println(g.toString());
					}
				}
				for(Pin p : i.getHwInterface().getPins())
				{
					System.out.println(p.toString());
				}
				System.out.println("\n");
			}
			
			System.out.println("end search");
			
			break;
		case 4: //addConfiguration
			 //fillDatabase(DBM);
			 addConf00(DBM);
			 addConf01(DBM);
			 addConf02(DBM);
			 addConf03(DBM);
			 addConf04(DBM);
			 addConf05(DBM);
			break;
		
		case 5: //search configuration
			
			LinkedList<FPGAConfiguration> lc = new LinkedList<FPGAConfiguration>();
			 String nIPs = new String("$");
			 idIP = new String("conf05");
			 name = new String("$");
			 maxLUTs = new String("$");
			 maxFFs = new String("$");
			 maxLatency = new String("$");
			 maxPowerConsuption = new String("$");
			 maxClockFrequency = new String("$");
			 idAuthor = new String("$");
			 nameAuthor = new String("$");
			 company = new String("$");
			 
			 l = new LinkedList<String>();
			 
			 l.add(nIPs); l.add(idIP); l.add(name); l.add(maxLUTs); 
				l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
			    l.add(maxClockFrequency); l.add(idAuthor);
				l.add(nameAuthor); l.add(company);
			
			lc = DBM.searchConfiguration(l);
			FPGAConfiguration.generateTopLevelEntity(lc.getFirst());
				
			break;
		case 6:
				DBM.removeConfiguration("fake", "$");
			break;
		default:
			System.out.println("Program is arresting");
			break;
		}
		
		//PrintMenu();
	}
	
	public static void printMenu() {
		System.out.println("**********MENU**********");
		System.out.println("1 - Add IPs");
		System.out.println("2 - Create a new configuration");
		System.out.println("3 - Erase all");
		System.out.println("0 - Exit");
		System.out.println("\nSELECT HERE: ");
	}
	
	public static int selectChoice(Scanner scannerIO) throws IOException {
		int a;
		
		a = scannerIO.nextInt();
		return a;
	
	}
	
	public static String getStringFromStdin() {
		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader myInput = new BufferedReader(reader);
		String str= new String();
				
		try {
				str = myInput.readLine();
				return str;
		} catch (IOException e) {
				System.out.println ("Si è verificato un errore: " + e);
				System.exit(-1);
		}
		return str;
	}
	
	public static String setDBPath()
	{
		String s = getStringFromStdin();
		return s;
	}
	
	public static void fillDatabase(DBManager DBM) throws SAXException, ParserConfigurationException, IOException, SQLException
	{
		LinkedList<IP> listIP = new LinkedList<IP>();
		String nameFile = new String();
		File file = new File("./src/polito/sdp2017/Tests");
		        if(file.isDirectory())
		        {
		            File[] filesInDir = file.listFiles();
		            for(File f : filesInDir)
		            {
		            	if(f.getName().contains(".xml"))
		            	{
		            		listIP = (LinkedList<IP>) IP.getFromXML("src/polito/sdp2017/Tests/" + f.getName());
		            		DBM.addIP(listIP.getFirst());
		            		//System.out.println(f.getName());
		            	}
		            }
		        }
	}
	
	public static void addConf00(DBManager DBM)
	{
		 LinkedList<IP>lip = new LinkedList<IP>();
		 String isIPCore = new String("false");
		 String idIP = new String("$");
		 String name = new String("$");
		 String maxLUTs = new String("$");
		 String maxFFs = new String("$");
		 String maxLatency = new String("$");
		 String maxPowerConsuption = new String("$");
		 String maxClockFrequency = new String("$");
		 String idAuthor = new String("$");
		 String nameAuthor = new String("$");
		 String company = new String("Politecnico di Torino");
		
		LinkedList<String> l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		IPManager manager = (IPManager) lip.getFirst(); /*Exception in thread "main" 
														  java.util.NoSuchElementException
														  if no manager found*/
		lip = new LinkedList<IP>();
		 isIPCore = new String("true");
		 idIP = new String("$");
		 name = new String("$");
		 maxLUTs = new String("$");
		 maxFFs = new String("$");
		 maxLatency = new String("$");
		 maxPowerConsuption = new String("$");
		 maxClockFrequency = new String("$");
		 idAuthor = new String("$");
		 nameAuthor = new String("$");
		 company = new String("Politecnico di Torino");
		
		l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		
		LinkedList<MappedIP> m = new LinkedList<MappedIP>();
		
		for(int i = 0; i<lip.size(); i++)
		{
			m.add(new MappedIP("mapIP_" + i , (IPCore)lip.get(i), i, "00x0000" + i));
		}
		
		FPGAConfiguration conf = new FPGAConfiguration("confTest0", "conf00",
				m, manager, "src/polito/sdp2017/Tests/bitstream0.c", 
				new HardwareProperties(99, 5, 655, 1, 5434, 37), 
				new Author("cp98", "Paolo Monti", "Politecnico di Torino", "paolomonti@live.it", "Student"), 
				null);
		DBM.addConfiguration(conf);
	}
	
	public static void addConf01(DBManager DBM)
	{
		 LinkedList<IP>lip = new LinkedList<IP>();
		 String isIPCore = new String("false");
		 String idIP = new String("$");
		 String name = new String("$");
		 String maxLUTs = new String("$");
		 String maxFFs = new String("$");
		 String maxLatency = new String("$");
		 String maxPowerConsuption = new String("$");
		 String maxClockFrequency = new String("$");
		 String idAuthor = new String("cp01");
		 String nameAuthor = new String("$");
		 String company = new String("$");
		
		LinkedList<String> l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		IPManager manager = (IPManager) lip.getFirst(); /*Exception in thread "main" 
														  java.util.NoSuchElementException
														  if no manager found*/
		lip = new LinkedList<IP>();
		 isIPCore = new String("true");
		 idIP = new String("$");
		 name = new String("$");
		 maxLUTs = new String("$");
		 maxFFs = new String("$");
		 maxLatency = new String("$");
		 maxPowerConsuption = new String("$");
		 maxClockFrequency = new String("3.0");
		 idAuthor = new String("$");
		 nameAuthor = new String("$");
		 company = new String("$");
		
		l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		
		LinkedList<MappedIP> m = new LinkedList<MappedIP>();
		
		for(int i = 0; i<lip.size(); i++)
		{
			m.add(new MappedIP("mapIP_" + i , (IPCore)lip.get(i), i, "00x0000" + i));
		}

		FPGAConfiguration conf = new FPGAConfiguration("confTest1", "conf01",
				m, manager, "src/polito/sdp2017/Tests/bitstream1.c", 
				new HardwareProperties(789, 23456, 69, 1, 4, 0.09), 
				new Author("cp97", "Genoveffa Alberti", "Corso Massimo", "genoveffa.alberti@live.it", "Pubbliche Relazioni"), 
				null);
		DBM.addConfiguration(conf);
	}
	
	public static void addConf02(DBManager DBM)
	{
		 LinkedList<IP>lip = new LinkedList<IP>();
		 String isIPCore = new String("false");
		 String idIP = new String("$");
		 String name = new String("$");
		 String maxLUTs = new String("$");
		 String maxFFs = new String("$");
		 String maxLatency = new String("1.0");
		 String maxPowerConsuption = new String("$");
		 String maxClockFrequency = new String("$");
		 String idAuthor = new String("$");
		 String nameAuthor = new String("$");
		 String company = new String("$");
		
		LinkedList<String> l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		IPManager manager = (IPManager) lip.getFirst(); /*Exception in thread "main" 
														  java.util.NoSuchElementException
														  if no manager found*/
		lip = new LinkedList<IP>();
		 isIPCore = new String("true");
		 idIP = new String("$");
		 name = new String("$");
		 maxLUTs = new String("800");
		 maxFFs = new String("800");
		 maxLatency = new String("$");
		 maxPowerConsuption = new String("$");
		 maxClockFrequency = new String("$");
		 idAuthor = new String("$");
		 nameAuthor = new String("$");
		 company = new String("$");
		
		l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		
		LinkedList<MappedIP> m = new LinkedList<MappedIP>();
		
		for(int i = 0; i<lip.size(); i++)
		{
			m.add(new MappedIP("mapIP_" + i , (IPCore)lip.get(i), i, "00x0000" + i));
		}

		FPGAConfiguration conf = new FPGAConfiguration("confTest2", "conf02",
				m, manager, "src/polito/sdp2017/Tests/bitstream2.c", 
				new HardwareProperties(79, 456, 9, 10, 40, 20.09), 
				new Author("cp96", "Francesca Bocchetti", "Telecom", "francescabocchetti@telecom.it", "Pubbliche Relazioni"), 
				null);
		DBM.addConfiguration(conf);
	}

	public static void addConf03(DBManager DBM)
	{
		 LinkedList<IP>lip = new LinkedList<IP>();
		 String isIPCore = new String("false");
		 String idIP = new String("$");
		 String name = new String("$");
		 String maxLUTs = new String("400");
		 String maxFFs = new String("$");
		 String maxLatency = new String("$");
		 String maxPowerConsuption = new String("$");
		 String maxClockFrequency = new String("$");
		 String idAuthor = new String("$");
		 String nameAuthor = new String("$");
		 String company = new String("$");
		
		LinkedList<String> l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		IPManager manager = (IPManager) lip.getFirst(); /*Exception in thread "main" 
														  java.util.NoSuchElementException
														  if no manager found*/
		lip = new LinkedList<IP>();
		 isIPCore = new String("true");
		 idIP = new String("$");
		 name = new String("$");
		 maxLUTs = new String("$");
		 maxFFs = new String("$");
		 maxLatency = new String("$");
		 maxPowerConsuption = new String("$");
		 maxClockFrequency = new String("$");
		 idAuthor = new String("$");
		 nameAuthor = new String("$");
		 company = new String("$");
		
		l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		
		LinkedList<MappedIP> m = new LinkedList<MappedIP>();
		
		for(int i = 0; i<lip.size(); i++)
		{
			m.add(new MappedIP("mapIP_" + i , (IPCore)lip.get(i), i, "00x0000" + i));
		}

		FPGAConfiguration conf = new FPGAConfiguration("confTest3", "conf03",
				m, manager, "src/polito/sdp2017/Tests/bitstream3.c", 
				new HardwareProperties(9, 556, 559, 140, 420, 201.09), 
				new Author("cp95", "Italo Treni", "Trenitalia", "delay@telecom.it", "Accompagnatore"), 
				null);
		DBM.addConfiguration(conf);
	}

	public static void addConf04(DBManager DBM)
	{
		 LinkedList<IP>lip = new LinkedList<IP>();
		 String isIPCore = new String("false");
		 String idIP = new String("$");
		 String name = new String("$");
		 String maxLUTs = new String("$");
		 String maxFFs = new String("$");
		 String maxLatency = new String("1.0");
		 String maxPowerConsuption = new String("$");
		 String maxClockFrequency = new String("$");
		 String idAuthor = new String("$");
		 String nameAuthor = new String("$");
		 String company = new String("$");
		
		LinkedList<String> l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		IPManager manager = (IPManager) lip.getFirst(); /*Exception in thread "main" 
														  java.util.NoSuchElementException
														  if no manager found*/
		lip = new LinkedList<IP>();
		 isIPCore = new String("true");
		 idIP = new String("$");
		 name = new String("Extender");
		 maxLUTs = new String("$");
		 maxFFs = new String("$");
		 maxLatency = new String("$");
		 maxPowerConsuption = new String("$");
		 maxClockFrequency = new String("$");
		 idAuthor = new String("$");
		 nameAuthor = new String("$");
		 company = new String("$");
		
		l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		
		LinkedList<MappedIP> m = new LinkedList<MappedIP>();
		
		for(int i = 0; i<lip.size(); i++)
		{
			m.add(new MappedIP("mapIP_" + i , (IPCore)lip.get(i), i, "00x0000" + i));
		}

		FPGAConfiguration conf = new FPGAConfiguration("confTest4", "conf04",
				m, manager, "src/polito/sdp2017/Tests/bitstream4.c", 
				new HardwareProperties(79, 456, 9, 10, 40, 20.09), 
				new Author("cp01", "Francesca Bocchetti", "Telecom", "francescabocchetti@telecom.it", "Pubbliche Relazioni"), 
				null);
		DBM.addConfiguration(conf);
	}

	public static void addConf05(DBManager DBM)
	{
		 LinkedList<IP>lip = new LinkedList<IP>();
		 String isIPCore = new String("false");
		 String idIP = new String("$");
		 String name = new String("$");
		 String maxLUTs = new String("$");
		 String maxFFs = new String("$");
		 String maxLatency = new String("$");
		 String maxPowerConsuption = new String("$");
		 String maxClockFrequency = new String("$");
		 String idAuthor = new String("$");
		 String nameAuthor = new String("Emanuele Garolla");
		 String company = new String("$");
		
		LinkedList<String> l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		IPManager manager = (IPManager) lip.getFirst(); /*Exception in thread "main" 
														  java.util.NoSuchElementException
														  if no manager found*/
		lip = new LinkedList<IP>();
		 isIPCore = new String("true");
		 idIP = new String("$");
		 name = new String("IP_Dummy");
		 maxLUTs = new String("$");
		 maxFFs = new String("$");
		 maxLatency = new String("$");
		 maxPowerConsuption = new String("$");
		 maxClockFrequency = new String("$");
		 idAuthor = new String("$");
		 nameAuthor = new String("$");
		 company = new String("$");
		
		l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		
		LinkedList<MappedIP> m = new LinkedList<MappedIP>();
		m.add(new MappedIP("mapIP_0" , (IPCore)lip.get(0), 0, "00x00000"));
		m.add(new MappedIP("mapIP_2" , (IPCore)lip.get(0), 2, "00x00002"));
		m.add(new MappedIP("mapIP_3" , (IPCore)lip.get(0), 3, "00x00003"));
		
		lip = new LinkedList<IP>();
		 isIPCore = new String("true");
		 idIP = new String("$");
		 name = new String("IP_Adder");
		 maxLUTs = new String("$");
		 maxFFs = new String("$");
		 maxLatency = new String("$");
		 maxPowerConsuption = new String("$");
		 maxClockFrequency = new String("$");
		 idAuthor = new String("$");
		 nameAuthor = new String("$");
		 company = new String("$");
		
		l = new LinkedList<String>();
		
		l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); 
		l.add(maxFFs); l.add(maxLatency); l.add(maxPowerConsuption);
	    l.add(maxClockFrequency); l.add(idAuthor);
		l.add(nameAuthor); l.add(company);
		
		lip = DBM.searchIP(l);
		
		m.add(new MappedIP("mapIP_1" , (IPCore)lip.get(0), 1, "00x00001"));
		
		
		FPGAConfiguration conf = new FPGAConfiguration("confTest5", "conf05",
				m, manager, "src/polito/sdp2017/Tests/bitstream4.c", 
				new HardwareProperties(9, 56, 0, 0, 0, 0.09), 
				new Author("cp94", "Fabrizio Telescopio", "Ottica San Paolo", "fabbro@live.it", "Oculista"), 
				null);
		DBM.addConfiguration(conf);
	}
}
