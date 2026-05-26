import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class TestImage {
    public static void main(String[] args) {
        try {
            Image img = ImageIO.read(new File("storedData/images/characters/characters1.png"));
            System.out.println("Image: " + img);
            if (img != null) {
                System.out.println("Width: " + img.getWidth(null));
                System.out.println("Height: " + img.getHeight(null));
            } else {
                System.out.println("Image is null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
