import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LevelPanel extends JPanel implements ActionListener
{
    private PageManager pm;
    private int level;
    private JLabel headingLabel;
    private JLabel storyLabel;
    private JButton continueBtn;
    private Timer typewriterTimer;
    private String fullStoryText;
    private int charIndex;

    private static final String[] STORIES = {
        // Level 1
        "Oh no! High levels of radiation have been recorded near your lab. It appears that a Radiation Goblin is trying to enter your lab. Keep your chemicals safe and answer questions to defend your lab!",
        // Level 2
        "Congratulations! You've defeated the Radiation Goblin. Welcome to Level 2! But the danger isn't over — a Toxic Troll has caught wind of your powerful chemicals. It's collecting poisonous sludge to throw at your beakers. Answer quickly and keep your lab clean!",
        // Level 3
        "Congratulations! You've driven off the Toxic Troll. Welcome to Level 3! A Plasma Phantom has materialized inside your ventilation system. It's disrupting your experiments with bursts of wild energy. Focus your knowledge and blast it out!",
        // Level 4
        "Congratulations! You've dissolved the Plasma Phantom. Welcome to Level 4! A Magma Marauder is melting through your lab's outer walls. Your fireproof containment units are at risk. Answer correctly to cool it down before it reaches your core samples!",
        // Level 5
        "Congratulations! You've cooled the Magma Marauder. Welcome to Level 5! A Cryo Creep has frozen your centrifuge and is spreading ice crystals across your equipment. Your reaction chambers are dropping to dangerous temperatures — warm things up with your knowledge!",
        // Level 6
        "Congratulations! You've thawed the Cryo Creep. Welcome to Level 6! An Acid Wraith is dissolving your lab's safety barriers one by one. It's immune to most physical attacks but vulnerable to sharp thinking. Answer fast before it reaches your specimen vault!",
        // Level 7
        "Congratulations! You've neutralized the Acid Wraith. Welcome to Level 7! A Storm Specter is generating electromagnetic pulses that are scrambling your lab's computers. Your data is at risk of being wiped forever. Channel your inner scientist and shut it down!",
        // Level 8
        "Congratulations! You've grounded the Storm Specter. Welcome to Level 8! A Shadow Stalker has snuck into your lab under the cover of darkness and is sabotaging your supply of rare elements. Use your wits to expose it before your reserves run dry!",
        // Level 9
        "Congratulations! You've exposed the Shadow Stalker. Welcome to Level 9! A Void Vandal is tearing holes in the fabric of your lab's reality. Entire shelves of chemicals are disappearing into nothingness. Only your strongest knowledge can seal the rift!",
        // Level 10
        "Congratulations! You've sealed the Void Vandal away. Welcome to Level 10! The ultimate threat has arrived — the Nuclear Overlord, the source of all the chaos. It has powered itself up using your stolen chemicals. This is your final stand. Defend your lab and save science itself!"
    };

    public LevelPanel(PageManager pageMngr, int lvl)
    {
        pm = pageMngr;
        level = lvl;
        fullStoryText = STORIES[level - 1];
        charIndex = 0;

        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        buildUI();
        startTypewriter();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Image bg = PageManager.Functions.getImage("levelBackground.png");
        if (bg != null)
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        else
            setBackground(new Color(20, 20, 40));
    }

    private void buildUI()
    {
        String headingText;
        if (level == 1)
            headingText = "Welcome to Level 1";
        else
            headingText = "Welcome to Level " + level;

        headingLabel = new JLabel(headingText, SwingConstants.CENTER);
        headingLabel.setFont(pm.titleFont);
        headingLabel.setForeground(Color.WHITE);
        add(headingLabel, BorderLayout.NORTH);

        storyLabel = new JLabel("", SwingConstants.CENTER);
        storyLabel.setFont(pm.normalFont);
        storyLabel.setForeground(Color.WHITE);
        storyLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel storyPanel = new JPanel(new BorderLayout());
        storyPanel.setOpaque(false);
        storyPanel.add(storyLabel, BorderLayout.CENTER);
        add(storyPanel, BorderLayout.CENTER);

        continueBtn = pm.new Button1("Continue", Color.WHITE, new Color(40, 80, 160));
        continueBtn.addActionListener(this);
        continueBtn.setVisible(false); //continue button is hidden until typewriter finishes

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(continueBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void startTypewriter()
    {
        charIndex = 0;
        storyLabel.setText("");
        continueBtn.setVisible(false);

        typewriterTimer = new Timer(30, new TypewriterTimerListener());
        typewriterTimer.start();
    }

    class TypewriterTimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (charIndex < fullStoryText.length())
            {
                charIndex++;
                storyLabel.setText(fullStoryText.substring(0, charIndex));
            }
            else
            {
                typewriterTimer.stop();
                continueBtn.setVisible(true);
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (typewriterTimer != null && typewriterTimer.isRunning())
        {
            typewriterTimer.stop(); //if the user clicks anywhere early, the full text becomes visible
            storyLabel.setText(fullStoryText);
            continueBtn.setVisible(true);
        }
        else
        {
            pm.changePanelCard("Battle");
        }
    }
}