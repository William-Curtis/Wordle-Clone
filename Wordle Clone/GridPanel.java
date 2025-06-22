import javax.swing.*;
import java.awt.*;

public class GridPanel extends JPanel {
    private Color backColour;
    // private int row;
    // private int col;
    private String letter;
    private JLabel label;

    public GridPanel(Color backColour, int row, int col) {
        this.backColour = backColour;
        this.setBackground(backColour);
        this.setPreferredSize(new Dimension(Wordle.gridSize, Wordle.gridSize));
        this.setBorder(BorderFactory.createLineBorder(new Color(58, 58, 60)));
        label = new JLabel();
        this.add(label);
        label.setFont(new Font("Verdana", Font.PLAIN, (int) Math.round(Wordle.gridSize * 0.8)));
        label.setForeground(Color.WHITE);
        setLetter("");

        // this.row = row;
        // this.col = col;
    }

    public Color get() {
        return backColour;
    }

    public void changeColour(Color colour) {
        this.setBackground(colour);
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        setLetter(String.valueOf(letter));
    }

    public void setLetter(String letter) {
        this.letter = letter.toUpperCase();
        this.label.setText(letter.toUpperCase());
    }

    public void clearLetter() {
        setLetter("");
    }
}