
package ch.hearc.mapeditorraycasting.tools;

import java.awt.image.BufferedImage;

/**
 * Cette classe "outil" permet de charger toutes les images une seule fois
 */
public class MagasinImage
	{

	public MagasinImage()
		{

		}

	public static final BufferedImage DEFAULT_WALL_TEX = ImageLoader.loadBufferedImage("sprite/wall/texturesMaison/02mur.jpg");
	public static final BufferedImage DEFAULT_GROUND_TEX = ImageLoader.loadBufferedImage("sprite/wall/texturesMaison/01herbe.jpg");

	}
