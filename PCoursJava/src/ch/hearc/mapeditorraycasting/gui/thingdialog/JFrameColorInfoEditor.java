
package ch.hearc.mapeditorraycasting.gui.thingdialog;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import ch.hearc.mapeditorraycasting.resources.ColorInfo;

public class JFrameColorInfoEditor extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameColorInfoEditor(ColorInfo colorInfo)
		{
		this.modified = colorInfo;
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
		panel = new JPanelColorInfoInput(modified, this);
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
		setTitle("Thing Editor");
		setSize(255, 115);
		setLocationRelativeTo(null); // frame centrer
		setVisible(true); // last!
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JPanelColorInfoInput panel;
	private ColorInfo modified;
	}
