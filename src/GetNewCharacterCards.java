import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

public class GetNewCharacterCards extends JPanel
{
	private PageManager pm;
	private GameData data;
	private String nextPage;

	public GetNewCharacterCards(PageManager pageMngr, int thisPanelNumber)
	{
		pm = pageMngr;
		data = pm.getGameData();
		nextPage = "Get New Cards " + (thisPanelNumber + 1);
		buildLayout();
	}

	public GetNewCharacterCards(PageManager pageMngr, String nextPageName)
	{
		pm = pageMngr;
		data = pm.getGameData();
		nextPage = nextPageName;
		buildLayout();
	}

	private void buildLayout()
	{
		setLayout(new BorderLayout(0, 30));
		setBackground(new Color(169, 169, 169));
		setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		JLabel heading = new JLabel("Pick 1 Card:"
		
		
		, SwingConstants.CENTER);
		heading.setFont(pm.titleFont);
		add(heading, BorderLayout.NORTH);

		add(new CharacterCardsGrid(), BorderLayout.CENTER);
	}

	class CharacterCardsGrid extends JPanel
	{
		public CharacterCardsGrid()
		{
			setLayout(new GridLayout(1, 3, 20, 0));
			setOpaque(false);

			int numToPick = Math.min(3, data.characters.length);
			int[] usedCharacterIdx = new int[numToPick];

			for (int i = 0; i < numToPick; i++)
			{
				int randomIdx;
				boolean alreadyUsed;
				do
				{
					randomIdx = (int)(Math.random() * data.characters.length);
					alreadyUsed = false;

					for (int j = 0; j < i && !alreadyUsed; j++)
					{
						if (usedCharacterIdx[j] == randomIdx)
							alreadyUsed = true;
					}

					for (int k = 0; k < data.userCharacters.length && !alreadyUsed; k++)
					{
						if (data.userCharacters[k] == randomIdx)
							alreadyUsed = true;
					}

				} while (alreadyUsed);

				usedCharacterIdx[i] = randomIdx;
			}

			add(new CharacterPanel(usedCharacterIdx[0]));
			add(new CharacterPanel(usedCharacterIdx[1]));
			add(new CharacterPanel(usedCharacterIdx[2]));
		}
	}

	public class CharacterPanel extends JButton implements ActionListener
	{
		private int character;
		private GameData.CharacterImage characterImg;

		public CharacterPanel(int characterIdx)
		{
			character = characterIdx;
			characterImg = data.characters[character];

			setBackground(new Color(215, 200, 169));
			setFocusPainted(false);
			setCursor(new Cursor(Cursor.HAND_CURSOR));

			try
			{
				setIcon(new ImageIcon(characterImg.image));
			}
			catch (GameData.CharacterImage.CharacterInitializationException e)
			{
				System.err.println("The " + characterImg.name + " could not be loaded.");
				e.printStackTrace();
				setText(characterImg.name);
			}

			setPreferredSize(new Dimension(characterImg.width, characterImg.height));
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e)
		{
			data.setUserCharacters(character);
			pm.changePanelCard(nextPage);
		}
	}
}