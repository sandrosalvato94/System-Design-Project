LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY b12to1MUX IS
	PORT(S : IN STD_LOGIC;
          IN1: IN STD_LOGIC;
          IN2: IN STD_LOGIC;
			M   : OUT STD_LOGIC);
END b12to1MUX;

ARCHITECTURE behavior OF b12to1MUX IS
BEGIN
	M<=(IN1 AND not(S)) OR (IN2 AND S);
END behavior;
			