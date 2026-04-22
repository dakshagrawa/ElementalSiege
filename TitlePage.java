import java.awt.*; import java.awt.event.*; import javax.swing.*; import javax.swing.event.*;

public class TitlePage extends JPanel 
{
    Image picture;
    
    public TitlePage()
    {
        setLayout(null);
        picture = null;
        StartButtons();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        getMyImage(); //need to input image  
        g.drawImage(picture, 0, 0, getWidth(),getHeight(),this);
    }

    public void getMyImage()
    {
        String picName = "images/HomeScreenBackground.png";
        try
        {
            picture = ImageIO.read(new File(picName));
        }

        catch(IOException e)
        {
            System.err.println("\n\n" + picName + " can't be found. \n\n");
            e.printStackTrace();        
        }
    }

    public void StartButtons() ///Login and sign-in button on title page
    {
        JButton login;
        JButton signUp;

        login = new JButton("Login");
        signUp = new JButton("Sign up");

        login.setBackground(Color.BLUE);
        signUp.setBackground(Color.GREEN);
        login.setBounds(500, 700, 200, 50);
        signUp.setBounds(750, 700, 200, 50);

        add(login);
        add(signUp);
    }
    
    
}
