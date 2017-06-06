package polito.sdp2017.Tests;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import  polito.sdp2017.Components.*;
import  polito.sdp2017.HardwareInterface.*; 

public class Test01 {
	
	final static String JavaApplicationRoot = "C:/Users/UTENTE/workspace/System-Design-Project/";
	final static String DiamondRoot = "/lscc/diamond/3.9_x64/bin/nt64/";
	
	/***********JAVA PATHS**********/
	final static String dbpath = 		   		JavaApplicationRoot + "src/polito/sdp2017/Tests/DB01.db";
	final static String JavaProjectVHDLs = 		JavaApplicationRoot + "src/polito/sdp2017/Tests/";
	final static String JavaProjectBitstreams = JavaApplicationRoot + "src/polito/sdp2017/Test/Bitstreams/";
	final static String TCLscriptTemplatePath = JavaApplicationRoot + "src/polito/sdp2017/Tests/TCLSCRIPT_TEMPLATE.tcl";
	/*******************************/
	
	/*********DIAMOND PATHS*********/
	final static String diamondShellPath = 		DiamondRoot + "pnmainc.exe";
	final static String diamondImplPath = 		DiamondRoot + "impl1";
	final static String diamondImplSourcePath = diamondImplPath + "source";
	final static String diamondTCLScritpPath = 	DiamondRoot + "scripts/myscript.tcl";
	final static String diamondBitstreamPath = 	DiamondRoot + "impl1/provaDefinitivaTCL_impl1.bit";
	final static String powerReportPath =  		diamondImplPath + "report_power_summary.html";
	final static String frequencyPath = 		diamondImplPath + "provaDefinitivaTCL_impl1.bgn";
	final static String LUTsFFsPath = 			diamondImplPath + "provaDefinitivaTCL_impl1_map.asd";
	final static String[] prv = {diamondShellPath, diamondTCLScritpPath};
	/*******************************/
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, SAXException, ParserConfigurationException, InterruptedException {
		
		int a; 
		
		Scanner scannerIO = new Scanner(System.in);
		DBManager DBM = new SQLiteManager();
		DBM.setDBName("System Design Project Database");
		DBM.openConnection(dbpath);
		//printMenu();
		//a = selectChoice(scannerIO);
		a =4; 
		
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
			 addConf21(DBM);
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
		
		case 7:
				
			break;
		case 8:
			
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

	public static void addConf21(DBManager DBM)
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
		m.add(new MappedIP("mapIP_21", (IPCore)lip.get(0), 21, "00x00004"));
		
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
		
		
		FPGAConfiguration conf = new FPGAConfiguration("confTest21", "conf21",
				m, manager, "src/polito/sdp2017/Tests/bitstream21.c", 
				new HardwareProperties(9, 56, 0, 0, 0, 0.09), 
				new Author("cp21", "Mr. Diamond", "Lattice Semiconductor", "fabbro@live.it", "Tester"), 
				null);
		DBM.addConfiguration(conf);
	}
	
	public static void deleteAllFiles(String path)
	{
		File directory = new File(path);
		File[] files = directory.listFiles();
		for (File f : files)
		f.delete();
	}

	public static void createConfiguration(FPGAConfiguration fpgaconf) throws IOException, InterruptedException
	{	
		final String[] prv = {diamondShellPath, diamondTCLScritpPath};
		
		createTCLScript(fpgaconf); //create new TCL script
		
		runSynthesis(fpgaconf);	//run synthesis (and copy bitstream)
		
		fpgaconf.setHwProperties(getHPFromLattice(diamondImplPath)); //
		
		deleteAllFiles(diamondImplPath);
	}
	
	public static HardwareProperties getHPFromLattice(String pathDir)
	{
		//LUTs
		//FFs
		//latency
		//nMemories = random?
		//maxPowerConsuption
		//maxClockFrequency
		
		int LUTs = 0, FFs = 0, nMemories = 0;
		Double latency = 0.0, maxPowerConsuption = 0.0, maxClockFrequency = 0.0;
		
		File f_power = new File(powerReportPath);
		File f_freq = new File(frequencyPath);
		File f_area = new File(LUTsFFsPath);
		
		if(f_power.exists())
		{
			try(BufferedReader in  = new BufferedReader(new FileReader(f_power)))
				{
					while(!in.readLine().matches("<td width=250><font class=\"table\">Total Power Est. Design </font></td>"))
					{
						
					}
					StringBuffer tmp = new StringBuffer(in.readLine());
					Pattern p = Pattern.compile(".*>([\\d|\\.]+)\\s+W.*", Pattern.DOTALL);
					Matcher m = p.matcher(tmp);
					m.matches();
					maxPowerConsuption = Double.parseDouble(m.group(1));
					
					in.close();
				}
				catch(Exception e)
				{
					//throw new RuntimeException("Unable to fi file");
				}
		}
		else
		{
			System.out.println("ERROR: power report not found.\nMax Power Consuption = 0");
		}
		if(f_freq.exists())
		{
			try(BufferedReader in  = new BufferedReader(new FileReader(f_freq)))
			{
				String tmp = new String();
				while(!(tmp = in.readLine()).matches("\\|.*MCCLK_FREQ.*"));
				
				
				Pattern p = Pattern.compile(".*(\\d+.\\d+)\\*.*"); //ii.dddd 
				Matcher m = p.matcher(tmp);
				m.matches();
				tmp = m.group(1);
				maxClockFrequency = Double.parseDouble(m.group(1));
				
				
				double temp = Math.pow(10, 2);
			    latency = Math.ceil((1/maxClockFrequency) * temp) / temp;
				
				in.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("Unable to write file");
			}
		}
		else
		{
			System.out.println("ERROR: timing report not found.\nMax Clock Frequency = 0\nLatency = 0");
		}
		if(f_area.exists())
		{
			try(BufferedReader in  = new BufferedReader(new FileReader(f_area)))
			{
				String tmp = new String();
				while(!(tmp = in.readLine()).matches("LUTS_used.*"));
				
				Pattern p = Pattern.compile(".*=\\s*(\\d{1,4}).*", Pattern.DOTALL);
				Matcher m = p.matcher(tmp);
				m.matches();
				LUTs = Integer.valueOf(m.group(1));
				
				while(!(tmp = in.readLine()).matches("FF_used.*"));
				
				Pattern p1 = Pattern.compile(".*=\\s*(\\d{1,4}).*", Pattern.DOTALL);
				Matcher m1 = p1.matcher(tmp);
				m1.matches();
				FFs = Integer.valueOf(m1.group(1));
				
				in.close();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		else
		{
			System.out.println("ERROR: area report not found.\nLUTs = 0\nFFs = 0");
		}
		
		HardwareProperties hp = new HardwareProperties(LUTs, FFs, latency, nMemories, maxPowerConsuption, maxClockFrequency);
		return hp;
	}

	public static boolean createTCLScript(FPGAConfiguration fpgaconf)
	{	
		if(newFile(diamondTCLScritpPath) != 1) {
			throw new RuntimeException("File can't be created");
		}
		
		File f_template = new File(TCLscriptTemplatePath);
		
		if(!f_template.exists()) {
			throw new RuntimeException("File TCLSCRIPT_TEMPLATE.tcl not found.");
		}
		String tmpString;
		
		try(BufferedReader in  = new BufferedReader(new FileReader(TCLscriptTemplatePath));
			BufferedWriter out = new BufferedWriter(new FileWriter(diamondTCLScritpPath)))
		{
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--VHDL HERE--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
			out.write("file copy -force -- " + "\"" + JavaProjectVHDLs + "/Tmp/TopLevelEntity.vhd\" "  +
			           "\"" + diamondImplSourcePath + "\"\n");
			out.write("file copy -force -- " + "\"" + JavaProjectVHDLs + "/BUFFER_DATA.vhd\" "  +
			           "\"" + diamondImplSourcePath + "\"\n");
			out.write("file copy -force -- " + "\"" + JavaProjectVHDLs + "/Tmp/constants.vhd\" "  +
			           "\"" + diamondImplSourcePath + "\"\n");
			
			Map<String, List<MappedIP>> hmap = fpgaconf.getMappedIPs().stream().collect(Collectors.groupingBy((l -> l.getIpCore().getIdIP()), Collectors.toList()));
			
			for(String s : hmap.keySet())
			{
				out.write("file copy -force -- " + "\"" + JavaApplicationRoot + hmap.get(s).get(0).getIpCore().getHdlSourcePath() +
						  "\" " + "\"" + diamondImplSourcePath + "\"\n");
			}
			while(!(tmpString = in.readLine()).matches("[\\s|.]*--END HERE--[\\s|.]*"))
			{
				out.write(tmpString + "\n");
			}
			
		}
		catch(Exception e)
		{
			throw new RuntimeException("Unable to write file");
		}
		
		return true;
	}
	
	public static boolean runSynthesis(FPGAConfiguration fpgaconf) throws InterruptedException, IOException
	{	
		try {
			Process proc = Runtime.getRuntime().exec(prv);
			BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((reader.readLine()) != null) {}
			proc.waitFor();
			proc.destroy();
			System.out.println("Synthesis ended.");

		} catch (IOException e) {
			 System.out.println(e.getMessage() + " [ERROR]\n");
			 return false;
		}
		
		File f = new File(diamondBitstreamPath);
		System.out.println("Looking for bitstream...");
		if(f.exists()) //copy file
		{
			System.out.println("Bitstream found!");
			copyBitstream(fpgaconf);
			return true;
		}
		else
		{
			System.out.println("ERROR: bitstream not found");
			return false;
		}
	}
	
	public static void copyBitstream(FPGAConfiguration fpgaconf) throws IOException
	{
		
		
		File f = new File(diamondBitstreamPath);
		
		FileInputStream in = new FileInputStream(f);
		FileOutputStream out = new FileOutputStream(JavaProjectBitstreams + fpgaconf.getIdConf() + ".bit");
		
		byte [] dati = new byte[in.available()];
		in.read(dati);
		out.write(dati);
		in.close();
		out.close();
		
		fpgaconf.setBitstreamPath(JavaProjectBitstreams + fpgaconf.getIdConf() + ".bit");
	}
	
	public static int newFile(String path) {
		 
	    try {
	        File file = new File(path);
	         
	        if (file.exists())
	        {
	            System.out.println("Il file " + path + " esiste gia'. Sovrascritto");
	            file.createNewFile();
	            return 1;
	        }
	        else 
	        {	if (file.createNewFile())
	        	{
	            	System.out.println("Il file " + path + " e' stato creato");
	            	return 1;
	        	}
	        	else
	        	{
	        		System.out.println("Il file " + path + " non puo' essere creato");
	        		return -1;
	        	}
	        }
	        
	     
	    } catch (IOException e) {
	        e.printStackTrace();
	        return -1;
	    }
	}
}
