import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Box {
    private int number;
    private boolean mine;
    private boolean visible;
    private boolean flag;
    public static final int sideLength = 40;
    public static final int distance = 2;
    private static final ArrayList<BufferedImage> images = new ArrayList<>(
            Arrays.asList(getScaledImage(new ImageIcon("Assets/0.png")),
                    getScaledImage(new ImageIcon("Assets/1.png")),
                    getScaledImage(new ImageIcon("Assets/2.png")),
                    getScaledImage(new ImageIcon("Assets/3.png")),
                    getScaledImage(new ImageIcon("Assets/4.png")),
                    getScaledImage(new ImageIcon("Assets/5.png")),
                    getScaledImage(new ImageIcon("Assets/6.png")),
                    getScaledImage(new ImageIcon("Assets/7.png")),
                    getScaledImage(new ImageIcon("Assets/8.png")),
                    getScaledImage(new ImageIcon("Assets/facingDown.png")),
                    getScaledImage(new ImageIcon("Assets/flagged.png")),
                    getScaledImage(new ImageIcon("Assets/Mine.png")))
    );
    private static final int FACING_DOWN_IMAGE = 9;
    private static final int FLAGGED_IMAGE = 10;
    private static final int MINE_IMAGE = 11;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean hasMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean hasFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isChecked() {
        return visible || flag;
    }

    public void reset() {
        number = 0;
        mine = false;
        visible = false;
        flag = false;
    }

    public void draw(Graphics g, int x, int y) {
        BufferedImage scaledImage;
        if (flag) {
            scaledImage = images.get(FLAGGED_IMAGE);
        } else if (mine && visible) {
            scaledImage = images.get(MINE_IMAGE);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, sideLength, sideLength);
        } else if (!visible) {
            scaledImage = images.get(FACING_DOWN_IMAGE);
        } else {
            scaledImage = images.get(number);
        }
        g.drawImage(scaledImage, x, y, sideLength, sideLength, null);
    }

    private static BufferedImage getScaledImage(ImageIcon imageIcon) {
        BufferedImage image = new BufferedImage(sideLength, sideLength, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(imageIcon.getImage(), 0, 0, sideLength, sideLength, null);
        return image;
    }
}
