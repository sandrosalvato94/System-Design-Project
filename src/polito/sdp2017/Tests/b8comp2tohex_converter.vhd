library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity b8comp2tohex_converter is
	port( in_string: IN std_logic_vector(7 downto 0);
			out1, out0: OUT std_logic_vector(3 downto 0));
end b8comp2tohex_converter;

architecture behavior of b8comp2tohex_converter is
	signal module: std_logic_vector(7 downto 0);
	signal temp: unsigned (7 downto 0);
begin
		temp<=unsigned(in_string);	
		                                                               	   
		process (temp)	 
		begin                                                              	   	                                                               	   
			if (temp(7) = '0') then
				module<= std_logic_vector(temp);
			else
				module<=std_logic_vector((not(temp) + 1));
			end if;
				
			out1<= module(7 downto 4);
			out0<= module(3 downto 0);	
			
		end process;
			                                                               	   
end behavior;