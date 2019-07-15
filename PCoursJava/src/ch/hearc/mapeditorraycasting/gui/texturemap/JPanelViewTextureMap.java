package ch.hearc.mapeditorraycasting.gui.texturemap;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import ch.hearc.mapeditorraycasting.resources.MapRattco;

public class JPanelViewTextureMap extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelViewTextureMap(JFrameTextureEditor _parent)
		{
		parent = _parent;
		mapRattco = _parent.mapRattco;
		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	@Override
	protected void paintComponent(Graphics g)
		{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform oldContext = g2d.getTransform();
		draw(g2d);
		g2d.setTransform(oldContext);
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void draw(Graphics2D g2d)
		{
		float h = mapRattco.textureMap.getHeight();
		float w = mapRattco.textureMap.getWidth();
		g2d.scale(getWidth() / w, getHeight() / h);
		g2d.drawImage(mapRattco.textureMap, 0, 0, null);
		}

	private void control()
		{
		// Quand on clique sur l'image, le pixel doit se colorer.
		addMouseListener(new MouseAdapter()
			{

			@Override
			public void mousePressed(MouseEvent e)
				{
				int mapH = mapRattco.textureMap.getHeight();
				int mapW = mapRattco.textureMap.getWidth();
				float fx = e.getX() / (float)getWidth();
				float fy = e.getY() / (float)getHeight();
				int x = (int)(fx * mapW);
				int y = (int)(fy * mapH);

				//Les textures s'appliquent uniquement sur les murs
				mapRattco.setRGBATextureMap(x, y, parent.getCurrentColorRGB());

				repaint();
				}

			});

		addMouseMotionListener(new MouseMotionAdapter()
			{

			@Override
			public void mouseDragged(MouseEvent e)
				{
				int mapH = mapRattco.textureMap.getHeight();
				int mapW = mapRattco.textureMap.getWidth();
				float fx = e.getX() / (float)getWidth();
				float fy = e.getY() / (float)getHeight();
				int x = (int)(fx * mapW);
				int y = (int)(fy * mapH);

				//On colorie
				mapRattco.setRGBATextureMap(x, y, parent.getCurrentColorRGB());

				repaint();
				}
			});
		}

	private void geometry()
		{
		// JComponent : Instanciation

		// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			// flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
			}

		// JComponent : add

		}

	private void appearance()
		{
		// rien
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JFrameTextureEditor parent;
	private MapRattco mapRattco;
	}
