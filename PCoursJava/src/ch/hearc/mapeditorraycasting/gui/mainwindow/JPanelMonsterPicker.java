
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.hearc.mapeditorraycasting.resources.ColorInfo;

/**
 * Interface permettant de créer et placer des monstres ou des murs sur la map
 * Fait la correspondance entre une couleur et un objet en jeu.
 *
 * @author Maxime Piergiovanni
 */
public class JPanelMonsterPicker extends JPanel
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JPanelMonsterPicker(JFrameMainWindow _parent)
		{
		parent = _parent;
		geometry();
		control();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		// JComponent : Instanciation
		addMonster = new JButton("Add");
		editMonster = new JButton("Edit");
		removeMonster = new JButton("Remove");

		monsterList = new JList<ColorInfo>(parent.listThings);
		monsterList.setVisibleRowCount(5);
		monsterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		monsterList.setLayoutOrientation(JList.VERTICAL);
		monsterList.setSelectedIndex(0);
		scrollPanel = new JScrollPane(monsterList);

		// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);
			}

		// JComponent : add
		add(scrollPanel);
		}

	private void control()
		{
		/*
		addMonster.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				ColorInfo clean = new ColorInfo("null", Color.BLACK, "Empty");
				new JFrameTextureInfoEditor(clean);
				parent.listThings.addElement(clean);
				}
			});

		editMonster.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				new JFrameTextureInfoEditor(monsterList.getSelectedValue());
				}
			});

		removeMonster.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				int index = monsterList.getSelectedIndex();
				parent.listThings.removeElementAt(index);
				}
			});
		*/
		monsterList.addListSelectionListener(new ListSelectionListener()
			{

			@Override
			public void valueChanged(ListSelectionEvent e)
				{
				if (e.getValueIsAdjusting() == false)
					{
					if (monsterList.getSelectedIndex() != -1)
						{
						ColorInfo current = monsterList.getSelectedValue();
						parent.setColor(current.color);
						}
					}
				}
			});
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JFrameMainWindow parent;

	private JList<ColorInfo> monsterList;
	private JScrollPane scrollPanel;
	private JButton addMonster;
	private JButton editMonster;
	private JButton removeMonster;
	}
