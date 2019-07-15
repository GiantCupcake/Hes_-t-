
package ch.hearc.mapeditorraycasting.gui.texturedialog;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.hearc.mapeditorraycasting.gui.texturedialog.JFrameTextureInfoEditor.DialogMode;
import ch.hearc.mapeditorraycasting.resources.TextureInfo;
import ch.hearc.mapeditorraycasting.tools.ImageLoader;

/**
 * Interface de création d'un item.
 * Une couleur correspondra soit à un mur d'une certaine texture,
 * soit à un monstre. On veut aller chercher toutes les ressources
 * nécessaires à l'affichage des items dans un seul répertoire.
 * Ce répertoire contiendra les textures et comportements nécessaires
 * à l'affichage.
 *
 * @author Maxime Piergiovanni
 */
public class JPaneTextureInfoInput extends Box
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPaneTextureInfoInput(JFrameTextureInfoEditor parent, TextureInfo modified)
		{
		super(BoxLayout.X_AXIS);
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
		infoInput.setText(modified.description);
		infoInput.setMaximumSize(new Dimension(300, 25));
		infoInput.setMinimumSize(new Dimension(200, 25));
		infoInput.setPreferredSize(new Dimension(250, 25));
		Box infoBox = new Box(BoxLayout.X_AXIS);

		indexLabel = new JLabel("Index : ");
		indexNumberLabel = new JLabel(((Integer)modified.index).toString());
		Box indexBox = new Box(BoxLayout.X_AXIS);

		colorLabel = new JLabel("Color : ");
		colorDisplay = new JPanel();
		colorDisplay.setMaximumSize(new Dimension(10, 10));
		colorDisplay.setMinimumSize(new Dimension(10, 10));
		colorDisplay.setBackground(modified.color);
		Box colorBox = new Box(BoxLayout.X_AXIS);

		pathLabel = new JLabel("Path : ");
		path = new JLabel("");
		Box pathBox = new Box(BoxLayout.X_AXIS);

		previewLabel = new JLabel("Texture Preview :");
		displayTexture = new JLabel("");
		if (modified.resource != null)
			{
			displayTexture.setIcon(new ImageIcon(modified.resource));
			}

		Box hPreviewBox = new Box(BoxLayout.X_AXIS);
		Box vPreviewBox = new Box(BoxLayout.Y_AXIS);

		ressourceFileChooser = new JButton("Choose Directory");
		colorPicker = new JButton("Pick a color");
		save = new JButton("Save");
		cancel = new JButton("Cancel");

		Box hBox = new Box(BoxLayout.Y_AXIS);

		Box buttonBox = new Box(BoxLayout.X_AXIS);

		infoBox.add(infoLabel);
		infoBox.add(infoInput);
		infoBox.add(Box.createHorizontalGlue());

		indexBox.add(indexLabel);
		indexBox.add(indexNumberLabel);
		indexBox.add(Box.createHorizontalGlue());

		colorBox.add(colorLabel);
		colorBox.add(colorDisplay);
		colorBox.add(Box.createHorizontalGlue());

		pathBox.add(pathLabel);
		pathBox.add(path);
		pathBox.add(Box.createHorizontalGlue());

		vPreviewBox.add(previewLabel);
		vPreviewBox.add(displayTexture);
		hPreviewBox.add(vPreviewBox);
		hPreviewBox.add(Box.createHorizontalGlue());

		buttonBox.add(ressourceFileChooser);
		buttonBox.add(colorPicker);
		buttonBox.add(save);
		buttonBox.add(cancel);
		buttonBox.add(Box.createHorizontalGlue());

		hBox.add(Box.createVerticalGlue());
		hBox.add(infoBox);
		hBox.add(Box.createVerticalGlue());
		hBox.add(indexBox);
		hBox.add(Box.createVerticalGlue());
		hBox.add(colorBox);
		hBox.add(Box.createVerticalGlue());
		hBox.add(pathBox);
		hBox.add(Box.createVerticalGlue());
		hBox.add(hPreviewBox);
		hBox.add(Box.createVerticalGlue());
		hBox.add(buttonBox);

		this.add(hBox);
		this.add(Box.createHorizontalGlue());
		}

	private void control()
		{
		colorPicker.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				Color basicColor = new Color(255, 255, modified.index);
				Color color = JColorChooser.showDialog(JPaneTextureInfoInput.this, "ColorPicker", basicColor);
				//On doit modifier la couleur pour que la valeur de bleu soit egale a l-index de la couleur
				color = new Color(color.getRed(), color.getGreen(), modified.index);
				currentColor = color;
				colorDisplay.setBackground(currentColor);
				}
			});

		save.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				if (parent.mode == DialogMode.EDIT)
					{
					parent.parent.parent.mapRattco.replaceColorInTextureMap(modified.color.getRGB(), currentColor.getRGB());
					parent.parent.parent.repaint();
					}
				modified.color = currentColor;
				modified.description = infoInput.getText();
				modified.index = Integer.parseInt(indexNumberLabel.getText());

				try
					{
					BufferedImage newImage = ImageIO.read(new File(path.getText()));
					modified.resource = newImage;
					}
				catch (Exception e2)
					{
					System.out.println("Caught an exception from ImageIO.read");
					}

				if (parent.mode == DialogMode.ADD)
					{
					saveModifiedTextureInfo();
					}

				parent.parent.parent.currentColor = currentColor;
				System.out.println("Path : " + path.getText());
				System.out.println("Texture modified : " + modified.description + "\n" + "\n" + modified.color);

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
					String filePath = findDirectoryURL();
					path.setText(filePath);
					BufferedImage image = ImageLoader.loadBufferedImage(path.getText());
					displayTexture.setIcon(new ImageIcon(image));
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

	private void saveModifiedTextureInfo()
		{
		parent.parent.parent.listModelTexture.addElement(this.modified);
		parent.parent.parent.mapRattco.listTextures.add(this.modified);
		}

	private String findDirectoryURL() throws AWTException
		{
		final JFileChooser fc = new JFileChooser();

		fc.setDialogTitle("Choisissez le répertoire contenant les ressources de votre objet");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		fc.setAcceptAllFileFilterUsed(false);

		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) { return fc.getSelectedFile().getAbsolutePath(); }
		return "";
		}
	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private TextureInfo modified;
	private Color currentColor;

	private JLabel infoLabel;
	private JTextField infoInput;

	private JLabel indexLabel;
	private JLabel indexNumberLabel;
	private JButton ressourceFileChooser;

	private JLabel colorLabel;
	private JPanel colorDisplay;
	private JButton colorPicker;

	private JLabel pathLabel;
	private JLabel path;

	private JLabel previewLabel;
	private JLabel displayTexture;

	private JButton save;
	private JButton cancel;

	private JFrameTextureInfoEditor parent;
	}
