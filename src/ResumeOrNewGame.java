import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;

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

        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(25, 25, 45));
        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        buildUI();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Image bg = PageManager.Functions.getImage("levelBackground.png");
        if (bg != null)
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        else
            setBackground(new Color(25, 25, 45));
    }

    private void buildUI()
    {
        //Top: heading
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 6));
        topPanel.setOpaque(false);

        JLabel heading = new JLabel("Welcome Back, " + data.getUsername() + "!", SwingConstants.CENTER);

        JLabel subheading = new JLabel("Here are the stats you had in your previous game when you left.", SwingConstants.CENTER);
        subheading.setFont(pm.normalFont);
        subheading.setForeground(new Color(180, 180, 200));
        topPanel.add(heading);
        topPanel.add(subheading);

        add(topPanel, BorderLayout.NORTH);

        //Center: card with previous stats
        StatsCard statsCard = new StatsCard();
        statsCard.setLayout(new GridLayout(3, 1, 0, 14));
        statsCard.setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

        statsCard.add(makeStatRow("Level Reached", "" + data.getSavedLevel(), new Color(255, 200, 80)));
        statsCard.add(makeStatRow("Health",        data.getSavedHealth() + " / 100", new Color(80, 200, 120)));
        statsCard.add(makeStatRow("Cards Owned",   buildCardNamesList(), new Color(180, 120, 255)));

        add(statsCard, BorderLayout.CENTER);

        //Bottom: buttons
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        resumeBtn  = pm.new Button1("Resume Game", Color.WHITE, new Color(40, 120, 40));
        newGameBtn = pm.new Button1("New Game",    Color.WHITE, new Color(140, 40, 40));

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

    /** Makes a single row with a colored label on the left and value on the right. */
    private JPanel makeStatRow(String labelText, String valueText, Color accentColor)
    {
        JPanel row = new JPanel(new GridLayout(1, 2, 20, 0));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText, SwingConstants.RIGHT);
        label.setFont(pm.normalBoldFont);
        label.setForeground(accentColor);

        JLabel value = new JLabel(valueText, SwingConstants.LEFT);
        value.setFont(pm.normalFont);
        value.setForeground(Color.WHITE);

        row.add(label);
        row.add(value);
        return row;
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
                if (foundAny) 
                    sb.append(", ");
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
        if (cmd.equals("Resume Game"))
        {
            pm.changePanelCard("Level " + data.getSavedLevel());
        }
        else if (cmd.equals("New Game"))
        {
            data.resetProgress();
            pm.changePanelCard("Tutorial");
        }
    }

    //Rounded card panel for the stats section
    class StatsCard extends JPanel
    {
        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(35, 35, 60, 220));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.setColor(new Color(80, 80, 130));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
        }
    }

    public void refresh()
    {
        removeAll();
        buildUI();
        revalidate();
        repaint();
    }
}