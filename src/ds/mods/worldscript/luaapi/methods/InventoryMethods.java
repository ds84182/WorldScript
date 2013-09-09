package ds.mods.worldscript.luaapi.methods;

import ds.mods.worldscript.luaapi.objects.InventoryObject;
import ds.mods.worldscript.luaapi.objects.ItemStackObject;
import ds.mods.worldscript.luaapi.tools.LuaMethod;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;

public class InventoryMethods {
	@LuaMethod
	public LuaValue getItem(InventoryObject obj, LuaValue v)
	{
		return new ItemStackObject(obj.inv.getStackInSlot(v.checkint()));
	}
	
	@LuaMethod
	public void setItem(InventoryObject obj, LuaValue slot, LuaValue item)
	{
		if (item instanceof ItemStackObject)
		{
			obj.inv.setInventorySlotContents(slot.checkint(), ((ItemStackObject) item).item);
			System.out.println("Set inv");
		}
		else
		{
			item.typerror("itemstack");
		}
	}
}
