
package ch.hearc.mapeditorraycasting.gui.thingdialog;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.hearc.mapeditorraycasting.resources.ColorInfo;

public class JPanelColorInfoInput extends Box
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelColorInfoInput(ColorInfo modified, JFrameColorInfoEditor parent)
		{
		super(BoxLayout.Y_AXIS);
		this.modified = modified;
		currentColor = this.modified.color;
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
		infoLabel = new JLabel("Description : ");
		infoInput = new JTextField();
		infoInput.setMaximumSize(new Dimension(180, 20));
		infoInput.setMinimumSize(new Dimension(180, 20));
		Box nameBox = new Box(BoxLayout.X_AXIS);

		ressourceLabel = new JLabel("Path : ");
		ressourcePath = new JLabel();
		Box ressourceBox = new Box(BoxLayout.X_AXIS);

		colorLabel = new JLabel("Color : ");
		colorDisplay = new JPanel();
		colorDisplay.setMaximumSize(new Dimension(10, 10));
		colorDisplay.setMinimumSize(new Dimension(10, 10));
		colorDisplay.setBackground(currentColor);
		Box colorBox = new Box(BoxLayout.X_AXIS);

		ressourceFileChooser = new JButton("Choose Directory");
		colorPicker = new JButton("Pick a color");
		save = new JButton("Save");
		cancel = new JButton("Cancel");
		Box buttonBox = new Box(BoxLayout.X_AXIS);

		nameBox.add(infoLabel);
		nameBox.add(infoInput);

		ressourceBox.add(ressourceLabel);
		ressourceBox.add(ressourcePath);

		colorBox.add(colorLabel);
		colorBox.add(colorDisplay);

		buttonBox.add(ressourceFileChooser);
		buttonBox.add(colorPicker);
		buttonBox.add(save);
		buttonBox.add(cancel);

		add(nameBox);
		add(Box.createVerticalGlue());
		add(ressourceBox);
		add(Box.createVerticalGlue());
		add(colorBox);
		add(Box.createVerticalGlue());
		add(buttonBox);
		// JComponent : add
		//		add(nameLabel);
		//		add(nameInput);
		//		add(colorLabel);
		//		add(colorDisplay);
		//		add(colorPicker);
		//		add(save);
		}

	private void control()
		{
		colorPicker.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				Color color = JColorChooser.showDialog(JPanelColorInfoInput.this, "ColorPicker", Color.WHITE);
				currentColor = color;
				colorDisplay.setBackground(currentColor);
				}
			});

		save.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				modified.color = currentColor;
				modified.ressources = infoInput.getText();
				parent.dispose();
				}
			});

		cancel.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				parent.dispose();
				}
			});

		ressourceFileChooser.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				// TODO Auto-generated method stub
				try
					{
					findDirectoryURL();
					}
				catch (AWTException e1)
					{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					}
				}
			});
		}

	private void appearance()
		{
		// rien
		}

	private void findDirectoryURL() throws AWTException
		{
		final JFileChooser fc = new JFileChooser();

		fc.setDialogTitle("Choisissez le répertoire contenant les ressources de votre objet");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		fc.setAcceptAllFileFilterUsed(false);

		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
			{
			String path = fc.getSelectedFile().getAbsolutePath();
			modified.ressources = path;
			ressourcePath.setText(path);
			}
		}
	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private ColorInfo modified;
	private Color currentColor;

	private JLabel infoLabel;
	private JTextField infoInput;
	private JLabel ressourceLabel;
	private JLabel ressourcePath;
	private JButton ressourceFileChooser;
	private JLabel colorLabel;
	private JPanel colorDisplay;
	private JButton colorPicker;

	private JButton save;
	private JButton cancel;

	private JFrameColorInfoEditor parent;
	}
