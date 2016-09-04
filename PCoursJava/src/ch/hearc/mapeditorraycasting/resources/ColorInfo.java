
package ch.hearc.mapeditorraycasting.resources;

import java.awt.Color;

public class ColorInfo
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public ColorInfo(String name, Color color, String type, String description)
		{
		this.color = color;
		this.ressources = name;
		this.type = type;
		this.description = description;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		builder.append(this.ressources);
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
	public String ressources;
	public String type;
	public String description;
 	//private ActionScript script;
	}
