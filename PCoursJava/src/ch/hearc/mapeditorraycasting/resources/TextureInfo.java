
package ch.hearc.mapeditorraycasting.resources;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class TextureInfo
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public TextureInfo(Color color, int index, String description)
		{
		this.color = color;
		this.index = index;
		this.description = description;
		this.resource = null;
		}

	public TextureInfo(Color color, int index, String description, BufferedImage resource)
		{
		this.color = color;
		this.index = index;
		this.description = description;
		this.resource = resource;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		builder.append(this.index);
		builder.append(" : ");
		builder.append(this.description);
		return builder.toString();
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

	public Color color;

	public int index;
	public String description;
	public BufferedImage resource;
	}
