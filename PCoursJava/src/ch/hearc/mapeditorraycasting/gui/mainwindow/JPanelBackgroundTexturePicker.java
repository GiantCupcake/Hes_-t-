
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class JPanelBackgroundTexturePicker extends Box
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelBackgroundTexturePicker(JFrameMainWindow parent)
		{
		super(BoxLayout.X_AXIS);
		this.parent = parent;
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
		modifyTexture = new JButton("Modify Background");
		clearTexture = new JButton("Clear");
		previewTexture = new JLabel("");
		if (parent.mapRattco.textureFond == null)
			{
			previewTexture.setText("No Background");
			}
		else
			{
			previewTexture.setIcon(new ImageIcon(parent.mapRattco.textureFond));
			}
		Box vbox = new Box(BoxLayout.Y_AXIS);

		Box hbox = new Box(BoxLayout.X_AXIS);
		hbox.add(modifyTexture);
		hbox.add(clearTexture);

		vbox.add(hbox);
		vbox.add(previewTexture);
		add(vbox);
		}

	private void control()
		{
		modifyTexture.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

				fc.setAcceptAllFileFilterUsed(false);

				int returnVal = fc.showOpenDialog(JPanelBackgroundTexturePicker.this);

				if (returnVal == JFileChooser.APPROVE_OPTION)
					{
					File file = new File(fc.getSelectedFile().getAbsolutePath());
					try
						{
						parent.mapRattco.textureFond = ImageIO.read(file);
						previewTexture.setIcon(new ImageIcon(parent.mapRattco.textureFond));
						previewTexture.setText("");
						repaint();
						}
					catch (IOException e1)
						{
						// TODO Auto-generated catch block
						e1.printStackTrace();
						}

					}

				}
			});

		clearTexture.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				parent.mapRattco.textureFond = null;
				previewTexture.setIcon(null);
				previewTexture.setText("No Background");

				}
			});
		}

	private void appearance()
		{
		// rien
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JFrameMainWindow parent;
	private JButton modifyTexture;
	private JButton clearTexture;
	private JLabel previewTexture;
	}
