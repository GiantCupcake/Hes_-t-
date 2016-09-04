
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.hearc.mapeditorraycasting.tools.DungeonBuilder;
import ch.hearc.mapeditorraycasting.tools.ImageLoader;

public class JPanelGenerateMap extends Box
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelGenerateMap(JFrameMainWindow _parent)
		{
		super(BoxLayout.Y_AXIS);
		parent = _parent;
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
		widthLabel = new JLabel("Width: ");
		SpinnerNumberModel widthModel = new SpinnerNumberModel(120, 8, 1024, 1);
		widthInput = new JSpinner(widthModel);

		widthBox = Box.createHorizontalBox();

		heightLabel = new JLabel("Height: ");
		SpinnerNumberModel heightModel = new SpinnerNumberModel(120, 8, 1024, 1);
		heightInput = new JSpinner(heightModel);
		heightBox = Box.createHorizontalBox();

		fillLabel = new JLabel("Fill (%) : ");
		SpinnerNumberModel fillModel = new SpinnerNumberModel(40, 0, 100, 1);
		fillInput = new JSpinner(fillModel);
		fillBox = Box.createHorizontalBox();

		saveButton = new JButton("Save");
		loadButton = new JButton("Load");
		generateButton = new JButton("Generate");
		buttonBox = Box.createHorizontalBox();

		// JComponent : add
		widthBox.add(widthLabel);
		widthBox.add(widthInput);

		heightBox.add(heightLabel);
		heightBox.add(heightInput);

		fillBox.add(fillLabel);
		fillBox.add(fillInput);

		buttonBox.add(generateButton);
		buttonBox.add(saveButton);
		buttonBox.add(loadButton);

		add(widthBox);
		add(Box.createVerticalGlue());
		add(heightBox);
		add(Box.createVerticalGlue());
		add(fillBox);
		add(Box.createVerticalGlue());
		add(buttonBox);
		}

	private void control()
		{
		generateButton.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				//Fonction de génération de map, puis on set la BufferedImage
				generateRandom();
				}

			});

		loadButton.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				//load un PNG dans la bufferedImage
				try
					{
					loadFile();
					}
				catch (AWTException e1)
					{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					}
				}

			});

		saveButton.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				// Sauvegarde du PNG
				try
					{
					parent.writeFile("TestLevel");
					}
				catch (IOException e1)
					{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					}
				}
			});
		}

	private void appearance()
		{
		this.setBackground(Color.CYAN);
		}

	private void generateRandom()
		{
		int h = (Integer)heightInput.getValue();
		int w = (Integer)widthInput.getValue();
		System.out.println(h);
		System.out.println(w);
		DungeonBuilder db = new DungeonBuilder(h, w, 1000);
		parent.setMap(db.getMap());
		}

	private void generateVoid(int h, int w)
		{
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		int x, y;

		for(x = 0; x < w; x++)
			{
			for(y = 0; y < h; y++)
				{
				img.setRGB(x, y, Color.WHITE.getRGB());
				}
			}

		for(x = 0, y = 0; x < w; x++)
			{
			img.setRGB(x, y, Color.BLACK.getRGB());
			}
		x = w - 1;
		for(; y < h; y++)
			{
			img.setRGB(x, y, Color.BLACK.getRGB());
			}
		y = h - 1;
		for(; x > 0; x--)
			{
			img.setRGB(x, y, Color.BLACK.getRGB());
			}
		for(; y > 0; y--)
			{
			img.setRGB(x, y, Color.BLACK.getRGB());
			}

		parent.setMap(img);
		}

	private void loadFile() throws AWTException
		{
		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Choisissez une carte, cliquez sur \"Annuler\" pour charger la map par défaut");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Map images", "png", "jpg", "gif");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
			{
			BufferedImage img = ImageLoader.loadBufferedImage(fc.getSelectedFile().getAbsolutePath());
			parent.setImage(img);
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JLabel widthLabel;
	private JSpinner widthInput;
	private Box widthBox;
	private JLabel heightLabel;
	private JSpinner heightInput;
	private Box heightBox;
	private JLabel fillLabel;
	private JSpinner fillInput;
	private Box fillBox;
	private JButton saveButton;
	private JButton generateButton;
	private JButton loadButton;
	private Box buttonBox;

	private JFrameMainWindow parent;
	}
