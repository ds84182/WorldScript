package ds.mods.worldscript.luaapi.tools;

import ds.mods.worldscript.luaapi.objects.NBTCompoundObject;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.Varargs;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class NBTTools {
	public static void setTag(NBTTagCompound nbt,String key, LuaValue value)
	{
		System.out.println("Setting "+key);
		switch(value.type())
		{
		case LuaValue.TBOOLEAN:
			nbt.setBoolean(key, value.checkboolean());
			break;
		case LuaValue.TINT:
			nbt.setInteger(key, value.checkint());
			break;
		case LuaValue.TNUMBER:
			nbt.setFloat(key, value.tofloat());
			break;
		case LuaValue.TSTRING:
			nbt.setString(key, value.checkjstring());
			break;
		case LuaValue.TTABLE:
			nbt.setTag(key, createNBT(key,value.checktable()).nbt);
		}
	}
	
	public static LuaValue getTag(NBTTagCompound nbt, String key)
	{
		NBTBase base = nbt.getTag(key);
		if (base instanceof NBTTagByte)
		{
			return LuaValue.valueOf(((NBTTagByte)base).data);
		}
		else if (base instanceof NBTTagByteArray)
		{
			LuaTable t = new LuaTable();
			byte[] array = ((NBTTagByteArray)base).byteArray;
			for (int i = 0; i<array.length; i++)
			{
				t.set(i+1, LuaValue.valueOf(array[i]));
			}
			return t;
		}
		else if (base instanceof NBTTagCompound)
		{
			return new NBTCompoundObject((NBTTagCompound) base);
		}
		else if (base instanceof NBTTagDouble)
		{
			return LuaValue.valueOf(((NBTTagDouble)base).data);
		}
		else if (base instanceof NBTTagFloat)
		{
			return LuaValue.valueOf(((NBTTagFloat)base).data);
		}
		else if (base instanceof NBTTagInt)
		{
			return LuaValue.valueOf(((NBTTagInt)base).data);
		}
		else if (base instanceof NBTTagIntArray)
		{
			LuaTable t = new LuaTable();
			int[] array = ((NBTTagIntArray)base).intArray;
			for (int i = 0; i<array.length; i++)
			{
				t.set(i+1, LuaValue.valueOf(array[i]));
			}
			return t;
		}
		else if (base instanceof NBTTagLong)
		{
			return LuaValue.valueOf(((NBTTagLong)base).data);
		}
		else if (base instanceof NBTTagShort)
		{
			return LuaValue.valueOf(((NBTTagShort)base).data);
		}
		else if (base instanceof NBTTagString)
		{
			return LuaValue.valueOf(((NBTTagString)base).data);
		}
		return LuaValue.NIL;
	}
	
	public static NBTBase serialize(String n, LuaValue v)
	{
		switch(v.type())
		{
		case LuaValue.TBOOLEAN:
			return new NBTTagByte(n, (byte) (v.toboolean() ? 1 : 0));
		case LuaValue.TINT:
			return new NBTTagInt(n, v.checkint());
		case LuaValue.TNUMBER:
			return new NBTTagFloat(n,v.tofloat());
		case LuaValue.TSTRING:
			return new NBTTagString(n,v.checkjstring());
		case LuaValue.TTABLE:
			return createNBT(n,v.checktable()).nbt;
		}
		return null;
	}
	
	public static NBTCompoundObject createNBT(String n,LuaTable t)
	{
		NBTTagCompound nbt = new NBTTagCompound(n);
		for (LuaValue key : t.keys())
		{
			setTag(nbt, key.checkjstring(), t.get(key));
		}
		return new NBTCompoundObject(nbt);
	}
}
