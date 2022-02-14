import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

public class CustomButton extends JButton {
    private Color MouseHover = new Color(255, 111, 99);
    private Color MousePress = new Color(36, 168, 110);
    private Color BackgroundColor = new Color(255, 51, 0);
    private boolean enter, press;
    private static final float fontSize = 18.0f;

    public boolean isEnter() {
        return enter;
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
        repaint();
    }

    public boolean isPress() {
        return press;
    }

    public void setPress(boolean press) {
        this.press = press;
        repaint();
    }

    public CustomButton(String Text) {
        super.setText(Text);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setForeground(Color.white);
        setFont(getFont().deriveFont(fontSize));


        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setEnter(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setEnter(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                setPress(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                setPress(false);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D gd = (Graphics2D) g.create();
        Color background = BackgroundColor;
        Shape shape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10);
        if (isEnter()) {
            background = MouseHover;
            if (isPress()) {
                background = MousePress;
            }
        }
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gd.setColor(background);
        gd.fill(shape);
        gd.dispose();
        super.paintComponent(g);
    }

    public Color getWarnaBackground() {
        return this.BackgroundColor;
    }

    public void setWarnaBackground(Color bgColor) {
        this.BackgroundColor = bgColor;
    }

    public Color getMouseHover() {
        return this.MouseHover;
    }

    public void setMouseHover(Color mouseHover) {
        this.MouseHover = mouseHover;
    }

    public Color getMousePress() {
        return this.MousePress;
    }

    public void setMousePress(Color mousePress) {
        this.MousePress = mousePress;
    }
}
