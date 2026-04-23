import java.awt.*; import java.awt.event.*; import javax.swing.*; import javax.swing.event.*; import java.util.*;

public class PageManager extends JPanel
{
    private CardLayout card;
    private GameData data;

    public PageManager()
    {
        data = new GameData();

        card = new CardLayout();
        setLayout(card);

        add(new TitlePage(this), "Title");
        add(new SignIn(this),"Login");
        add(new Settings(this),"Settings");

        card.show(this,"Title");
    }

    public void changeCard(String cardName)
    {
        card.show(this,cardName);
    }

    public GameData getGameData()
    {
        return data;
    }
}
