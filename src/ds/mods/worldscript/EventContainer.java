package ds.mods.worldscript;

import java.util.ArrayList;

import ds.mods.worldscript.org.luaj.vm2.LuaError;
import ds.mods.worldscript.org.luaj.vm2.LuaFunction;
import ds.mods.worldscript.org.luaj.vm2.Varargs;

public class EventContainer {
	public ArrayList<LuaFunction> handlers = new ArrayList<LuaFunction>();
	
	public void addHandler(LuaFunction func)
	{
		handlers.add(func);
	}
	
	public void removeHandler(LuaFunction func)
	{
		handlers.remove(func);
	}
	
	public void removeAll()
	{
		handlers.clear();
	}
	
	public void invoke(Varargs args)
	{
		synchronized (EventThread.eventStack)
		{
			synchronized (EventThread.varargStack)
			{
				EventThread.eventStack.add(this);
				EventThread.varargStack.add(args);
			}
		}
	}
	
	public void runFunctions(Varargs args)
	{
		for (LuaFunction f : handlers)
		{
			synchronized(f)
			{
				try
				{
					f.invoke(args);
				}
				catch(LuaError e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
