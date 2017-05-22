library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use ieee.std_logic_unsigned.all;


entity AdderN is
	generic (N : integer := 16);
	port (  A : in std_logic_vector(N-1 downto 0);
			B : in std_logic_vector(N-1 downto 0);
			S : out std_logic_vector(N-1 downto 0)
			);
end AdderN;

architecture Behavioral of AdderN is
begin

	S <= A + B;

end Behavioral;

