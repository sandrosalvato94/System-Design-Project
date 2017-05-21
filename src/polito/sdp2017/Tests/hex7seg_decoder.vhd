library ieee;
use ieee.std_logic_1164.all;

entity hex7seg_decoder is
	port( in_char: IN std_logic_vector(3 downto 0);
			out_string: OUT std_logic_vector (6 downto 0));
end hex7seg_decoder;

architecture behavior of hex7seg_decoder is
begin
	with in_char select
		out_string<= "1000000" when x"0",
					 "1111001" when x"1",
					 "0100100" when x"2",
		             "0110000" when x"3",
		             "0011001" when x"4",
					 "0010010" when x"5",
		             "0000010" when x"6",
	                 "1011000" when x"7",
		             "0000000" when x"8",
		             "0010000" when x"9",
		             "0001000" when x"A",
					 "0000011" when x"B",
			         "1000110" when x"C",
			         "0100001" when x"D",
			         "0000110" when x"E",
			         "0001110" when x"F",
			         "1111111" when others;	
end behavior;