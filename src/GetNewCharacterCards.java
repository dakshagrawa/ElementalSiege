import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import javax.swing.border.Border;

public class GetNewCharacterCards extends JPanel
{
	private PageManager pm;
	private GameData data;
	private int thisPageNumber;
	private String nextPage;

	public GetNewCharacterCards(PageManager PageMngr, int thisPanelNumber)
	{
		pm = PageMngr;
		data = pm.getGameData();
		thisPageNumber = thisPanelNumber;

		setLayout(new FlowLayout(FlowLayout.CENTER,100,100));
		setBackground(new Color(169,169,169));

		JPanel adjusterPanel = new JPanel(new BorderLayout());
		add(pickLabel());
		add(new CharacterCardsGrid());
	}
	public GetNewCharacterCards(PageManager PageMngr, String nextPageName)
	{
		pm = PageMngr;
		data = pm.getGameData();
		nextPage = nextPageName;

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

			int[] usedCharacterIdx = new int[3];
			for(int i = 0; i < usedCharacterIdx.length; i++)
			{
				int randomIdx;
				boolean foundRepetition;
				do
				{
					randomIdx = (int)(Math.random()*data.characters.length);
					foundRepetition = false;
					for(int j = 0; j < i && !foundRepetition; j++)
					{
						if(usedCharacterIdx[j] == randomIdx)
						{
							foundRepetition = true;
						}
					}
				} while (foundRepetition);
				usedCharacterIdx[i] = randomIdx;
			}

			CharacterPanel character1 = new CharacterPanel(usedCharacterIdx[0]);
			CharacterPanel character2 = new CharacterPanel(usedCharacterIdx[1]);
			CharacterPanel character3 = new CharacterPanel(usedCharacterIdx[2]);

			add(character1);
			add(character2);
			add(character3);

			setPreferredSize(new Dimension((data.characters[0].width+15)*3, data.characters[0].height));
		} 
	}
	public class CharacterPanel extends JPanel implements MouseListener
	{
		private int character;
		private GameData.CharacterImage characterImg;

		public CharacterPanel(int characterIdx)
		{
			character = characterIdx;
			characterImg = data.characters[character];

			setLayout(new BorderLayout()); // Use BorderLayout to center the label
			setBackground(new Color(215, 200, 169));
			
			// 1. Setup Borders
			Border etch = BorderFactory.createEtchedBorder(new Color(52, 53, 55), new Color(156, 157, 156));
			Border thickEtch = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(52, 53, 55), 10), etch);
			Border padding = BorderFactory.createEmptyBorder(30, 30, 30, 30);
			setBorder(BorderFactory.createCompoundBorder(thickEtch, padding));

			// 2. Create ImageIcon and Scale it
			// Note: Scaling here assumes you want it to fit within the card dimensions
			Image scaledImg = characterImg.image.getScaledInstance(characterImg.width, characterImg.height, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(scaledImg);

			// 3. Add to Label
			JLabel imageLabel = new JLabel(icon);
			add(imageLabel, BorderLayout.CENTER);

			addMouseListener(this);
			setPreferredSize(new Dimension(characterImg.width + 84, characterImg.height + 84)); // Added space for margins
		}

		// paintComponent is no longer needed for the image!
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
		}

		public void mouseClicked(MouseEvent e) 
		{
			data.setUserCharacters(character);
			if(thisPageNumber != 0)
				pm.changePanelCard("Get New Cards " + (thisPageNumber++));
			else
				pm.changePanelCard(nextPage);
		}

		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		public void mouseExited(MouseEvent e) {}
	}
}
