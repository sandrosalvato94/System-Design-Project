library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity Extender is
	generic (N : integer := 8);
	port (A : in std_logic_vector(N-1 downto 0);
			Y : out std_logic_vector((2*N)-1 downto 0)
			);
end Extender;

architecture Behavioral of Extender is

begin

	process(A)
	begin
		if (A(N-1) = '0') then
			Y(N-1 downto 0) <= A;
			Y((2*N)-1 downto N) <= (others => '0');
		else
			Y(N-1 downto 0) <= A;
			Y((2*N)-1 downto N) <= (others => '1');
		end if;
	end process;
	

end Behavioral;

