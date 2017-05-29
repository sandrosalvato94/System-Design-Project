package polito.sdp2017.Tests;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.sun.javafx.geom.transform.GeneralTransform3D;

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
		DBM.generateNewDatabase(DBM.getDBPath());
		//printMenu();
		//a = selectChoice(scannerIO);
		a = 3;
		
		switch (a) {
		case 1:	//addIP
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
		case 3:
			LinkedList<IP> lip = new LinkedList<IP>();
			String isIPCore = new String("true");
			String idIP = new String("$");
			String name = new String("$");
			String maxLUTs = new String("900");
			String minLUTs = new String("399");
			String maxFFs = new String("$");
			String minFFs = new String("$");
			String maxLatency = new String("$");
			String minLatency = new String("$");
			String maxNMemories = new String("$");
			String minNMemories = new String("$");
			String maxPowerConsuption = new String("$");
			String minPowerConsuption = new String("$");
			String maxClockFrequency = new String("$");
			String idAuthor = new String("$");
			String nameAuthor = new String("$");
			String company = new String("$");
			
			LinkedList<String> l = new LinkedList<String>();
			
			l.add(isIPCore); l.add(idIP); l.add(name); l.add(maxLUTs); l.add(minLUTs);
			l.add(maxFFs); l.add(minFFs); l.add(maxLatency); l.add(minLatency);
			l.add(maxNMemories); l.add(minNMemories); l.add(maxPowerConsuption);
			l.add(minPowerConsuption); l.add(maxClockFrequency); l.add(idAuthor);
			l.add(nameAuthor); l.add(company);
			
			lip = DBM.searchIP(l);
			
			System.out.println("end search");
			
			
			
			
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
	
}
