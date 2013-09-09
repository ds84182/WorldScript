package ds.mods.worldscript.luaapi;

import java.util.HashMap;

import net.minecraftforge.common.DimensionManager;
import ds.mods.worldscript.luaapi.objects.WorldObject;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.lib.OneArgFunction;

public class DimensionLib extends OneArgFunction {

	@Override
	public LuaValue call(LuaValue arg) {
		LuaTable tab = new LuaTable();
		
		tab.set("get", new get());
		
		env.set("dim",tab);
		return tab;
	}
	
	public class get extends OneArgFunction{

		@Override
		public LuaValue call(LuaValue arg) {
			return new WorldObject(DimensionManager.getWorld(arg.checkint()));
		}
		
	}
}
