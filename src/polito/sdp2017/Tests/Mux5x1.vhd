library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity Mux5x1 is
	generic (N : integer := 16);
	port (B : in std_logic_vector(N-1 downto 0);
	      C : in std_logic_vector(N-1 downto 0); 
		  D: in std_logic_vector(N-1 downto 0);
		  E : in std_logic_vector(N-1 downto 0); 
			sel : in std_logic_vector(2 downto 0);
			O : out std_logic_vector(N-1 downto 0)
			);
end Mux5x1;

architecture Behavioral of Mux5x1 is
	constant S : std_logic_vector(N-1 downto 0) := (others => '0');
begin

	process(B, C, D, E, sel)
	begin
		case sel is
			when "000" => -- 0
				O <= S;
			when "001" => -- +A
				O <= B;
			when "010" => -- -A
				O <= C;
			when "011" => -- +2A
				O <= D;
			when "100" => -- -2A
				O <= E;
			when others =>
				O <= (others => '0');
		end case;
	end process;

end Behavioral;

