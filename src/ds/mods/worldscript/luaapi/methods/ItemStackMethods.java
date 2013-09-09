package ds.mods.worldscript.luaapi.methods;

import ds.mods.worldscript.luaapi.objects.ItemStackObject;
import ds.mods.worldscript.luaapi.objects.NBTCompoundObject;
import ds.mods.worldscript.luaapi.tools.LuaMethod;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import static ds.mods.worldscript.org.luaj.vm2.LuaValue.*;

public class ItemStackMethods {
	@LuaMethod
	public LuaValue getID(ItemStackObject obj)
	{
		return valueOf(obj.item.itemID);
	}
	
	@LuaMethod
	public LuaValue getStackSize(ItemStackObject obj)
	{
		return valueOf(obj.item.stackSize);
	}
	
	@LuaMethod
	public LuaValue getDamage(ItemStackObject obj)
	{
		return valueOf(obj.item.getItemDamage());
	}
	
	@LuaMethod
	public LuaValue getNBT(ItemStackObject obj)
	{
		return new NBTCompoundObject(obj.item.getTagCompound());
	}
	
	@LuaMethod
	public LuaValue getName(ItemStackObject obj)
	{
		return valueOf(obj.item.getDisplayName());
	}
	
	@LuaMethod
	public LuaValue copy(ItemStackObject obj)
	{
		return new ItemStackObject(obj.item.copy());
	}
	
	@LuaMethod
	public LuaValue getMaxStackSize(ItemStackObject obj)
	{
		return valueOf(obj.item.getMaxStackSize());
	}
	
	@LuaMethod
	public LuaValue getMaxDamage(ItemStackObject obj)
	{
		return valueOf(obj.item.getMaxDamage());
	}
	
	@LuaMethod
	public void setID(ItemStackObject obj, LuaValue v)
	{
		obj.item.itemID = v.checkint();
	}
	
	@LuaMethod
	public void setStackSize(ItemStackObject obj, LuaValue v)
	{
		obj.item.stackSize = v.checkint();
	}
	
	@LuaMethod
	public void setDamage(ItemStackObject obj, LuaValue v)
	{
		obj.item.setItemDamage(v.checkint());
	}
	
	@LuaMethod
	public void setNBT(ItemStackObject obj, LuaValue v)
	{
		if (v instanceof NBTCompoundObject)
		{
			obj.item.setTagCompound(((NBTCompoundObject) v).nbt);
		}
		else
		{
			v.typerror("nbt");
		}
	}
	
	@LuaMethod
	public void setName(ItemStackObject obj, LuaValue v)
	{
		obj.item.setItemName(v.checkjstring());
	}
}
