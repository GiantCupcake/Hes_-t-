
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ch.hearc.mapeditorraycasting.LaunchLocalServerAndConnect;
import ch.hearc.mapeditorraycasting.tools.DungeonBuilder;

/**
 * Interface de création de carte.
 * Permet à l'utilisateur de changer les paramètres de génération de carte
 * par l'algorithme.
 *
 * @author Maxime Piergiovanni
 */
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
		nameLabel = new JLabel("Level Name : ");
		nameInput = new JTextField("FaqfinouLevel", 12);
		nameInput.setMaximumSize(new Dimension(100, 20));
		nameInput.setMinimumSize(new Dimension(100, 20));
		nameBox = Box.createHorizontalBox();

		widthLabel = new JLabel("Width: ");
		SpinnerNumberModel widthModel = new SpinnerNumberModel(32, 8, 2048, 1);
		widthInput = new JSpinner(widthModel);
		widthInput.setMaximumSize(new Dimension(100, 20));
		widthInput.setMinimumSize(new Dimension(100, 20));
		widthBox = Box.createHorizontalBox();

		heightLabel = new JLabel("Height: ");
		SpinnerNumberModel heightModel = new SpinnerNumberModel(32, 8, 2048, 1);
		heightInput = new JSpinner(heightModel);
		heightBox = Box.createHorizontalBox();
		heightInput.setMaximumSize(new Dimension(100, 20));
		heightInput.setMinimumSize(new Dimension(100, 20));

		typeInput = new ButtonGroup();
		typeEmpty = new JRadioButton("Empty");
		typeDungeon = new JRadioButton("Dungeon");
		typeInput.add(typeEmpty);
		typeInput.add(typeDungeon);
		typeDungeon.setSelected(true);
		typeBox = new Box(BoxLayout.X_AXIS);

		fillLabel = new JLabel("N° of items : ");
		SpinnerNumberModel fillModel = new SpinnerNumberModel(8, 0, Integer.MAX_VALUE, 1);
		fillInput = new JSpinner(fillModel);
		fillBox = Box.createHorizontalBox();
		fillInput.setMaximumSize(new Dimension(100, 20));
		fillInput.setMinimumSize(new Dimension(100, 20));

		saveButton = new JButton("Save");
		loadButton = new JButton("Load");
		generateButton = new JButton("Generate");
		testButton = new JButton("Test");
		buttonBox = Box.createHorizontalBox();

		// JComponent : add
		nameBox.add(nameLabel);
		nameBox.add(Box.createHorizontalGlue());
		nameBox.add(nameInput);

		widthBox.add(widthLabel);
		widthBox.add(Box.createHorizontalGlue());
		widthBox.add(widthInput);

		heightBox.add(heightLabel);
		heightBox.add(Box.createHorizontalGlue());
		heightBox.add(heightInput);

		typeBox.add(typeEmpty);
		typeBox.add(typeDungeon);

		fillBox.add(fillLabel);
		fillBox.add(Box.createHorizontalGlue());
		fillBox.add(fillInput);

		buttonBox.add(generateButton);
		buttonBox.add(saveButton);
		buttonBox.add(loadButton);
		buttonBox.add(testButton);

		add(nameBox);
		add(widthBox);
		add(heightBox);
		add(typeBox);
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
				if (typeDungeon.isSelected())
					{
					generateDungeon();
					}
				else
					{
					int h = (Integer)heightInput.getValue();
					int w = (Integer)widthInput.getValue();
					parent.setMap(DungeonBuilder.generateVoid(w, h));
					}
				}

			});

		loadButton.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				try
					{
					loadDirectory();
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
					parent.writeMapDirectory(nameInput.getText());
					}
				catch (IOException e1)
					{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					}
				}
			});

		testButton.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				saveButton.doClick();
				LaunchLocalServerAndConnect.launch("maps/" + nameInput.getText());

				}
			});
		}

	private void appearance()
		{
		this.setBackground(Color.CYAN);
		}

	/**
	 * Génère un donjon d'une certaine taille et contenant un certain nombre de features
	 * ici les features sont : soit une salle, soit un couloir.
	 */
	private void generateDungeon()
		{
		int h = (Integer)heightInput.getValue();
		int w = (Integer)widthInput.getValue();
		int fill = (Integer)fillInput.getValue();
		DungeonBuilder db = new DungeonBuilder(h, w, fill);
		parent.setMap(db.getMap());
		}

	/**
	 * On peut charger une image déjà existante
	 */
	private void loadDirectory() throws AWTException
		{
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Choisissez une map");
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
			{
			parent.ReadMapDirectory(fc.getSelectedFile().getAbsolutePath());
			}
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JLabel nameLabel;
	private JTextField nameInput;
	private Box nameBox;

	private JLabel widthLabel;
	private JSpinner widthInput;
	private Box widthBox;

	private JLabel heightLabel;
	private JSpinner heightInput;
	private Box heightBox;

	private ButtonGroup typeInput;
	private JRadioButton typeEmpty;
	private JRadioButton typeDungeon;
	private Box typeBox;

	private JLabel fillLabel;
	private JSpinner fillInput;
	private Box fillBox;

	private JButton saveButton;
	private JButton generateButton;
	private JButton loadButton;
	private JButton testButton;
	private Box buttonBox;

	private JFrameMainWindow parent;
	}
