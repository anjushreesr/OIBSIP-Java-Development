import java.util.Random;
import java.util.Scanner;

/**
 * Number Guessing Game - Oasis Infobyte Java Development Internship (Task 2)
 *
 * Features:
 *  - Random number generation
 *  - Easy / Medium / Hard difficulty levels
 *  - Attempt counter and limited attempts per round
 *  - "Too High", "Too Low" and "Correct" hints
 *  - Play Again option
 *  - Score tracking across multiple rounds
 *  - Safe handling of invalid input
 */
public class NumberGuessingGame {

    // ---------- Shared objects ----------
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    // ---------- Score tracking (kept across rounds) ----------
    private static int totalScore = 0;
    private static int roundsPlayed = 0;
    private static int roundsWon = 0;
    private static int roundsLost = 0;
    private static int bestAttempts = 0; // 0 means "no win yet"

    // ---------- Settings for the chosen difficulty ----------
    private static String difficultyName;
    private static int maxNumber;       // the number is between 1 and maxNumber
    private static int maxAttempts;     // attempts allowed per round
    private static int pointsPerAttempt; // scoring multiplier

    // =====================================================
    // main: program entry point
    // =====================================================
    public static void main(String[] args) {
        printWelcome();

        boolean playAgain = true;
        while (playAgain) {
            chooseDifficulty();
            playRound();
            printScoreboard();
            playAgain = askPlayAgain();
        }

        System.out.println();
        System.out.println("=== FINAL RESULTS ===");
        printScoreboard();
        System.out.println("Thanks for playing. Goodbye!");
    }

    // =====================================================
    // Welcome message
    // =====================================================
    private static void printWelcome() {
        System.out.println("=====================================");
        System.out.println("      WELCOME TO NUMBER GUESSING     ");
        System.out.println("=====================================");
        System.out.println("I will think of a secret number.");
        System.out.println("Try to guess it before you run out of attempts!");
        System.out.println();
    }

    // =====================================================
    // Let the user pick Easy, Medium or Hard
    // =====================================================
    private static void chooseDifficulty() {
        System.out.println("Choose a difficulty level:");
        System.out.println("  1. Easy   (1-50,  10 attempts)");
        System.out.println("  2. Medium (1-100,  7 attempts)");
        System.out.println("  3. Hard   (1-200,  5 attempts)");

        int choice = readInt("Enter 1, 2 or 3: ", 1, 3);

        if (choice == 1) {
            difficultyName = "Easy";
            maxNumber = 50;
            maxAttempts = 10;
            pointsPerAttempt = 5;
        } else if (choice == 2) {
            difficultyName = "Medium";
            maxNumber = 100;
            maxAttempts = 7;
            pointsPerAttempt = 10;
        } else {
            difficultyName = "Hard";
            maxNumber = 200;
            maxAttempts = 5;
            pointsPerAttempt = 20;
        }

        System.out.println();
        System.out.println("Difficulty: " + difficultyName);
        System.out.println("I'm thinking of a number between 1 and " + maxNumber
                + ". You have " + maxAttempts + " attempts.");
        System.out.println();
    }

    // =====================================================
    // Play one full round
    // =====================================================
    private static void playRound() {
        int secretNumber = random.nextInt(maxNumber) + 1; // 1 .. maxNumber
        int attempts = 0;
        boolean won = false;

        while (attempts < maxAttempts && !won) {
            System.out.println("Attempt " + (attempts + 1) + " of " + maxAttempts);
            int guess = readInt("Enter your guess (1-" + maxNumber + "): ", 1, maxNumber);
            attempts++; // only valid guesses count as attempts

            if (guess == secretNumber) {
                System.out.println("Correct! You guessed it in " + attempts
                        + (attempts == 1 ? " attempt." : " attempts."));
                won = true;
            } else if (guess > secretNumber) {
                System.out.println("Too High!");
            } else {
                System.out.println("Too Low!");
            }
            System.out.println();
        }

        roundsPlayed++;

        if (won) {
            // Fewer attempts used = more points
            int remaining = maxAttempts - attempts;
            int points = (remaining + 1) * pointsPerAttempt;
            totalScore += points;
            roundsWon++;
            if (bestAttempts == 0 || attempts < bestAttempts) {
                bestAttempts = attempts;
            }
            System.out.println("You won this round! +" + points + " points");
        } else {
            roundsLost++;
            System.out.println("You Lost! The number was " + secretNumber + ".");
        }
        System.out.println();
    }

    // =====================================================
    // Show scores across all rounds
    // =====================================================
    private static void printScoreboard() {
        System.out.println("------------- SCOREBOARD -------------");
        System.out.println("Rounds played : " + roundsPlayed);
        System.out.println("Rounds won    : " + roundsWon);
        System.out.println("Rounds lost   : " + roundsLost);
        System.out.println("Total score   : " + totalScore);
        if (bestAttempts == 0) {
            System.out.println("Best win      : none yet");
        } else {
            System.out.println("Best win      : " + bestAttempts + " attempt(s)");
        }
        System.out.println("--------------------------------------");
        System.out.println();
    }

    // =====================================================
    // Ask the user if they want another round (y/n)
    // =====================================================
    private static boolean askPlayAgain() {
        while (true) {
            String answer = readLine("Play again? (y/n): ").trim().toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) {
                System.out.println();
                return true;
            } else if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Invalid input. Please type y or n.");
        }
    }

    // =====================================================
    // Read an integer within [min, max]; repeat until valid.
    // Invalid input never crashes the program.
    // =====================================================
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt).trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    // =====================================================
    // Read one line of text. If input ends (Ctrl+D / Ctrl+Z),
    // show the results and exit cleanly instead of crashing.
    // =====================================================
    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println();
            System.out.println("Input closed. Exiting game.");
            printScoreboard();
            System.exit(0);
        }
        return scanner.nextLine();
    }
}