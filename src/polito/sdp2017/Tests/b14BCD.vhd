LIBRARY ieee;
USE ieee.std_logic_1164.all;
USE ieee.numeric_std.all;

ENTITY b14BCD IS
PORT ( in_bin: IN STD_LOGIC_VECTOR(13 downto 0);
	   out_migl, out_cent, out_dec, out_unit: OUT STD_LOGIC_VECTOR (3 downto 0));
END b14BCD;

ARCHITECTURE behavior OF b14BCD IS
	SIGNAL in_int, migl, cent, dec, unit: integer;
BEGIN

	in_int<=TO_INTEGER(UNSIGNED(in_bin));
	
	migl<=in_int/1000;
	out_migl<=STD_LOGIC_VECTOR(TO_UNSIGNED((migl), 4));
	
	cent<=(in_int-migl*1000)/100;
	out_cent<=STD_LOGIC_VECTOR(TO_UNSIGNED((cent), 4));	
	
	dec<=(in_int-migl*1000-cent*100)/10;
	out_dec<=STD_LOGIC_VECTOR(TO_UNSIGNED((dec), 4));
	
	unit<=(in_int-migl*1000-cent*100-dec*10);
	out_unit<=STD_LOGIC_VECTOR(TO_UNSIGNED((unit), 4));	
		
END behavior;