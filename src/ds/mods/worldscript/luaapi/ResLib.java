package ds.mods.worldscript.luaapi;

import java.io.*;
import java.util.*;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import ds.mods.worldscript.WorldScript;
import ds.mods.worldscript.luaapi.tools.FSTools;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.lib.OneArgFunction;

public class ResLib extends OneArgFunction {
	public static ArrayList<String> stuffToPreload = new ArrayList<String>();

	@Override
	public LuaValue call(LuaValue arg) {
		LuaTable tab = new LuaTable();
		
		tab.set("preload", new preload());
		
		env.set("res",tab);
		return tab;
	}
	
	public class preload extends OneArgFunction
	{

		@Override
		public LuaValue call(LuaValue arg) {
			String filename = FSTools.cleanPath(arg.checkjstring());
			if (!stuffToPreload.contains(filename))
			{
				stuffToPreload.add(filename);
				sendPreloadToAll(filename);
			}
			return null;
		}
		
	}
	
	public static void sendPreloadToAll(String filename)
	{
		File file = WorldScript.getWorldDir("lua/res"+filename);
		if (file.exists())
		{
			if (file.isFile())
			{
				//Open and send file to server.
				//Only can send 32kb of data per packet
				//Save it in a file.
				long uuid = 0L;
				long uuid2 = 0L;
				byte[] fileContents = new byte[(int) file.length()];
				try {
					FileInputStream fin = new FileInputStream(file);
					fin.read(fileContents);
					fin.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (int i = 0; i<filename.length(); i++)
				{
					uuid += filename.charAt(i);
				}
				for (int i = 0; i<fileContents.length; i++)
				{
					uuid2 += fileContents[i];
				}
				String tempFileName = (new UUID(uuid,uuid2).toString())+filename.substring(filename.length()-4);
				Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

	            while (iterator.hasNext())
	            {
	                EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();

					UUID rand = UUID.randomUUID();
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeByte(0);
					out.writeUTF(tempFileName);
					out.writeUTF(rand.toString());
					out.writeUTF(filename);
					WorldScript.ServPacketHandler.uploadingResources.put(rand.toString(), new ByteArrayInputStream(fileContents));
					entityplayermp.playerNetServerHandler.sendPacketToPlayer(new Packet250CustomPayload("WorldScript", out.toByteArray()));
					WorldScript.ServPacketHandler.playerMap.put(rand.toString(), (Player) entityplayermp);
	            }
			}
		}
	}

	public static void sendPreload(Player p, String filename)
	{
		File file = WorldScript.getWorldDir("lua/res"+filename);
		if (file.exists())
		{
			if (file.isFile())
			{
				//Open and send file to server.
				//Only can send 32kb of data per packet
				//Save it in a file.
				long uuid = 0L;
				long uuid2 = 0L;
				byte[] fileContents = new byte[(int) file.length()];
				try {
					FileInputStream fin = new FileInputStream(file);
					fin.read(fileContents);
					fin.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (int i = 0; i<filename.length(); i++)
				{
					uuid += filename.charAt(i);
				}
				for (int i = 0; i<fileContents.length; i++)
				{
					uuid2 += fileContents[i];
				}
				String tempFileName = (new UUID(uuid,uuid2).toString())+filename.substring(filename.length()-4);
				
					UUID rand = UUID.randomUUID();
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeByte(0);
					out.writeUTF(tempFileName);
					out.writeUTF(rand.toString());
					out.writeUTF(filename);
					WorldScript.ServPacketHandler.uploadingResources.put(rand.toString(), new ByteArrayInputStream(fileContents));
					PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("WorldScript", out.toByteArray()),p);
					WorldScript.ServPacketHandler.playerMap.put(rand.toString(), p);
			}
		}
	}
}
