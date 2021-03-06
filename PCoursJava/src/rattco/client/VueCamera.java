
package rattco.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import rattco.thing.Thing;
import rattco.thing.personnage.JoueurOnline;
import rattco.thing.weapon.Axe;
import rattco.thing.weapon.Chainsaw;
import rattco.thing.weapon.PrecisionRifle;
import rattco.thing.weapon.ShootGun;
import rattco.tools.MagasinImage;
import rattco.tools.raycasting.Vector2D;

/**
 * Cette classe est horrible. C'est ici que sont fait tous les rendus
 * des murs, des things, du HUD, du fond, etc... Ces rendus sont fait sous forme
 * de couches, chaque image est d'abord pr�par�e, puis elles sont dessin�es.
 * Cela permettrait de mettre en concurrence la pr�paration des images, nous avons
 * essay�s mais �a posait d'autres probl�mes
 */
public class VueCamera extends Renderer
	{
	
	private static final long serialVersionUID = 5940938109641662048L;
	public static final String strMort = new String("Vous �tes mort!");
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	private Vector2D pos, dir, plane;
	private int frameH, frameW, h, w;
	private int customH = 280, customW = 510;
	private final int InitialcustomHeight = 288, InitialcustomWidth = 512;
	
	private double scaleWidth = (double)customW / InitialcustomWidth;
	private double scaleHeight = (double)customH / InitialcustomHeight;
	
	private int texWidth = 256;
	private int texHeight = 256;
	
	protected double[] tabDistStripes;
	
	private BufferedImage buffImgMurs, buffImgHUD, buffImgThings, buffImgWeapon, buffDeathScreen, buffImgFond, buffImgImpact, buffImgDamage, buffImgScores;
	private Graphics2D g2d, g2dMurs, g2dThings, g2dHUD, g2dWeapon, g2dDeathScreen, g2dFond, g2dImpact, g2dDamage, g2dScores;
	
	private boolean readyToDraw;
	private BufferedImage currentSprite;
	
	private TreeMap<Double, Thing> chosesAAfficher;
	private PriorityQueue<JoueurOnline> joueursTries;
	
	public VueCamera(LogiqueClient _logique, int customHeight)
		{
		super(_logique);
		customH = customHeight;
		customW = customH * 16 / 9;
		// necessaire pour la fonction scale
		scaleWidth = (double)customW / InitialcustomWidth;
		scaleHeight = (double)customH / InitialcustomHeight;
		
		// il ne faut commencer � dessiner seulement une fois la fen�tre et 
		// toutes les BufferedImages cr��es
		readyToDraw = false;
		
		pos = lc.joueur.getPosition();
		plane = new Vector2D(0, 0);
		setDirection(lc.joueur.getDirection());
		chosesAAfficher = new TreeMap<Double, Thing>();
		
		/**
		 *  un heap qui permet de trier les joueurs avant de 
		 *  les afficher dans le tableau des scores.
		 *  Pour �a, JoueurOnline impl�mente l'interface Comparable
		 */
		joueursTries = new PriorityQueue<JoueurOnline>(8);
		
		setBackground(Color.black);
		
		addResizeListener();
		}
		
	@Override
	protected void paintComponent(Graphics g)
		{
		
		super.paintComponent(g);
		g2d = (Graphics2D)g;
		if (readyToDraw)
			{
			draw();
			}
		else
			{
			g2d.setColor(Color.white);
			g2d.drawString("Chargement...", 0, 0);
			}
			
		}
		
	private void addResizeListener()
		{
		addComponentListener(new ComponentAdapter()
			{
			
			@Override
			public void componentResized(ComponentEvent e)
				{
				init();
				}
				
			});
		}
		
	private void init()
		{
		readyToDraw = false;
		
		frameH = getHeight();
		frameW = getWidth();
		
		h = customH;
		w = customW;
		
		tabDistStripes = new double[w];
		
		buffImgFond = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dFond = buffImgFond.createGraphics();
		
		buffImgMurs = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dMurs = buffImgMurs.createGraphics();
		
		buffImgThings = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dThings = buffImgThings.createGraphics();
		g2dThings.setFont(new Font("Helvetica", Font.PLAIN, 20));
		
		buffImgHUD = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dHUD = buffImgHUD.createGraphics();
		
		buffImgWeapon = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dWeapon = buffImgWeapon.createGraphics();
		
		buffImgImpact = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dImpact = buffImgImpact.createGraphics();
		
		buffDeathScreen = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dDeathScreen = buffDeathScreen.createGraphics();
		g2dDeathScreen.setFont(new Font("Helvetica", Font.BOLD, 60));
		
		buffImgScores = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dScores = buffImgScores.createGraphics();
		g2dScores.setFont(new Font("Helvetica", Font.BOLD, 20));
		
		buffImgDamage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g2dDamage = buffImgDamage.createGraphics();
		// cette image ne change pas, on la pr�pare qu'une fois
		prepareDeathScreenImg();
		prepareDamageScreenIMg();
		
		readyToDraw = true;
		}
		
	private void draw()
		{
		setPosition(lc.joueur.getPosition());
		setDirection(lc.joueur.getDirection());
		
		g2d.translate(frameW / 2, frameH / 2);
		// on zoom dans l'image si on vise dans la lunette du fusil de pr�cision
		if (JFrameClient.mouseRightPressed && lc.joueur.getArme() instanceof PrecisionRifle)
			{
			g2d.scale(2, 2);
			}
			
		/**
		 * Pr�paration des images
		 */
		if (MagasinImage.buffFond != null) prepareFondImg();
		prepareWallsImg();
		prepareThingsImg();
		if (!lc.joueur.getMort())
			{
			prepareImpacteMur();
			prepareImpacteEnnemi();
			prepareHUDImg();
			prepareWeaponImg();
			}
		if (lc.touchesEnfoncees.contains(KeyEvent.VK_Q)) prepareScoresImg();
		
		/**
		 * Dessin des images
		 */
		drawImage(buffImgFond);
		drawImage(buffImgMurs);
		drawImage(buffImgThings);
		if (!lc.joueur.getMort())
			{
			drawImage(buffImgImpact);
			drawImage(buffImgHUD);
			drawImage(buffImgWeapon);
			if (lc.joueur.prendDegats())
				{
				drawImage(buffImgDamage);
				}
			}
		else
			{
			drawImage(buffDeathScreen);
			}
		if (lc.touchesEnfoncees.contains(KeyEvent.VK_Q)) drawImage(buffImgScores);
		
		}
		
	private void drawImage(BufferedImage bi)
		{
		g2d.drawImage(bi, -frameW / 2, -frameH / 2, frameW, frameH, null);
		}
		
	/**
	 * 
	 * 
	 * PREPARATION
	 * 
	 * 
	 */
	
	/**
	 * Pr�pare l'image de fond. Selon la direction du joueur,
	 * l'image sera dessin�e � partir d'un offsetX proportionnel � l'angle
	 * du vecteur direction du joueur
	 */
	private void prepareFondImg()
		{
		double theta = lc.joueur.getDirection().getTheta();
		double xOffset = theta / (2 * Math.PI);
		//System.out.println(xOffset);
		double rapportWH = MagasinImage.buffFond.getWidth() / MagasinImage.buffFond.getHeight();
		double scaledHeight = h / 2.0;
		
		double scaledWidth = scaledHeight * rapportWH;
		double xImgOffset = scaledWidth * xOffset;
		
		g2dFond.drawImage(MagasinImage.buffFond, (int)-xImgOffset, 0, (int)scaledWidth, (int)scaledHeight, null);
		
		// on redessine une fois l'image si on en a fait tout le tour
		if (xImgOffset + w >= scaledWidth) g2dFond.drawImage(MagasinImage.buffFond, (int)(scaledWidth - xImgOffset), 0, (int)scaledWidth, (int)scaledHeight, null);
		}
		
	/**
	 * Pr�pare l'image des murs gr�ce � la technique du ray-casting.
	 * Cette m�thode est pleine de tricks math�matiques incompr�hensbles.
	 * Elle est adapt�e du tutoriel ray-casting : http://lodev.org/cgtutor/raycasting.html
	 */
	private void prepareWallsImg()
		{
		g2dMurs.setBackground(TRANSPARENT);
		g2dMurs.clearRect(0, 0, w, h);
		
		for(int x = 0; x < w; x++)
			{
			double cameraX = 2 * x / (double)w - 1;
			double rayPosX = pos.getdX();
			double rayPosY = pos.getdY();
			double rayDirX = dir.getdX() + plane.getdX() * cameraX;
			double rayDirY = dir.getdY() + plane.getdY() * cameraX;
			
			int mapX = (int)rayPosX;
			int mapY = (int)rayPosY;
			
			double sideDistX;
			double sideDistY;
			
			double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
			double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
			double perpWallDist;
			
			int stepX;
			int stepY;
			
			int hit = 0;
			int side = 0;
			
			if (rayDirX < 0)
				{
				stepX = -1;
				sideDistX = (rayPosX - mapX) * deltaDistX;
				}
			else
				{
				stepX = 1;
				sideDistX = (mapX + 1.0 - rayPosX) * deltaDistX;
				}
			if (rayDirY < 0)
				{
				stepY = -1;
				sideDistY = (rayPosY - mapY) * deltaDistY;
				}
			else
				{
				stepY = 1;
				sideDistY = (mapY + 1.0 - rayPosY) * deltaDistY;
				}
			int step = 0;
			while(hit == 0 && ++step < 1000)
				{
				if (sideDistX < sideDistY)
					{
					sideDistX += deltaDistX;
					mapX += stepX;
					side = 0;
					}
				else
					{
					sideDistY += deltaDistY;
					mapY += stepY;
					side = 1;
					}
				if (lc.map.inWall(mapX, mapY))
					{
					hit = 1;
					}
				}
			if (side == 0)
				{
				perpWallDist = (mapX - rayPosX + (1 - stepX) / 2) / rayDirX;
				}
			else
				{
				perpWallDist = (mapY - rayPosY + (1 - stepY) / 2) / rayDirY;
				}
			int lineHeight = (int)(h / perpWallDist);
			tabDistStripes[x] = perpWallDist;
			
			int drawStart = -lineHeight / 2 + h / 2;
			if (drawStart < 0) drawStart = 0;
			int drawEnd = lineHeight / 2 + h / 2;
			if (drawEnd >= h) drawEnd = h - 1;
			
			double wallX; // where exactly the wall was hit
			if (side == 0) wallX = rayPosY + perpWallDist * rayDirY;
			else wallX = rayPosX + perpWallDist * rayDirX;
			wallX -= Math.floor((wallX));
			
			int texX = (int)(wallX * texWidth);
			if (side == 0 && rayDirX > 0) texX = texWidth - texX - 1;
			if (side == 1 && rayDirY < 0) texX = texWidth - texX - 1;
			
			int numeroTexture = lc.map.getTextureTab(mapX, mapY);
			
			for(int y = drawStart; y < drawEnd; y++)
				{
				int d = y * 256 - h * 128 + lineHeight * 128;
				int texY = ((d * texHeight) / lineHeight) / 256;
				if (numeroTexture >= MagasinImage.buffTextMur.length) numeroTexture = 0;
				BufferedImage img = MagasinImage.buffTextMur[numeroTexture];
				// l'operateur ternaire c'est pour �viter les ArrayIndexOutOfBound
				Color c = new Color(img.getRGB(texX > 0 ? texX : 0, texY > 0 ? texY : 0));
				if (side == 1) c = c.darker();
				buffImgMurs.setRGB(x, y, c.getRGB());
				
				}
				
			double floorXWall;
			double floorYWall; // x, y position of the floor texel at the bottom
								// of the wall
			
			// 4 different wall directions possible
			if (side == 0 && rayDirX > 0)
				{
				floorXWall = mapX;
				floorYWall = mapY + wallX;
				}
			else if (side == 0 && rayDirX < 0)
				{
				floorXWall = mapX + 1.0;
				floorYWall = mapY + wallX;
				}
			else if (side == 1 && rayDirY > 0)
				{
				floorXWall = mapX + wallX;
				floorYWall = mapY;
				}
			else
				{
				floorXWall = mapX + wallX;
				floorYWall = mapY + 1.0;
				}
				
			double distWall, distPlayer, currentDist;
			
			distWall = perpWallDist;
			distPlayer = 0.0;
			
			if (drawEnd < 0) drawEnd = h; // becomes < 0 when the integer overflows
			
			// draw the floor from drawEnd to the bottom of the screen
			for(int y = drawEnd + 1; y < h; y++)
				{
				currentDist = h / (2.0 * y - h); // you could make a small
													// lookup table for this
													// instead
				
				double weight = (currentDist - distPlayer) / (distWall - distPlayer);
				
				double currentFloorX = weight * floorXWall + (1.0 - weight) * pos.getdX();
				double currentFloorY = weight * floorYWall + (1.0 - weight) * pos.getdY();
				
				int floorTexX = (int)(currentFloorX * texWidth) % texWidth;
				int floorTexY = (int)(currentFloorY * texHeight) % texHeight;
				
				int floorTexture = lc.map.getTextureTab((int)currentFloorX, (int)currentFloorY);
				
				Color c = new Color(MagasinImage.buffTextMur[floorTexture].getRGB(floorTexX, floorTexY));
				c = c.darker();
				buffImgMurs.setRGB(x, y, c.getRGB());
				
				if (MagasinImage.buffFond == null)
					{
					c = new Color(MagasinImage.buffTextMur[floorTexture].getRGB(floorTexY, floorTexX));
					buffImgMurs.setRGB(x, h - y, c.getRGB());
					}
					
				}
			}
		}
		
	/**
	 * Pr�pare le dessin des Things, ici aussi, c'est adapt� du tutoriel
	 */
	private void prepareThingsImg()
		{
		// Ici �a permet de clear rapidement une bufferedImage
		g2dThings.setBackground(TRANSPARENT);
		g2dThings.clearRect(0, 0, w, h);
		
		Vector2D deltaPos;
		// TODO : ajouter les objets aussi dans la liste de choses a afficher
		for(JoueurOnline j:lc.joueurs.values())
			{
			
			deltaPos = j.getPosition().sub(pos);
			
			// On veut les trier dans l'ordre d�croissant, on les ajoute donc
			// dans un TreeMap selon
			// leur distance � la cam�ra. On veut que les plus loin soit les
			// derniers, d'o� le -length()
			
			if (j.id != lc.joueur.id && deltaPos.length() > 0.75 && !j.getMort()) chosesAAfficher.put(-deltaPos.length(), j);
			}
		// pareil avec les things
		for(Thing o:lc.objets)
			{
			deltaPos = o.getPosition().sub(pos);
			if (o.exists()) chosesAAfficher.put(-deltaPos.length(), o);
			}
			
		Set<Double> keys = chosesAAfficher.keySet();
		Iterator<Double> i = keys.iterator();
		Thing current;
		
		while(i.hasNext())
			{
			current = chosesAAfficher.get(i.next());
			
			deltaPos = current.getPosition().sub(pos);
			
			double l = plane.getdX() * dir.getdY() - dir.getdX() * plane.getdY();
			double[][] matrix = { { dir.getdY() / l, -dir.getdX() / l }, { -plane.getdY() / l, plane.getdX() / l } };
			Vector2D projected = deltaPos.toutdansunpackageplyByMatrice(matrix);
			double transformX = projected.getdX();
			double transformY = projected.getdY();
			
			if (current instanceof JoueurOnline)
				{
				Vector2D v2 = current.getDirection().mult(-1);
				double dx = current.getPosition().getdX() - pos.getdX();
				double dy = current.getPosition().getdY() - pos.getdY();
				Vector2D v1 = new Vector2D(dx, dy);
				double angle = Math.atan2(v2.getdY(), v2.getdX()) - Math.atan2(v1.getdY(), v1.getdX());
				if (angle < 0) angle += 2 * Math.PI;
				int nbSecteurs = current.getNbSecteurs();
				int secteur = (int)(((nbSecteurs * angle / (2 * Math.PI)) + 0.5) % nbSecteurs);
				
				currentSprite = current.getSprite(secteur);
				}
			else
				{
				currentSprite = current.getSprite();
				}
				
			int imageWidth = currentSprite.getWidth();
			int imageHeight = currentSprite.getHeight();
			
			int spriteScreenX = (int)(w / 2 * (1 + transformX / transformY));
			
			int spriteHeight = Math.abs((int)(h / transformY));
			int drawStartY = -spriteHeight / 2 + h / 2;
			if (drawStartY < 0)
				{
				drawStartY = 0;
				}
			int drawEndY = spriteHeight / 2 + h / 2;
			if (drawEndY >= h)
				{
				drawEndY = h - 1;
				}
				
			int spriteWidth = Math.abs((int)(w / transformY));
			int drawStartX = -spriteWidth / 2 + spriteScreenX;
			if (drawStartX < 0)
				{
				drawStartX = 0;
				}
			int drawEndX = spriteWidth / 2 + spriteScreenX;
			if (drawEndX >= w)
				{
				drawEndX = w - 1;
				}
				
			for(int j = drawStartX; j < drawEndX; j++)
				{
				int texX = (256 * (j - (-spriteWidth / 2 + spriteScreenX)) * imageWidth / spriteWidth) / 256;
				if (transformY > 0 && j > 0 && j < w && transformY < tabDistStripes[j])
					{
					if (current instanceof JoueurOnline)
						{
						// affichage du nom au dessus du joueur
						g2dThings.drawString(((JoueurOnline)current).pseudo, spriteScreenX, drawStartY);
						}
					for(int y = drawStartY; y < drawEndY; y++)
						{
						int d = (y) * 256 - h * 128 + spriteHeight * 128;
						int texY = ((d * imageHeight) / spriteHeight) / 256;
						
						if (texY < 0)
							{
							texY = 0;
							}
						if (texX < 0)
							{
							texX = 0;
							}
							
						// pixel invisible: 16777215
						int rgb = currentSprite.getRGB(texX, texY);
						if (rgb != 16777215)
							{
							buffImgThings.setRGB(j, y, rgb);
							}
						}
					}
				}
			}
		chosesAAfficher.clear();
		
		}
		
	/**
	 * Pr�pare le dessin du tableau des scores
	 */
	private void prepareScoresImg()
		{
		g2dScores.setBackground(TRANSPARENT);
		g2dScores.clearRect(0, 0, w, h);
		g2dScores.setColor(new Color(1f, 0f, 0f, 1f));
		
		int valTabX = w / 5;
		int valTabY = h / 3;
		int stringHei = (int)g2dScores.getFontMetrics().getStringBounds("Joueur", g2dScores).getHeight();
		
		g2dScores.drawString("Temps restant : " + lc.tempsSecondes / 60 + ":" + lc.tempsSecondes % 60, valTabX, valTabY - 2 * stringHei);
		g2dScores.drawString("Joueur", valTabX, valTabY);
		g2dScores.drawString("Kill", 3 * valTabX, valTabY);
		g2dScores.drawString("Death", 4 * valTabX, valTabY);
		
		// Trier la liste de joueur en fonction du score
		for(JoueurOnline j:lc.joueurs.values())
			{
			joueursTries.add(j);
			}
			
		int i = 0;
		JoueurOnline j;
		while(!joueursTries.isEmpty())
			{
			j = joueursTries.poll();
			g2dScores.drawString(j.pseudo, valTabX, stringHei * (i + 1) + valTabY);
			g2dScores.drawString(j.getNbKill() + "", 3 * valTabX, stringHei * (i + 1) + valTabY);
			g2dScores.drawString(j.getNbDeath() + "", 4 * valTabX, stringHei * (i + 1) + valTabY);
			i++;
			}
		}
		
	/**
	 * Pr�pare l'�cran qui s'affiche lorsqu'on meurt
	 */
	private void prepareDeathScreenImg()
		{
		Rectangle2D rectangle = new Rectangle2D.Double(0, 0, w, h);
		
		g2dDeathScreen.setColor(new Color(0f, 0f, 0f, .5f));
		g2dDeathScreen.draw(rectangle);
		g2dDeathScreen.fill(rectangle);
		
		g2dDeathScreen.setColor(new Color(1f, 0f, 0f, 1f));
		// Pour centrage:
		int stringLen = (int)g2dDeathScreen.getFontMetrics().getStringBounds(strMort, g2dDeathScreen).getWidth();
		g2dDeathScreen.drawString(strMort, w / 2 - stringLen / 2, h / 2);
		}
		
	/**
	 * Pr�pare l'effet qui se superpose � l'image quand on prend des d�g�ts
	 */
	private void prepareDamageScreenIMg()
		{
		Point2D center = new Point2D.Float(w / 2, h / 2);
		float radius = w / 2;
		float[] dist = { 0.0f, 0.9f };
		
		Color trans = new Color(1f, 1f, 1f, 0f);
		Color rouge = new Color(1f, 0f, 0f, .5f);
		
		Color[] colors = { trans, rouge };
		RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
		
		Rectangle2D rectangle = new Rectangle2D.Double(0, 0, w, h);
		g2dDamage.setPaint(gradient);
		g2dDamage.fill(rectangle);
		
		}
		
	public void prepareImpacteMur()
		{
		g2dImpact.setBackground(TRANSPARENT);
		g2dImpact.clearRect(0, 0, w, h);
		
		prepareImpact(lc.impactMurLine, lc.joueur.getArme().getSpriteImpactMur());
		}
		
	public void prepareImpacteEnnemi()
		{
		prepareImpact(lc.impactEnnemiLines, lc.joueur.getArme().getSpriteImpactEnnemi());
		}
		
	/**
	 * Pr�pare le dessin des impacts
	 */
	private void prepareImpact(ArrayList<Line2D> impactLine, BufferedImage spriteImpact)
		{
		if (lc.isFiring && !(lc.joueur.getArme() instanceof Chainsaw) && !impactLine.isEmpty())
			{
			Iterator<Line2D> iterator = impactLine.iterator();
			while(iterator.hasNext())
				{
				Line2D line = iterator.next();
				double longueurligne = line.getP1().distance(line.getP2());
				
				if (lc.joueur.getArme().computeDamage(longueurligne) > 0)
					{
					BufferedImage img = scale(spriteImpact, scaleWidth / (longueurligne / 4), scaleHeight / (longueurligne / 4));
					
					if (lc.joueur.getArme() instanceof ShootGun)
						{
						Vector2D vec = new Vector2D(line.getX2() - line.getX1(), line.getY2() - line.getY1());
						double angle = Math.atan2(vec.getdY(), vec.getdX()) - Math.atan2(lc.joueur.getDirection().getdY(), lc.joueur.getDirection().getdX());
						angle = 180 * angle / Math.PI;
						
						g2dImpact.drawImage(img, null, w / 2 - (int)(Math.tan(angle) * longueurligne) - img.getWidth() / 2, h / 2 - img.getHeight() / 2);
						
						}
					else
						{
						g2dImpact.drawImage(img, null, w / 2 - img.getWidth() / 2, h / 2 - img.getHeight() / 2);
						}
						
					}
					
				}
			}
		}
		
	/**
	 * Pr�pare le dessin de l'arme
	 */
	private void prepareWeaponImg()
		{
		
		g2dWeapon.setBackground(TRANSPARENT);
		g2dWeapon.clearRect(0, 0, w, h);
		
		if (lc.joueur.getArme() instanceof PrecisionRifle && JFrameClient.mouseRightPressed)
			{
			
			Point2D center = new Point2D.Float(w / 2, h / 2);
			float radius = w / 8;
			float[] dist = { 0.0f, 0.9f, 1.0f };
			
			Color[] colors = { TRANSPARENT, TRANSPARENT, Color.black };
			RadialGradientPaint gradient = new RadialGradientPaint(center, radius, dist, colors);
			
			Rectangle2D rectangle = new Rectangle2D.Double(0, 0, w, h);
			g2dWeapon.setPaint(gradient);
			g2dWeapon.fill(rectangle);
			
			g2dWeapon.setColor(Color.black);
			g2dWeapon.drawLine(w / 2, 0, w / 2, h);
			g2dWeapon.drawLine(0, h / 2, w, h / 2);
			
			}
		else if (lc.joueur.getArme() != null)
			{
			BufferedImage img = scale(lc.joueur.getArme().getSpriteHUD(), scaleWidth, scaleHeight);
			g2dWeapon.drawImage(img, null, w / 2 - img.getWidth() / 2, h - img.getHeight());
			
			}
			
		}
		
	/**
	 * Pr�pare le dessin du HUD
	 */
	private void prepareHUDImg()
		{
		g2dHUD.setBackground(TRANSPARENT);
		g2dHUD.clearRect(0, 0, w, h);
		
		g2dHUD.translate(w / 2, h / 2);
		g2dHUD.drawImage(scale(MagasinImage.buffHud[0], scaleWidth, scaleHeight), null, -w / 2, h / 4);
		g2dHUD.setColor(new Color(0, 97, 255));
		g2dHUD.setFont(new Font("Arial", Font.PLAIN, (int)(30 * scaleHeight)));
		g2dHUD.drawString("" + lc.joueur.getArmure(), -w / 2 + w / 10, h / 4 + h / 9);
		g2dHUD.setColor(Color.RED);
		// g2dHUD.setFont(new Font("Arial", Font.PLAIN, (int) (30 *
		// scaleHeight)));
		g2dHUD.drawString("" + lc.joueur.getVie(), -w / 2 + w / 10, h / 4 + h / 5 + h / 35);
		
		String str = "";
		
		if (lc.joueur.getArme() instanceof Axe)
			{
			str = new String("0/0");
			}
		else if (lc.joueur.getArme() != null)
			{
			str = new String(lc.joueur.getArme().getAmmo() + "/" + lc.joueur.getArme().getMaxAmmo());
			}
			
		g2dHUD.drawImage(scale(MagasinImage.buffHud[1], scaleWidth, scaleHeight), null, w / 4, h / 4);
		g2dHUD.setColor(new Color(175, 175, 175));
		g2dHUD.setFont(new Font("Arial", Font.PLAIN, (int)(25 * scaleHeight)));
		int strLen = (int)g2dHUD.getFontMetrics().getStringBounds(str, g2dHUD).getWidth();
		
		g2dHUD.drawString(str, w / 4 + w / 6 - strLen, h / 4 + w / 10 + w / 50);
		
		g2dHUD.translate(-w / 2, -h / 2);
		
		}
		
	/**
	 * Fonction utilitaire
	 */
	public void setPosition(Vector2D position)
		{
		pos = position;
		}
		
	/**
	 * Fonction utilitaire
	 */
	public void setDirection(Vector2D direction)
		{
		dir = direction;
		Vector2D vec = direction.rotate(Math.PI / 2.0);
		plane.setdX(vec.getdX());
		plane.setdY(vec.getdY());
		}
		
	/**
	 * Fonction utilitaire
	 */
	public static BufferedImage scale(BufferedImage bi, double scaleWidth2, double scaleHeight2)
		{
		int width = (int)(bi.getWidth() * scaleWidth2);
		int height = (int)(bi.getHeight() * scaleHeight2);
		BufferedImage biNew;
		try
			{
			biNew = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			}
		catch (IllegalArgumentException e)
			{
			biNew = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
			}
		Graphics2D graphics = biNew.createGraphics();
		
		graphics.drawImage(bi, 0, 0, width, height, null);
		graphics.dispose();
		return biNew;
		}
	}
