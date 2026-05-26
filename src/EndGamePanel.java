import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class EndGamePanel extends JPanel implements ActionListener
{
    private PageManager pm;
    private GameData data;
    private boolean playerWon;

    // true = beat all 10 levels, false = health hit 0
    public EndGamePanel(PageManager pageMngr, boolean won)
    {
        pm = pageMngr;
        data = pm.getGameData();
        playerWon = won;

        setLayout(new BorderLayout(0, 30));
        setBorder(BorderFactory.createEmptyBorder(60, 80, 60, 80));

        buildUI();
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
        if (playerWon)
            headingText = "You Did It!";
        else
            headingText = "Game Over";
        JLabel heading = new JLabel(headingText, SwingConstants.CENTER);
        heading.setFont(pm.titleFont);

        if (playerWon)
            heading.setForeground(new Color(255, 220, 50));
        else
            heading.setForeground(new Color(220, 60, 60));

        add(heading, BorderLayout.NORTH);

        //Panel for the win or loss message
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        centerPanel.setOpaque(false);

        String line1, line2, line3;

        if (playerWon)
        {
            line1 = "Congratulations, " + data.getUsername() + "!";
            line2 = "You defeated all 10 enemies and saved your lab from destruction.";
            line3 = "Science is safe — for now. Well done, defender!";
        }
        else
        {
            line1 = "Your lab has been overtaken, " + data.getUsername() + ".";
            line2 = "Your health dropped to zero and the enemy broke through.";
            line3 = "Better luck next time — the lab needs you!";
        }

        JLabel msgLabel1 = new JLabel(line1, SwingConstants.CENTER);
        JLabel msgLabel2 = new JLabel(line2, SwingConstants.CENTER);
        JLabel msgLabel3 = new JLabel(line3, SwingConstants.CENTER);

        msgLabel1.setFont(pm.normalBoldFont);
        msgLabel2.setFont(pm.normalFont);
        msgLabel3.setFont(pm.normalFont);

        msgLabel1.setForeground(Color.WHITE);
        msgLabel2.setForeground(Color.LIGHT_GRAY);
        msgLabel3.setForeground(Color.LIGHT_GRAY);

        centerPanel.add(msgLabel1);
        centerPanel.add(msgLabel2);
        centerPanel.add(msgLabel3);
        add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        btnPanel.setOpaque(false);

        JButton playAgainBtn = pm.new Button1("Play Again", Color.WHITE, new Color(40, 100, 40));
        JButton quitBtn      = pm.new Button1("Quit",       Color.WHITE, new Color(120, 40, 40));

        playAgainBtn.setFont(pm.normalBoldFont);
        quitBtn.setFont(pm.normalBoldFont);

        playAgainBtn.addActionListener(this);
        quitBtn.addActionListener(this);

        pm.addHoverEffect(playAgainBtn, Color.LIGHT_GRAY, playAgainBtn.getBackground());
        pm.addHoverEffect(quitBtn,      Color.LIGHT_GRAY, quitBtn.getBackground());

        btnPanel.add(playAgainBtn);
        btnPanel.add(quitBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("Play Again"))
        {
            data.resetProgress();
            pm.changePanelCard("Level 1");
        }
        else if (cmd.equals("Quit"))
        {
            pm.frame.dispose();
            System.exit(0);
        }
    }
}