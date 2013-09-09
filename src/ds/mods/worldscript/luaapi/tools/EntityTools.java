package ds.mods.worldscript.luaapi.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ds.mods.worldscript.luaapi.objects.EntityObject;
import ds.mods.worldscript.luaapi.objects.EntityPlayerObject;

public class EntityTools {
	public static EntityObject createEntity(Entity e)
	{
		if (e instanceof EntityPlayer)
			return new EntityPlayerObject(e);
		else
			return new EntityObject(e);
	}
}
