
package ch.hearc.mapeditorraycasting.gui.texturemap;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.hearc.mapeditorraycasting.gui.texturedialog.JFrameTextureInfoEditor;
import ch.hearc.mapeditorraycasting.gui.texturedialog.JFrameTextureInfoEditor.DialogMode;
import ch.hearc.mapeditorraycasting.resources.TextureInfo;

public class JPanelTexturePick extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelTexturePick(JFrameTextureEditor _parent)
		{
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
		addTexture = new JButton("Add");
		editTexture = new JButton("Edit");
		removeTexture = new JButton("Remove");

		textureList = new JList<TextureInfo>(parent.listModelTexture);
		textureList.setVisibleRowCount(5);
		textureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		textureList.setLayoutOrientation(JList.VERTICAL);
		textureList.setSelectedIndex(0);
		scrollPanel = new JScrollPane(textureList);

		// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);
			}

		// JComponent : add
		add(scrollPanel);
		add(addTexture);
		add(editTexture);
		add(removeTexture);
		}

	private void control()
		{

		addTexture.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				//On cherche dans la liste de TextureInfo le premier indice libre
				int newIndex = findFirstAvailableIndex();

				Color newCol = new Color(255, 255, newIndex);
				TextureInfo clean = new TextureInfo(newCol, newIndex, "", null);
				new JFrameTextureInfoEditor(clean, JPanelTexturePick.this, DialogMode.ADD);
				}
			});

		editTexture.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				TextureInfo toEdit = textureList.getSelectedValue();
				new JFrameTextureInfoEditor(toEdit, JPanelTexturePick.this, DialogMode.EDIT);
				System.out.println("Out of there");
				}
			});

		removeTexture.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				int index = textureList.getSelectedIndex();

				parent.mapRattco.listTextures.remove(textureList.getSelectedValue());
				parent.listModelTexture.removeElementAt(index);
				}
			});

		textureList.addListSelectionListener(new ListSelectionListener()
			{

			@Override
			public void valueChanged(ListSelectionEvent e)
				{
				if (e.getValueIsAdjusting() == false)
					{
					if (textureList.getSelectedIndex() != -1)
						{
						TextureInfo current = textureList.getSelectedValue();
						parent.setColor(current.color);
						System.out.println(current.description + "   " + current.color);
						}
					}
				}
			});
		}

	private void appearance()
		{
		// rien
		}

	private int findFirstAvailableIndex()
		{
		TreeSet<Integer> taken = new TreeSet<Integer>();
		//On mets dans taken tous les indexs utilises
		for(TextureInfo e:parent.mapRattco.listTextures)
			{
			taken.add(e.index);
			}
		//On trouve le premier indice i non contenu dans taken
		int i = 0;
		while(i < 256)
			{
			//Si i n'est pas pris
			if (!taken.contains(i)) { return i; }
			i++;
			}
		return -1;
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	public JFrameTextureEditor parent;
	private JButton addTexture;
	private JButton editTexture;
	private JButton removeTexture;

	private JList<TextureInfo> textureList;
	private JScrollPane scrollPanel;
	}
