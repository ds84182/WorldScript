package ds.mods.worldscript.luaapi.objects;

import net.minecraft.item.ItemStack;
import ds.mods.worldscript.luaapi.methods.ItemStackMethods;
import ds.mods.worldscript.luaapi.tools.LuaBind;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;

public class ItemStackObject extends LuaBind<ItemStackMethods> {
	public ItemStack item;
	
	public ItemStackObject(ItemStack i)
	{
		item = i;
		obj = new ItemStackMethods();
	}

	@Override
	public int type() {
		return TUSERDATA;
	}

	@Override
	public String typename() {
		return "itemstack";
	}
	
	@Override
	public String tojstring() {
		return "itemstack "+item.getDisplayName();
	}
	
	@Override
	public LuaValue get(LuaValue key) {
		synchronized(item)
		{
			return super.get(key);
		}
	}
}
