import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;


public class LevelPanel extends JPanel implements ActionListener
{
    private PageManager pm;
    private int level;
    private JLabel headingLabel;
    private JTextArea storyLabel;
    private JButton continueBtn;
    private Timer typewriterTimer;
    private String fullStoryText;
    private int charIndex;

    private static final String[] STORIES = {"Oh no! High levels of radiation have been recorded near your lab. It appears that a Radiation Goblin is trying to enter your lab. Keep your chemicals safe and answer questions to defend your lab!", 
        "Congratulations! You've defeated the Radiation Goblin. Welcome to Level 2! But the danger isn't over — a Toxic Troll has caught wind of your powerful chemicals. It's collecting poisonous sludge to throw at your beakers. Answer quickly and keep your lab clean!",
        "Congratulations! You've driven off the Toxic Troll. Welcome to Level 3! A Plasma Phantom has materialized inside your ventilation system. It's disrupting your experiments with bursts of wild energy. Focus your knowledge and blast it out!", 
        "Congratulations! You've dissolved the Plasma Phantom. Welcome to Level 4! A Magma Marauder is melting through your lab's outer walls. Your fireproof containment units are at risk. Answer correctly to cool it down before it reaches your core samples!",
        "Congratulations! You've cooled the Magma Marauder. Welcome to Level 5! A Cryo Creep has frozen your centrifuge and is spreading ice crystals across your equipment. Your reaction chambers are dropping to dangerous temperatures — warm things up with your knowledge!",
        "Congratulations! You've treated the Cryo Creep. Welcome to Level 6! An Acid Wraith is dissolving your lab's safety barriers one by one. It's immune to most physical attacks but vulnerable to sharp thinking. Answer fast before it reaches your specimen vault!",
        "Congratulations! You've neutralized the Acid Wraith. Welcome to Level 7! A Storm Specter is generating electromagnetic pulses that are scrambling your lab's computers. Your data is at risk of being wiped forever. Channel your inner scientist and shut it down!",
        "Congratulations! You've grounded the Storm Specter. Welcome to Level 8! A Shadow Stalker has snuck into your lab under the cover of darkness and is sabotaging your supply of rare elements. Use your wits to expose it before your reserves run dry!",
        "Congratulations! You've exposed the Shadow Stalker. Welcome to Level 9! A Void Vandal is tearing holes in the fabric of your lab's reality. Entire shelves of chemicals are disappearing into nothingness. Only your strongest knowledge can seal the rift!",
        "Congratulations! You've sealed the Void Vandal away. Welcome to Level 10! The ultimate threat has arrived — the Nuclear Overlord, the source of all the chaos. It has powered itself up using your stolen chemicals. This is your final stand. Defend your lab and save science itself!"};

    public LevelPanel(PageManager pageMngr, int lvl)
    {
        pm = pageMngr;
        level = lvl;
        fullStoryText = STORIES[level - 1];
        charIndex = 0;

        setLayout(null); 
        setOpaque(false);

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
        {
            g.setColor(new Color(20, 20, 40));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void buildUI()
    {
        int archX      = 449;
        int archY      = 230;
        int archWidth  = 1055-archX;
        int archHeight = 540-archY;
        int padding    = 40;

        JLabel levelBadge = new JLabel("— LEVEL " + level + " —", SwingConstants.CENTER);
        levelBadge.setFont(pm.normalBoldFont);
        levelBadge.setForeground(new Color(120, 220, 180));
        levelBadge.setBounds(archX, archY, archWidth, 28);
        add(levelBadge);

        String headingText;
        if (level == 1)
            headingText = "Welcome to Level 1";
        else
            headingText = "Welcome to Level " + level;

        headingLabel = new JLabel(headingText, SwingConstants.CENTER);
        headingLabel.setFont(pm.titleFont);
        headingLabel.setForeground(new Color(255, 230, 120));
        headingLabel.setBounds(archX, archY + 36, archWidth, 40);
        add(headingLabel);

        JLabel divider = new JLabel("────────────────────────────", SwingConstants.CENTER);
        divider.setForeground(new Color(100, 160, 140, 180));
        divider.setFont(pm.normalFont);
        divider.setBounds(archX + padding, archY + 84, archWidth - padding * 2, 20);
        add(divider);

        storyLabel = new JTextArea();
        storyLabel.setFont(pm.normalFont);
        storyLabel.setForeground(new Color(220, 220, 240));
        storyLabel.setBackground(new Color(0, 0, 0, 0));
        storyLabel.setOpaque(false);
        storyLabel.setEditable(false);
        storyLabel.setLineWrap(true);
        storyLabel.setWrapStyleWord(true);
        
        // Changed height from 300 to 120 so it stops overlapping the button
        storyLabel.setBounds(archX + padding, archY + 114, archWidth - padding * 2, 120);
        add(storyLabel);
        
        //Continue button
        continueBtn = new JButton("Continue →");
        continueBtn.setBackground(new Color(30, 60, 50));
        continueBtn.setForeground(new Color(255, 230, 120));
        continueBtn.setOpaque(true);
        continueBtn.setBorderPainted(false);
        continueBtn.setFocusPainted(false);
        continueBtn.setFont(pm.normalBoldFont);
        continueBtn.addActionListener(this);
        continueBtn.setVisible(true); 
        continueBtn.setBounds(archX + (archWidth / 2) - 110, archY + archHeight - 70, 220, 44);
        add(continueBtn);
    }

    private void startTypewriter()
    {
        charIndex = 0;
        storyLabel.setText("");
        continueBtn.setVisible(true); 

        typewriterTimer = new Timer(28, new TypewriterTimer());
        typewriterTimer.start();
    }

    class TypewriterTimer implements ActionListener
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
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (typewriterTimer != null && typewriterTimer.isRunning())
        {
            typewriterTimer.stop();
        }
        
        pm.changePanelCard("Questions");
    }
}