
package ch.hearc.mapeditorraycasting;

import java.awt.Color;

import ch.hearc.mapeditorraycasting.resources.MapRattco;
import ch.hearc.mapeditorraycasting.resources.TextureInfo;
import ch.hearc.mapeditorraycasting.tools.MagasinImage;

public class UseTextureDialog
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static void main(String[] args)
		{
		main();
		}

	public static void main()
		{
		TextureInfo modify = new TextureInfo(Color.BLUE, 25, "Bois", MagasinImage.DEFAULT_WALL_TEX);

		MapRattco map = new MapRattco(20, 30);
		map.listTextures.add(new TextureInfo(Color.BLUE, 5, "1st added"));
		map.listTextures.add(new TextureInfo(Color.BLUE, 8, "2nd added"));
		map.listTextures.add(new TextureInfo(Color.BLUE, 1, "3rd added"));
		map.listTextures.add(new TextureInfo(Color.BLUE, 3, "4th added"));

		System.out.println(map.listTextures);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	}
