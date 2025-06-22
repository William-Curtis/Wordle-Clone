import java.util.Random;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Wordle implements KeyListener {
    // Globals
    private static Set<String> WORDS; // set of all 5 letter words
    private static String WORD; // the word to guess
    private static final int GUESSES = 6;
    private static final int WORD_LEN = 5;
    private static GridPanel[][] grid = new GridPanel[GUESSES][WORD_LEN];
    public static final int gridSize = 100;
    private static String typedChar;
    private static JPanel WordleBoard;
    // public static JLabel status = new JLabel("Game Status: ");

    private Wordle() {
        // Initial Setup for the User Interface
        JFrame mainWindow = new JFrame();
        mainWindow.setTitle("Wordle Clone");
        mainWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainWindow.setResizable(false);
        mainWindow.setSize(gridSize * WORD_LEN, gridSize * GUESSES);
        mainWindow.setLayout(null);
        mainWindow.addKeyListener(this);

        WordleBoard = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        WordleBoard.setLayout(new GridLayout(GUESSES, WORD_LEN));
        for (int row = 0; row < GUESSES; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col] = new GridPanel(new Color(18, 18, 19), row, col);
                WordleBoard.add(grid[row][col]);
            }
        }

        GridBagLayout gridBag = new GridBagLayout();
        mainWindow.getContentPane().setLayout(gridBag);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = WORD_LEN;
        mainWindow.getContentPane().add(WordleBoard, gridBagConstraints);

        // mainWindow.getContentPane().add(status, gridBagConstraints);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_BACK_SPACE)) {
            Wordle.typedChar = "DEL";
        }
        else if (e.getKeyChar() == KeyEvent.getExtendedKeyCodeForChar(KeyEvent.VK_ENTER)) {
            Wordle.typedChar = "ENTER";
        }
        else if (Character.isAlphabetic(e.getKeyChar())) {
            Wordle.typedChar = String.valueOf(e.getKeyChar());
        }
        // System.out.println("Input: " + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    private static void initializeWordsDictonary(String filename) {
        // Initializes the global var words to a set of valid 5 letter words from a specified file
        Set<String> words = new HashSet<>();

        // File IO
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine().toLowerCase());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File IO Error.");
            e.printStackTrace();
        }

        // Initializes the global var words
        Wordle.WORDS = words;
    }

    private static void initializeRandomWord() {
        // Initializes the global var word to a random word in the var words
        int item = new Random().nextInt(WORDS.size());
        int i = 0;
        for (String word : WORDS) {
            if (i == item) {
                Wordle.WORD = word;
                break;
            }
            i++;
        };
    }

    private static boolean isValidWord(String word) {
        // Checks if the word is valid by checking if it is in the set of 5 letter words
        return WORDS.contains(word.toLowerCase());
    }

    private static void updateBoard(String word, int row, int col) {
        if (word.toLowerCase().charAt(col) == WORD.charAt(col)) {
            // Set tile to Green
            grid[row][col].changeColour(new Color(0, 128, 0));
        }
        else if (WORD.contains(String.valueOf(word.toLowerCase().charAt(col)))) {
            // Set tile to Dark Yellow
            if (countOccurrences(word, String.valueOf(word.toLowerCase().charAt(col))) > 1) {
                int amount = countOccurrences(WORD, String.valueOf(word.toLowerCase().charAt(col)));
                int count = 0;
                for (int i = 0; i < WORD_LEN; i++) {
                    if (word.toLowerCase().charAt(i) == WORD.charAt(i) && word.toLowerCase().charAt(i) == word.toLowerCase().charAt(col)) {
                        count++;
                    }
                }
                for (int i = 0; i <= col; i++) {
                    if (word.toLowerCase().charAt(i) == word.charAt(col) && !(word.toLowerCase().charAt(i) == WORD.charAt(i))) {
                        count++;
                    }
                }
                if (count <= amount) {
                    grid[row][col].changeColour(new Color(186, 142, 35));
                }
                else {
                    grid[row][col].changeColour(new Color(58, 58, 60));
                }
            }
            else {
                grid[row][col].changeColour(new Color(186, 142, 35));
            }
        }
        else {
            grid[row][col].changeColour(new Color(58, 58, 60));
        }
    }

    private static int countOccurrences(String word, String chr) {
        return word.length() - word.replace(chr, "").length();
    }

    public static void main(String[] args) {
        // Initialize the set of valid words
        initializeWordsDictonary("WORDS");

        // Setup the UI
        new Wordle();

        // Replay
        boolean play = true;
        while (play) {
            // Initialize the word to guess
            initializeRandomWord();
            WORD = "coral";
            System.out.println("The word is: " + WORD);

            // Initialize misc variables
            String userGuess = "";
            int charCount;
            int guess;

            // Clear board
            for (int row = 0; row < GUESSES; row++) {
                for (int col = 0; col < WORD_LEN; col++) {
                    grid[row][col].clearLetter();
                    grid[row][col].changeColour(new Color(18, 18, 19));
                }
            }

            // Loop for each guess/turn
            for (guess = 0; guess < GUESSES && !userGuess.equals(WORD); guess++) { // Loops until User either guesses the word or runs out of guesses
                // Get User to input a word
                charCount = 0;
                userGuess = "";
                do {
                    typedChar = "";
                    System.out.print("");
                    while (charCount < WORD_LEN) {
                        System.out.print("");
                        if (typedChar.equals("") || typedChar.equals("ENTER")) {}
                        else if (typedChar.equals("DEL")) {
                            if (charCount > 0) {
                                charCount--;
                            }
                            grid[guess][charCount].clearLetter();
                            typedChar = "";
                        }
                        else {
                            grid[guess][charCount].setLetter(typedChar);
                            charCount++;
                            typedChar = "";
                        }
                    }
                    if (typedChar.equals("DEL")) {
                        charCount--;
                        grid[guess][charCount].clearLetter();
                    }
                    else if (charCount == 5 && typedChar.equals("ENTER")) {
                        userGuess = (grid[guess][0].getLetter() + grid[guess][1].getLetter() + grid[guess][2].getLetter() + grid[guess][3].getLetter() + grid[guess][4].getLetter()).toLowerCase();
                        if (!WORDS.contains(userGuess)) {
                            JOptionPane.showMessageDialog(WordleBoard, userGuess + " is not in the word list");
                        }
                    }
                } while (!isValidWord(userGuess)); // Flag to check if the word is valid

                // Update UI box
                for (int i = 0; i < WORD_LEN; i++) {
                    updateBoard(userGuess, guess, i);
                }
            }
            // End
            if ((userGuess.toLowerCase().equals(WORD) && JOptionPane.showConfirmDialog(WordleBoard, "Congratulations!\nPlay Again?", "Wordle Clone", 0) == 1) || (!userGuess.toLowerCase().equals(WORD) && JOptionPane.showConfirmDialog(WordleBoard, "Better Luck Next Time!\nPlay Again?", "Wordle Clone", 0) == 1)) {
                System.exit(0);
            }
        }
    }
}