package ds.mods.worldscript;

import java.util.Stack;

import ds.mods.worldscript.org.luaj.vm2.Varargs;

public class EventThread extends Thread {
	public static Stack<EventContainer> eventStack = new Stack<EventContainer>();
	public static Stack<Varargs> varargStack = new Stack<Varargs>();

	@Override
	public void run() {
		while (true)
		{
			synchronized (eventStack)
			{
				synchronized (varargStack)
				{
					//So main thread blocks while I try to go down the stack
					while (!eventStack.isEmpty())
					{
						EventContainer ec = eventStack.pop();
						Varargs var = varargStack.pop();
						ec.runFunctions(var);
					}
				}
			}
		}
	}

}
