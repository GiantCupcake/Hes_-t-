
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

/**
 * Permet de visionner la map ainsi que de dessiner dessus.
 *
 * @author maxpi
 */
public class JPanelViewMap extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelViewMap(JFrameMainWindow _parent)
		{
		parent = _parent;
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
		float h = parent.mapRattco.map.getHeight();
		float w = parent.mapRattco.map.getWidth();
		g2d.scale(getWidth() / w, getHeight() / h);
		g2d.drawImage(parent.mapRattco.map, 0, 0, null);
		}

	private void control()
		{
		// Quand on clique sur l'image, le pixel doit se colorer.
		addMouseListener(new MouseAdapter()
			{

			@Override
			public void mousePressed(MouseEvent e)
				{
				int mapH = parent.mapRattco.map.getHeight();
				int mapW = parent.mapRattco.map.getWidth();
				float fx = e.getX() / (float)getWidth();
				float fy = e.getY() / (float)getHeight();
				int x = (int)(fx * mapW);
				int y = (int)(fy * mapH);

				//On colorie, un mur noir ne peut être remplacé que par du blanc
				if (parent.mapRattco.map.getRGB(x, y) == Color.BLACK.getRGB() && parent.currentColor.getRGB() != Color.white.getRGB()) { return; }
				parent.mapRattco.setRGBMap(x, y, parent.currentColor.getRGB());

				repaint();
				parent.frameTextureEditor.panelViewMap.repaint();
				}

			});

		addMouseMotionListener(new MouseMotionAdapter()
			{

			@Override
			public void mouseDragged(MouseEvent e)
				{
				int mapH = parent.mapRattco.map.getHeight();
				int mapW = parent.mapRattco.map.getWidth();
				float fx = e.getX() / (float)getWidth();
				float fy = e.getY() / (float)getHeight();
				int x = (int)(fx * mapW);
				int y = (int)(fy * mapH);

				//On colorie
				if (parent.mapRattco.map.getRGB(x, y) == Color.BLACK.getRGB() && parent.currentColor.getRGB() != Color.white.getRGB()) { return; }

				parent.mapRattco.setRGBMap(x, y, parent.currentColor.getRGB());
				repaint();
				parent.frameTextureEditor.panelViewMap.repaint();
				}
			});
		}

	private void appearance()
		{
		setMinimumSize(new Dimension(300, 200));
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	JFrameMainWindow parent;
	}
