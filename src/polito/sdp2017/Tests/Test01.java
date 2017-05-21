package polito.sdp2017.Tests;
import java.io.IOException;
import java.util.Scanner;

import  polito.sdp2017.Components.*;
import  polito.sdp2017.HardwareInterface.*; 

public class Test01 {

	public static void main(String[] args) throws IOException {
		
		int a;
		Scanner scannerIO = new Scanner(System.in);
		DBManager DBM = new SQLiteManager();
		DBM.setDBName("System Design Project Database");
		DBM.setDBPath("jdbc:sqlite:.\\src\\polito\\sdp2017\\Tests\\DB01.db");
		DBM.generateNewDatabase(DBM.getDBPath());
		printMenu();
		a = selectChoice(scannerIO);
		System.out.println(a);
		
		//PrintMenu();

	}
	
	public static void printMenu() {
		System.out.println("**********MENU**********");
		System.out.println("1 - Add IPs");
		System.out.println("2 - Create a new configuration");
		System.out.println("3 - Erase all");
		System.out.println("0 - Exit");
		System.out.println("\n");
	}
	
	public static int selectChoice(Scanner scannerIO) throws IOException
	{
		int a;
		
		a = scannerIO.nextInt();
	
		return a;
	}
}
