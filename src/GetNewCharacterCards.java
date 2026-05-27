import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.BorderLayout;

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
		setBackground(new Color(30, 30, 50));
		setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		JLabel heading = new JLabel("Pick 1 Card:", SwingConstants.CENTER);
		heading.setFont(pm.titleFont);
		heading.setForeground(Color.WHITE);
		add(heading, BorderLayout.NORTH);

		add(new CharacterCardsGrid(), BorderLayout.CENTER);
	}

	class CharacterCardsGrid extends JPanel
    {
        public CharacterCardsGrid()
        {
            setLayout(new GridLayout(1, 3, 20, 0));
            setOpaque(false);

            // ROULETTE WHEEL CARD SELECTION LOGIC
            // Roll 1-100 to pick target rarity
            int roll = (int)(Math.random() * 100) + 1; // 1-100 inclusive
            String targetRarity;
            if (roll <= 40) targetRarity = "Common";
            else if (roll <= 70) targetRarity = "Rare";
            else if (roll <= 90) targetRarity = "Epic";
            else targetRarity = "Legendary";

            // Filter characters by target rarity
            java.util.ArrayList<Integer> eligibleIndices = new java.util.ArrayList<>();
            for (int i = 0; i < data.characters.length; i++) {
                if (data.characters[i] != null && data.characters[i].rarity != null && data.characters[i].rarity.equalsIgnoreCase(targetRarity)) {
                    eligibleIndices.add(i);
                }
            }

            // Backfill guardrail: ensure at least 3 unique cards in pool
            java.util.HashSet<Integer> selectionPool = new java.util.HashSet<>(eligibleIndices);
            if (selectionPool.size() < 3) {
                // Add Common and Rare characters to guarantee minimum pool size
                for (int i = 0; i < data.characters.length; i++) {
                    String rarity = data.characters[i].rarity.toLowerCase();
                    if (rarity.equals("common") || rarity.equals("rare")) {
                        selectionPool.add(i);
                    }
                    if (selectionPool.size() >= 3) break; // Stop once we have 3
                }
            }

            // Select 3 unique cards from the safe pool
            java.util.ArrayList<Integer> poolList = new java.util.ArrayList<>(selectionPool);
            java.util.HashSet<Integer> selectedIndices = new java.util.HashSet<>();
            int[] usedCharacterIdx = new int[3];
            
            for (int i = 0; i < 3; i++) {
                int randomIdx;
                boolean validSelection;
                do {
                    randomIdx = poolList.get((int)(Math.random() * poolList.size()));
                    validSelection = !selectedIndices.contains(randomIdx);
                    // also check not already owned
                    if (validSelection) {
                        for (int k = 0; k < data.userCharacters.length; k++) {
                            if (data.userCharacters[k] == randomIdx) {
                                validSelection = false;
                                break;
                            }
                        }
                    }
                } while (!validSelection);
                
                selectedIndices.add(randomIdx);
                usedCharacterIdx[i] = randomIdx;
            }

            // Add the selected character panels
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

			setOpaque(false);
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setCursor(new Cursor(Cursor.HAND_CURSOR));

			try
			{
				if (characterImg.image != null) {
					Image scaled = characterImg.image.getScaledInstance(200, 280, Image.SCALE_SMOOTH);
					setIcon(new ImageIcon(scaled));
				} else {
					setText(characterImg.name);
				}
			}
			catch (Exception e)
			{
				System.err.println("The " + characterImg.name + " could not be loaded.");
				e.printStackTrace();
				setText(characterImg.name);
			}

			setPreferredSize(new Dimension(200, 280));
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e)
		{
			data.setUserCharacters(character);
			pm.changePanelCard(nextPage);
		}
	}
}