import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MinesweeperMenu extends JFrame implements ActionListener {
    private CustomButton BeginnerButton, IntermediateButton, ExpertButton, CustomDiffButton, ExitButton;
    private static int difficultyOption;

    static private final ArrayList<Difficulty> difficulties = new ArrayList<>(
            Arrays.asList(new Difficulty(9, 9, 10),
                    new Difficulty(16, 16, 40),
                    new Difficulty(16, 30, 99)));

    private static final int width = 1024;
    private static final int height = 720;
    private static final int buttonWidth = 200;
    private static final int buttonHeight = 60;

    public MinesweeperMenu() {
        this.setTitle("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        Container frameContainer = this.getContentPane();

        initButtons(frameContainer);

        ImagePanel BackgroundImage = new ImagePanel("Assets/Background.jpg");
        this.add(BackgroundImage);

        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    private void initButtons(Container frameContainer) {
        BeginnerButton = new CustomButton("Beginner");
        BeginnerButton.setBounds(40, 150, buttonWidth, buttonHeight);
        frameContainer.add(BeginnerButton);

        IntermediateButton = new CustomButton("Intermediate");
        IntermediateButton.setBounds(40, 220, buttonWidth, buttonHeight);
        frameContainer.add(IntermediateButton);

        ExpertButton = new CustomButton("Expert");
        ExpertButton.setBounds(40, 290, buttonWidth, buttonHeight);
        frameContainer.add(ExpertButton);

        CustomDiffButton = new CustomButton("Custom");
        CustomDiffButton.setBounds(40, 360, buttonWidth, buttonHeight);
        frameContainer.add(CustomDiffButton);

        ExitButton = new CustomButton("Exit");
        ExitButton.setBounds(40, 430, buttonWidth, buttonHeight);
        frameContainer.add(ExitButton);

        BeginnerButton.addActionListener(this);
        IntermediateButton.addActionListener(this);
        ExpertButton.addActionListener(this);
        CustomDiffButton.addActionListener(this);
        ExitButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        String key = actionEvent.getActionCommand();

        switch (key) {
            case "Beginner" -> {
                this.dispose();
                //this.setVisible(false);
                difficultyOption = 0;
                BeginnerButton.setEnter(false);
                initGame();
            }
            case "Intermediate" -> {
                this.dispose();
                difficultyOption = 1;
                IntermediateButton.setEnter(false);
                initGame();
            }
            case "Expert" -> {
                this.dispose();
                difficultyOption = 2;
                ExpertButton.setEnter(false);
                initGame();
            }
            case "Custom" -> {
                this.dispose();
                difficultyOption = 3;
                CustomDiffButton.setEnter(false);
                initGame();
            }
            default -> System.exit(0);
        }
    }

    private Difficulty selectDifficulty() {
        if (difficultyOption < difficulties.size()) {
            return difficulties.get(difficultyOption);
        } else {
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                    new CustomBorder(),
                    new EmptyBorder(new Insets(4, 4, 4, 4))));
            JTextField heightText = new JTextField(3);
            JTextField widthText = new JTextField(3);
            JTextField mineText = new JTextField(3);
            heightText.setSize(3, 10);

            Object[] msg = {"Height:", heightText, "Width:", widthText, "Mines:", mineText};

            int result = JOptionPane.showConfirmDialog(
                    null,
                    msg,
                    "Custom Difficulty",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            int boardHeight;
            int boardWidth;
            int mineNumber;

            if (result == JOptionPane.YES_OPTION) {
                try {
                    boardHeight = heightText.getText().length() != 0 ? Integer.parseInt(heightText.getText()) : 0;
                    boardWidth = widthText.getText().length() != 0 ? Integer.parseInt(widthText.getText()) : 0;
                    mineNumber = mineText.getText().length() != 0 ? Integer.parseInt(mineText.getText()) : 0;
                    if (boardHeight > 3 && boardWidth > 3 && mineNumber != 0) {
                        return new Difficulty(boardHeight, boardWidth, mineNumber);
                    }
                } catch (NumberFormatException exc) {
                    exc.printStackTrace();
                    System.out.println("Error for custom difficulty");
                }
            } else {
                System.out.println("Canceled");
            }
        }
        return null;
    }

    private void initGame() {
        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Difficulty difficulty = selectDifficulty();
        if (difficulty == null) {
            MinesweeperMenu.this.setVisible(true);
            return;
        }
        Minesweeper game = new Minesweeper(difficulty);
        frame.add(game);
        frame.setSize(game.getDimensions());
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                MinesweeperMenu.this.setVisible(true);
            }
        });
    }


    public static void main(String[] args) {
        new MinesweeperMenu();
    }
}
