import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

public class Minesweeper extends JPanel {
    private ArrayList<ArrayList<Box>> matrix;
    private ArrayList<ArrayList<Boolean>> added;
    private int height;
    private int width;
    private int mineNumber;
    private int remainingFlags;
    private int flags;
    private int interval = 0;
    private Timer timer;
    private boolean isRunning = false;

    public Minesweeper(Difficulty difficultyOption) {
        setDifficulty(difficultyOption);
        createMatrix();
        createAdded();
        addMouseListener(mouseAdapter);
        initResetButton();
    }

    public Dimension getDimensions() {
        return new Dimension((width + 3) * Box.sideLength + Box.distance * width - 30,
                (height + 3) * Box.sideLength + Box.distance * height - Box.distance);
    }

    private void setDifficulty(Difficulty difficultyOption) {
        height = difficultyOption.getHeight();
        width = difficultyOption.getWidth();
        mineNumber = difficultyOption.getMineNumber();
        if (mineNumber >= height * width) {
            mineNumber = height * width - 9;
        }
        remainingFlags = mineNumber;
        flags = mineNumber;
    }

    private void createMatrix() {
        matrix = new ArrayList<>(height);
        for (int indexRows = 0; indexRows < height; ++indexRows) {
            matrix.add(new ArrayList<>(width));
            for (int indexColumns = 0; indexColumns < width; ++indexColumns) {
                matrix.get(indexRows).add(new Box());
            }
        }
    }

    private void initResetButton() {
        CustomButton reset = new CustomButton("Restart");
        reset.setBounds(width / 2 - 10, 10, 20, 10);
        reset.setFont(getFont().deriveFont(12.0f));
        this.add(reset);
        reset.addActionListener(e -> {
            restart();
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("TimesRoman", Font.BOLD, Box.sideLength / 2));
        g.drawString(Integer.toString(remainingFlags), Box.sideLength + Box.distance * 2, Box.sideLength * 2 / 3);
        g.drawString(Integer.toString(interval), getDimensions().width - Box.sideLength * 2, Box.sideLength * 2 / 3);
        for (int row = 0; row < matrix.size(); ++row) {
            for (int column = 0; column < matrix.get(row).size(); ++column) {
                matrix.get(row).get(column).draw(g, (column + 1) * Box.sideLength + Box.distance * column,
                        (row + 1) * Box.sideLength + Box.distance * row);
            }
        }
    }

    private final MouseAdapter mouseAdapter = new MouseAdapter() {
        public void mousePressed(MouseEvent mouseEvent) {
            Position position = getPosition(mouseEvent.getPoint());
            if (position == null) {
                return;
            }
            if (!isRunning) {
                Timer();
                isRunning = true;
                generateMines(position);
                generateNumbers();
            }
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                leftMousePressed(position);
            } else if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                rightMousePressed(position);
            }
//            if (remainingFlags != 0) {
//                return;
//            }
            if (checkForWin()) {
                System.out.println("YOU WON");
                removeMouseListener(mouseAdapter);
                timer.cancel();
                JOptionPane.showMessageDialog(null,
                        "YOU WON");
                repaint();
            }
        }
    };

    private void leftMousePressed(Position position) {
        Box box = matrix.get(position.getRow()).get(position.getColumn());
        if (box.hasFlag()) {
            return;
        }
        if (box.hasMine()) {
            System.out.println("GAME OVER");
            showMines();
            JOptionPane.showMessageDialog(null,
                    "GAME OVER");
        }
        show(position.getRow(), position.getColumn());
        repaint();
    }

    private void rightMousePressed(Position position) {
        Box box = matrix.get(position.getRow()).get(position.getColumn());
        if (box.hasFlag()) {
            box.setFlag(false);
            ++remainingFlags;
            repaint();
            return;
        }
        if (remainingFlags == 0 || box.isVisible()) {
            return;
        }
        box.setFlag(true);
        --remainingFlags;
        repaint();
    }

    private Position getPosition(Point mousePoint) {
        for (int indexRows = 0; indexRows < height; ++indexRows) {
            for (int indexColumns = 0; indexColumns < width; ++indexColumns) {
                if (mousePoint.getX() >= (indexColumns + 1) * Box.sideLength + Box.distance * indexColumns &&
                        mousePoint.getX() <= (indexColumns + 2) * Box.sideLength + Box.distance * indexColumns &&
                        mousePoint.getY() >= (indexRows + 1) * Box.sideLength + Box.distance * indexRows &&
                        mousePoint.getY() <= (indexRows + 2) * Box.sideLength + Box.distance * indexRows) {
                    return new Position(indexRows, indexColumns);
                }
            }
        }
        return null;
    }

    private void Timer() {
        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ++interval;
                repaint();
            }
        }, delay, period);
    }

    private boolean checkForWin() {
        int counter = 0;
        ArrayList<Box> boxes = new ArrayList<>();
        for (ArrayList<Box> row : matrix) {
            for (Box box : row) {
                if (!box.isChecked()) {
                    ++counter;
                    if (counter > remainingFlags) {
                        return false;
                    }
                    boxes.add(box);
                }
            }
        }
        if (counter == remainingFlags) {
            for (Box box : boxes) {
                box.setFlag(true);
            }
            remainingFlags = 0;
            return true;
        }
        return false;
    }

    private void showMines() {
        for (ArrayList<Box> row : matrix) {
            for (Box box : row) {
                if (box.hasMine()) {
                    box.setVisible(true);
                }
            }
        }
        removeMouseListener(mouseAdapter);
        timer.cancel();
        repaint();
    }

    private void restart() {
        removeMouseListener(mouseAdapter);
        addMouseListener(mouseAdapter);
        resetMatrix();
        remainingFlags = flags;
        interval = 0;
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        //Timer();
        isRunning = false;
        repaint();
    }

    private void resetMatrix() {
        for (ArrayList<Box> row : matrix) {
            for (Box box : row) {
                box.reset();
            }
        }
    }

    private void generateMines(Position position) {
        int remainingMines = mineNumber;
        do {
            Random rand = new Random();
            int row = rand.nextInt(height);
            int column = rand.nextInt(width);
            if (row >= position.getRow() - 1 && row <= position.getRow() + 1 &&
                    column >= position.getColumn() - 1 && column <= position.getColumn() + 1) {
                continue;
            }
            Box box = matrix.get(row).get(column);
            if (!box.hasMine()) {
                box.setMine(true);
                --remainingMines;
            }
        } while (remainingMines != 0);
    }

    private void generateNumbers() {
        for (int indexRows = 0; indexRows < height; ++indexRows) {
            for (int indexColumns = 0; indexColumns < width; ++indexColumns) {
                matrix.get(indexRows).get(indexColumns).setNumber(getNumber(indexRows, indexColumns));
            }
        }
    }

    private int getNumber(int row, int column) {
        if (matrix.get(row).get(column).hasMine()) {
            return 0;
        }
        int count = 0;
        count += checkBox(row - 1, column - 1);
        count += checkBox(row - 1, column);
        count += checkBox(row - 1, column + 1);
        count += checkBox(row, column + 1);
        count += checkBox(row + 1, column + 1);
        count += checkBox(row + 1, column);
        count += checkBox(row + 1, column - 1);
        count += checkBox(row, column - 1);
        return count;
    }

    private int checkBox(int row, int column) {
        if (row >= 0 && row < height && column >= 0 && column < width) {
            if (matrix.get(row).get(column).hasMine()) {
                return 1;
            }
        }
        return 0;
    }

    private void show(int row, int column) {
        Queue<Position> queue = new LinkedList<>();
        queue.add(new Position(row, column));
        while (!queue.isEmpty()) {
            Position current = queue.poll();
            Box box = matrix.get(current.getRow()).get(current.getColumn());
            box.setVisible(true);
            if (box.getNumber() != 0 || box.hasMine()) {
                continue;
            }
            showCheck(queue, current.getRow() - 1, current.getColumn());
            showCheck(queue, current.getRow(), current.getColumn() + 1);
            showCheck(queue, current.getRow() + 1, current.getColumn());
            showCheck(queue, current.getRow(), current.getColumn() - 1);

            if (box.getNumber() == 0) {
                showCheck(queue, current.getRow() - 1, current.getColumn() - 1);
                showCheck(queue, current.getRow() - 1, current.getColumn() + 1);
                showCheck(queue, current.getRow() + 1, current.getColumn() + 1);
                showCheck(queue, current.getRow() + 1, current.getColumn() - 1);
            }
        }
        resetAdded();
    }

    private boolean checkPosition(int row, int column) {
        return row >= 0 && row < height && column >= 0 && column < width;
    }

    private void showCheck(Queue<Position> queue, int row, int column) {
        if (!checkPosition(row, column)) {
            return;
        }
        Box box = matrix.get(row).get(column);
        if (!box.isVisible() && !box.hasMine() && !added.get(row).get(column)) {
            queue.add(new Position(row, column));
            added.get(row).set(column, true);
        }
    }

    private void createAdded() {
        added = new ArrayList<>(height);
        for (int indexRows = 0; indexRows < height; ++indexRows) {
            added.add(new ArrayList<>(width));
            for (int indexColumns = 0; indexColumns < width; ++indexColumns) {
                added.get(indexRows).add(false);
            }
        }
    }

    private void resetAdded() {
        for (int row = 0; row < height; ++row) {
            for (int column = 0; column < width; ++column) {
                added.get(row).set(column, false);
            }
        }
    }
}
