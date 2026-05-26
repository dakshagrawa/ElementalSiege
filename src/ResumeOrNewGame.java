import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ResumeOrNewGame extends JPanel implements ActionListener
{
    private PageManager pm;
    private GameData data;
    private JButton resumeBtn;
    private JButton newGameBtn;

    

    public ResumeOrNewGame(PageManager pageMngr)
    {
        pm = pageMngr;
        data = pm.getGameData();

        setLayout(new BorderLayout(0, 30));
        setBackground(new Color(40, 40, 60));
        setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        buildUI();
    }

    private void buildUI()
    {
        JLabel heading = new JLabel("Welcome Back!", SwingConstants.CENTER);
        heading.setFont(pm.titleFont);
        heading.setForeground(Color.WHITE);
        add(heading, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(4, 1, 0, 15));
        statsPanel.setOpaque(false);

        JLabel usernameLabel = new JLabel("Username: " + data.getUsername(), SwingConstants.CENTER);
        usernameLabel.setFont(pm.normalBoldFont);
        usernameLabel.setForeground(Color.WHITE);

        JLabel levelLabel = new JLabel("Level reached: " + data.getSavedLevel(), SwingConstants.CENTER);
        levelLabel.setFont(pm.normalBoldFont);
        levelLabel.setForeground(Color.WHITE);

        JLabel healthLabel = new JLabel("Health when last saved: " + data.getSavedHealth() + "/100", SwingConstants.CENTER);
        healthLabel.setFont(pm.normalBoldFont);
        healthLabel.setForeground(Color.WHITE);

        String cardNames = buildCardNamesList();
        JLabel cardsLabel = new JLabel("Cards owned: " + cardNames, SwingConstants.CENTER);
        cardsLabel.setFont(pm.normalFont);
        cardsLabel.setForeground(Color.LIGHT_GRAY);

        statsPanel.add(usernameLabel);
        statsPanel.add(levelLabel);
        statsPanel.add(healthLabel);
        statsPanel.add(cardsLabel);

        add(statsPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        btnPanel.setOpaque(false);

        resumeBtn  = pm.new Button1("Resume Game", Color.WHITE, new Color(40, 120, 40));
        newGameBtn = pm.new Button1("New Game",    Color.WHITE, new Color(120, 40, 40));

        resumeBtn.setFont(pm.normalBoldFont);
        newGameBtn.setFont(pm.normalBoldFont);

        resumeBtn.addActionListener(this);
        newGameBtn.addActionListener(this);

        pm.addHoverEffect(resumeBtn,  Color.LIGHT_GRAY, resumeBtn.getBackground());
        pm.addHoverEffect(newGameBtn, Color.LIGHT_GRAY, newGameBtn.getBackground());

        btnPanel.add(resumeBtn);
        btnPanel.add(newGameBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private String buildCardNamesList()
    {
        int[] userCards = data.userCharacters;
        if (userCards == null || userCards.length == 0)
            return "None";

        StringBuilder sb = new StringBuilder();
        boolean foundAny = false;
        for (int i = 0; i < userCards.length; i++)
        {
            if (userCards[i] != -1)
            {
                if (foundAny) sb.append(", ");
                sb.append(data.characters[userCards[i]].name);
                foundAny = true;
            }
        }
        if (foundAny)
            return sb.toString();
        else
            return "None";
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("Resume Game")) //go to the level they were on if they say resume
        {
            pm.changePanelCard("Level " + data.getSavedLevel());
        }
        else if (cmd.equals("New Game")) //start like you would for a normal person
        {
            data.resetProgress();
            pm.changePanelCard("Level 1");
        }
    }
}