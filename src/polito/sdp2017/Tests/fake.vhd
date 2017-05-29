library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity Shifter2 is
	generic (N : integer := 16);
	port (A1 : in std_logic_vector(N-1 downto 0);
		  A2 : in std_logic_vector(N-1 downto 0);
		  A3 : in std_logic_vector(N-1 downto 0);
		  A4 : in std_logic_vector(N-1 downto 0);
		 B : out std_logic_vector(N-1 downto 0);
		 C : out std_logic_vector(N-1 downto 0); 
		 D : out std_logic_vector(N-1 downto 0);
		 E : out std_logic_vector(N-1 downto 0));
end Shifter2;

architecture Behavioral of Shifter2 is

begin

	B(N-1 downto 2) <= A1(N-3 downto 0); 
	B(1) <= '0';
	B(0) <= '0';
	
	C(N-1 downto 2) <= A2(N-3 downto 0); 
	C(1) <= '0';
	C(0) <= '0';
	
	D(N-1 downto 2) <= A3(N-3 downto 0); 
	D(1) <= '0';
	D(0) <= '0';
	
	E(N-1 downto 2) <= A4(N-3 downto 0); 
	E(1) <= '0';
	E(0) <= '0';

end Behavioral;

