package ds.mods.worldscript;

import ds.mods.worldscript.BlockRegistry.RegisteredBlock;
import net.minecraft.tileentity.TileEntity;

public class TileEntityScripted extends TileEntity {
	public RegisteredBlock block = WorldScript.c_br.blocks.get(0);
	public int unregisteredFor = 0;

	@Override
	public void updateEntity() {
		if (!block.isRegistered)
		{
			unregisteredFor++;
			if (worldObj.isRemote)
			{
				for (RegisteredBlock r : WorldScript.c_br.blocks)
				{
					if (r.name.equals(block.name))
					{
						block = r;
					}
				}
			}
			else
			{
				for (RegisteredBlock r : WorldScript.s_br.blocks)
				{
					if (r.name.equals(block.name))
					{
						block = r;
					}
				}
			}
		}
		else
		{
			unregisteredFor = 0;
			//TODO: Run luafunction here
		}
	}
}
