package ds.mods.worldscript.luaapi;

import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import ds.mods.worldscript.EventContainer;
import ds.mods.worldscript.WorldScript;
import ds.mods.worldscript.luaapi.EventLib.EventC;
import ds.mods.worldscript.luaapi.objects.NBTCompoundObject;
import ds.mods.worldscript.luaapi.tools.NBTTools;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.lib.OneArgFunction;
import ds.mods.worldscript.org.luaj.vm2.lib.ZeroArgFunction;

public class NBTLib extends OneArgFunction {

		@Override
		public LuaValue call(LuaValue arg) {
			LuaTable tab = new LuaTable();
			
			tab.set("new", new newNBT());
			tab.set("createFromTable", new createFromTable());
			
			env.set("nbt",tab);
			return tab;
		}
		
		public class createFromTable extends OneArgFunction
		{

			@Override
			public LuaValue call(LuaValue arg) {
				return NBTTools.createNBT("", arg.checktable());
			}
			
		}
		
		public class newNBT extends ZeroArgFunction
		{

			@Override
			public LuaValue call() {
				return new NBTCompoundObject(new NBTTagCompound());
			}
			
		}

}
