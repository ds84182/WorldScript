package ds.mods.worldscript.client;

import java.util.List;

import ds.mods.worldscript.WorldScript;
import ds.mods.worldscript.BlockRegistry.RegisteredBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabRegistered extends CreativeTabs {

	public CreativeTabRegistered(String label) {
		super(label);
	}

	@Override
	public void displayAllReleventItems(List par1List) {
		for (RegisteredBlock r : WorldScript.c_br.blocks)
		{
			par1List.add(new ItemStack(WorldScript.scriptedBlock,1,WorldScript.c_br.blocks.indexOf(r)));
		}
	}

}
