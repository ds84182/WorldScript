package ds.mods.worldscript.luaapi.methods;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatMessageComponent;
import cpw.mods.fml.common.FMLLog;
import ds.mods.worldscript.luaapi.objects.EntityPlayerObject;
import ds.mods.worldscript.luaapi.objects.InventoryObject;
import ds.mods.worldscript.luaapi.tools.LuaMethod;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import static ds.mods.worldscript.org.luaj.vm2.LuaValue.*;

public class EntityPlayerMethods extends EntityMethods {
	@LuaMethod
	public LuaValue getInventory(EntityPlayerObject obj)
	{
		//FMLLog.info("Getting inv");
		return new InventoryObject(obj.player.inventory);
	}
	
	@LuaMethod
	public LuaValue getEnderInventory(EntityPlayerObject obj)
	{
		//FMLLog.info("Getting einv");
		return new InventoryObject(obj.player.getInventoryEnderChest());
	}
	
	@LuaMethod
	public void sendChat(EntityPlayerObject obj, LuaValue str)
	{
		if (obj.player instanceof EntityPlayerMP)
		{
			obj.player.sendChatToPlayer(ChatMessageComponent.createFromText(str.checkjstring()));
		}
	}
	
	@LuaMethod
	public LuaValue getUsername(EntityPlayerObject obj)
	{
		return valueOf(obj.player.username);
	}
}
