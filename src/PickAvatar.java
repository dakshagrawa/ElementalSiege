import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class PickAvatar extends JPanel
{
	private PageManager pm;
	private GameData data;

	public PickAvatar(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();

		setLayout(new GridBagLayout());
		setBackground(new Color(30, 30, 50));

		JPanel card = new JPanel(new BorderLayout(0, 20));
		card.setBackground(new Color(22, 22, 40, 220));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(255, 255, 255, 60), 1),
			BorderFactory.createEmptyBorder(30, 40, 30, 40)
		));
		card.setPreferredSize(new Dimension(900, 480));

		// Title
		JLabel titleLabel = new JLabel("Choose Your Avatar", SwingConstants.CENTER);
		titleLabel.setFont(pm.titleFont);
		titleLabel.setForeground(Color.WHITE);
		card.add(titleLabel, BorderLayout.NORTH);

		// Avatars grid
		card.add(new AvatarsGrid(), BorderLayout.CENTER);

		add(card);
	}

	class AvatarsGrid extends JPanel
	{
		public AvatarsGrid()
		{
			setLayout(new GridLayout(1, 3, 40, 0));
			setOpaque(false);

			add(new AvatarPanel(1));
			JPanel placeholder = new JPanel();
			placeholder.setOpaque(false);
			add(placeholder);
			add(new AvatarPanel(2));
		}

		class AvatarPanel extends JPanel implements MouseListener
		{
			private int avatar;
			private Image avatarImg;

			public AvatarPanel(int avatarInt)
			{
				avatar = avatarInt;

				String imgName = (avatar == 1) ? "femaleAvatar.png" : "maleAvatar.png";
				avatarImg = PageManager.Functions.getImage(imgName);

				setBackground(new Color(40, 40, 60));
				setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 2),
					BorderFactory.createEmptyBorder(15, 15, 15, 15)
				));
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				addMouseListener(this);
			}

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				if (avatarImg == null) return;
				double scale = (double) getWidth() / avatarImg.getWidth(null);
				int finalHeight = (int) (avatarImg.getHeight(null) * scale);
				g.drawImage(avatarImg, 15, 15 + ((getHeight() / 2) - (finalHeight / 2)), getWidth() - 30, finalHeight - 30, this);
			}

			public void mouseClicked(MouseEvent e)
			{
				pm.changePanelCard("Get New Cards 1");
			}

			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}

			public void mouseEntered(MouseEvent e)
			{
				setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(100, 180, 255), 3),
					BorderFactory.createEmptyBorder(15, 15, 15, 15)
				));
				repaint();
			}

			public void mouseExited(MouseEvent e)
			{
				setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(255, 255, 255, 80), 2),
					BorderFactory.createEmptyBorder(15, 15, 15, 15)
				));
				repaint();
			}
		}
	}
}
