package ds.mods.worldscript.luaapi;

import ds.mods.worldscript.luaapi.ResLib.preload;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.lib.OneArgFunction;

public class ServerLib extends OneArgFunction {

	@Override
	public LuaValue call(LuaValue arg) {
		LuaTable tab = new LuaTable();
		
		env.set("server",tab);
		return tab;
	}

}
