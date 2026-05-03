import java.awt.*;
import javax.swing.*;

public class StartGamePanel extends JPanel
{
	private PageManager pm;
	private GameData data;

	public StartGamePanel(PageManager pageMngr)
	{
		pm = pageMngr;
		data = pm.getGameData();

		setLayout(new BorderLayout());
	}
}
