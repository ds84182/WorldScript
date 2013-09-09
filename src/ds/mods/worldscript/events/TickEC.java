package ds.mods.worldscript.events;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Map.Entry;

import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import ds.mods.worldscript.EventContainer;
import ds.mods.worldscript.WorldScript;
import ds.mods.worldscript.WorldScript.ServPacketHandler;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;

public class TickEC extends EventContainer implements ITickHandler {
	public TickEC()
	{
		TickRegistry.registerTickHandler(this, Side.SERVER);
	}
	//TODO: Switch these

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		//Don't you like recycling?
		ArrayList<String> rem = new ArrayList<String>();
		for (Entry<String,Boolean> confirm : WorldScript.ServPacketHandler.confirmMap.entrySet())
		{
			if (confirm.getValue())
			{
				//Send the next packet
				int len = 32767-(confirm.getKey().length()+3);
				ByteArrayInputStream bin = WorldScript.ServPacketHandler.uploadingResources.get(confirm.getKey());
				len = Math.min(len, bin.available());
				byte[] data = new byte[len];
				try {
					bin.read(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeByte(1);
				out.writeUTF(confirm.getKey());
				out.write(data);
				PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("WorldScript", out.toByteArray()), WorldScript.ServPacketHandler.playerMap.get(confirm.getKey()));
				if (bin.available() == 0)
				{
					//Send end packet
					out = ByteStreams.newDataOutput();
					out.writeByte(2);
					out.writeUTF(confirm.getKey());
					PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload("WorldScript", out.toByteArray()), WorldScript.ServPacketHandler.playerMap.get(confirm.getKey()));
					rem.add(confirm.getKey());
				}
			}
			else
			{
				rem.add(confirm.getKey());
			}
		}
		for (String tr : rem)
		{
			ServPacketHandler.confirmMap.remove(tr);
			ServPacketHandler.playerMap.remove(tr);
			ServPacketHandler.uploadingResources.remove(tr);
		}
		invoke(LuaValue.NONE);
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "WorldScriptTick";
	}
}
