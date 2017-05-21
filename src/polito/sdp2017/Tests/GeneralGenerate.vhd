
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity GeneralGenerate is
    Port ( G_ik : in  STD_LOGIC;
           P_ik : in  STD_LOGIC;
           G_km1_j : in  STD_LOGIC;
           G_ij : out  STD_LOGIC);
end GeneralGenerate;

architecture Behavioral of GeneralGenerate is

begin

	G_ij <= G_ik OR (P_ik AND G_km1_j);

end Behavioral;

