
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

public class JPanelViewMap extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelViewMap(JFrameMainWindow _parent)
		{
		parent = _parent;
		//Par défaut Black.
		pressed = false;
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

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/


	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/



	private void draw(Graphics2D g2d)
		{
		float h = parent.map.getHeight();
		float w = parent.map.getWidth();
		g2d.scale(getWidth() / w, getHeight() / h);
		g2d.drawImage(parent.map, 0, 0, null);
		}

	private void geometry()
		{

		}

	private void control()
		{
		// Quand on clique sur l'image, le pixel doit être coloré.
		addMouseListener(new MouseAdapter()
			{

			@Override
			public void mousePressed(MouseEvent e)
				{
				pressed = true;
				int mapH = parent.map.getHeight();
				int mapW = parent.map.getWidth();
				float fx = e.getX() / (float)getWidth();
				float fy = e.getY() / (float)getHeight();
				int x = (int)(fx * mapW);
				int y = (int)(fy * mapH);

				//On colorie
				parent.map.setRGB(x, y, parent.currentColor.getRGB());
				repaint();
				}

			@Override
			public void mouseReleased(MouseEvent e)
				{
				pressed = false;
				}

			});

		addMouseMotionListener(new MouseMotionAdapter()
			{


			@Override
			public void mouseDragged(MouseEvent e)
				{
				int mapH = parent.map.getHeight();
				int mapW = parent.map.getWidth();
				float fx = e.getX() / (float)getWidth();
				float fy = e.getY() / (float)getHeight();
				int x = (int)(fx * mapW);
				int y = (int)(fy * mapH);

				//On colorie
				parent.map.setRGB(x, y, parent.currentColor.getRGB());
				repaint();
				}
			});
		}

	private void appearance()
		{

		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	JFrameMainWindow parent;
	boolean pressed;
	}
