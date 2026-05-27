import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.Timer;

public class TutorialPanel extends JPanel implements ActionListener
{
    private PageManager pm;
    private GameData data;
    private String nextPage;

    private int currentSlide = 0;
    private JPanel slideArea;
    private JLabel slideImageLabel;
    private JLabel slideTitleLabel;
    private JLabel slideTextLabel;
    private JLabel slideCounterLabel;
    private JButton leftBtn;
    private JButton rightBtn;
    private JButton skipBtn;
    private DotIndicator dotIndicator;

    private Timer slideTimer;
    private float animAlpha = 1.0f;
    private boolean fadingOut = false;
    private int pendingSlide = 0;

    private static final String[] TITLES = {"Welcome to Elemental Siege!", "Your Mission", "Choose Your Cards", "Battle Time",
        "Cards Revealed", "How Damage Works", "Win or Lose", "Good Luck!"};

    private static final String[] TEXTS = {"Here's a quick tutorial to get you ready for battle.",
        "You are a chemistry researcher. Your goal is to defend your laboratory against invading monsters. Battles take place by comparing cards.",
        "In the next screen, choose four cards. Pick cards with the highest numbers next to Problem Solving, Wisdom, and Curiosity.",
        "After answering a question, a card is randomly chosen from your collection. Pick an attribute from your card to compete with.",
        "The enemy's card flips over. The game compares the number value of the attribute you picked on both cards. Higher value = you deal damage. Lower value = you take damage.",
        "Damage is based on the difference between the two values. Example: if enemy Curiosity is 2 higher than yours, you lose 2 health.",
        "There are 10 levels until victory and the starting health of the enemy will keep increasing in each level. If your health drops to 0 at any point in the game, you have lost and your lab has been sacrificed.",
        "You've got the knowledge. Now go defend the lab!"};

    private static final String[] IMAGE_NAMES = {null, null, "tutorial_get_cards.png", "tutorial_battle.png", "tutorial_reveal.png",
        "tutorial_damage.png", "tutorial_health.png", null};

       //null opening slide -- heading: no image 
       //null objective slide -- mission: no image
       //"tutorial_get_cards.png" - getNewCharacterCards panel screenshot
       //"tutorial_battle.png" -- battle scene screenshot
       //"tutorial_reveal.png" -- card reveal screenshot
       //"tutorial_damage.png" -- damage example screenshot
       //"tutorial_health.png" -- health bar/levels screenshot
       //null final slide -- no image

    private Color getSlideAccent(int index)
    {
        if (index == 0) return new Color(100, 180, 255);
        else if (index == 1) return new Color(120, 220, 160);
        else if (index == 2) return new Color(255, 200, 80);
        else if (index == 3) return new Color(220, 100, 100);
        else if (index == 4) return new Color(180, 120, 255);
        else if (index == 5) return new Color(255, 140, 80);
        else if (index == 6) return new Color(80, 200, 200);
        else if (index == 7) return new Color(255, 220, 50);
        else return Color.WHITE;
    }
    //there is a new accent color for each slide just so that the tutorial slides don't looking boring and wordy

    public TutorialPanel(PageManager pageMngr, String nextPageName)
    {
        pm = pageMngr;
        data = pm.getGameData();
        nextPage = nextPageName;

        setLayout(new BorderLayout());
        setOpaque(false); //background is drawn by paintComponent

        buildDimOverlay();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //blurs out the background game when tutorial pop-up is open
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void buildDimOverlay()
    {
        //Centers the popup card
        JPanel outerWrapper = new JPanel(new GridBagLayout());
        outerWrapper.setOpaque(false);
        outerWrapper.add(buildPopupCard());
        add(outerWrapper, BorderLayout.CENTER);

        //Skip button in case user doesn't want tutorial
        skipBtn = pm.new Button1("Skip Tutorial", new Color(180, 180, 180));
        skipBtn.setFont(pm.normalFont);
        skipBtn.addActionListener(this);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        topBar.setOpaque(false);
        topBar.add(skipBtn);
        add(topBar, BorderLayout.SOUTH);
    }

    private JPanel buildPopupCard()
    {
        PopupCard card = new PopupCard();
        card.setLayout(new BorderLayout(0, 14));
        card.setPreferredSize(new Dimension(700, 480));
        card.setBorder(BorderFactory.createEmptyBorder(30, 36, 24, 36));

        //Top of each slide wil always contain the title from TITLES array
        slideTitleLabel = new JLabel(TITLES[0], SwingConstants.CENTER);
        slideTitleLabel.setFont(pm.titleFont);
        slideTitleLabel.setForeground(getSlideAccent(0));
        card.add(slideTitleLabel, BorderLayout.NORTH);

        //Center: image + text under each other
        JPanel centerArea = new JPanel(new BorderLayout(0, 16));
        centerArea.setOpaque(false);

        slideImageLabel = new JLabel("", SwingConstants.CENTER);
        slideImageLabel.setPreferredSize(new Dimension(600, 160));
        centerArea.add(slideImageLabel, BorderLayout.CENTER);

        slideTextLabel = new JLabel(wrapText(TEXTS[0]), SwingConstants.CENTER);
        slideTextLabel.setFont(pm.normalFont);
        slideTextLabel.setForeground(Color.WHITE);
        slideTextLabel.setVerticalAlignment(SwingConstants.TOP);
        centerArea.add(slideTextLabel, BorderLayout.SOUTH);

        card.add(centerArea, BorderLayout.CENTER);

        //Bottom: left/right buttons + dot indicator
        JPanel bottomBar = new JPanel(new BorderLayout(0, 8));
        bottomBar.setOpaque(false);

        dotIndicator = new DotIndicator(TITLES.length);
        JPanel dotWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dotWrapper.setOpaque(false);
        dotWrapper.add(dotIndicator);
        bottomBar.add(dotWrapper, BorderLayout.NORTH);

        JPanel navRow = new JPanel(new GridLayout(1, 3, 20, 0));
        navRow.setOpaque(false);

        leftBtn = pm.new Button1("< Prev", Color.WHITE, new Color(60, 60, 90));
        leftBtn.setFont(pm.normalFont);
        leftBtn.addActionListener(this);
        leftBtn.setEnabled(false); //disables back button on first slide

        slideCounterLabel = new JLabel("1 / " + TITLES.length, SwingConstants.CENTER);
        slideCounterLabel.setFont(pm.normalFont);
        slideCounterLabel.setForeground(new Color(180, 180, 180));

        rightBtn = pm.new Button1("Next >", Color.WHITE, new Color(60, 60, 90));
        rightBtn.setFont(pm.normalFont);
        rightBtn.addActionListener(this);

        navRow.add(leftBtn);
        navRow.add(slideCounterLabel);
        navRow.add(rightBtn);
        bottomBar.add(navRow, BorderLayout.SOUTH);

        card.add(bottomBar, BorderLayout.SOUTH);

        return card;
    }

    //we use timer to give each slide a fade transition like in Google Slides
    private void goToSlide(int index)
    {
        if (index < 0 || index >= TITLES.length) return;
        pendingSlide = index;
        fadingOut = true;
        animAlpha = 1.0f;

        slideTimer = new Timer(16, new SlideTimerListener());
        slideTimer.start();
    }

    class SlideTimerListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) //MARK
        {
            if (fadingOut)
            {
                animAlpha -= 0.08f;
                if (animAlpha <= 0f)
                {
                    animAlpha = 0f;
                    fadingOut = false;
                    applySlide(pendingSlide);
                }
            }
            else
            {
                animAlpha += 0.08f;
                if (animAlpha >= 1f)
                {
                    animAlpha = 1f;
                    slideTimer.stop();
                }
            }
            //Apply alpha to labels
            applyAlpha(animAlpha);
            repaint();
        }
    }

    private void applySlide(int index)
    {
        currentSlide = index;

        slideTitleLabel.setText(TITLES[index]);
        slideTitleLabel.setForeground(getSlideAccent(index));
        slideTextLabel.setText(wrapText(TEXTS[index]));
        slideCounterLabel.setText((index + 1) + " / " + TITLES.length);

        //Load image if available (looks at the images array from above)
        String imgName = IMAGE_NAMES[index];
        if (imgName != null)
        {
            Image img = PageManager.Functions.getImage(imgName);
            if (img != null)
            {
                Image scaled = img.getScaledInstance(580, 150, Image.SCALE_SMOOTH);
                slideImageLabel.setIcon(new ImageIcon(scaled));
                slideImageLabel.setText("");
                slideImageLabel.setVisible(true);
            }
            else
            {
                slideImageLabel.setIcon(null);
                slideImageLabel.setText("[ screenshot ]"); //in case it can't display
                slideImageLabel.setForeground(new Color(100, 100, 120));
                slideImageLabel.setVisible(true);
            }
        }
        else
        {
            slideImageLabel.setIcon(null);
            slideImageLabel.setText("");
            slideImageLabel.setVisible(false); //hides imageLabel so that text is centered when the slide doesn't have an image to display
        }

        leftBtn.setEnabled(index > 0);
        if (index == TITLES.length - 1)
            rightBtn.setText("Let's Go!");
        else
            rightBtn.setText("Next >");

        dotIndicator.setActive(index);
        dotIndicator.repaint();
    }

    private void applyAlpha(float alpha) //MARK
    {
        //Simulate fade by blending foreground toward background color
        Color base = new Color(30, 30, 50);
        slideTitleLabel.setForeground(blend(getSlideAccent(currentSlide), base, alpha));
        slideTextLabel.setForeground(blend(Color.WHITE, base, alpha));
        slideCounterLabel.setForeground(blend(new Color(180, 180, 180), base, alpha));
    }

    private Color blend(Color a, Color b, float t)
    {
        int r = (int)(a.getRed()   * t + b.getRed()   * (1 - t));
        int g = (int)(a.getGreen() * t + b.getGreen() * (1 - t));
        int bl = (int)(a.getBlue() * t + b.getBlue()  * (1 - t));
        return new Color(Math.max(0, Math.min(255, r)), Math.max(0, Math.min(255, g)), Math.max(0, Math.min(255, bl)));
    }

    //Wraps long text into multi-line using basic line breaks
    private String wrapText(String text)
    {
        //Split into ~60 char chunks at word boundaries
        StringBuilder result = new StringBuilder("<html><div style='text-align:center;width:560px'>");
        String[] words = text.split(" ");
        int lineLen = 0;
        for (String word : words)
        {
            if (lineLen + word.length() > 60)
            {
                result.append("<br>");
                lineLen = 0;
            }
            result.append(word).append(" ");
            lineLen += word.length() + 1;
        }
        result.append("</div></html>");
        return result.toString();
    }

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        if (cmd.equals("< Prev"))
        {
            goToSlide(currentSlide - 1);
        }
        else if (cmd.equals("Next >"))
        {
            goToSlide(currentSlide + 1);
        }
        else if (cmd.equals("Let's Go!") || cmd.equals("Skip Tutorial"))
        {
            pm.changePanelCard(nextPage);
        }
    }

    class PopupCard extends JPanel
    {
        public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Rounded dark background for the pop up card
            g2.setColor(new Color(22, 22, 40, 240));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

            //There's a glow at the top of each slide that matches the accent color of the slide
            Color accent = getSlideAccent(currentSlide);
            GradientPaint glow = new GradientPaint(
                0, 0, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 60),
                0, getHeight() / 2, new Color(0, 0, 0, 0)
            );
            g2.setPaint(glow);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);

            //Border
            g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 100));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 24, 24);
        }
    }

    //indicates what slide user is on
    class DotIndicator extends JPanel
    {
        private int total;
        private int active;
        private static final int DOT_SIZE = 10;
        private static final int DOT_GAP  = 8;

        public DotIndicator(int totalSlides)
        {
            total = totalSlides;
            active = 0;
            setOpaque(false);
            setPreferredSize(new Dimension(total * (DOT_SIZE + DOT_GAP), DOT_SIZE + 4));
        }

        public void setActive(int index)
        {
            active = index;
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int totalWidth = total * DOT_SIZE + (total - 1) * DOT_GAP;
            int startX = (getWidth() - totalWidth) / 2;

            for (int i = 0; i < total; i++)
            {
                int x = startX + i * (DOT_SIZE + DOT_GAP);
                int y = (getHeight() - DOT_SIZE) / 2;
                if (i == active)
                {
                    g2.setColor(getSlideAccent(active));
                    g2.fillOval(x, y, DOT_SIZE, DOT_SIZE);
                }
                else
                {
                    g2.setColor(new Color(100, 100, 130));
                    g2.fillOval(x, y, DOT_SIZE, DOT_SIZE);
                }
            }
        }
    }
} //yay we're done