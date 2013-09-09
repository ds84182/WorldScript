package ds.mods.worldscript.luaapi.tools;

import java.util.Stack;

public class FSTools {
	public static String cleanPath(String path)
	{
		StringBuilder build = new StringBuilder();
		
		path = path.replace("\\", "/");
		Stack<String> segments = new Stack<String>();
		for (String seg : path.split("/"))
		{
			if (seg.equals(".."))
			{
				segments.pop();
			}
			else if (!seg.equals("."))
			{
				segments.push(seg);
			}
		}
		
		for (String seg : segments)
		{
			build.append("/"+seg);
		}
		
		return build.toString();
	}
}
