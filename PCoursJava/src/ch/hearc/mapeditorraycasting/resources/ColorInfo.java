
package ch.hearc.mapeditorraycasting.resources;

import java.awt.Color;

/**
 * Bête Structure de donnée permettant de lier les information d'un item
 *
 * @author maxpi
 */
public class ColorInfo
	{
	
	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/
	
	public ColorInfo(String name, Color color, String description)
		{
		this.color = color;
		this.name = name;
		this.ressources = name;
		this.description = description;
		}
		
	@Override
	public String toString()
		{
		StringBuilder builder = new StringBuilder();
		builder.append(this.name);
		return builder.toString();
		}
		
	public Color color;
	public String ressources;
	public String description;
	public String name;
	}
