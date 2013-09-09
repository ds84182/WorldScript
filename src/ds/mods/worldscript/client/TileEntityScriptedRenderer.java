package ds.mods.worldscript.client;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import ds.mods.worldscript.TileEntityScripted;

public class TileEntityScriptedRenderer extends TileEntitySpecialRenderer {
	public HashMap<String,Integer> fileToGLIDMap = new HashMap<String, Integer>();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
			double d2, float f) {
		/*
		 * Load block texture from file
		 * Render standard block with texture
		 */
		TileEntityScripted tile = (TileEntityScripted)tileentity;
		bindTexture(tile.block.texture);
		GL11.glPushMatrix();
		
		GL11.glTranslated(d0, d1, d2);
		GL11.glColor3f(255, 255, 255);
		
		GL11.glBegin(GL11.GL_QUADS);
		
		GL11.glTexCoord2d(0D, 0D);
		GL11.glVertex3d(0D, 1D, 0D);
		GL11.glTexCoord2d(0D, 1D);
		GL11.glVertex3d(0D, 1D, 1D);
		GL11.glTexCoord2d(1D, 1D);
		GL11.glVertex3d(1D, 1D, 1D);
		GL11.glTexCoord2d(1D, 0D);
		GL11.glVertex3d(1D, 1D, 0D);
		
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
	
	public int getGlTextureId()
    {
        return TextureUtil.glGenTextures();
    }
	
	public void loadTexture(String tex)
	{
		try {
			FileInputStream inputstream = new FileInputStream(tex);
			BufferedImage bufferedimage = ImageIO.read(inputstream);
			int glid = getGlTextureId();
			TextureUtil.uploadTextureImageAllocate(glid, bufferedimage, false, false);
			fileToGLIDMap.put(tex, glid);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void bindTexture(String tex)
	{
		if (!fileToGLIDMap.containsKey(tex))
		{
			loadTexture(tex);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fileToGLIDMap.get(tex));
	}

}
