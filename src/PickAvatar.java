import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PickAvatar extends JPanel
{
	private PageManager pm;
	private GameData data;

	public PickAvatar(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();

		setLayout(new BorderLayout());
		setBackground(new Color(169,169,169));

		add(new AvatarPanel());
	}

	class AvatarPanel extends Panel implements MouseListener
	{
		public AvatarPanel()
		{
			setLayout(new GridLayout(1,2,100,10));
		}
		
		public Image getImage(String imgName)
		{
			Image picture = null;
			String pictName = "../storedData/images/"+imgName;

			try
			{
				picture = ImageIO.read(new File(pictName));
			}
			catch(IOException e)
			{
				System.err.println("\n\n" + pictName + " can't be found. \n\n");
				e.printStackTrace();        
			}

			return picture;
		}

	}






}
