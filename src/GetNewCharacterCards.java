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
			// setPreferredSize(new Dimension(1200, 400));

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

			// 1. Your existing thick etched frame
			Border etch = BorderFactory.createEtchedBorder(new Color(52, 53, 55), new Color(156, 157, 156));
			Border thickEtch = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(52, 53, 55), 10), etch);

			// 2. Padding (increased to 30px for a cleaner look)
			Border padding = BorderFactory.createEmptyBorder(30, 30, 30, 30);

			// 3. Final nested border: thickEtch is outer, padding is inner
			setBorder(BorderFactory.createCompoundBorder(thickEtch, padding));


			setBackground(new Color(215, 200, 169));

			addMouseListener(this);
			setPreferredSize(new Dimension(characterImg.height, characterImg.width));
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			
			//the total margin: 10 (line) + 2 (etch) + 30 (padding) = 42
			int margin = 42;
			double scale = (double)(getWidth()-30) / characterImg.width;
			int finalHeight = (int) (characterImg.height * scale);
			int y = margin + (getHeight()/2)-(finalHeight/2);
			g.drawImage(characterImg.image, 15, y, margin + (getWidth() - (margin * 2)), finalHeight+y, characterImg.x1, characterImg.y1, characterImg.x2, characterImg.y2, this);
		}

		public void mouseClicked(MouseEvent e) 
		{
			data.setUserCharacters(character);
			if(thisPageNumber != 0)
				pm.changePanelCard("Get New Cards "+(thisPageNumber++));
			else
				pm.changePanelCard(nextPage);
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
