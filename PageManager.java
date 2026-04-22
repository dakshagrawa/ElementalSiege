import java.awt.*; import java.awt.event.*; import javax.swing.*; import javax.swing.event.*;

public class PageManager extends JPanel
{
    public PageManager()
    {
        CardLayout card = new CardLayout();
        setLayout(card);

        JPanel title = new TitlePage();

        add(title, "Title");

        card.show(this,"Title");
    }
}
