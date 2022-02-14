import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    ImageIcon backImage;
    public ImagePanel(String imageName) {
        backImage = new ImageIcon(imageName);
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage scaledImage = getScaledImage();
        super.paintComponent(g);
        g.drawImage(scaledImage, 0, 0, null);
    }

    private BufferedImage getScaledImage(){
        BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(backImage.getImage(), 0, 0,getWidth(),getHeight(), null);

        return image;
    }

}