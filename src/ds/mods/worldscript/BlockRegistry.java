package ds.mods.worldscript;

import java.util.ArrayList;

public class BlockRegistry {
	//Block registry hold registered block
	//TODO: Create a block ID system for registered blocks
	public ArrayList<RegisteredBlock> blocks = new ArrayList<BlockRegistry.RegisteredBlock>();
	
	public BlockRegistry()
	{
		blocks.add(new RegisteredBlock("Lua", "./lua.png"));
	}
	
	public class RegisteredBlock
	{
		public String name;
		public String texture;
		public boolean isRegistered = false;
		
		public RegisteredBlock(String n, String t)
		{
			name = n;
			texture = t;
			isRegistered = true;
		}
	}
}
