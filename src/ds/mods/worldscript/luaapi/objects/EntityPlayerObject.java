package ds.mods.worldscript.luaapi.objects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ds.mods.worldscript.luaapi.methods.EntityPlayerMethods;

public class EntityPlayerObject extends EntityObject {
	public EntityPlayer player;

	public EntityPlayerObject(Entity e) {
		super(e);
		player = (EntityPlayer) e;
		this.obj = new EntityPlayerMethods();
		//System.out.println(obj);
		set("type", "player");
	}

}
