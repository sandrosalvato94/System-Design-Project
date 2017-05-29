library IEEE;
use IEEE.std_logic_1164.all;
use IEEE.std_logic_unsigned.all;
use WORK.all;
use WORK.constants.all;

-- N : number of register 	32
-- M : bitwidth	      	64
-- K : number of bit address 	5

entity register_file is
 generic(N: integer := 32;
         M: integer := 64;
         K: integer := 5
	);
 port ( CLK: 		IN std_logic;
         RESET: 	IN std_logic;
	 ENABLE: 	IN std_logic;
	 RD1: 		IN std_logic;
	 RD2: 		IN std_logic;
	 WR: 		IN std_logic;
	 ADD_WR: 	IN std_logic_vector(K-1 downto 0); 
	 ADD_RD1: 	IN std_logic_vector(K-1 downto 0);
	 ADD_RD2: 	IN std_logic_vector(K-1 downto 0);
	 DATAIN: 	IN std_logic_vector(M-1 downto 0);
         OUT1: 		OUT std_logic_vector(M-1 downto 0);
	 OUT2: 		OUT std_logic_vector(M-1 downto 0));
end register_file;

architecture Behavioral of register_file is

        -- suggested structures
        subtype REG_ADDR is natural range 0 to N-1; -- using natural type
	type REG_ARRAY is array(REG_ADDR) of std_logic_vector(M-1 downto 0); 
	signal REGISTERS : REG_ARRAY; 
	
	signal cntr1,cntr2, cntr0: std_logic;

	
begin 
-- write your RF code 

	wrt_proc: process(clk)
	begin
		if(enable = '1') then
			if(clk = '1') then
				if(reset = '1') then
					REGISTERS <= (others => (others => '0'));
				elsif(WR = '1') then
					if(cntr1 = '0' AND cntr2 = '0') then --no conflict!
						REGISTERS(conv_integer(ADD_WR)) <= DATAIN;
					end if;
				end if;
			end if;
			if(clk = '0') then
--				if(cntr1 = '1' OR cntr2 = '1') then
--					REGISTERS(conv_integer(ADD_WR)) <= DATAIN;
--				end if;

				if(cntr0 = '1') then
					REGISTERS(conv_integer(ADD_WR)) <= DATAIN;
				end if;
				
			end if;
		end if;
	end process;
	
	read_proc: process(clk, enable)
	begin
		if(enable = '1') then
			if(clk = '1' and clk'event) then
				if(reset = '0') then -- if reset \= '1'
					if(cntr1 = '1' or cntr2 = '1') then -- se conflitto
						if(RD1 = '1') then
							OUT1 <= REGISTERS(conv_integer(ADD_RD1));
						end if;
				
						if(RD2 = '1') then
							OUT2 <= REGISTERS(conv_integer(ADD_RD2));
						end if;
						
						cntr0 <= '1';
					else
						if(RD1 = '1') then
							OUT1 <= REGISTERS(conv_integer(ADD_RD1));
						end if;
				
						if(RD2 = '1') then
							OUT2 <= REGISTERS(conv_integer(ADD_RD2));
						end if;
						
						cntr0 <= '0';
					end if;
				else 
					OUT1 <= (others => '0');
					OUT2 <= (others => '0');
					cntr0 <= '0';
				end if;
			end if;
		end if;
	end process;
	
--	process(cntr1, clk)
--	begin
--		if (cntr1 = '1') then -- conflict
--			if(clk = '0' and clk'event) then --updates at falling edge
--				REGISTERS(conv_integer(ADD_WR)) <= DATAIN;
--			end if;
--			
--		end if;
--	end process;
--	
--	process(cntr2, clk)
--	begin
--		if(cntr2 = '1') then -- conflict
--			if(clk = '0' and clk'event) then -- updates at falling edge
--				REGISTERS(conv_integer(ADD_WR)) <= DATAIN;
--			end if;
--		end if;
--	end process;
	
	
	conflict: process(RD1, RD2, WR, ADD_RD1, ADD_RD2, ADD_WR)
	begin
		if((RD1 = '1' AND WR = '1') AND (ADD_RD1 = ADD_WR)) then
			cntr1 <= '1';
		else
			cntr1 <= '0';
		end if;
		
		if((RD2 = '1' AND WR = '1') AND (ADD_RD2 = ADD_WR)) then
			cntr2 <= '1';
		else 
			cntr2 <= '0';
		end if;
		
		--cntr0 <= cntr1 or cntr2;
	end process;

end behavioral;

----


configuration CFG_RF_BEH of register_file is
  for behavioral
  end for;
end configuration;
