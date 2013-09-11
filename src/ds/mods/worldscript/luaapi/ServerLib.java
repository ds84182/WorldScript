package ds.mods.worldscript.luaapi;

import java.util.Iterator;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import ds.mods.worldscript.luaapi.ResLib.preload;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.lib.OneArgFunction;

public class ServerLib extends OneArgFunction {

	@Override
	public LuaValue call(LuaValue arg) {
		LuaTable tab = new LuaTable();
		
		tab.set("sendChat",new sendChat());
		
		env.set("server",tab);
		return tab;
	}
	
	public class sendChat extends OneArgFunction
	{

		@Override
		public LuaValue call(LuaValue arg) {
			MinecraftServer.getServer().sendChatToPlayer(ChatMessageComponent.createFromText(arg.checkjstring()));
			Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

            while (iterator.hasNext())
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
                entityplayermp.sendChatToPlayer(ChatMessageComponent.createFromText(arg.checkjstring()));
            }
			return null;
		}
		
	}

}
