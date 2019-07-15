
package ch.hearc.mapeditorraycasting;

import java.io.IOException;

import rattco.client.PcClient;
import rattco.server.PcServer;

public class LaunchLocalServerAndConnect
	{

	/*------------------------------------------------------------------*\
	|*							Methodes Public							*|
	\*------------------------------------------------------------------*/

	public static void main(String[] args)
		{
		launch("maps/dust");
		}

	public static void launch(String map)
		{
		String[] args = (map + " 8 1000").split(" ");

		try
			{
			new PcServer(args);
			}
		catch (Exception e)
			{
			// TODO: handle exception
			}

		try
			{
			Thread.sleep(2000);
			new PcClient("127.0.0.1", "Max", 320);
			}
		catch (InterruptedException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		catch (IOException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}

	/*------------------------------------------------------------------*\
	|*							Methodes Private						*|
	\*------------------------------------------------------------------*/

	}
