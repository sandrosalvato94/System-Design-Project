library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity Encoder is
	port (A : in std_logic_vector(2 downto 0);
			O : out std_logic_vector(2 downto 0)
			);
end Encoder;

architecture Behavioral of Encoder is

begin

	process(A)
	begin
		case A is
			when "000" =>
				O <= "000";
			when "001" =>
				O <= "001";
			when "010" =>
				O <= "001";
			when "011" =>
				O <= "011";
			when "100" =>
				O <= "100";
			when "101" =>
				O <= "010";
			when "110" =>
				O <= "010";
			when "111" =>
				O <= "000";
			when others =>
		end case;
	end process;

end Behavioral;

