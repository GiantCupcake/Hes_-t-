
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.hearc.mapeditorraycasting.gui.thingdialog.JFrameColorInfoEditor;
import ch.hearc.mapeditorraycasting.resources.ColorInfo;

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
		colorPick = new JButton("Pick a color");
		addMonster = new JButton("Add");
		editMonster = new JButton("Edit");
		removeMonster = new JButton("Remove");

		monsterList = new JList<ColorInfo>(parent.listModel);
		monsterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		monsterList.setLayoutOrientation(JList.VERTICAL);
		// Layout : Specification
			{
			FlowLayout flowlayout = new FlowLayout(FlowLayout.CENTER);
			setLayout(flowlayout);

			// flowlayout.setHgap(20);
			// flowlayout.setVgap(20);
			}

		// JComponent : add
		add(colorPick);
		add(monsterList);
		add(addMonster);
		add(editMonster);
		add(removeMonster);
		}

	private void control()
		{
		colorPick.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				Color color = JColorChooser.showDialog(JPanelMonsterPicker.this, "ColorPicker", Color.WHITE);
				parent.setColor(color);
				}
			});

		addMonster.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				ColorInfo clean = new ColorInfo("null", Color.BLACK, "wall", "Empty");
				new JFrameColorInfoEditor(clean);
				parent.listModel.addElement(clean);
				}
			});

		editMonster.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				new JFrameColorInfoEditor(monsterList.getSelectedValue());
				}
			});

		removeMonster.addActionListener(new ActionListener()
			{

			@Override
			public void actionPerformed(ActionEvent e)
				{
				int index = monsterList.getSelectedIndex();
				parent.listModel.removeElementAt(index);
				}
			});

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
						System.out.println("Picked a " + current.ressources);
						parent.setColor(current.color);
						}
					}
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
	private JButton colorPick;
	private JFrameMainWindow parent;

	private JList<ColorInfo> monsterList;
	private JButton addMonster;
	private JButton editMonster;
	private JButton removeMonster;
	}
