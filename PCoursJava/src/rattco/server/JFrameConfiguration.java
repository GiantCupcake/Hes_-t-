
package rattco.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import rattco.tools.JPanelDecorator;
import rattco.tools.JPanelTemps;
import rattco.tools.SpringUtilities;

/**
 *  Cette classe sert � cr�er un objet PcServer selon les informations
 *  entr�es par l'utilisateur.
 */
public class JFrameConfiguration extends JFrame
	{
	
	private static final long serialVersionUID = 2308761730886294316L;
	private static final String defaultMapName = "maps/maison";
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(JFrameConfiguration.class);
	
	private JSpinner spinNbJoueur;
	private JPanelTemps panChoixTemps;
	private JButton btnLancer;
	private JButton btnAnnuler;
	private String[] args;
	private JComboBox<String> cbbMaps;
	
	public JFrameConfiguration()
		{
		geometry();
		control();
		appearance();
		}
		
	/**
	 * Instanciation et placement des composants swings
	 */
	private void geometry()
		{
		JPanel formPanel = new JPanel(new SpringLayout());
		
		// Configuration des JSpinner
		int valMin = 2;
		int valMax = 12;
		
		SpinnerModel modelNbJoueur = new SpinnerNumberModel(8, valMin, valMax, 1);
		
		spinNbJoueur = new JSpinner(modelNbJoueur);
		
		panChoixTemps = new JPanelTemps();
		new JButton("Map");
		
		cbbMaps = new JComboBox<String>(loadFoldersFromFolder("maps"));
		
		btnLancer = new JButton("D�marrer");
		btnAnnuler = new JButton("Annuler");
		
		JLabel lblChoixMap = new JLabel("Choix de la map : ", SwingConstants.TRAILING);
		formPanel.add(lblChoixMap);
		lblChoixMap.setLabelFor(cbbMaps);
		formPanel.add(cbbMaps);
		
		JLabel lblNbJoueur = new JLabel("Maximum de joueurs : ", SwingConstants.TRAILING);
		formPanel.add(lblNbJoueur);
		lblNbJoueur.setLabelFor(spinNbJoueur);
		formPanel.add(spinNbJoueur);
		
		JLabel lblTempsPartie = new JLabel("Temps de la partie : ", SwingConstants.TRAILING);
		formPanel.add(lblTempsPartie);
		lblTempsPartie.setLabelFor(panChoixTemps);
		formPanel.add(panChoixTemps);
		
		formPanel.add(btnLancer);
		formPanel.add(btnAnnuler);
		
		SpringUtilities.makeCompactGrid(formPanel, 4, 2, // rows, cols
				6, 6, // initX, initY
				6, 6); // xPad, yPad
		
		JPanel panel = new JPanel();
		panel.add(formPanel);
		
		setContentPane(new JPanelDecorator(panel, 10));
		
		new String(defaultMapName);
		args = new String[3];
		}
		
	/**
	 * Description des actions � effectuer au clic sur l'un des boutons
	 */
	private void control()
		{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getRootPane().setDefaultButton(btnLancer);
		
		btnAnnuler.addActionListener(new ActionListener()
			{
			
			@Override
			public void actionPerformed(ActionEvent e)
				{
				dispose();
				}
			});
		
		btnLancer.addActionListener(new ActionListener()
			{
			
			@Override
			public void actionPerformed(ActionEvent e)
				{
				savePreferences();
				args[0] = "maps/" + cbbMaps.getSelectedItem().toString();
				args[1] = spinNbJoueur.getValue() + "";
				args[2] = panChoixTemps.getTempsSecondes() + "";
				new PcServer(args);
				dispose();
				
				}
			});
		
		}
		
	private void appearance()
		{
		setSize(350, 200);
		setResizable(false);
		setTitle("Configuration");
		
		loadPreferences();
		setVisible(true); // last!
		}
		
	private void savePreferences()
		{
		PREFERENCES.putInt("px", (int)this.getLocation().getX());
		PREFERENCES.putInt("py", (int)this.getLocation().getY());
		PREFERENCES.putInt("minutes", panChoixTemps.getMinute());
		PREFERENCES.putInt("secondes", panChoixTemps.getSecondes());
		PREFERENCES.putInt("nbJoueurs", (Integer)spinNbJoueur.getValue());
		}
		
	private void loadPreferences()
		{
		int px = PREFERENCES.getInt("px", 0);
		int py = PREFERENCES.getInt("py", 0);
		if (px + py == 0)
			{
			setLocationRelativeTo(null); // frame centrer
			}
		else
			{
			setLocation(px, py);
			}
			
		int minutes = PREFERENCES.getInt("minutes", 5);
		int secondes = PREFERENCES.getInt("secondes", 0);
		int joueurs = PREFERENCES.getInt("nbJoueurs", 8);
		
		panChoixTemps.setMinute(minutes);
		panChoixTemps.setSeconde(secondes);
		spinNbJoueur.setValue(joueurs);
		
		}
		
	/**
	 * retourne un tableau contenant les noms des dossiers contenu dans le dossier
	 * donn� en param�tre.
	 * Cela permet de remplir la liste des maps en fonctions des maps pr�sente dans
	 * le dossier maps � la racine du dossier du jar
	 */
	public static String[] loadFoldersFromFolder(String folderName)
		{
		File file = new File(folderName);
		String[] directories = file.list(new FilenameFilter()
			{
			
			@Override
			public boolean accept(File current, String name)
				{
				return new File(current, name).isDirectory();
				}
			});
		return directories;
		}
	}
