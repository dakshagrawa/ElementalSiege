import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BattlePanel extends JPanel implements ActionListener
{
    private GameData data;
	private PageManager pm;

    public BattlePanel(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();

        setLayout(new GridLayout(1, 4, 10, 10));

        
        Image userCard = PageManager.Functions.getImage("");



    }

	public void actionPerformed(ActionEvent e) 
	{

	}
}