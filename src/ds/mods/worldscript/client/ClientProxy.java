package ds.mods.worldscript.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import ds.mods.worldscript.CommonProxy;
import ds.mods.worldscript.TileEntityScripted;

public class ClientProxy extends CommonProxy {
	@Override
	public void init()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScripted.class, new TileEntityScriptedRenderer());
		new CreativeTabRegistered("WorldScript blocks");
	}
}
