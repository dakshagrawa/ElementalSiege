import java.awt.*; import java.awt.event.*; import javax.swing.*; import javax.swing.event.*; import java.util.*; import java.io.*;

public class SignIn extends JPanel
{
    private JTextField username;
    private JTextField password;
    private JButton login;
    private JButton cancel;
    private GameData data;
    private PageManager pageMngr;

    public SignIn(PageManager pm)
    {
        data = pm.getGameData();
        pageMngr = pm;
        setLayout(new BorderLayout());

        add(new CenterPanel(), BorderLayout.CENTER);
        add(new SouthPanel(), BorderLayout.SOUTH);
    }
    
    public class CenterPanel extends JPanel
    {
        public CenterPanel()
        {
            setLayout(new GridLayout(2,1));

            username = new JTextField("Username", 20);
            password = new JTextField("Password", 20);

            add(username);
            add(password);
            
            username.setEditable(true);
            password.setEditable(true);
        }

        public class AccountInfo
        {
            public AccountInfo()
            {
                Scanner inFile = null;
                String fileName = "AccountInfo.txt";
                File inputFile = new File(fileName);

                try
                {
                    inFile = new Scanner(inputFile);
                }

                catch(FileNotFoundException e)
                {
                    System.err.printf("ERROR: Cannot open %s\n,", fileName);
                    System.out.println(e);
                    System.exit(1);
                }
            }

    public class SouthPanel extends JPanel implements ActionListener
    {
        public SouthPanel()
        {
            setLayout(new GridLayout(2,1));

            login = new JButton("Login");
            cancel = new JButton("Cancel");
            
            login.addActionListener(this);
            cancel.addActionListener(this);

            add(cancel);
            add(login);
        }

        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();

            if(command.equals("Login"))
            {
                String name = username.getText();
                String pwd = password.getText();
            }
            else if (command.equals("Cancel"))
            {
                pageMngr.changeCard("Cancel");
            }
        }
    }
}