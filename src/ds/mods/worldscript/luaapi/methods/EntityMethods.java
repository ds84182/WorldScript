package ds.mods.worldscript.luaapi.methods;

import static ds.mods.worldscript.org.luaj.vm2.LuaValue.NONE;
import static ds.mods.worldscript.org.luaj.vm2.LuaValue.valueOf;
import static ds.mods.worldscript.org.luaj.vm2.LuaValue.varargsOf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import ds.mods.worldscript.luaapi.objects.EntityObject;
import ds.mods.worldscript.luaapi.tools.LuaMethod;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.Varargs;

public class EntityMethods {
	@LuaMethod
	public Varargs getPosition(EntityObject obj)
	{
		return varargsOf(new LuaValue[]{
				valueOf(obj.entity.posX),
				valueOf(obj.entity.posY),
				valueOf(obj.entity.posZ)
		});
	}
	
	@LuaMethod
	public Varargs setPosition(EntityObject obj, Varargs args) {
		double x = args.checknumber(1).todouble();
		double y = args.checknumber(2).todouble();
		double z = args.checknumber(3).todouble();
		obj.entity.setPosition(x, y, z);
		return NONE;
	}
	
	@LuaMethod
	public Varargs getVelocity(EntityObject obj)
	{
		return varargsOf(new LuaValue[]{
				valueOf(obj.entity.motionX),
				valueOf(obj.entity.motionY),
				valueOf(obj.entity.motionZ)
		});
	}
	
	@LuaMethod
	public LuaValue setVelocity(EntityObject obj, Varargs args) {
		double x = args.checknumber(1).todouble();
		double y = args.checknumber(2).todouble();
		double z = args.checknumber(3).todouble();
		obj.entity.setVelocity(x, y, z);
		return NONE;
	}
	
	@LuaMethod
	public LuaValue getRider(EntityObject obj) {
		return obj.entity.riddenByEntity != null ? new EntityObject(obj.entity.riddenByEntity) : NONE;
	}
	
	@LuaMethod
	public Varargs getRiding(EntityObject obj) {
		return obj.entity.ridingEntity != null ? new EntityObject(obj.entity.ridingEntity) : NONE;
	}
	
	@LuaMethod
	public void ride(EntityObject obj, Varargs args) {
		LuaValue val = args.checknotnil(1);
		if (val instanceof EntityObject)
		{
			obj.entity.mountEntity(((EntityObject)val).entity);
		}
		else
		{
			val.typerror("entity");
		}
	}
	
	@LuaMethod
	public void kill(EntityObject obj)
	{
		if (obj.entity instanceof EntityLivingBase)
		{
			System.out.println("KillingLB");
			((EntityLivingBase)obj.entity).heal(-9001);
			((EntityLivingBase)obj.entity).onDeath(DamageSource.generic);
		}
		else if (obj.entity instanceof Entity)
			obj.entity.setDead();
	}
}
