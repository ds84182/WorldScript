package ds.mods.worldscript.luaapi.objects;

import java.util.HashMap;

import net.minecraft.world.World;
import ds.mods.worldscript.luaapi.methods.WorldMethods;
import ds.mods.worldscript.luaapi.tools.LuaBind;
import ds.mods.worldscript.org.luaj.vm2.LuaFunction;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.lib.jse.JavaInstance;

public class WorldObject extends LuaBind<WorldMethods> {
	public World world;
	
	public WorldObject(World w)
	{
		world = w;
		obj = new WorldMethods();
	}

	@Override
	public int type() {
		return TUSERDATA;
	}

	@Override
	public String typename() {
		return "world";
	}
	
	@Override
	public String tojstring() {
		return "world "+world.provider.dimensionId;
	}

	@Override
	public LuaValue get(LuaValue key) {
		synchronized(world)
		{
			return super.get(key);
		}
	}
	
}
