package ds.mods.worldscript;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bouncycastle.util.Arrays;

import net.minecraft.block.material.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;
import ds.mods.worldscript.events.EntityJoinWorldEC;
import ds.mods.worldscript.events.PlayerInteractEventEC;
import ds.mods.worldscript.events.TickEC;
import ds.mods.worldscript.luaapi.ResLib;
import ds.mods.worldscript.org.luaj.vm2.*;
import ds.mods.worldscript.org.luaj.vm2.compiler.LuaC;
import ds.mods.worldscript.org.luaj.vm2.lib.jse.JsePlatform;

@Mod(modid="WorldScript",name="WorldScript")
@NetworkMod(clientSideRequired=true,serverSideRequired=true,
			clientPacketHandlerSpec=@SidedPacketHandler(channels = { "WorldScript" }, packetHandler = WorldScript.CliPacketHandler.class),
			serverPacketHandlerSpec=@SidedPacketHandler(channels = { "WorldScript" }, packetHandler = WorldScript.ServPacketHandler.class)
)
public class WorldScript {
	public static ArrayList<LuaThread> runningThreads = new ArrayList<LuaThread>();
	
	public static BlockScripted scriptedBlock = new BlockScripted(500, Material.rock);
	public static BlockRegistry s_br = new BlockRegistry();
	public static BlockRegistry c_br = new BlockRegistry();
	
	@SidedProxy(clientSide="ds.mods.worldscript.client.ClientProxy",serverSide="ds.mods.worldscript.CommonProxy")
	public static CommonProxy proxy;
	
	public static HashMap<String,EventContainer> eventContainers = new HashMap<String,EventContainer>()
	{{
		put("Tick",new TickEC());
		put("EntityJoinWorld",new EntityJoinWorldEC());
		put("PlayerInteract",new PlayerInteractEventEC());
	}};
	
	public static File getWorldDir(String child)
	{
		return MinecraftServer.getServer().getFile((FMLCommonHandler.instance().getSide() == Side.CLIENT ? "saves/" : "")+DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName()+"/"+child);
	}
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		GameRegistry.registerPlayerTracker(new PlayerTracker());
		GameRegistry.registerBlock(scriptedBlock, "LuaScriptedBlock");
		GameRegistry.registerTileEntity(TileEntityScripted.class, "tilescripted");
		proxy.init();
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent event)
	{
		//Ok, load lua files in WORLD_DIR/lua
		File file = getWorldDir("lua");
		file.mkdirs();
		getWorldDir("lua/res").mkdir();
		File scripts = new File(file,"scripts");
		scripts.mkdir();
		LuaC.install();
		load(scripts);
		
		CommandHandler ch = ((CommandHandler)MinecraftServer.getServer().getCommandManager());
		ch.registerCommand(new CommandLua());
		ch.registerCommand(new CommandLuaUnload());
		ch.registerCommand(new CommandLuaLoad());
		ch.registerCommand(new CommandLuaReload());
	}
	
	public static void unload()
	{
		for (LuaThread t : LuaThread.threads)
		{
			t.abandon();
		}
		//Unregister all events
		for (EventContainer ec : eventContainers.values())
		{
			ec.removeAll();
		}
		runningThreads.clear();
		ResLib.stuffToPreload.clear();
		//s_br.blocks.clear();
		//TODO: Send client a packet to clear it's block registry
	}
	
	public static void load(File scripts)
	{
		for (File f : scripts.listFiles())
		{
			if (f.getPath().endsWith(".lua"))
			{
				try {
					LuaFunction func = LoadState.load(new FileInputStream(f), f.getName(), JsePlatform.standardGlobals());
					LuaThread thread = new LuaThread(func,func.getfenv());
					runningThreads.add(thread);
					thread.resume(LuaValue.NONE);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (LuaError e) {
					ChatMessageComponent comp = new ChatMessageComponent();
					comp.setBold(true);
					comp.setColor(EnumChatFormatting.RED);
					comp.setUnderline(true);
					comp.addText(e.getMessage());
					MinecraftServer.getServer().sendChatToPlayer(comp);
					Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

		            while (iterator.hasNext())
		            {
		                EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();

		                if (MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(entityplayermp.getCommandSenderName()))
		                {
		                    entityplayermp.sendChatToPlayer(comp);
		                }
		            }
				}
			}
		}
	}
	
	public class CommandLua extends CommandBase
	{

		@Override
		public String getCommandName() {
			return "lua";
		}

		@Override
		public String getCommandUsage(ICommandSender icommandsender) {
			return "Something something blah";
		}
		
		@Override
		public int getRequiredPermissionLevel()
	    {
	        return 2;
	    }

		@Override
		public void processCommand(ICommandSender icommandsender,
				String[] astring) {
			String s = "";
			for (String v : astring)
			{
				s+=v+" ";
			}
			LuaFunction func;
			try {
				func = LoadState.load(new ByteArrayInputStream(s.getBytes()), "cmdin", JsePlatform.standardGlobals());
				LuaThread thread = new LuaThread(func,func.getfenv());
				runningThreads.add(thread);
				thread.resume(LuaValue.NONE);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (LuaError e) {
				ChatMessageComponent comp = new ChatMessageComponent();
				comp.setBold(true);
				comp.setColor(EnumChatFormatting.RED);
				comp.setUnderline(true);
				comp.addText(" ["+icommandsender.getCommandSenderName()+"] "+e.getMessage());
				icommandsender.sendChatToPlayer(comp);
			}
		}
		
	}
	
	public class CommandLuaUnload extends CommandBase
	{

		@Override
		public String getCommandName() {
			return "luaunload";
		}

		@Override
		public String getCommandUsage(ICommandSender icommandsender) {
			return "Something something blah";
		}
		
		@Override
		public int getRequiredPermissionLevel()
	    {
	        return 2;
	    }

		@Override
		public void processCommand(ICommandSender icommandsender,
				String[] astring) {
			unload();
		}
		
	}
	
	public class CommandLuaLoad extends CommandBase
	{

		@Override
		public String getCommandName() {
			return "luaload";
		}

		@Override
		public String getCommandUsage(ICommandSender icommandsender) {
			return "Something something blah";
		}
		
		@Override
		public int getRequiredPermissionLevel()
	    {
	        return 2;
	    }

		@Override
		public void processCommand(ICommandSender icommandsender,
				String[] astring) {
			File file = MinecraftServer.getServer().getFile((FMLCommonHandler.instance().getSide() == Side.CLIENT ? "saves/" : "")+DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName()+"/lua");
			File scripts = new File(file,"scripts");
			load(scripts);
		}
		
	}
	
	public class CommandLuaReload extends CommandBase
	{

		@Override
		public String getCommandName() {
			return "luareload";
		}

		@Override
		public String getCommandUsage(ICommandSender icommandsender) {
			return "Something something blah";
		}
		
		@Override
		public int getRequiredPermissionLevel()
	    {
	        return 2;
	    }

		@Override
		public void processCommand(ICommandSender icommandsender,
				String[] astring) {
			unload();
			File file = MinecraftServer.getServer().getFile((FMLCommonHandler.instance().getSide() == Side.CLIENT ? "saves/" : "")+DimensionManager.getWorld(0).getSaveHandler().getWorldDirectoryName()+"/lua");
			File scripts = new File(file,"scripts");
			load(scripts);
		}
		
	}
	
	public static class ServPacketHandler implements IPacketHandler
	{
		public static final int RESNO    = 0;
		public static final int RESGO    = 1;
		public static HashMap<String, ByteArrayInputStream> uploadingResources = new HashMap<String, ByteArrayInputStream>();
		public static HashMap<String, Boolean> confirmMap = new HashMap<String, Boolean>();
		public static HashMap<String, Player> playerMap = new HashMap<String, Player>();
		
		@Override
		public void onPacketData(INetworkManager manager,
				Packet250CustomPayload packet, Player player) {
			ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
			byte typ = dat.readByte();
			switch(typ)
			{
			case RESNO:
			{
				String UUID = dat.readUTF();
				confirmMap.put(UUID, false);
				break;
			}
			case RESGO:
			{
				String UUID = dat.readUTF();
				confirmMap.put(UUID, true);
				break;
			}
			}
		}
		
	}
	
	public static class CliPacketHandler implements IPacketHandler
	{
		public static final int RESHEADER = 0;
		public static final int RESDATA   = 1;
		public static final int RESEND    = 2;
		
		public HashMap<String,OutputStream> downloadingResources = new HashMap<String, OutputStream>();
		public static HashMap<String,String> serverFileToClientFile = new HashMap<String, String>();
		
		@Override
		public void onPacketData(INetworkManager manager,
				Packet250CustomPayload packet, Player player) {
			ByteArrayDataInput dat = ByteStreams.newDataInput(packet.data);
			byte typ = dat.readByte();
			switch(typ)
			{
			case RESHEADER:
			{
				//We have a resource that needs to be downloaded.
				String FUUID = dat.readUTF(); //This also include an extention
				String UUID = dat.readUTF();
				String serverFileName = dat.readUTF();
				serverFileToClientFile.put(serverFileName, FUUID);
				//Check if it exists, if it doesnt, send the server a packet to start the download
				//TODO: Check the hash of it
				new File("./restemp").mkdirs();
				if (!new File("./restemp/"+FUUID).exists())
				{
					try {
						FileOutputStream fout = new FileOutputStream("./restemp/"+FUUID);
						downloadingResources.put(UUID, fout);
						ByteArrayDataOutput out = ByteStreams.newDataOutput();
						out.writeByte(1);
						out.writeUTF(UUID);
						Packet p = new Packet250CustomPayload("WorldScript", out.toByteArray());
						PacketDispatcher.sendPacketToServer(p);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				else
				{
					//Send resno packet
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeByte(0);
					out.writeUTF(UUID);
					Packet p = new Packet250CustomPayload("WorldScript", out.toByteArray());
					PacketDispatcher.sendPacketToServer(p);
				}
				break;
			}
			case RESDATA:
			{
				String UUID = dat.readUTF();
				byte[] arr = Arrays.copyOfRange(packet.data, UUID.length()+3, packet.data.length);
				try {
					downloadingResources.get(UUID).write(arr);
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			}
			case RESEND:
			{
				String UUID = dat.readUTF();
				try {
					downloadingResources.remove(UUID).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			}
		}
		
	}
	
	public class PlayerTracker implements IPlayerTracker
	{

		@Override
		public void onPlayerLogin(EntityPlayer player) {
			//Send preloaded crap
			for (String filename : ResLib.stuffToPreload)
			{
				ResLib.sendPreload((Player) player, filename);
			}
			//TODO: Event
		}

		@Override
		public void onPlayerLogout(EntityPlayer player) {
			//TODO: Event
		}

		@Override
		public void onPlayerChangedDimension(EntityPlayer player) {
			//TODO: Event
		}

		@Override
		public void onPlayerRespawn(EntityPlayer player) {
			//TODO: Event
		}
		
	}
}
