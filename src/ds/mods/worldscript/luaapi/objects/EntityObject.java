package ds.mods.worldscript.luaapi.objects;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import ds.mods.worldscript.luaapi.methods.EntityMethods;
import ds.mods.worldscript.luaapi.tools.LuaBind;
import ds.mods.worldscript.org.luaj.vm2.LuaFunction;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.lib.jse.JavaInstance;

public class EntityObject extends LuaBind<EntityMethods> {
	public Entity entity;
	
	public EntityObject(Entity e)
	{
		entity = e;
		obj = new EntityMethods();
		
		String typ = EntityList.getEntityString(e);
		set("type",typ != null ? typ : "");
	}

	@Override
	public int type() {
		return TUSERDATA;
	}

	@Override
	public String typename() {
		return "entity";
	}
	
	@Override
	public String tojstring() {
		return "entity "+entity.entityId;
	}
	
	@Override
	public LuaValue get(LuaValue key) {
		synchronized(entity)
		{
			return super.get(key);
		}
	}
}
