import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class ExplanationPanel extends JPanel implements ActionListener
{
    private PageManager pm;
    private GameData data;
    private JLabel titleLabel;
    private JTextArea explanationArea;
    private JScrollPane scroller;
    private JButton continueBtn;
    private String nextPage;

    public ExplanationPanel(PageManager pageMngr, String nextPageName)
    {
        pm = pageMngr;
        data = pm.getGameData();
        nextPage = nextPageName;

        setLayout(new BorderLayout());
        setOpaque(false); //makes the rest of the game dark except for pop up explanation card

        buildUI();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void buildUI()
    {
        //Center popup card
        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setOpaque(false);

        PopupCard card = new PopupCard();
        card.setLayout(new BorderLayout(0, 16));
        card.setPreferredSize(new Dimension(680, 460));
        card.setBorder(BorderFactory.createEmptyBorder(30, 36, 24, 36));

        //Top: "oh no" heading
        JLabel oopsLabel = new JLabel("Not quite!", SwingConstants.CENTER);
        oopsLabel.setFont(pm.titleFont);
        oopsLabel.setForeground(new Color(220, 100, 100));
        card.add(oopsLabel, BorderLayout.NORTH);

        //Center: topic title + explanation text
        JPanel centerPanel = new JPanel(new BorderLayout(0, 12));
        centerPanel.setOpaque(false);

        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setFont(pm.normalBoldFont);
        titleLabel.setForeground(new Color(100, 200, 255));
        centerPanel.add(titleLabel, BorderLayout.NORTH);

        explanationArea = new JTextArea();
        explanationArea.setFont(pm.normalFont);
        explanationArea.setForeground(Color.WHITE);
        explanationArea.setBackground(new Color(30, 30, 50));
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setEditable(false);
        explanationArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scroller = new JScrollPane(explanationArea);
        scroller.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 120), 1));
        scroller.setOpaque(false);
        scroller.setBackground(new Color(30, 30, 50));
        explanationArea.setBackground(new Color(30, 30, 50));
        centerPanel.add(scroller, BorderLayout.CENTER);

        card.add(centerPanel, BorderLayout.CENTER);

        //Bottom: continue button
        continueBtn = pm.new Button1("Got it! Continue", Color.WHITE, new Color(40, 100, 40));
        continueBtn.setFont(pm.normalBoldFont);
        continueBtn.addActionListener(this);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setOpaque(false);
        btnPanel.add(continueBtn);
        card.add(btnPanel, BorderLayout.SOUTH);

        outerWrapper.add(card);
        add(outerWrapper, BorderLayout.CENTER);
    }

    public void loadExplanation(int questionNumber)
    {
        String fileName = "../storedData/Explanations.txt";
        Scanner input = null;
        try
        {
            input = new Scanner(new File(fileName));
        }
        catch (FileNotFoundException e)
        {
            titleLabel.setText("Explanation not found.");
            explanationArea.setText("Could not load explanation file.");
            return;
        }

        String foundTopic       = "";
        String foundExplanation = "";
        String currentTopic     = "";
        StringBuilder currentExplanation = new StringBuilder();
        boolean inMatchingTopic = false;
        boolean foundMatch      = false;

        while (input.hasNextLine())
        {
            String line = input.nextLine();

            if (line.startsWith("Topic:"))
            {
                //Save previous topic if it was a match
                if (inMatchingTopic)
                {
                    foundTopic       = currentTopic;
                    foundExplanation = currentExplanation.toString().trim();
                    foundMatch       = true;
                    break;
                }

                //Start a new topic block
                currentTopic = line.substring(6).trim();
                currentExplanation = new StringBuilder();
                inMatchingTopic = false;
            }
            else if (line.startsWith("Questions:"))
            {
                //Check if this topic covers current question number
                String numbersPart = line.substring(10).trim();
                String[] numbers = numbersPart.split(",");
                for (int i = 0; i < numbers.length; i++)
                {
                    int num = Integer.parseInt(numbers[i].trim());
                    if (num == questionNumber)
                    {
                        inMatchingTopic = true;
                        break;
                    }
                }
            }
            else
            {
                currentExplanation.append(line).append("\n");
            }
        }

        //Catch last topic block if file ends without a new Topic: line
        if (inMatchingTopic && !foundMatch)
        {
            foundTopic       = currentTopic;
            foundExplanation = currentExplanation.toString().trim();
            foundMatch       = true;
        }

        input.close();

        if (foundMatch)
        {
            titleLabel.setText("Topic: " + foundTopic);
            explanationArea.setText(foundExplanation);
            scroller.getVerticalScrollBar().setValue(0); //scrolls to top of explanation in pop-up
        }
        else //in case error occurs and explanation isn't retrieved from txt file
        {
            titleLabel.setText("No explanation available.");
            explanationArea.setText("No explanation was found for question " + questionNumber + ".");
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        pm.changePanelCard(nextPage);
    }

    //dark background for the pop-up and rounds the corners of pop-up
    class PopupCard extends JPanel
    {
        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(22, 22, 40, 240));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

            g2.setColor(new Color(220, 100, 100, 80));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 24, 24);
        }
    }
}