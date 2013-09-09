package ds.mods.worldscript.luaapi;

import java.util.Map.Entry;

import ds.mods.worldscript.EventContainer;
import ds.mods.worldscript.WorldScript;
import ds.mods.worldscript.org.luaj.vm2.LuaFunction;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.lib.OneArgFunction;

public class EventLib extends OneArgFunction {

	@Override
	public LuaValue call(LuaValue arg) {
		LuaTable tab = new LuaTable();
		
		for (Entry<String,EventContainer> ec : WorldScript.eventContainers.entrySet())
		{
			tab.set(ec.getKey(),new EventC(ec.getValue()));
		}
		
		env.set("event",tab);
		return tab;
	}
	
	public class EventC extends LuaTable
	{
		EventContainer ec;
		public EventC(EventContainer e)
		{
			ec = e;
			set("connect",new connect());
		}

		@Override
		public LuaValue call(LuaValue arg) {
			return get("connect").call(arg);
		}
		
		public class connect extends OneArgFunction
		{

			@Override
			public LuaValue call(LuaValue arg) {
				ec.addHandler(arg.checkclosure());
				return NIL;
			}
			
		}
		
	}

}
