
package ch.hearc.mapeditorraycasting.gui.mainwindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import ch.hearc.mapeditorraycasting.resources.ColorInfo;
import ch.hearc.mapeditorraycasting.tools.ImageLoader;

public class JFrameMainWindow extends JFrame
	{

	/*------------------------------------------------------------------*\
	|*							Constructeurs							*|
	\*------------------------------------------------------------------*/

	public JFrameMainWindow()
		{
		currentColor = Color.BLACK;
		map = ImageLoader.loadBufferedImage("faqfinouWorld.png");
		geometry();
		control();
		appearance();
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public void setImage(BufferedImage img)
		{
		map = img;
		viewMap.repaint();
		}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setColor(Color c)
		{
		currentColor = c;
		}

	public void setMap(BufferedImage i)
		{
		map = i;
		viewMap.repaint();
		}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	private void geometry()
		{
		instanciateList();
		// JComponent : Instanciation
		viewMap = new JPanelViewMap(this);
		monsterPicker = new JPanelMonsterPicker(this);
		generateMap = new JPanelGenerateMap(this);
		// Layout : Specification
			{
			BorderLayout borderLayout = new BorderLayout();
			setLayout(borderLayout);
			}

		// JComponent : add
		//add(TODO,BorderLayout.CENTER);
		add(viewMap, BorderLayout.CENTER);
		add(monsterPicker, BorderLayout.SOUTH);
		add(generateMap, BorderLayout.EAST);
		}

	private void control()
		{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		}

	private void appearance()
		{
		setSize(600, 400);
		setLocationRelativeTo(null); // frame centrer
		setVisible(true); // last!
		}

	private void instanciateList()
		{
		listModel = new DefaultListModel<ColorInfo>();
		listModel.addElement(new ColorInfo("wall", Color.BLACK, "wall", "Basic Wall"));
		listModel.addElement(new ColorInfo("finish", Color.RED, "thing", "Finish Point"));
		listModel.addElement(new ColorInfo("key", Color.YELLOW, "thing", "Key"));
		listModel.addElement(new ColorInfo("empty", Color.WHITE, "thing", "Empty Space"));
		}

	public void writeFile(String fileName) throws IOException
		{
		File img = new File(fileName + "/map.png");
		img.getParentFile().mkdirs();

		ImageIO.write(map, "png", img);

		try
			{
			writeXML(fileName);
			}
		catch (FactoryConfigurationError e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		catch (XMLStreamException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}

		}

	private void writeXML(String fileName) throws FactoryConfigurationError, XMLStreamException, IOException
		{
		StringWriter stringWriter = new StringWriter();

		XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

		xMLStreamWriter.writeStartDocument();
		xMLStreamWriter.writeStartElement("list");
		//Tant qu'on a des éléments dans la liste
		while(!listModel.isEmpty())
			{
			ColorInfo current = listModel.remove(0);

			xMLStreamWriter.writeStartElement("color");
			xMLStreamWriter.writeAttribute("type", current.type);
			xMLStreamWriter.writeStartElement("rgb");
			xMLStreamWriter.writeCharacters("" + current.color.getRGB());
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeStartElement("name");
			xMLStreamWriter.writeCharacters("" + current.ressources);
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeEndElement();
			}

		xMLStreamWriter.writeEndElement();
		xMLStreamWriter.writeEndDocument();

		xMLStreamWriter.flush();
		xMLStreamWriter.close();

		String xmlString = stringWriter.getBuffer().toString();

		stringWriter.close();

		File xmlFile = new File(fileName + "/infoColor.xml");

		FileWriter fw = new FileWriter(xmlFile);
		BufferedWriter bw = new BufferedWriter(fw);

		bw.write(xmlString);

		bw.close();
		fw.close();
		}

	/*------------------------------------------------------------------*\
	|*							Attributs Private						*|
	\*------------------------------------------------------------------*/

	// Tools
	private JPanelViewMap viewMap;
	private JPanelMonsterPicker monsterPicker;
	private JPanelGenerateMap generateMap;
	public BufferedImage map;
	public Color currentColor;
	public DefaultListModel<ColorInfo> listModel;
	}
