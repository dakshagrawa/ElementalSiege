import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class BattlePanel extends JPanel implements ActionListener
{
    private GameData data;
    private PageManager pm;

    private GameData.CharacterImage userCard;
    private GameData.CharacterImage enemyCard;
    public int userHealth;
    private int enemyHealth;
    private int maxUserHealth = 100;
    private int maxEnemyHealth;
    public int level;
	
    private HealthBarPanel userHealthPanel;
    private HealthBarPanel enemyHealthPanel;
    private JPanel userCardPanel;
    private JPanel enemyCardPanel;
    private JPanel centerResultPanel;
    private JLabel resultLabel;
    private JLabel streakLabel;
    private int streak = 0;

    private JButton problemSolvingBtn;
    private JButton curiosityBtn;
    private JButton wisdomBtn;
    private boolean attributeButtonsEnabled = false;
    private Timer displayTimer;
    private boolean lastAnswerCorrect;

    public boolean startTimer;

    public BattlePanel(PageManager pageMngr, boolean doTimer)
    {
        pm = pageMngr;
        data = pm.getGameData();
        level = 1;
        startTimer = doTimer;

        userHealth = maxUserHealth;
        maxEnemyHealth = (int)(level * 1.7) + 17;
        enemyHealth = maxEnemyHealth;

        setLayout(new BorderLayout());
        setBackground(new Color(40, 40, 50));

        buildUI();
        drawNewCards();
    }

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
		resultLabel.setFont(pm.normalBoldFont);

		streakLabel = new JLabel("Streak: " + streak, SwingConstants.CENTER);
		streakLabel.setForeground(new Color(255, 220, 50));
		streakLabel.setFont(pm.normalBoldFont);

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
        maxEnemyHealth = (int)(level * 1.7) + 17;
        enemyHealth = maxEnemyHealth;

        int userIdx = (int)(Math.random() * data.userCharacters.length);
        userCard = data.characters[userIdx];

        int enemyIdx = (int)(Math.random() * data.characters.length);
        enemyCard = data.characters[enemyIdx];

        userCardPanel.removeAll();
        enemyCardPanel.removeAll();

        userCardPanel.add(buildUserCardDisplay(), BorderLayout.CENTER);
        enemyCardPanel.add(buildEnemyCardDisplay(), BorderLayout.CENTER);

        resultLabel.setText("Study your card... (5s)");
        attributeButtonsEnabled = false;
        problemSolvingBtn.setEnabled(false);
        curiosityBtn.setEnabled(false);
        wisdomBtn.setEnabled(false);

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

    class DisplayTimer implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            pm.changePanelCard("Questions");
        }
    }

    private JPanel buildUserCardDisplay()
    {
        JPanel card = makeCardShell(userCard.name, userCard.rarity, userCard.image);

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

    private JPanel buildEnemyCardDisplay()
    {
        JPanel back = new JPanel(new BorderLayout());
        back.setBackground(new Color(60, 60, 80));
        back.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        JLabel hidden = new JLabel("?", SwingConstants.CENTER); //for now it's question mark
        hidden.setFont(pm.titleFont);
        hidden.setForeground(Color.LIGHT_GRAY);
        back.add(hidden, BorderLayout.CENTER);

        JLabel enemyName = new JLabel("Enemy", SwingConstants.CENTER);
        enemyName.setForeground(Color.LIGHT_GRAY);
        enemyName.setFont(pm.normalFont);
        back.add(enemyName, BorderLayout.SOUTH);

        return back;
    }

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

    private JPanel makeCardShell(String name, String rarity, Image img)
    {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(new Color(55, 55, 70));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(pm.normalBoldFont);
        JLabel rarityLabel = new JLabel(rarity, SwingConstants.CENTER);
        rarityLabel.setForeground(rarityColor(rarity));
        rarityLabel.setFont(pm.normalFont);
        header.add(nameLabel);
        header.add(rarityLabel);
        card.add(header, BorderLayout.NORTH);

        JLabel imgLabel;
        if (img != null)
        {
            Image scaled = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            imgLabel = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
        }
        else
        {
            imgLabel = new JLabel("Problem loading your image", SwingConstants.CENTER);
            imgLabel.setForeground(Color.GRAY);
        }
        card.add(imgLabel, BorderLayout.CENTER);

        return card; // TODO: stackOverflow citation 
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
        btn.setFont(pm.normalFont);
        btn.setActionCommand(command);
        btn.addActionListener(this);
        return btn;
    }

    private void highlightButton(JButton btn, boolean highlight)
    {
        if (highlight)
        {
            btn.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
            btn.setBorderPainted(true);
        }
    }

    private Color rarityColor(String rarity)
    {
    	if (rarity == null)
		{
			return Color.LIGHT_GRAY;
		}
		else if (rarity.toLowerCase().equals("legendary"))
		{
			return new Color(255, 180, 0);
		}
		else if (rarity.toLowerCase().equals("epic"))
		{
			return new Color(160, 80, 220);
		}
        else if (rarity.toLowerCase().equals("rare"))
        {
            return new Color(60, 130, 220);
        }
		else if (rarity.toLowerCase().equals("uncommon"))
		{
			return new Color(80, 190, 80);
		}
		else
		{
			return Color.LIGHT_GRAY;
		}
    }

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

    private String getLowestAttribute(GameData.CharacterImage card)
    {
        int ps = card.problemSolving;
        int cu = card.curiosity;
        int wi = card.wisdom;
        int min = Math.min(ps, Math.min(cu, wi));
        if (min == ps) return "problemsolving";
        if (min == cu) return "curiosity";
        return "wisdom";
    }

    private void doResolution(String attribute)
    {
        problemSolvingBtn.setEnabled(false);
        curiosityBtn.setEnabled(false);
        wisdomBtn.setEnabled(false);

        int userValue  = getAttributeValue(userCard,  attribute);
        int enemyValue = getAttributeValue(enemyCard, attribute);
        int diff = Math.abs(userValue - enemyValue);
        int damage = diff;

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
        streakLabel.setText("Streak: " + streak);

        revalidate();
        repaint();

        checkBattleEnd();
    }

    private int getAttributeValue(GameData.CharacterImage card, String attribute)
    {
        if (attribute.equals("problemsolving"))
    	{
        	return card.problemSolving;
    	}
    	else if (attribute.equals("curiosity"))
    	{
        	return card.curiosity;
    	}
    	else if (attribute.equals("wisdom"))
    	{
        	return card.wisdom;
    	}
    	else
    	{
        	return 0;
    	}
    }

    private void checkBattleEnd()
    {
        if (userHealth <= 0 || enemyHealth <= 0)
        {
            pm.changePanelCard("homePanel");
        }
        else
        {
            //use timer to wait 5 seconds to show user who got damaged before moving on to question
            Timer delay = new Timer(5000, new CardDelay());
            delay.setRepeats(false);
            delay.start();
        }
    }

    class CardDelay implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            drawNewCards();
            pm.changePanelCard("Questions");
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("problemsolving") || cmd.equals("curiosity") || cmd.equals("wisdom"))
        {
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

        public HealthBarPanel(String ownerName, int current, int max, Color barColor)
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

            if ((double) current / max < 0.25)
                bar.setForeground(new Color(220, 60, 60)); //is tis red?
            else
                bar.setForeground(barColor);

            repaint();
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