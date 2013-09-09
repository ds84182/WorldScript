package ds.mods.worldscript.luaapi;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ds.mods.worldscript.luaapi.objects.ItemStackObject;
import ds.mods.worldscript.luaapi.objects.NBTCompoundObject;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.Varargs;
import ds.mods.worldscript.org.luaj.vm2.lib.OneArgFunction;
import ds.mods.worldscript.org.luaj.vm2.lib.VarArgFunction;

public class ItemStackLib extends OneArgFunction {

	@Override
	public LuaValue call(LuaValue arg) {
		LuaTable tab = new LuaTable();
		
		tab.set("new", new newItemStack());
		
		env.set("itemstack",tab);
		return tab;
	}
	
	public class newItemStack extends VarArgFunction
	{

		@Override
		public Varargs invoke(Varargs args) {
			int itemID = args.checkint(1);
			int stackSize = args.isnil(2) ? 1 : args.checkint(2);
			short stackDamage = args.isnil(3) ? 0 : args.toshort(3);
			NBTTagCompound nbt = args.arg(4) instanceof NBTCompoundObject ? ((NBTCompoundObject)args.arg(4)).nbt : new NBTTagCompound();
			ItemStack item = new ItemStack(itemID, stackSize, stackDamage);
			item.setTagCompound(nbt);
			return new ItemStackObject(item);
		}
		
	}

}
