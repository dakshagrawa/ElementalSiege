import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.CardLayout;

public class BattlePanel extends JPanel implements ActionListener
{
    private GameData data;
    private PageManager pm;

    //cards drawn each round for user and enemy
    private GameData.CharacterImage userCard;
    private GameData.CharacterImage enemyCard;

    //health values are public so Settings can read them when user exits and saves
    public int userHealth;
    private int enemyHealth;
    private int maxUserHealth = 100;
    private int maxEnemyHealth;
    public int level;

    //Getters for the main frame to read user's current stats when they manually exit and choose to save stats
    public int getUserHealth() {
    	return userHealth;
    }
	
    public int getLevel() {
    	return level;
    }
	
    private HealthBarPanel userHealthPanel;
    private HealthBarPanel enemyHealthPanel;
    private JPanel userCardPanel;
    private JPanel enemyCardPanel;
    private JPanel centerResultPanel;
    private JLabel resultLabel;
    private JLabel streakLabel;
    private int streak = 0;

    //the three attribute buttons on the user's card
    private JButton problemSolvingBtn;
    private JButton curiosityBtn;
    private JButton wisdomBtn;
    private boolean attributeButtonsEnabled = false;

    //displayTimer gives user some time to study their card before each round starts
    private Timer displayTimer;
    //delay gives user time to see the result of that round's "battle" before moving on to question
    private Timer delay;
    private boolean lastAnswerCorrect;

    private QuestionPanel questionPanel;

    public boolean startTimer;

    public BattlePanel(PageManager pageMngr, boolean doTimer)
    {
        pm = pageMngr;
        data = pm.getGameData();
        level = 1;
        startTimer = doTimer;

        userHealth = maxUserHealth;
        maxEnemyHealth = (int)(level * 1.7)+17;
        enemyHealth = maxEnemyHealth;

        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 50));

        buildUI();
        drawNewCards();
    }

    public void getQuestionPanel(QuestionPanel qPanel)
    {
        questionPanel = qPanel;
    }

    //this method is called when user clicks the play again button from the end game panel
    //everything is reset back to level 1
    public void resetForNewGame()
    {
        level = 1;
        userHealth = 100;
        maxEnemyHealth = (int)(level * 1.7) + 17;
        enemyHealth = maxEnemyHealth;
        streak = 0;
        startTimer = false;
        attributeButtonsEnabled = false;
        if (displayTimer != null) { displayTimer.stop(); displayTimer = null; }
        if (delay != null) { delay.stop(); delay = null; }
        userHealthPanel.setHealth(userHealth);
        enemyHealthPanel.setHealth(enemyHealth);
        enemyHealthPanel.setMax(maxEnemyHealth);
        resultLabel.setText("Pick an attribute!");
        streakLabel.setText("Streak: 0");
        userCardPanel.removeAll();
        enemyCardPanel.removeAll();
        revalidate();
        repaint();
    }

    //health bar is at the top -- user's bar is green and enemy's is red
    private void buildUI()
    {
        JPanel topBar = new JPanel(new GridLayout(1, 2, 20, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        userHealthPanel = new HealthBarPanel("You", userHealth, maxUserHealth, new Color(80, 200, 80));
        enemyHealthPanel = new HealthBarPanel("Enemy", enemyHealth, maxEnemyHealth, new Color(200, 80, 80));
        topBar.add(userHealthPanel);
        topBar.add(enemyHealthPanel);
        add(topBar, BorderLayout.NORTH);

        //the center of battle panel has 3 columns: user card, result of who won, and enemy card
        JPanel centerArea = new JPanel(new GridLayout(1, 3, 20, 0));
        centerArea.setOpaque(false);
        centerArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        userCardPanel = new JPanel(new BorderLayout());
        userCardPanel.setOpaque(false);

        enemyCardPanel = new JPanel(new BorderLayout());
        enemyCardPanel.setOpaque(false);

		centerResultPanel = new JPanel(new GridLayout(2, 1, 0, 10));
		centerResultPanel.setOpaque(false);

		resultLabel = new JLabel("Pick an attribute!", SwingConstants.CENTER);
		resultLabel.setForeground(Color.WHITE);
		resultLabel.setFont(pm.titleFont);

		streakLabel = new JLabel("Streak: " + streak, SwingConstants.CENTER);
		streakLabel.setForeground(new Color(255, 220, 50));
		streakLabel.setFont(pm.titleFont);

		centerResultPanel.add(resultLabel);
		centerResultPanel.add(streakLabel);

        centerArea.add(userCardPanel);
        centerArea.add(centerResultPanel);
        centerArea.add(enemyCardPanel);
        add(centerArea, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomBar.setOpaque(false);
        add(bottomBar, BorderLayout.SOUTH);
    }

    public void drawNewCards()
    {
        //enemy's starting health increases with each level to make it harder to defeat the level
        maxEnemyHealth = (int)(level * 1.7) + 5;
        enemyHealthPanel.setMax(maxEnemyHealth);
        if (enemyHealth > maxEnemyHealth)
            enemyHealth = maxEnemyHealth;

        int[] temp = new int[4];
        int j = 0;
        for(int i = 0; i < data.userCharacters.length; i++)
        {
            if(data.userCharacters[i]>=0)
            {
                temp[j] = data.userCharacters[i];
                j++;
            }
        }
        
        int userIdx = (int)(Math.random() * temp.length);
        
        userCard = data.characters[temp[userIdx]];

        //when we let the program choose the enemy's cards randomly, the enemy card was often of a higher status than the user's
        //which is a problem in lower levels that are meant to be easy so we're using this chunk of code to limit
        //how often the enemy gets legendary cards -- the lower level cards have a higher chance of occurring which helps player
        int roll = (int)(Math.random() * 100) + 1;
        String targetRarity;
        if (level == 1)
        {
            //level 1 -- enemy should almost always be Common or Rare
            if (roll <= 70) 
                targetRarity = "Common";
            else if (roll <= 95) 
                targetRarity = "Rare";
            else 
                targetRarity = "Epic";
            //no Legendary on level 1
        }
        else
        {
            //level 2 -- slightly harder but still weighted toward weaker cards
            if (roll <= 40) 
                targetRarity = "Common";
            else if (roll <= 75) 
                targetRarity = "Rare";
            else if (roll <= 95) 
                targetRarity = "Epic";
            else 
                targetRarity = "Legendary";
        }

        //goes through the available cards and filters through them to pick out the ones that have the chosen rarity
        int count = 0;
        for (int i = 0; i < data.characters.length; i++)
        {
            if (data.characters[i] != null && data.characters[i].rarity != null && data.characters[i].rarity.equalsIgnoreCase(targetRarity))
                count++;
        }

        //goes through the available cards and filters through them to pick out the ones that have the chosen rarity
        int[] eligibleIndices = new int[count];
        int pos = 0;
        for (int i = 0; i < data.characters.length; i++)
        {
            if (data.characters[i] != null && data.characters[i].rarity != null && data.characters[i].rarity.equalsIgnoreCase(targetRarity))
            {
                eligibleIndices[pos] = i;
                pos++;
            }
        }

        //selects one card out of the elegiible cards
        //if for some reason there is an error and it can't find any cards that are of the chosen rarity, it'll pick a random card
        int enemyIdx;
        if (count == 0)
            enemyIdx = (int)(Math.random() * data.characters.length);
        else
            enemyIdx = eligibleIndices[(int)(Math.random() * count)];

        enemyCard = data.characters[enemyIdx];

        userCardPanel.removeAll();
        enemyCardPanel.removeAll();

        userCardPanel.add(buildUserCardDisplay(), BorderLayout.CENTER);
        enemyCardPanel.add(buildEnemyCardDisplay(), BorderLayout.CENTER);

        resultLabel.setText("Study your card... (5s)");
        //disables the attrubute buttons when user is studying their card
        attributeButtonsEnabled = false;
        problemSolvingBtn.setEnabled(false);
        curiosityBtn.setEnabled(false);
        wisdomBtn.setEnabled(false);

        //starts the timer that moves user to questions screen after they've had time to look at their card
        if(startTimer)
        {
            if (displayTimer != null) 
                displayTimer.stop();
            displayTimer = new Timer(10000, new DisplayTimer());
            displayTimer.setRepeats(false);
            displayTimer.start();
        }   

        revalidate();
        repaint();
    }

    //after user has had 10 seconds to look at the round's results, it moves on to next question
    class DisplayTimer implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (delay != null)
                delay.stop();
            pm.changePanelCard("Questions");
        }
    }

    private JPanel buildUserCardDisplay()
    {
        JPanel card = makeCardShell(userCard.name, userCard.rarity, userCard.image);

        //user clciks these buttons to decide what they battle with
        problemSolvingBtn = makeAttributeButton("Problem Solving", userCard.problemSolving, new Color(180, 60, 60), "problemsolving");
        curiosityBtn = makeAttributeButton("Curiosity", userCard.curiosity, new Color(60, 90, 180), "curiosity");
        wisdomBtn   = makeAttributeButton("Wisdom",   userCard.wisdom,   new Color(120, 60, 160), "wisdom");

        JPanel attrs = new JPanel(new GridLayout(3, 1, 0, 4));
        attrs.setOpaque(false);
        attrs.add(problemSolvingBtn);
        attrs.add(curiosityBtn);
        attrs.add(wisdomBtn);
        attrs.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        card.add(attrs, BorderLayout.SOUTH);
        return card;
    }

    //enemy card starts hidden so that user can't see before picking the attribute
    private JPanel buildEnemyCardDisplay()
    {
        JPanel back = new JPanel(new BorderLayout());
        back.setBackground(new Color(60, 60, 80));
        back.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JLabel hidden = new JLabel("?", SwingConstants.CENTER);
        hidden.setFont(pm.titleFont);
        hidden.setForeground(Color.LIGHT_GRAY);
        back.add(hidden, BorderLayout.CENTER);

        JLabel enemyName = new JLabel("Enemy", SwingConstants.CENTER);
        enemyName.setForeground(Color.LIGHT_GRAY);
        enemyName.setFont(pm.titleFont);
        back.add(enemyName, BorderLayout.SOUTH);

        return back;
    }

    //the enemy's card is revealed after user picks an attribute and the chosen attribute is highlighted
    private JPanel buildRevealedEnemyCard(String chosenAttribute)
    {
        JPanel card = makeCardShell(enemyCard.name, enemyCard.rarity, enemyCard.image);

        JButton sBtn = makeAttributeButton("Problem Solving", enemyCard.problemSolving, new Color(180, 60, 60), "");
        JButton cBtn = makeAttributeButton("Curiosity",  enemyCard.curiosity,  new Color(60, 90, 180), "");
        JButton wBtn = makeAttributeButton("Wisdom",    enemyCard.wisdom,    new Color(120, 60, 160), "");

        highlightButton(sBtn, chosenAttribute.equals("problemsolving"));
        highlightButton(cBtn, chosenAttribute.equals("curiosity"));
        highlightButton(wBtn, chosenAttribute.equals("wisdom"));

        sBtn.setEnabled(false);
        cBtn.setEnabled(false);
        wBtn.setEnabled(false);

        JPanel attrs = new JPanel(new GridLayout(3, 1, 0, 4));
        attrs.setOpaque(false);
        attrs.add(sBtn);
        attrs.add(cBtn);
        attrs.add(wBtn);
        attrs.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        card.add(attrs, BorderLayout.SOUTH);
        return card;
    }

    //this will build the "shell" where the user's card, name of card, and rarity and the image are placed
    private JPanel makeCardShell(String name, String rarity, Image img)
    {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(new Color(55, 55, 70));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        card.setPreferredSize(new Dimension(211, 400));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(pm.titleFont);
        JLabel rarityLabel = new JLabel(rarity, SwingConstants.CENTER);
        rarityLabel.setForeground(rarityColor(rarity));
        rarityLabel.setFont(pm.normalBoldFont);
        header.add(nameLabel);
        header.add(rarityLabel);
        card.add(header, BorderLayout.NORTH);

        JLabel imgLabel = new JLabel("", SwingConstants.CENTER);
        if (img != null)
        {
            //Scale to fill image to the card width while keeping proportions
            int cropHeight = (int)(img.getHeight(null) * 0.62);
            int displayWidth  = 200;
            int displayHeight = (int)(cropHeight * ((double) displayWidth / img.getWidth(null)));
            Image scaled = img.getScaledInstance(displayWidth, displayHeight, Image.SCALE_SMOOTH);

            imgLabel = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
        }
        card.add(imgLabel, BorderLayout.CENTER);

        return card;
    }

    private JButton makeAttributeButton(String label, int value, Color bg, String command)
    {
        JButton btn = new JButton(label + "  " + value);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(pm.normalBoldFont);
        btn.setPreferredSize(new Dimension(180, 44)); //taller buttons
        btn.setActionCommand(command);
        btn.addActionListener(this);
        return btn;
    }

    //when user picks an attribute, we add a yellow border to that button so it's clear which one was chosen
    //we also do this on the enemy's revealed card so user can see both values being compared side by side
    private void highlightButton(JButton btn, boolean highlight)
    {
        if (highlight)
        {
            btn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            btn.setBorderPainted(true);
        }
    }
    //each rarity gets its own color so user can tell at a glance how strong their card is
    private Color rarityColor(String rarity)
    {
    	if (rarity == null)
			return Color.LIGHT_GRAY;
		else if (rarity.toLowerCase().equals("legendary"))
			return new Color(255, 180, 0);
		else if (rarity.toLowerCase().equals("epic"))
			return new Color(160, 80, 220);
        else if (rarity.toLowerCase().equals("rare"))
            return new Color(60, 130, 220);
		else if (rarity.toLowerCase().equals("uncommon"))
			return new Color(80, 190, 80);
		else
			return Color.LIGHT_GRAY;
    }
    //this is called from QuestionPanel after user answers -- correct answer lets user pick their attribute,
    //wrong answer auto-picks the weakest attribute on their card (increases chance of user losing that round)
    public void resolveRound(boolean correct)
    {
        lastAnswerCorrect = correct;
        if (correct)
        {
            resultLabel.setText("Correct! Choose your attribute!");
            attributeButtonsEnabled = true;
            problemSolvingBtn.setEnabled(true);
            curiosityBtn.setEnabled(true);
            wisdomBtn.setEnabled(true);
        }
        else
        {
            resultLabel.setText("Wrong! Auto-attacking with lowest stat...");
            attributeButtonsEnabled = false;
            String lowest = getLowestAttribute(userCard);
            doResolution(lowest);
        }
        revalidate();
        repaint();
    }
    //finds which of the 3 attributes has the lowest value on the card -- used when user gets a question wrong
    private String getLowestAttribute(GameData.CharacterImage card)
    {
        int ps = card.problemSolving;
        int cu = card.curiosity;
        int wi = card.wisdom;
        int min = Math.min(ps, Math.min(cu, wi));
        if (min == ps) 
            return "problemsolving";
        if (min == cu) 
            return "curiosity";
        return "wisdom";
    }

    //compares user and enemy attribute values, calculates damage, updates health bars
    private void doResolution(String attribute)
    {
        problemSolvingBtn.setEnabled(false);
        curiosityBtn.setEnabled(false);
        wisdomBtn.setEnabled(false);

        int userValue  = getAttributeValue(userCard,  attribute);
        int enemyValue = getAttributeValue(enemyCard, attribute);
        int diff = Math.abs(userValue - enemyValue);
        int damage = diff;

        //flips the enemy card now that the attribute has been chosen
        enemyCardPanel.removeAll();
        enemyCardPanel.add(buildRevealedEnemyCard(attribute), BorderLayout.CENTER);

        String resultText;
        if (userValue > enemyValue)
        {
            enemyHealth = Math.max(0, enemyHealth - damage);
            streak++;
            resultText = "You Win. Deal " + damage + " Damage";
        }
        else if (enemyValue > userValue)
        {
            userHealth = Math.max(0, userHealth - damage);
            streak = 0;
            resultText = "You Lose. Take " + damage + " Damage";
        }
        else
        {
            resultText = "Tie! No damage dealt";
        }

        userHealthPanel.setHealth(userHealth);
        enemyHealthPanel.setHealth(enemyHealth);

        resultLabel.setText(resultText);
        if (streak <= 0)
            streakLabel.setText("");
        else if (streak <= 3)
            streakLabel.setText("Streak: " + streak);
        else
            streakLabel.setText("Streak: " + streak + " 🔥!!!");
        revalidate();
        repaint();

        checkBattleEnd();
    }

    private int getAttributeValue(GameData.CharacterImage card, String attribute)
    {
        if (attribute.equals("problemsolving"))
        	return card.problemSolving;
    	else if (attribute.equals("curiosity"))
        	return card.curiosity;
    	else if (attribute.equals("wisdom"))
        	return card.wisdom;
    	else
        	return 0;
    }

    private void checkBattleEnd()
    {
        if (userHealth <= 0)
        {
            //In case user loses before saving game, this will prevent them from getting stuck the next time they log in
            //by resetting health to 100 and level to 1
            pm.getGameData().saveUserProgress(100, 1);
            pm.changePanelCard("Game Over");
            if (displayTimer != null) displayTimer.stop();
            if (delay != null) delay.stop();
        }
        else if (enemyHealth <= 0)
        {
            if (level >= 10)
            {
                //Resets progress on final victory to also prevent user from getting stuck when they log in again
                pm.getGameData().saveUserProgress(100, 1);
                pm.changePanelCard("You Win");
                if (displayTimer != null)
                    displayTimer.stop();
                if (delay != null)
                    delay.stop();
            }
            else //if user didn't die or win, move on to the next level
            {
                if (displayTimer != null) displayTimer.stop();
                if (delay != null) delay.stop();
                level++;
                enemyHealth = (int)(level * 1.7) + 17;
                maxEnemyHealth = enemyHealth;
                enemyHealthPanel.setMax(maxEnemyHealth);
                userHealthPanel.setHealth(userHealth);
                enemyHealthPanel.setHealth(enemyHealth);
                pm.changePanelCard("Level " + level);
            }
        }
        else //battle isn't over yet -- wait 8 seconds so user can see the result then draw new cards
        {
            if (delay != null) delay.stop();
            delay = new Timer(8000, new CardDelay());
            delay.setRepeats(false);
            delay.start();
        }
        revalidate();
        repaint();
    }
    //moves on after 6 second delay -- draws new cards and moves to questions for next round
    class CardDelay implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            drawNewCards();
            delay.stop();
            pm.changePanelCard("Questions");
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("problemsolving") || cmd.equals("curiosity") || cmd.equals("wisdom"))
        {
            if (!attributeButtonsEnabled) return;
            doResolution(cmd);
        }
    }

    class HealthBarPanel extends JPanel
    {
        private int current;
        private int max;
        private Color barColor;
        private JLabel label;
        private JProgressBar bar;

        public HealthBarPanel(String ownerName, int current, int max, Color barColor) //a health bar is displayed
        //separately for user and enemy and it shows up only during the "battle"
        {
            this.current = current;
            this.max = max;
            this.barColor = barColor;

            setLayout(new BorderLayout(0, 4));
            setOpaque(false);

            label = new JLabel(ownerName + ": " + current + "/" + max, SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            label.setFont(pm.normalFont);
            add(label, BorderLayout.NORTH);

            bar = new JProgressBar(0, max);
            bar.setValue(current);
            bar.setForeground(barColor);
            bar.setBackground(new Color(80, 80, 80));
            bar.setStringPainted(false);
            bar.setPreferredSize(new Dimension(200, 22));
            bar.setBorderPainted(false);
            add(bar, BorderLayout.CENTER);
        }

        public void setHealth(int newHealth)
        {
            current = newHealth;
            bar.setValue(current);
            label.setText(label.getText().split(":")[0] + ": " + current + "/" + max);
            
            //user's health bar turns red when health drops below 25% to warn user
            if ((double) current / max < 0.25)
                bar.setForeground(new Color(220, 60, 60));
            else
                bar.setForeground(barColor);

            repaint();
        }

        public void setMax(int newMax)
        {
            max = newMax;
            bar.setMaximum(max);
            label.setText(label.getText().split(":")[0] + ": " + current + "/" + max);
        }
    }

    public class CharacterCard
    {
        public String name = "Unknown";
        public String rarity = "Common";
        public Image image = null;
        public int problemSolving = 10;
        public int curiosity = 10;
        public int wisdom   = 10;
    }
}