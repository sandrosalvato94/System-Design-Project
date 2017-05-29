----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 	Alessandro Salvato
-- 
-- Create Date:    12:09:40 05/18/2017 
-- Design Name: 
-- Module Name:    ControlUnit_FSM - Behavioral 
-- Project Name: 
-- Target Devices: 
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
use work.myTypes.all;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity ControlUnit_FSM is
--trs iofk
generic (
    	MICROCODE_MEM_LENGHT : integer := 19;  
	
    	CW_SIZE : integer := 13;  --ciao emanuele
		--questa riga essere bastarda
	ALU_nWIRE_OP : integer := 2); --malvagita al massimo
	-- non ne parliamo proprio
	--cena
  port (
	
    	Clk : in std_logic;  
    	Rst : in std_logic;  
          OPCODE : in  std_logic_vector(OP_CODE_SIZE - 1 downto 0);
          FUNC   : in  std_logic_vector(FUNC_SIZE - 1 downto 0);

    
	EN1 : out std_logic;  
	RF1 : out std_logic;  
	RF2 : out std_logic;  		--sono felice
	-- che rumore fa la happiness 
	--terza riga di commento
	WF1 : out std_logic;  		--NO
    	
    	EN2 : out std_logic;  
    	S1 : out std_logic; 
    	S2 : out std_logic; 
    	
    	EN3 : out std_logic; 
    	RM : out std_logic;   
    	WM : out std_logic;  -- write in of memory enable
    	S3 : out std_logic;
		ALU : out std_logic_vector(ALU_nWIRE_OP -1 downto 0)  );  -- ma se io scrivo qui?
		--e poi qui?
		--ALESSANDRO

end ControlUnit_FSM;

architecture Behavioral of ControlUnit_FSM is
	
	type State is (RESET, DECODE, EXEC_I, EXEC_R, MEMORY_I, WB, WAIT1, WAIT2);
	
	signal CurrentState, NextState : State;
	signal cw : std_logic_vector(CW_SIZE-1 downto 0);
begin
	
	state_process: process(clk, rst)
	begin
		if(rst='0') then		--asyncronous reset
			CurrentState <= RESET;
		elsif(clk = '1') then
			CurrentState <= NextState;
		end if;
	end process;
	
	combinational_process: process(CurrentState)
	begin
		case CurrentState is
			WHEN RESET => cw <= (others => '0');
				    NextState <= DECODE;
			WHEN DECODE => 
					cw(CW_SIZE-5 downto 0) <= (others => '0');
					if(OPCODE = RTYPE) then
						NextState <= EXEC_R;
						cw(CW_SIZE-1 downto CW_SIZE-4) <= "1101";
					else
						case OPCODE is
							when ITYPE_ADDI1 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "0101";
								NextState <= EXEC_I;
							when ITYPE_SUBI1 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "0101";
								NextState <= EXEC_I;
							when ITYPE_ANDI1 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "0101";
								NextState <= EXEC_I;
							when ITYPE_ORI1 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "0101";
								NextState <= EXEC_I;
							when ITYPE_ADDI2 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "1001";
								NextState <= EXEC_I;
							when ITYPE_SUBI2 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "1001";
								NextState <= EXEC_I;
							when ITYPE_ANDI2 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "1001";
								NextState <= EXEC_I;
							when ITYPE_ORI2 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "1001";
								NextState <= EXEC_I;
							when ITYPE_MOV => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "1001";
								NextState <= EXEC_I;
							when ITYPE_S_REG1 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "0001";
								NextState <= EXEC_I;
							when ITYPE_S_REG2 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "0001";
								NextState <= EXEC_I;
							when ITYPE_S_MEM2 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "1101";
								NextState <= EXEC_I;
							when ITYPE_L_MEM1 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "0101";
								NextState <= EXEC_I;
							when ITYPE_L_MEM2 => 
								cw(CW_SIZE-1 downto CW_SIZE-4) <= "1001";
								NextState <= EXEC_I;
							when others => NextState <= WAIT2;
								     cw <= (others => '0');
						end case;		
					end if;
			WHEN EXEC_I => case OPCODE is
					when ITYPE_ADDI1 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "00";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
						NextState <= WB;
					when ITYPE_SUBI1 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "00";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "011";
						NextState <= WB;
					when ITYPE_ANDI1 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "00";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "101";
						NextState <= WB;
					when ITYPE_ORI1 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "00";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "111";
						NextState <= WB;
					when ITYPE_ADDI2 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "11";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
						NextState <= WB;
					when ITYPE_SUBI2 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "11";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "011";
						NextState <= WB;
					when ITYPE_ANDI2 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "11";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "101";
						NextState <= WB;
					when ITYPE_ORI2 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "11";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "111";
						NextState <= WB;
					when ITYPE_MOV => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "11";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
						NextState <= WB;
					when ITYPE_S_REG1 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "00";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
						NextState <= WB;
					when ITYPE_S_REG2 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "11";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
						NextState <= WB;
					when ITYPE_S_MEM2 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "11";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
						NextState <= MEMORY_I;
					when ITYPE_L_MEM1 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "00";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
						NextState <= MEMORY_I;
					when ITYPE_L_MEM2 => 
						cw(CW_SIZE-5 downto CW_SIZE-6) <= "11";
						cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
						NextState <= MEMORY_I;
					when others => cw <= (others => '0');
						     NextState <= WAIT1;
				end case;
			WHEN EXEC_R => 
					cw(CW_SIZE-5 downto CW_SIZE-6) <= "10"; --control signals for muxs
					case FUNC is
						when RTYPE_ADD =>
							cw(CW_SIZE-7 downto CW_SIZE-9) <= "001";
							NextState <= WB;
						when RTYPE_SUB =>
							cw(CW_SIZE-7 downto CW_SIZE-9) <= "011";
							NextState <= WB;
						when RTYPE_AND =>
							cw(CW_SIZE-7 downto CW_SIZE-9) <= "101";
							NextState <= WB;
						when RTYPE_OR =>
							cw(CW_SIZE-7 downto CW_SIZE-9) <= "111";
							NextState <= WB;
--						when NOP =>
--							cw(CW_SIZE-7 downto CW_SIZE-9) <= "000";
--							NextState <= WB;
--							--alu output register disabled
						when others => 
							cw(CW_SIZE-7 downto CW_SIZE-9) <= "000";
							NextState <= WAIT1;
							--same behaviour NOP
					end case;
			WHEN MEMORY_I => 
					cw(CW_SIZE-1 downto CW_SIZE-2) <= (others => '0');
					cw(CW_SIZE-4 downto CW_SIZE-9) <= (others => '0');
					case OPCODE is
						when ITYPE_S_MEM2 => 
							cw(CW_SIZE-10 downto CW_SIZE-13) <= "0110"; 
							cw(CW_SIZE-3) <= '0';
							NextState <= DECODE;
						when ITYPE_L_MEM1 => 
							cw(CW_SIZE-10 downto CW_SIZE-13) <= "1010"; 
							cw(CW_SIZE-3) <= '1';
							NextState <= DECODE;
						when ITYPE_L_MEM2 => 
							cw(CW_SIZE-10 downto CW_SIZE-13) <= "1010"; 
							cw(CW_SIZE-3) <= '1';
							NextState <= DECODE;
						when others =>
							cw(CW_SIZE-10 downto CW_SIZE-13) <= "0000"; 
							cw(CW_SIZE-3) <= '0';
							NextState <= DECODE;
					end case;
					
			WHEN WB => 
					cw(CW_SIZE-10 downto CW_SIZE-13) <= "0001"; 
					cw(CW_SIZE-3) <= '1';
					cw(CW_SIZE-1 downto CW_SIZE-2) <= (others => '0');
					cw(CW_SIZE-4 downto CW_SIZE-9) <= (others => '0');
					NextState <= DECODE;
			WHEN WAIT2 =>
					NextState <= WAIT1;
			WHEN WAIT1 =>
					NextState <= DECODE;
			WHEN OTHERS => 
					cw <= (others => '0');
					NextState <= DECODE;
		end case;
	end process;

	RF1 <= cw(CW_SIZE-1);
	RF2 <= cw(CW_SIZE-2);
	WF1 <= cw(CW_SIZE-3);
	EN1 <= cw(CW_SIZE-4);
	
	S1 <= cw(CW_SIZE-5);
	S2 <= cw(CW_SIZE-6);
	ALU(1) <= cw(CW_SIZE-7);
	ALU(0) <= cw(CW_SIZE-8);
    	EN2 <= cw(CW_SIZE-9);
	
	RM <= cw(CW_SIZE-10);
    	WM <= cw(CW_SIZE-11);
    	EN3 <= cw(CW_SIZE-12);
    	S3 <= cw(CW_SIZE-13);
	
	
end Behavioral;

