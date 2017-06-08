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

	component DATA_BUFFER is
	port(	
 
		rst              : in std_logic;		
		row_0		     : out std_logic_vector (DATA_WIDTH-1 downto 0); -- First line of the buffer. Must be read constantly by the ip manager
		--PORT_0
		data_cpu	     : inout std_logic_vector (DATA_WIDTH-1 downto 0);
		address_cpu      : in std_logic_vector(ADD_WIDTH-1 downto 0);
		WE_CPU 		     : in std_logic;
		RE_CPU 		     : in std_logic;
		GE_CPU		      : in std_logic;
		
		--PORT_1

		data_in_ip	  	: in std_logic_vector (DATA_WIDTH-1 downto 0);
		data_out_ip		: out std_logic_vector (DATA_WIDTH-1 downto 0);
		address_ip     	: in std_logic_vector(ADD_WIDTH-1 downto 0);
		WE_IP 			: in std_logic;
		RE_IP  			: in std_logic;
		GE_IP			: in std_logic

		);
	end component DATA_BUFFER;
	

component ip_manager is
	port (
		clk : in std_logic;
		rst : in std_logic;
		data_in : out std_logic_vector(data_width-1 downto 0);
		data_out : in std_logic_vector(data_width-1 downto 0);
		add : out std_logic_vector(add_width-1 downto 0);
		w_enable : out std_logic;
		r_enable : out std_logic;
		generic_en : out std_logic;
		interrupt : out std_logic;
		row_0 : in std_logic_vector(data_width-1 downto 0);
		data_in_ips : in data_array;
		data_out_ips : out data_array;
		add_ips : in add_array;
		w_enable_ips : in std_logic_vector(0 to num_ips-1);
		r_enable_ips : in std_logic_vector(0 to num_ips-1);
		generic_en_ips : in std_logic_vector(0 to num_ips-1);
		enable_ips : out std_logic_vector(0 to num_ips-1);
		ack_ips : out std_logic_vector(0 to num_ips-1);
		interrupt_ips : in std_logic_vector(0 to num_ips-1));
end component ip_manager;

component ip_adder is
	port (
		clk : in std_logic;
		rst : in std_logic;
		data_in : out std_logic_vector(data_width-1 downto 0);
		data_out : in std_logic_vector(data_width-1 downto 0);
		address : out std_logic_vector(add_width-1 downto 0);
		w_enable : out std_logic;
		r_enable : out std_logic;
		generic_en : out std_logic;
		enable : in std_logic;
		ack : in std_logic;
		interrupt : out std_logic);
end component ip_adder;


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
	
	
ip_man: ip_manager
		port map(	clk 			=>	clock,
					rst				=>	reset,
					data_in			=>	data_in_ip,
					data_out		=>	data_out_ip,				
					add           	=>	address_ip,
					W_enable		=>	WE_IP,
					R_enable		=>	RE_IP,
					generic_en		=>	GE_IP,
					interrupt		=>  interrupt,
					row_0			=>	row_0,
					data_in_IPs		=>	data_in_IPs,
					data_out_IPs	=>	data_out_IPs,				
					add_IPs		    =>	add_IPs,
					W_enable_IPs	=>	W_enable_IPs,
					R_enable_IPs  	=>	R_enable_IPs,			
					generic_en_IPs	=>	generic_en_IPs,
					enable_IPs		=>	enable_IPs,
					ack_IPs			=>	ack_IPs,
					interrupt_IPs	=>	interrupt_IPs);
	

mapIP_0: IP_Adder
	PORT MAP(
		clk	=> clock,
		rst	=> reset,
		data_in	=> data_in_IPs(0),
		data_out	=> data_out_IPs(0),
		address	=> add_IPs(0),
		W_enable	=> W_enable_IPs(0),
		R_enable	=> R_enable_IPs(0),
		generic_en	=> generic_en_IPs(0),
		enable	=> enable_IPs(0),
		ack	=> ack_IPs(0),
		interrupt	=> interrupt_IPs(0));

end architecture;