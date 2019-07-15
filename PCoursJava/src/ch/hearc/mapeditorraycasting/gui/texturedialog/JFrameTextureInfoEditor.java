
package ch.hearc.mapeditorraycasting.gui.texturedialog;

import java.awt.BorderLayout;

import javax.swing.JDialog;

import ch.hearc.mapeditorraycasting.gui.texturemap.JPanelTexturePick;
import ch.hearc.mapeditorraycasting.resources.TextureInfo;

public class JFrameTextureInfoEditor extends JDialog
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameTextureInfoEditor(TextureInfo textureInfo, JPanelTexturePick jPanelTexturePick, DialogMode mode)
		{
		this.modified = textureInfo;
		this.parent = jPanelTexturePick;
		this.mode = mode;
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

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		// JComponent : Instanciation
		panel = new JPaneTextureInfoInput(this, modified);
		// Layout : Specification
			{
			BorderLayout borderLayout = new BorderLayout();
			setLayout(borderLayout);

			// borderLayout.setHgap(20);
			// borderLayout.setVgap(20);
			}

		// JComponent : add
		add(panel, BorderLayout.CENTER);
		}

	private void control()
		{

		}

	private void appearance()
		{
		setTitle("Texture Editor");
		setSize(400, 450);
		setLocationRelativeTo(null); // frame centrer
		setVisible(true); // last!
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	public JPanelTexturePick parent;
	private JPaneTextureInfoInput panel;
	private TextureInfo modified;

	public DialogMode mode;

	static public enum DialogMode
		{
		ADD, EDIT
		}
	}
