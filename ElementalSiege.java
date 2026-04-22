// Daksh Agrawal & Dhikshitha Aiely
// Period 7

import javax.swing.JFrame;
import java.awt.Color;

public class ElementalSiege 
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Elemental Siege");

		frame.setSize(1500, 800);
		frame.setLocation(10, 0);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
		frame.setBackground(Color.GRAY);
        frame.setContentPane(new TitlePage());
        frame.setVisible(true);
    }
}

