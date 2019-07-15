
package ch.hearc.mapeditorraycasting.resources;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import ch.hearc.mapeditorraycasting.tools.DungeonBuilder;
import ch.hearc.mapeditorraycasting.tools.ImageLoader;
import ch.hearc.mapeditorraycasting.tools.MagasinImage;

public class MapRattco
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public MapRattco(int width, int height)
		{
		map = DungeonBuilder.generateVoid(width, height);
		generateTextureMapFromMap();
		listTextures = new TreeSet<TextureInfo>(new Comparator<TextureInfo>()
			{

			@Override
			public int compare(TextureInfo o1, TextureInfo o2)
				{
				return o1.index - o2.index;
				}
			});

		listTextures.add(new TextureInfo(COLOR_GROUND_DEFAULT, 1, "Default ground", MagasinImage.DEFAULT_GROUND_TEX));
		listTextures.add(new TextureInfo(COLOR_WALL_DEFAULT, 0, "Default Wall", MagasinImage.DEFAULT_WALL_TEX));
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void setRGBMap(int x, int y, int color)
		{
		map.setRGB(x, y, color);
		if (map.getRGB(x, y) != Color.BLACK.getRGB())
			{
			setRGBATextureMap(x, y, COLOR_GROUND_DEFAULT.getRGB());
			}
		else
			{
			setRGBATextureMap(x, y, COLOR_WALL_DEFAULT.getRGB());
			}
		}

	public void setRGBATextureMap(int x, int y, int color)
		{
		textureMap.setRGB(x, y, color);
		}

	public void loadFromDirectory(String directoryPath)
		{
		map = ImageLoader.loadBufferedImage(directoryPath + "/map.png");
		textureMap = ImageLoader.loadBufferedImage(directoryPath + "/mapTexture.png");

		File backgroundFile = new File(directoryPath + "/fond.png");
		if (backgroundFile.exists())
			{
			textureFond = ImageLoader.loadBufferedImage(directoryPath + "/fond.png");
			}

		listTextures.clear();
		findColorWithBlueComponent(textureMap, 2);
		File folder = new File(directoryPath + "/textures");
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i < listOfFiles.length; i++)
			{
			int col = findColorWithBlueComponent(textureMap, i);
			String name = listOfFiles[i].getName();
			BufferedImage image = null;
			try
				{
				System.out.println(listOfFiles[i]);
				image = ImageIO.read(listOfFiles[i]);
				}
			catch (IOException e)
				{
				System.out.println("Problem encountered when reading textures");
				}
			listTextures.add(new TextureInfo(new Color(col), i, name, image));
			}
		}

	private int findColorWithBlueComponent(BufferedImage textureMap, int blueComponent)
		{
		for(int i = 0; i < textureMap.getWidth(); i++)
			{
			for(int j = 0; j < textureMap.getHeight(); j++)
				{
				int col = textureMap.getRGB(i, j);
				if ((col & 0x000F) == blueComponent) { return col; }
				}
			}
		return 0;
		}

	public void generateTextureMapFromMap()
		{
		textureMap = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < map.getWidth(); i++)
			{
			for(int j = 0; j < map.getHeight(); j++)
				{
				if (map.getRGB(i, j) != Color.BLACK.getRGB())
					{
					textureMap.setRGB(i, j, COLOR_GROUND_DEFAULT.getRGB());
					}
				else
					{
					textureMap.setRGB(i, j, COLOR_WALL_DEFAULT.getRGB());
					}
				}
			}
		}

	public void replaceColorInTextureMap(int color1, int color2)
		{
		for(int i = 0; i < textureMap.getWidth(); i++)
			{
			for(int j = 0; j < textureMap.getHeight(); j++)
				{
				if (textureMap.getRGB(i, j) == color1)
					{
					textureMap.setRGB(i, j, color2);
					}
				}
			}
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	public BufferedImage map;

	public BufferedImage textureMap;
	public TreeSet<TextureInfo> listTextures;
	public BufferedImage textureFond;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	static public Color COLOR_GROUND_DEFAULT = new Color(255, 0, 1);
	static public Color COLOR_WALL_DEFAULT = new Color(255, 255, 0);
	}
