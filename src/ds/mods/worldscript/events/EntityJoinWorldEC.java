package ds.mods.worldscript.events;

import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import ds.mods.worldscript.luaapi.objects.EntityObject;
import ds.mods.worldscript.luaapi.objects.WorldObject;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;

public class EntityJoinWorldEC extends ForgeEventContainer {
	@ForgeSubscribe
	public void playerJoinWorld(EntityJoinWorldEvent event)
	{
		invoke(LuaValue.varargsOf(new LuaValue[]{
				new EntityObject(event.entity),
				new WorldObject(event.world)
		}));
	}
}
