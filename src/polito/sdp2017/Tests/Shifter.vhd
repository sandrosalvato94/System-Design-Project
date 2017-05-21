library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use ieee.std_logic_unsigned.all;


entity Shifter is	
		generic (N : integer := 16);
		port (A : in std_logic_vector(N-1 downto 0);
				B, C, D, E : out std_logic_vector(N-1 downto 0)
				);
end Shifter;

architecture Behavioral of Shifter is
begin

	B <= A; -- A
	
	C <= not(A) + 1; -- -A
	
	D(N-1 downto 1) <= A(N-2 downto 0); -- 2A
	D(0) <= '0';
	
	process(A) -- -2A
		variable var : std_logic_vector(N-1 downto 0);
	begin
		var := not(A) + 1;
		E(N-1 downto 1) <= var(N-2 downto 0);
		E(0) <= '0';
	end process;

end Behavioral;

