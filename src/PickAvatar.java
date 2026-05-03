import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.Border;

public class PickAvatar extends JPanel
{
	private PageManager pm;
	private GameData data;

	public PickAvatar(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();

		setLayout(new FlowLayout(FlowLayout.CENTER,100,100));
		setBackground(new Color(169,169,169));

		add(pickLabel());
		add(new AvatarsGrid());
	}

	public JLabel pickLabel()
	{
		JLabel label = new JLabel("Pick an Avatar:");
		label.setFont(pm.titleFont);
		return label;
	}

	class AvatarsGrid extends JPanel
	{
		public AvatarsGrid()
		{
			setLayout(new GridLayout(1,3,20,0));
			setOpaque(false);
			setPreferredSize(new Dimension(1200, 400));

			AvatarPanel avatar1 = new AvatarPanel(1);
			AvatarPanel avatar2 = new AvatarPanel(2);

			add(avatar1);
			JPanel placeholder = new JPanel();
			placeholder.setOpaque(false);
			add(placeholder);
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

				// 1. Create your existing thick etch
				Border etch = BorderFactory.createEtchedBorder(new Color(52, 53, 55), new Color(156, 157, 156));
				Border thickEtch = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(52, 53, 55), 3), etch);

				// 2. Create 15 pixels of internal padding
				Border padding = BorderFactory.createEmptyBorder(15, 15, 15, 15);

				// 3. Combine them: thickEtch is outer, padding is inner
				setBorder(BorderFactory.createCompoundBorder(thickEtch, padding));


				setBackground(new Color(215, 200, 169));

				addMouseListener(this);
			}

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				double scale = (double) getWidth() / avatarImg.getWidth(null);
                int finalHeight = (int) (avatarImg.getHeight(null) * scale);
                g.drawImage(avatarImg, 15, 15+((getHeight()/2)-(finalHeight/2)), getWidth()-30, finalHeight-30, this);
			}

			public void mouseClicked(MouseEvent e) 
			{
				data.setAvatar(avatar);
				pm.changePanelCard("Get New Cards 1");
			}

			public void mousePressed(MouseEvent e) {}

			public void mouseReleased(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) 
			{
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(MouseEvent e) {}
		}
	}
}
