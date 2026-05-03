// Daksh Agrawal & Dhikshitha Aiely
// Period 7

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame; 

public class ElementalSiege extends JFrame implements WindowListener
{
	private PageManager pm;

    public ElementalSiege()
	{
        super("Elemental Siege");

		pm = new PageManager(this);
		
        setSize(1500, 800);
        setLocation(10, 0);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setContentPane(pm);
        setVisible(true);
		addWindowListener(this);
    }

    public static void main(String[] args) 
	{
        ElementalSiege es = new ElementalSiege();
    }

	public void windowClosing(WindowEvent e) 
	{
		if (!pm.currentCardName.equals("Close")) 
		{
			pm.changePanelCard("Close");
		}
	}
	
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}
