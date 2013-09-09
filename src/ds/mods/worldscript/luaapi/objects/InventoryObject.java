package ds.mods.worldscript.luaapi.objects;

import net.minecraft.inventory.IInventory;
import ds.mods.worldscript.luaapi.methods.InventoryMethods;
import ds.mods.worldscript.luaapi.tools.LuaBind;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;

public class InventoryObject extends LuaBind<InventoryMethods> {
	public IInventory inv;
	
	public InventoryObject(IInventory i)
	{
		inv = i;
		obj = new InventoryMethods();
	}
	
	@Override
	public int type() {
		return TUSERDATA;
	}

	@Override
	public String typename() {
		return "inventory";
	}
	
	@Override
	public String tojstring() {
		return "inventory "+inv.getInvName()+"@"+Integer.toHexString(inv.hashCode());
	}
	
	@Override
	public LuaValue get(LuaValue key) {
		synchronized(inv)
		{
			return super.get(key);
		}
	}
}
