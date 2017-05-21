----------------------------------------------------------------------------------
-- Company: Politecnico di Torino
-- Engineer: Alessandro Salvato
-- 
-- Create Date:    11:31:23 12/10/2016 
-- Design Name: 
-- Module Name:    Control_Logic - Behavioral 
-- Project Name: 
-- Target Devices: It must manage the behaviour of our profiler
-- Tool versions: 
-- Description: 
--
-- Dependencies: 
--
-- Revision: 
-- Revision 0.01 - File Created
-- Additional Comments: 
--
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.NUMERIC_STD.ALL;

entity Control_Logic is
    Port ( configuration_word : in  STD_LOGIC_VECTOR (31 downto 0);
           dump_end : in  STD_LOGIC;
           clk : in  STD_LOGIC;
           count_max : out  STD_LOGIC_VECTOR (9 downto 0);
           target_instr : out  STD_LOGIC_VECTOR (5 downto 0);
           EnableRF : out  STD_LOGIC;
           WriteRF : out  STD_LOGIC;
           Read16RF : out  STD_LOGIC;
           Read32RF : out  STD_LOGIC;
           EnableDump : out  STD_LOGIC;
           Reset : out  STD_LOGIC;
           Sel_mux : out  STD_LOGIC_VECTOR (1 downto 0);
           dump_detection : in  STD_LOGIC;
           carry_detection : in  STD_LOGIC);
end Control_Logic;

architecture Behavioral of Control_Logic is

--TYPE State IS (Start, Off, Idle, Dump_profiler, Dump_signature, Reset);
--signal CurrentState, NextState : State;

--signal PreviousState: std_logic_vector(5 downto 0) := "000000";
signal State: std_logic_vector(5 downto 0);
signal NextState: std_logic_vector(5 downto 0);

--shared variable flag: integer := 0;

begin

--	change_state: process(clk, configuration_word)
--	begin
--		if(clk = '1' and clk'event) then
--			State <= configuration_word(31 downto 26); 
--		end if;
--	end process;
	
	process(clk, State, configuration_word)
	begin
	if(clk = '0' and clk'event) then
		case State is
			when "000001" => --OFF
				EnableRF <= '0'; Read32RF <= '0'; WriteRF <= '0'; EnableDump <= '0'; Reset <= '0'; Sel_mux <= "11";
				if(configuration_word(0) = '1') then
					target_instr <= configuration_word(25 downto 20);
					count_max <= configuration_word(19 downto 10);
				end if;
				NextState <= configuration_word(31 downto 26);
				
			when "000010" => --IDLE
				EnableRF <= '1'; Read32RF <= '0'; WriteRF <= '1'; EnableDump <= '1'; Reset <= '0'; Sel_mux <= "00";
				if(dump_detection = '1') then
					NextState <= "000100";
				else
					NextState <= configuration_word(31 downto 26);
				end if;
			
			when "000100" => --DUMP PROFILER
				EnableRF <= '1'; Read32RF <= '1'; WriteRF <= '0'; EnableDump <= '1'; Reset <= '0'; Sel_mux <= "00";
				if(dump_end = '1') then
					NextState <= "010000";
				else
					if(dump_detection = '1') then
						NextState <= "000100";
					else
						NextState <= configuration_word(31 downto 26);
					end if;
				end if;
				
			when "001000" => --DUMP SIGNATURE
				EnableRF <= '0'; Read32RF <= '0'; WriteRF <= '0'; EnableDump <= '0'; Reset <= '0'; Sel_mux <= "01";
				NextState <= configuration_word(31 downto 26);
			
			when "010000" => --RESET
				EnableRF <= '0'; Read32RF <= '0'; WriteRF <= '0'; EnableDump <= '0'; Reset <= '1'; Sel_mux <= "11";
				NextState <= configuration_word(31 downto 26);
				
			when others => 
				EnableRF <= '0';
				NextState <= configuration_word(31 downto 26);
		end case;
		
		State <= NextState;
		
	end if;
	end process;

end Behavioral;

