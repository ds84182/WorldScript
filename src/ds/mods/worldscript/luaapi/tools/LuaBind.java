package ds.mods.worldscript.luaapi.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.FMLLog;
import ds.mods.worldscript.org.luaj.vm2.LuaTable;
import ds.mods.worldscript.org.luaj.vm2.LuaValue;
import ds.mods.worldscript.org.luaj.vm2.Varargs;
import ds.mods.worldscript.org.luaj.vm2.lib.VarArgFunction;

//Creates a table that is binded to the class T
public class LuaBind<T> extends LuaTable {
	public T obj;
	public HashMap<String,MethodFunction> funcMap = new HashMap<String, LuaBind<T>.MethodFunction>();
	public LuaBind that = this;
	
	@Override
	public LuaValue get(LuaValue key) {
		//FMLLog.info("Getting "+key.checkjstring());
		if (!super.get(key).isnil())
		{
			return super.get(key);
		}
		if (!funcMap.containsKey(key.checkjstring()))
		{
			//Lets see if it is createable
			String mn = key.checkjstring();
			boolean done = false;
			System.out.println(obj);
			for (Method m : obj.getClass().getMethods())
			{
				if (m.getName().equals(mn) && m.isAnnotationPresent(LuaMethod.class))
				{
					funcMap.put(mn, new MethodFunction(m));
					done = true;
					break;
				}
			}
			if (!done)
			{
				error("unknown method "+mn);
			}
		}
		return funcMap.get(key.checkjstring());
	}

	private class MethodFunction extends VarArgFunction
	{

		Method m;
		ArrayList<Integer> paramLayout = new ArrayList<Integer>();
		int returnType = VOID;
		public static final int VALUE = 0;
		public static final int VARARGS = 1;
		public static final int VOID = 2;
		
		public MethodFunction(Method m)
		{
			this.m = m;
			Class<?>[] params = m.getParameterTypes();
			for (int i = 1; i<params.length; i++)
			{
				Class<?> c = params[i];
				String name = c.getName();
				if (name.equals(LuaValue.class.getName()))
				{
					paramLayout.add(VALUE);
				}
				else if (name.equals(Varargs.class.getName()))
				{
					paramLayout.add(VARARGS);
					break;
				}
				else
				{
					throw new RuntimeException("Unknown type "+name);
				}
			}
			Class<?> retType = m.getReturnType();
			returnType = retType.getName().equals("void") ? VOID : VALUE;
		}
		
		@Override
		public Varargs invoke(Varargs args) {
			Varargs ret = NIL;
			Object[] o = new Object[paramLayout.size()+1];
			o[0] = that;
			//Now lay out the params
			for (int i = 0; i<o.length-1; i++)
			{
				switch (paramLayout.get(i))
				{
				case VALUE:
					//FMLLog.info("Arg "+(i+1)+" is LuaValue");
					o[i+1] = args.arg(i+1);
					break;
				case VARARGS:
				{
					int vleft = args.narg()-i;
					//FMLLog.info("Varargs "+vleft);
					LuaValue[] val = new LuaValue[vleft];
					for (int v = i; v<args.narg(); v++)
					{
						//FMLLog.info("Varargs n "+v);
						val[v] = args.arg(v+1);
					}
					o[i+1] = varargsOf(val);
					break;
				}
				}
			}
			try {
				Object r = m.invoke(obj, o);
				ret = (Varargs) r;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				FMLLog.info(e.getMessage());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				FMLLog.info(e.getMessage());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				FMLLog.info(e.getMessage());
			} catch (Exception e)
			{
				FMLLog.info(e.getMessage());
			}
			ret = ret != null ? ret : NIL;
			return ret;
		}
		
	}
}
