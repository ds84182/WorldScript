package ds.mods.worldscript.luaapi.methods;

import static ds.mods.worldscript.org.luaj.vm2.LuaValue.*;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import ds.mods.worldscript.luaapi.objects.*;
import ds.mods.worldscript.luaapi.tools.EntityTools;
import ds.mods.worldscript.luaapi.tools.LuaMethod;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.Varargs;

public class WorldMethods {
	@LuaMethod
	public Varargs getBlockID(WorldObject obj, Varargs args) {
		int x = args.checkint(1);
		int y = args.checkint(2);
		int z = args.checkint(3);
		return varargsOf(new LuaValue[]{valueOf(obj.world.getBlockId(x, y, z)), valueOf(obj.world.getBlockMetadata(x, y, z))});
	}

	@LuaMethod
	public void setBlockID(WorldObject obj, Varargs args) {
		int x = args.checkint(1);
		int y = args.checkint(2);
		int z = args.checkint(3);
		int id = args.checkint(4);
		int meta = 0;
		if (!args.isnil(5))
		{
			meta = args.checkint(5);
		}
		obj.world.setBlock(x, y, z, 0, 0,3);
		obj.world.setBlock(x, y, z, id,meta,3);
	}

	@LuaMethod
	public Varargs getFirstEntity(WorldObject obj,Varargs args) {
		Entity e = null;
		String id = args.checkjstring(1);
		if (EntityList.stringToClassMapping.containsKey(id))
		{
			//Entity exists
			Class clazz = (Class) EntityList.stringToClassMapping.get(id);
			if (args.isnil(2))
			{
				for (Object o : obj.world.loadedEntityList)
				{
					Entity fe = (Entity)o;
					if (clazz.isInstance(fe))
					{
						e = fe;
						break;
					}
				}
			}
			else
			{
				//Search for it in a n radius around point p
				int x = args.checkint(2);
				int y = args.checkint(3);
				int z = args.checkint(4);
				int r = 64;
				if (args.isnumber(5)) r = args.toint(5);
				List l = obj.world.getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x-r, y-r, z-r, x+r, y+r, z+r));
				e = (Entity) (l.size() > 0 ? l.get(0) : null);
			}
		}
		else
		{
			error("unknown entity \""+id);
		}
		return e != null ? EntityTools.createEntity(e) : NONE;
	}

	@LuaMethod
	public Varargs getFirstPlayer(WorldObject obj,Varargs args) {
		Entity e = null;
		//Entity exists
		if (args.isnil(1))
		{
			List l = obj.world.playerEntities;
			e = (Entity) (l.size() > 0 ? l.get(0) : null);
		}
		else
		{
			//Search for it in a n radius around point p
			int x = args.checkint(2);
			int y = args.checkint(3);
			int z = args.checkint(4);
			int r = 64;
			if (args.isnumber(5)) r = args.toint(5);
			List l = obj.world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(x-r, y-r, z-r, x+r, y+r, z+r));
			e = (Entity) (l.size() > 0 ? l.get(0) : null);
		}
		return e != null ? new EntityPlayerObject(e) : NONE; //TODO: Create player object
	}

	@LuaMethod
	public Varargs spawn(WorldObject obj,Varargs args) {
		String id = args.checkjstring(1);
		final int x = args.checkint(2);
		final int y = args.checkint(3);
		final int z = args.checkint(4);
		NBTTagCompound nbt = null;
		if (args.isnil(5))
		{
			nbt = new NBTTagCompound();
		}
		else
		{
			if (args.arg(5) instanceof NBTCompoundObject)
			{
				nbt = (NBTTagCompound) ((NBTCompoundObject)args.arg(5)).nbt.copy();
			}
			else
			{
				args.arg(5).typerror("nbt");
			}
		}
		nbt.setString("id", id);
		nbt.setTag("Pos", new NBTTagList("Pos"){{
			this.appendTag(new NBTTagDouble(null, x));
			this.appendTag(new NBTTagDouble(null, y));
			this.appendTag(new NBTTagDouble(null, z));
		}});
		nbt.setTag("Motion", new NBTTagList("Motion"){{
			this.appendTag(new NBTTagDouble(null, 0));
			this.appendTag(new NBTTagDouble(null, 0));
			this.appendTag(new NBTTagDouble(null, 0));
		}});
		nbt.setTag("Rotation", new NBTTagList("Rotation"){{
			this.appendTag(new NBTTagFloat(null, 0));
			this.appendTag(new NBTTagFloat(null, 1));
		}});
		Entity e = EntityList.createEntityFromNBT(nbt, obj.world);
		obj.world.spawnEntityInWorld(e);
		return EntityTools.createEntity(e);
	}

	@LuaMethod
	public Varargs getPlayer(WorldObject obj,Varargs args) {
		LuaTable tab = new LuaTable();
		for (Object o : obj.world.playerEntities)
		{
			tab.insert(tab.length()+1, new EntityPlayerObject((Entity) o)); //TODO: Create a player object
		}
		return varargsOf(new LuaValue[]{tab});
	}

	public Varargs getTileNBT(WorldObject obj,Varargs args) {
		int x = args.checkint(1);
		int y = args.checkint(2);
		int z = args.checkint(3);
		TileEntity tile = obj.world.getBlockTileEntity(x, y, z);
		if (tile != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			tile.writeToNBT(nbt);
			return new NBTCompoundObject(nbt);
		}
		return NONE;
	}

	@LuaMethod
	public Varargs setTileNBT(WorldObject obj,Varargs args) {
		int x = args.checkint(1);
		int y = args.checkint(2);
		int z = args.checkint(3);
		LuaValue nbtobj = args.arg(4);
		if (nbtobj instanceof NBTCompoundObject)
		{
			TileEntity tile = obj.world.getBlockTileEntity(x, y, z);
			if (tile != null)
			{
				tile.readFromNBT(((NBTCompoundObject) nbtobj).nbt);
			}
		}
		return NONE;
	}

	@LuaMethod
	public Varargs getEntityList(WorldObject obj,Varargs args) {
		LuaTable tab = new LuaTable();
		for (Object o : obj.world.loadedEntityList)
		{
			tab.set(tab.length()+1, EntityTools.createEntity((Entity) o));
		}
		return tab;
	}
	
	public LuaValue getInventory(WorldObject obj, LuaValue vx, LuaValue vy, LuaValue vz)
	{
		TileEntity tile = obj.world.getBlockTileEntity(vx.checkint(), vy.checkint(), vz.checkint());
		return tile instanceof IInventory ? new InventoryObject((IInventory) tile) : NIL;
	}
}
