
package ch.hearc.mapeditorraycasting.tools;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DungeonBuilder
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public DungeonBuilder(int height, int width, int askedFeatures)
		{
		this.height = height;
		this.width = width;
		this.askedFeatures = askedFeatures;
		this.nbFeatures = 0;
		this.maxRoomHeight = Math.round(height / (float)MAXSIZECONST);
		this.maxRoomWidth = Math.round(width / (float)MAXSIZECONST);
		this.minRoomHeight = Math.round(maxRoomHeight / (float)MINSIZECONST);
		this.minRoomWidth = Math.round(maxRoomWidth / (float)MINSIZECONST);
		dice = new Random();

		fillDungeon();
		digFirstRoom();
		loopUntilComplete();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public BufferedImage getMap()
		{
		return dungeon;
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void fillDungeon()
		{
		//On remplit la map de murs, puis on va creuser des salles, couloirs...
		this.dungeon = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i < width; i++)
			{
			for(int j = 0; j < height; j++)
				{
				dungeon.setRGB(i, j, Color.BLACK.getRGB());
				}
			}
		}

	private void digFirstRoom()
		{
		int roomHeight = dice.nextInt(maxRoomHeight - minRoomHeight) + minRoomHeight;
		int roomWidth = dice.nextInt(maxRoomWidth - minRoomWidth) + minRoomWidth;

		checkSpace(width / 2 - roomWidth / 2, height / 2 - roomHeight / 2, width / 2 - roomWidth / 2 + roomWidth, height / 2 - roomHeight / 2 + roomHeight);

		}

	private boolean digRoom(int x, int y, char dir)
		{
		//Salle de taille variable
		int roomHeight = dice.nextInt(maxRoomHeight - minRoomHeight) + minRoomHeight;
		int roomWidth = dice.nextInt(maxRoomWidth - minRoomWidth) + minRoomWidth;

		switch(dir)
			{
			case 'N':
				return checkSpace(x - roomWidth / 2, y - 1 - roomHeight, x - roomWidth / 2 + roomWidth, y - 1);

			case 'S':
				return checkSpace(x - roomWidth / 2, y + 1, x - roomWidth / 2 + roomWidth, y + 1 + roomHeight);

			case 'E':
				return checkSpace(x + 1, y - roomHeight / 2, x + 1 + roomWidth, y - roomHeight / 2 + roomHeight);

			case 'W':
				return checkSpace(x - 1 - roomWidth, y - roomHeight / 2, x - 1, y - roomHeight / 2 + roomHeight);

			default:
				return false;
			}

		}

	//Vérifie si l'espace est libre puis creuse
	private boolean checkSpace(int x1, int y1, int x2, int y2)
		{
		for(int i = x1 - 1; i <= x2 + 1; i++)
			{
			for(int j = y1 - 1; j <= y2 + 1; j++)
				{
				//Si autre chose est construit ou si on sort des limites
				if (i < 0 || i >= width || j < 0 || j >= height || dungeon.getRGB(i, j) == Color.WHITE.getRGB()) { return false; }
				}
			}

		for(int i = x1; i <= x2; i++)
			{
			for(int j = y1; j <= y2; j++)
				{
				dungeon.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		//On a créé une nouvelle pièce / corridor.
		return true;
		}

	private boolean digCorridor(int x, int y, char dir)
		{
		int corridorHeight = dice.nextInt(maxRoomHeight - minRoomHeight) + minRoomHeight;
		int corridorWidth = dice.nextInt(maxRoomWidth - minRoomWidth) + minRoomWidth;

		switch(dir)
			{
			case 'N':
				return checkSpace(x, y - 1 - corridorHeight, x, y - 1);

			case 'S':
				return checkSpace(x, y + 1, x, y + 1 + corridorHeight);

			case 'E':
				return checkSpace(x + 1, y, x + 1 + corridorWidth, y);

			case 'W':
				return checkSpace(x - 1 - corridorWidth, y, x - 1, y);

			default:
				return false;
			}
		}

	private void loopUntilComplete()
		{
		int failedAttempt = 0;
		while(nbFeatures <= askedFeatures && failedAttempt < 1000)
			{
			Point wall = findWall();

			//Selon ou se trouve l'espace blanc à coté de notre mur,
			//l'orientation de notre élément change
			int x = wall.x;
			int y = wall.y;
			if (dungeon.getRGB(x, y - 1) == Color.WHITE.getRGB())
				{
				if (digFeature(x, y, 'S'))
					{
					dungeon.setRGB(x, y, Color.WHITE.getRGB());
					nbFeatures++;
					failedAttempt = 0;
					}
				else
					{
					failedAttempt++;
					}
				}
			else if (dungeon.getRGB(x, y + 1) == Color.WHITE.getRGB())
				{
				if (digFeature(x, y, 'N'))
					{
					dungeon.setRGB(x, y, Color.WHITE.getRGB());
					nbFeatures++;
					failedAttempt = 0;
					}
				else
					{
					failedAttempt++;
					}
				}
			else if (dungeon.getRGB(x + 1, y) == Color.WHITE.getRGB())
				{
				if (digFeature(x, y, 'W'))
					{
					dungeon.setRGB(x, y, Color.WHITE.getRGB());
					nbFeatures++;
					failedAttempt = 0;
					}
				else
					{
					failedAttempt++;
					}
				}
			else if (dungeon.getRGB(x - 1, y) == Color.WHITE.getRGB())
				{
				if (digFeature(x, y, 'E'))
					{
					dungeon.setRGB(x, y, Color.WHITE.getRGB());
					nbFeatures++;
					failedAttempt = 0;
					}
				else
					{
					failedAttempt++;
					}
				}
			}
		}

	//Choisit aléatoirement une feature à construire
	private boolean digFeature(int x, int y, char dir)
		{
		int rand = dice.nextInt(AVAILABLEFEATURES);

		switch(rand)
			{
			case 0:
				return digCorridor(x, y, dir);
			case 1:
				return digRoom(x, y, dir);

			default:
				System.out.println("[DungeonBuilder] digFeature : Shouldn't be there");
				return false;
			}
		}

	//Cherche simplement un mur en essayant un tas de points au hasard
	private Point findWall()
		{
		while(true)
			{
			//Entre 1 et width -1
			int x = dice.nextInt(width - 2) + 1;
			int y = dice.nextInt(height - 2) + 1;

			if (dungeon.getRGB(x, y) != Color.BLACK.getRGB())
				{
				continue;
				}
			if (dungeon.getRGB(x + 1, y) != Color.BLACK.getRGB() || dungeon.getRGB(x - 1, y) != Color.BLACK.getRGB() || dungeon.getRGB(x, y + 1) != Color.BLACK.getRGB() || dungeon.getRGB(x, y - 1) != Color.BLACK.getRGB()) { return new Point(x, y); }
			}
		}

	private int randomHeight()
		{
		return dice.nextInt(maxRoomHeight - minRoomHeight) + minRoomHeight;
		}

	private int randomWidth()
		{
		return dice.nextInt(maxRoomWidth - minRoomWidth) + minRoomWidth;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	private int height;
	private int width;

	private int maxRoomHeight;
	private int minRoomHeight;
	private int maxRoomWidth;
	private int minRoomWidth;

	private BufferedImage dungeon;

	private final Random dice;
	private int nbFeatures;
	private final int askedFeatures;

	private static final int MAXSIZECONST = 4;
	private static final int MINSIZECONST = 10;
	private static final int AVAILABLEFEATURES = 2;
	}
