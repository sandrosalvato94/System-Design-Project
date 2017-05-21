library ieee;
use ieee.std_logic_1164.all;

entity b1fulladder is
	port(a, b, ci: IN std_logic;
			s,co: OUT std_logic);
end b1fulladder;

architecture behavior of b1fulladder is
	component b12to1MUX
		PORT(S, IN1, IN2: IN STD_LOGIC;
			M   : OUT STD_LOGIC);
	end component;

	signal sel: std_logic;
begin
	
	sel<= a xor b;
	s<= ci xor sel;
	
	m0: b12to1MUX port map(sel, b, ci, co);

end behavior;