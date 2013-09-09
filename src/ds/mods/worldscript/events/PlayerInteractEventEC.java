package ds.mods.worldscript.events;

import static ds.mods.worldscript.org.luaj.vm2.LuaValue.valueOf;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import ds.mods.worldscript.luaapi.objects.EntityPlayerObject;
import ds.mods.worldscript.luaapi.objects.WorldObject;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;

public class PlayerInteractEventEC extends ForgeEventContainer {
	@ForgeSubscribe
	public void interactEvent(PlayerInteractEvent event)
	{
		//TODO: Event cancelation
		invoke(LuaValue.varargsOf(new LuaValue[]{
				new EntityPlayerObject(event.entityPlayer),
				new WorldObject(event.entity.worldObj),
				valueOf(event.action.name()),
				valueOf(event.x),
				valueOf(event.y),
				valueOf(event.z),
				valueOf(event.face),
		}));
	}
}
