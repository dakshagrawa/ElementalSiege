import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BattlePanel extends JPanel implements ActionListener
{
    private GameData data;
	private PageManager pm;

    public BattlePanel(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();

        setLayout(new GridLayout(1, 4, 10, 10));

        //Image userCard = data.characters[data.userCharacters[(int)(Math.random()*data.userCharacters.length)]].image;
		
		RelatedToBattlePanel rtbp = new RelatedToBattlePanel();
		add(rtbp);

    }
	
	class UserInformationAndHealthBar extends JPanel
	{
		public UserInformationAndHealthBar()
		{
			setLayout(new BorderLayout());
			setOpaque(false);
			add(userHealthBar());
		}

		public JPanel userHealthBar()
		{
			JPanel userHealthPanel = new JPanel();
			userHealthPanel.add();
			return userHealthPanel;

			
			
		}
	}

	class UserCardInterface
	{
		public UserCardInterface()
		{
			setLayout(new BorderLayout());

			add(new getUsersCard(), BorderLayout.CENTER);
		}

		class getUsersCard extends JPanel
		{
			data.userCharacters[(int)(Math.random()*data.userCharacters.length)];
		}
	}


	class RelatedToBattlePanel extends JPanel
	{
		private int userHealth;
		private String userHealthDisplay;
		private int enemyHealth;
		private String enemyHealthDisplay;
		private int level;

		public RelatedToBattlePanel()
		{	
			level = 1;

			userHealth = 0;
			userHealthDisplay = userHealth + "/100";

			
			JTextArea healthBar = new JTextArea(userHealthDisplay, 1, 10);

			enemyHealth = (int)(level*1.7) + 17;
			enemyHealthDisplay = enemyHealth + "/" + enemyHealth;

			JTextArea enemyHealthBar = new JTextArea(enemyHealthDisplay, 1, 10);
			add(healthBar);
			add(enemyHealthBar);
		}

		public void switchToQuestions()
		{
			while(enemyHealth > 0 || userHealth > 0)
			{
				pm.changePanelCard("Questions");
			}
		}

	}
		

	public void actionPerformed(ActionEvent e) 
	{

	}
}