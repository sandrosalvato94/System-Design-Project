package polito.sdp2017.Constants;

public class Constants {
	public static String JavaApplicationRoot = "C:/Users/UTENTE/workspace/System-Design-Project/";
	public static String DiamondRoot = "/lscc/diamond/3.9_x64/bin/nt64/";
	
	/***********JAVA PATHS**********/
	public static String dbpath = 		   		JavaApplicationRoot + "src/polito/sdp2017/Tests/DB01.db";
	public static String JavaProjectVHDLs = 		JavaApplicationRoot + "src/polito/sdp2017/Tests/";
	public static String JavaProjectBitstreams = JavaApplicationRoot + "src/polito/sdp2017/Test/Bitstreams/";
	public static String TCLscriptTemplatePath = JavaApplicationRoot + "src/polito/sdp2017/Tests/TCLSCRIPT_TEMPLATE.tcl";
	/*******************************/
	
	/*********DIAMOND PATHS*********/
	public static String diamondShellPath = 		DiamondRoot + "pnmainc.exe";
	public static String diamondImplPath = 		DiamondRoot + "impl1";
	public static String diamondImplSourcePath = diamondImplPath + "/source";
	public static String diamondTCLScritpPath = 	DiamondRoot + "scripts/myscript.tcl";
	public static String diamondBitstreamPath = 	DiamondRoot + "impl1/provaDefinitivaTCL_impl1.bit";
	public static String powerReportPath =  		diamondImplPath + "report_power_summary.html";
	public static String frequencyPath = 		diamondImplPath + "provaDefinitivaTCL_impl1.bgn";
	public static String LUTsFFsPath = 			diamondImplPath + "provaDefinitivaTCL_impl1_map.asd";
	public static String[] prv = {diamondShellPath, diamondTCLScritpPath};
	/*******************************/
}
