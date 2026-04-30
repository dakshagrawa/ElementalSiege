import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.Border;

public class GetNewCards extends JPanel
{
	private PageManager pm;
	private GameData data;

	public GetNewCards(PageManager PageMngr)
	{
		pm = PageMngr;
		data = pm.getGameData();

		setLayout(new FlowLayout(FlowLayout.CENTER,100,100));
		setBackground(new Color(169,169,169));

		add(pickLabel());
		add(new CharacterCardsGrid());
	}

	public JLabel pickLabel()
	{
		JLabel label = new JLabel("Pick 1 Card:");
		label.setFont(pm.titleFont);
		return label;
	}

	class CharacterCardsGrid extends JPanel
	{
		public CharacterCardsGrid()
		{
			setLayout(new GridLayout(1,3,20,0));
			setOpaque(false);
			setPreferredSize(new Dimension(1200, 400));

			CharacterPanel character1 = new CharacterPanel();
			CharacterPanel character2 = new CharacterPanel();
			CharacterPanel character3 = new CharacterPanel();

			add(character1);
			JPanel placeholder = new JPanel();
			placeholder.setOpaque(false);
			add(placeholder);
			add(character2);
			add(placeholder);
			add(character3);
		}

		class CharacterPanel extends JPanel implements MouseListener
		{
			private int character;
			private Image characterImg;
			private String imgName;

			public CharacterPanel()
			{
				character = (int)(Math.random()*10);

				characterImg = PageManager.Functions.getImage(imgName);

				// 1. Create your existing thick etch
				Border etch = BorderFactory.createEtchedBorder(new Color(52, 53, 55), new Color(156, 157, 156));
				Border thickEtch = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(52, 53, 55), 3), etch);

				// 2. Create 15 pixels of internal padding
				Border padding = BorderFactory.createEmptyBorder(15, 15, 15, 15);

				// 3. Combine them: thickEtch is outer, padding is inner
				this.setBorder(BorderFactory.createCompoundBorder(thickEtch, padding));


				setBackground(new Color(215, 200, 169));

				addMouseListener(this);
			}

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				double scale = (double) getWidth() / characterImg.getWidth(null);
                int finalHeight = (int) (characterImg.getHeight(null) * scale);
                g.drawImage(characterImg, 15, 15+((getHeight()/2)-(finalHeight/2)), getWidth()-30, finalHeight-30, this);
			}

			public void mouseClicked(MouseEvent e) 
			{
				data.setAvatar(character);
				pm.changeCard("Get New Cards"); // TODO: complete this cardname
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
