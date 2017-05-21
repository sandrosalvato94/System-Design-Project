LIBRARY ieee;
USE ieee.std_logic_1164.all;

ENTITY b4dec10_comparator IS
PORT ( V: IN std_logic_vector (3 downto 0);
		Z: OUT std_logic);
END b4dec10_comparator;

ARCHITECTURE behavior OF b4dec10_comparator IS
BEGIN
	Z<=(V(3) AND V(2)) OR (V(3) AND V(1));
END behavior;
