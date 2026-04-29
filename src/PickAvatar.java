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

		add(new AvatarsGrid());
	}

	class AvatarsGrid extends JPanel
	{
		public AvatarsGrid()
		{
			setLayout(new GridLayout(1,2,100,0));

			AvatarPanel avatar1 = new AvatarPanel(1);
			AvatarPanel avatar2 = new AvatarPanel(2);

			add(avatar1);
			add(avatar2);
		}

		class AvatarPanel extends JPanel implements MouseListener
		{
			private int avatar;
			private Image avatarImg;
			private String imgName;

			public AvatarPanel(int avatarInt)
			{
				avatar = avatarInt;
				
				if(avatar==1)
					imgName = "femaleAvatar.png";
				else if(avatar==2)
					imgName = "maleAvatar.png";

				avatarImg = PageManager.Functions.getImage(imgName);
			}

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.drawImage(avatarImg, 0,0, 200,avatarImg.getHeight(null),this);
			}

			public void mouseClicked(MouseEvent e) 
			{
				data.setAvatar(avatar);
			}

			public void mousePressed(MouseEvent e) {}

			public void mouseReleased(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {}

			public void mouseExited(MouseEvent e) {}
		}
	}
}
