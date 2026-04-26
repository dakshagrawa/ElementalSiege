import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;    
public class Settings extends JPanel implements ActionListener, ChangeListener
{
    private GameData data;

    public Settings(PageManager pm)
    {
        data = pm.getGameData();
        setLayout(new GridLayout(3, 1, 10, 20));
        
        JSlider volume = new JSlider(0, 100, 100);
        volume.setOrientation(JSlider.HORIZONTAL);
        volume.addChangeListener(this);
        add(volume);

        JButton exit = new JButton("Save & Exit");
        exit.addActionListener(this);
        add(exit);

        JButton quit = new JButton("Quit");
        quit.addActionListener(this);
        add(quit);
    }

    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if (command.equals("Save & Exit"))
        {

        }
        else if (command.equals("Quit"))
        {

        }
        
    }

    public void stateChanged(ChangeEvent e)
    {

    }
}