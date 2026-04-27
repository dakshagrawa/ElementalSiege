// Daksh Agrawal & Dhikshitha Aiely
// Period 7

import javax.swing.JFrame;

public class ElementalSiege extends JFrame 
{
	private PageManager pm;

    public ElementalSiege()
	{
        super("Elemental Siege");
        setSize(1500, 800);
        setLocation(10, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
		pm = new PageManager();
        setContentPane(pm);
        setVisible(true);
    }

    public static void main(String[] args) 
	{
        ElementalSiege es = new ElementalSiege();
    }
}
