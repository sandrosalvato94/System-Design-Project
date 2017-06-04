library ieee;
use ieee.std_logic_1164.all;
use work.CONSTANTS.all;

entity TOP_ENTITY is
	port(	
			clock			: in std_logic;	
			reset			: in std_logic;		
			data			: inout std_logic_vector (DATA_WIDTH-1 downto 0);
			address	        : in std_logic_vector(ADD_WIDTH-1 downto 0);
			W_enable  		: in std_logic;
			R_enable  		: in std_logic;
			generic_enable	: in std_logic;
			interrupt		: out std_logic
			);
end TOP_ENTITY;

architecture STRUCTURAL of TOP_ENTITY is

	signal	row_0			  	: std_logic_vector (DATA_WIDTH-1 downto 0); 
	signal	data_in_ip		  	: std_logic_vector (DATA_WIDTH-1 downto 0);
	signal	data_out_ip			: std_logic_vector (DATA_WIDTH-1 downto 0);	
	signal	address_ip     		: std_logic_vector (ADD_WIDTH-1 downto 0);
	signal	WE_IP				: std_logic;
	signal	RE_IP				: std_logic;
	signal	GE_IP				: std_logic;
	signal	data_in_IPs     	: data_array; 
	signal	data_out_IPs    	: data_array;     
	signal	add_IPs         	: add_array;            
	signal	W_enable_IPs    	: std_logic_vector(0 to NUM_IPS-1);    
	signal	R_enable_IPs    	: std_logic_vector(0 to NUM_IPS-1);                
	signal	generic_en_IPs  	: std_logic_vector(0 to NUM_IPS-1);    
	signal	enable_IPs      	: std_logic_vector(0 to NUM_IPS-1);    
	signal	ack_IPs         	: std_logic_vector(0 to NUM_IPS-1);    
	signal	interrupt_IPs   	: std_logic_vector(0 to NUM_IPS-1);    
	
begin

	
	data_buff: DATA_BUFFER
		port map(	rst				=>	reset,
					row_0			=>	row_0,
					--PORT_0
					data_cpu		=>	data,
					address_cpu		=>	address,
					WE_CPU	 		=>	W_enable,
					RE_CPU	 		=>	R_enable,
					GE_CPU			=>	generic_enable,
					--PORT_1
					data_in_ip		=>	data_in_ip,
					data_out_ip		=>	data_out_ip,
					address_ip      =>	address_ip,
					WE_IP			=>	WE_IP,
					RE_IP			=>	RE_IP,
					GE_IP			=>	GE_IP);
	