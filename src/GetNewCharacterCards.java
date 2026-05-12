import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		nextPage = "Get New Cards "+(thisPageNumber+1);

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

			int numToPick = Math.min(3, data.characters.length);
    		int[] usedCharacterIdx = new int[numToPick];
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
	public static class CharacterPanel extends JButton implements ActionListener
	{
		private int character;
		private GameData.CharacterImage characterImg;

		public CharacterPanel(int characterIdx)
		{
			character = characterIdx;
			characterImg = data.characters[character];

			setBackground(new Color(215, 200, 169));

			try
			{
				ImageIcon scaledCharacterIcon = new ImageIcon(characterImg.image.getScaledInstance(200, 300, Image.SCALE_SMOOTH));
				setIcon(scaledCharacterIcon);
			}
			catch(GameData.CharacterImage.CharacterInitializationException e)
			{
				System.err.println("Could not load " + characterImg.name);
				e.printStackTrace();
				setText(characterImg.name);
			}

			addActionListener(this);
			setPreferredSize(new Dimension(characterImg.width, characterImg.height));
			setCursor(new Cursor(Cursor.HAND_CURSOR));
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

		public void actionPerformed(ActionEvent e) 
		{
			data.setUserCharacters(character);
			pm.changePanelCard(nextPage);
		}
	}
}
