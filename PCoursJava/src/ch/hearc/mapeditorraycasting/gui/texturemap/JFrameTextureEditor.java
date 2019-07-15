
package ch.hearc.mapeditorraycasting.gui.texturemap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import ch.hearc.mapeditorraycasting.gui.mainwindow.JFrameMainWindow;
import ch.hearc.mapeditorraycasting.resources.MapRattco;
import ch.hearc.mapeditorraycasting.resources.TextureInfo;

public class JFrameTextureEditor extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameTextureEditor(JFrameMainWindow _parent)
		{
		parent = _parent;
		mapRattco = _parent.mapRattco;
		initTextureList();
		currentColor = MapRattco.COLOR_GROUND_DEFAULT;
		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setColor(Color color)
		{
		// TODO Auto-generated method stub
		this.currentColor = color;
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public int getCurrentColorRGB()
		{
		return this.currentColor.getRGB();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	public void initTextureList()
		{
		//Quand on passe par init, deux couleurs de base, blanc et noir
		//blanc => sol de base. noir => mur de base
		listModelTexture = new DefaultListModel<TextureInfo>();

		for(TextureInfo e:parent.mapRattco.listTextures)
			{
			listModelTexture.addElement(e);
			}
		}

	private void geometry()
		{
		// JComponent : Instanciation
		panelViewMap = new JPanelViewTextureMap(this);
		panelPicker = new JPanelTexturePick(this);
		// Layout : Specification
			{
			BorderLayout borderLayout = new BorderLayout();
			setLayout(borderLayout);

			// borderLayout.setHgap(20);
			// borderLayout.setVgap(20);
			}

		// JComponent : add
		add(panelViewMap, BorderLayout.CENTER);
		add(panelPicker, BorderLayout.SOUTH);
		}

	private void control()
		{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

	private void appearance()
		{
		setSize(600, 500);
		setLocation(new Point(parent.getWidth(), 0));
		setVisible(true); // last!
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/
	private JFrameMainWindow parent;

	public JPanelViewTextureMap panelViewMap;
	private JPanelTexturePick panelPicker;
	public DefaultListModel<TextureInfo> listModelTexture;
	public Color currentColor;
	public MapRattco mapRattco;
	}
