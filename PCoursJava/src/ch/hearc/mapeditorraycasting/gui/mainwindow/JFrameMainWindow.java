
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Formatter;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import ch.hearc.mapeditorraycasting.gui.texturemap.JFrameTextureEditor;
import ch.hearc.mapeditorraycasting.resources.ColorInfo;
import ch.hearc.mapeditorraycasting.resources.MapRattco;
import ch.hearc.mapeditorraycasting.resources.TextureInfo;

/**
 * La fenêtre principale qui assurera la communication entre les différents modules
 * @author maxime piergiovanni
 */
public class JFrameMainWindow extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameMainWindow()
		{
		currentColor = Color.BLACK;
		mapRattco = new MapRattco(32, 32);
		geometry();
		control();
		appearance();
		frameTextureEditor = new JFrameTextureEditor(this);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setColor(Color c)
		{
		currentColor = c;
		}

	public void setMap(BufferedImage i)
		{
		mapRattco.map = i;
		mapRattco.generateTextureMapFromMap();
		viewMap.repaint();
		frameTextureEditor.repaint();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		instanciateList();
		// JComponent : Instanciation
		viewMap = new JPanelViewMap(this);
		monsterPicker = new JPanelMonsterPicker(this);
		backgroundTexturePicker = new JPanelBackgroundTexturePicker(this);
		generateMap = new JPanelGenerateMap(this);
		// Layout : Specification
			{
			BorderLayout borderLayout = new BorderLayout();
			setLayout(borderLayout);
			}

		eastBox = new Box(BoxLayout.Y_AXIS);
		eastBox.add(generateMap);
		eastBox.add(Box.createVerticalGlue());
		eastBox.add(monsterPicker);
		// JComponent : add
		add(viewMap, BorderLayout.CENTER);
		add(backgroundTexturePicker, BorderLayout.SOUTH);
		add(eastBox, BorderLayout.EAST);
		}

	private void control()
		{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

	private void appearance()
		{
		setTitle("Editeur de Map Raycasting");
		setSize(700, 500);
		setLocation(new Point(0, 0));
		setVisible(true); // last!
		}

	/**
	 * On instancie les items d'un projet par défaut
	 * Ici : un mur, le vide, une clé, une sortie
	 */
	private void instanciateList()
		{
		listThings = new DefaultListModel<ColorInfo>();

		listThings.addElement(new ColorInfo("wall", Color.BLACK, "Basic Wall"));
		listThings.addElement(new ColorInfo("empty", Color.WHITE, "Empty Space"));

		listThings.addElement(new ColorInfo("Medipack", Color.decode("#ff0000"), "Regain Health"));
		listThings.addElement(new ColorInfo("Armor", Color.decode("#0000ff"), "Increase Armor"));
		listThings.addElement(new ColorInfo("Spawn Point", Color.decode("#ffff00"), "One starting position"));
		listThings.addElement(new ColorInfo("Hand Gun", Color.decode("#00ff00"), "Always Handy"));
		listThings.addElement(new ColorInfo("HandGun Ammo Pack", Color.decode("#00ff50"), "Free refill"));
		listThings.addElement(new ColorInfo("Submachine Gun", Color.decode("#00ee00"), "They will run"));
		listThings.addElement(new ColorInfo("Machine Gun Ammo Pack", Color.decode("#00ee50"), "More Refill"));
		listThings.addElement(new ColorInfo("Shotgun", Color.decode("#00dd00"), "Blasting through"));
		listThings.addElement(new ColorInfo("Shotgun Ammo Pack", Color.decode("#00dd50"), "More Pellets"));
		listThings.addElement(new ColorInfo("Precision Rifle", Color.decode("#00cc00"), "Imprecision Not Implemented"));
		listThings.addElement(new ColorInfo("Precision Rifle Ammo Pack", Color.decode("#00cc50"), "One bullet Should Be Enough"));
		listThings.addElement(new ColorInfo("Chainsaw", Color.decode("#00bb00"), "If you can cut wood..."));
		listThings.addElement(new ColorInfo("Chainsaw Fuel", Color.decode("#00bb50"), "Set your chainsaw ablaze"));
		listThings.addElement(new ColorInfo("Assault Rifle", Color.decode("#00aa00"), "Specialist's Weapon"));
		listThings.addElement(new ColorInfo("Assault Rifle Ammo Pack", Color.decode("#00aa50"), "Specialist's Refill"));

		}

	/**
	 * Sauvegarde le projet
	 * Créé : 	une image(png) de la map.
	 * 			une image(png) de la map de texture
	 * 			un dossier contenant toutes les textures
	 */
	public void writeMapDirectory(String fileName) throws IOException
		{
		deleteFolder(new File("maps/" + fileName));

		File imgMap = new File("maps/" + fileName + "/map.png");
		imgMap.getParentFile().mkdirs();
		ImageIO.write(mapRattco.map, "png", imgMap);

		File imgMapTexture = new File(imgMap.getParentFile().toString() + "/mapTexture.png");
		ImageIO.write(mapRattco.textureMap, "png", imgMapTexture);

		File imgBackgroundTexture = new File(imgMap.getParentFile().toString() + "/fond.png");
		if (mapRattco.textureFond != null)
			{
			ImageIO.write(mapRattco.textureFond, "png", imgBackgroundTexture);
			}

		System.out.println(mapRattco.listTextures);

		for(TextureInfo e:mapRattco.listTextures)
			{
			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb);
			formatter.format("%03d %s", e.index, e.description);
			formatter.close();
			sb.append(".jpg");
			String textureName = sb.toString().replaceAll("\\s+", "");
			File tex = new File(imgMap.getParentFile().toString() + "/Textures/" + textureName);
			tex.getParentFile().mkdirs();

			try
				{
				BufferedImage imgTex = e.resource;
				ImageIO.write(imgTex, "jpg", tex);
				}
			catch (Exception e1)
				{
				e1.printStackTrace();
				}

			}
		}

	/*
	 *
	 * https://stackoverflow.com/questions/779519/delete-directories-recursively-in-java
	 */
	private void deleteFolder(File dir) throws IOException
		{
		if (!dir.exists()) { return; }
		System.out.println(dir.getAbsolutePath());
		Files.walkFileTree(dir.toPath(), new FileVisitor<Path>()
			{

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
				{
				System.out.println("Deleting " + file.toString());
				Files.delete(file);
				return FileVisitResult.CONTINUE;
				}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
				{
				System.out.println("Deleting" + dir.toString());
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
				}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
				{
				return FileVisitResult.CONTINUE;
				}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
				{
				return FileVisitResult.CONTINUE;
				}
			});
		}

	public void ReadMapDirectory(String directoryPath)
		{
		remove(viewMap);
		remove(monsterPicker);
		remove(generateMap);
		remove(backgroundTexturePicker);
		remove(frameTextureEditor);
		remove(eastBox);
		mapRattco.loadFromDirectory(directoryPath);
		geometry();
		control();
		appearance();
		repaint();
		frameTextureEditor.dispose();
		frameTextureEditor = new JFrameTextureEditor(this);

		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JPanelViewMap viewMap;
	private JPanelMonsterPicker monsterPicker;
	private JPanelGenerateMap generateMap;
	private JPanelBackgroundTexturePicker backgroundTexturePicker;
	private Box eastBox;
	public JFrameTextureEditor frameTextureEditor;
	public MapRattco mapRattco;
	public Color currentColor;
	public DefaultListModel<ColorInfo> listThings;
	}
