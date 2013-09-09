package ds.mods.worldscript.luaapi.methods;

import ds.mods.worldscript.luaapi.objects.NBTCompoundObject;
import ds.mods.worldscript.luaapi.tools.LuaMethod;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import static ds.mods.worldscript.org.luaj.vm2.LuaValue.*;

public class NBTCompoundMethods {
	@LuaMethod
	public LuaValue getBoolean(NBTCompoundObject obj, LuaValue v)
	{
		return valueOf(obj.nbt.getBoolean(v.checkjstring()));
	}
	
	@LuaMethod
	public LuaValue getByte(NBTCompoundObject obj, LuaValue v)
	{
		return valueOf(obj.nbt.getByte(v.checkjstring()));
	}
	
	@LuaMethod
	public LuaValue getByteArray(NBTCompoundObject obj, LuaValue v)
	{
		LuaTable tab = new LuaTable();
		byte[] array = obj.nbt.getByteArray(v.checkjstring());
		for (int i = 0; i<array.length; i++)
		{
			tab.set(i, valueOf(array[i]));
		}
		return tab;
	}
	
	@LuaMethod
	public LuaValue getCompoundTag(NBTCompoundObject obj, LuaValue v)
	{
		return new NBTCompoundObject(obj.nbt.getCompoundTag(v.checkjstring()));
	}
	
	@LuaMethod
	public LuaValue getDouble(NBTCompoundObject obj, LuaValue v)
	{
		return valueOf(obj.nbt.getDouble(v.checkjstring()));
	}
	
	@LuaMethod
	public LuaValue getFloat(NBTCompoundObject obj, LuaValue v)
	{
		return valueOf(obj.nbt.getFloat(v.checkjstring()));
	}
	
	@LuaMethod
	public LuaValue getIntArray(NBTCompoundObject obj, LuaValue v)
	{
		LuaTable tab = new LuaTable();
		int[] array = obj.nbt.getIntArray(v.checkjstring());
		for (int i = 0; i<array.length; i++)
		{
			tab.set(i, valueOf(array[i]));
		}
		return tab;
	}
	
	@LuaMethod
	public LuaValue getInteger(NBTCompoundObject obj, LuaValue v)
	{
		return valueOf(obj.nbt.getInteger(v.checkjstring()));
	}
	
	@LuaMethod
	public LuaValue getLong(NBTCompoundObject obj, LuaValue v)
	{
		return valueOf(obj.nbt.getLong(v.checkjstring()));
	}
	
	@LuaMethod
	public LuaValue getShort(NBTCompoundObject obj, LuaValue v)
	{
		return valueOf(obj.nbt.getShort(v.checkjstring()));
	}
	
	@LuaMethod
	public LuaValue getString(NBTCompoundObject obj, LuaValue v)
	{
		return valueOf(obj.nbt.getString(v.checkjstring()));
	}
	
	//TODO: Tag lists
	
	@LuaMethod
	public void setBoolean(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		obj.nbt.setBoolean(k.checkjstring(), v.checkboolean());
	}
	
	@LuaMethod
	public void setByte(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		obj.nbt.setByte(k.checkjstring(), (byte) v.checkint());
	}
	
	@LuaMethod
	public void setByteArray(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		LuaTable t = v.checktable();
		byte[] array = new byte[t.length()];
		for (int i = 0; i<array.length; i++)
		{
			array[i] = t.get(i+1).tobyte();
		}
		obj.nbt.setByteArray(k.checkjstring(), array);
	}
	
	@LuaMethod
	public void setCompoundTag(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		if (v instanceof NBTCompoundObject)
		{
			obj.nbt.setCompoundTag(k.checkjstring(), ((NBTCompoundObject)v).nbt);
		}
		else
		{
			v.typerror("nbt");
		}
	}
	
	@LuaMethod
	public void setDouble(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		obj.nbt.setDouble(k.checkjstring(), v.todouble());
	}
	
	@LuaMethod
	public void setFloat(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		obj.nbt.setFloat(k.checkjstring(), v.tofloat());
	}
	
	@LuaMethod
	public void setIntArray(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		LuaTable t = v.checktable();
		int[] array = new int[t.length()];
		for (int i = 0; i<array.length; i++)
		{
			array[i] = t.get(i+1).tobyte();
		}
		obj.nbt.setIntArray(k.checkjstring(), array);
	}
	
	@LuaMethod
	public void setInteger(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		obj.nbt.setInteger(k.checkjstring(), v.toint());
	}
	
	@LuaMethod
	public void setLong(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		obj.nbt.setLong(k.checkjstring(), v.tolong());
	}
	
	@LuaMethod
	public void setShort(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		obj.nbt.setShort(k.checkjstring(), v.toshort());
	}
	
	@LuaMethod
	public void setString(NBTCompoundObject obj, LuaValue k, LuaValue v)
	{
		obj.nbt.setString(k.checkjstring(), v.checkjstring());
	}
}
