package ds.mods.worldscript.luaapi.methods;

import cpw.mods.fml.common.FMLLog;
import ds.mods.worldscript.luaapi.objects.EntityPlayerObject;
import ds.mods.worldscript.luaapi.objects.InventoryObject;
import ds.mods.worldscript.luaapi.tools.LuaMethod;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;

public class EntityPlayerMethods extends EntityMethods {
	@LuaMethod
	public LuaValue getInventory(EntityPlayerObject obj)
	{
		FMLLog.info("Getting inv");
		return new InventoryObject(obj.player.inventory);
	}
	
	@LuaMethod
	public LuaValue getEnderInventory(EntityPlayerObject obj)
	{
		FMLLog.info("Getting einv");
		return new InventoryObject(obj.player.getInventoryEnderChest());
	}
}
