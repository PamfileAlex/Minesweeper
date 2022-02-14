public class Difficulty {
    private final int height;
    private final int width;
    private final int mineNumber;

    public Difficulty(int height, int width, int mineNumber) {
        this.height = height;
        this.width = width;
        this.mineNumber = mineNumber;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getMineNumber() {
        return mineNumber;
    }
}
