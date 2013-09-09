package ds.mods.worldscript.luaapi.objects;

import net.minecraft.nbt.NBTTagCompound;
import ds.mods.worldscript.luaapi.methods.NBTCompoundMethods;
import ds.mods.worldscript.luaapi.tools.LuaBind;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;

public class NBTCompoundObject extends LuaBind<NBTCompoundMethods> {
	public NBTTagCompound nbt;
	
	public NBTCompoundObject(NBTTagCompound n)
	{
		nbt = n;
		obj = new NBTCompoundMethods();
	}

	@Override
	public int type() {
		return TUSERDATA;
	}

	@Override
	public String typename() {
		return "nbt";
	}

	@Override
	public LuaValue get(LuaValue key) {
		synchronized(nbt)
		{
			return super.get(key);
		}
	}
}
