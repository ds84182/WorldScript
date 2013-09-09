package ds.mods.worldscript.events;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import ds.mods.worldscript.EventContainer;

public class ForgeEventContainer extends EventContainer {
	public ForgeEventContainer()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
}
