/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package edu.baker.project9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods refactored for testability using dependency injection:
 * each method receives its input source, date, or random generator from the
 * caller instead of creating it internally.
 *
 * @author Richard Lesh
 */
public class Project9 {
    /**
     * Counts the characters in the input.  Line terminators are not counted.
     *
     * @param input Reader to serve as input.  In production code pass a Reader
     *              connected to a file, e.g. Files.newBufferedReader(path).
     *              In test code pass a StringReader.
     * @return Count of characters in the input.
     */
    public static long countChars(Reader input) {
        long count = 0;
        try (BufferedReader reader = new BufferedReader(input)) {
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) break;
                count += line.length();
            }
        } catch (IOException ex) {
            Logger.getLogger(Project9.class.getName()).log(Level.SEVERE, "I/O Error", ex);
        }
        return count;
    }

    /**
     * Computes the number of whole years between your birthday and a given date.
     *
     * @param birthday Your birthday
     * @param today    The date to measure your age on.  In production code pass
     *                 LocalDate.now(); in test code pass a fixed date.
     * @return Your age in whole years as of today.
     */
    public static long yearsOld(LocalDate birthday, LocalDate today) {
        return ChronoUnit.YEARS.between(birthday, today);
    }

    /**
     * Returns a random five card poker hand as a String.
     * @param remainingDeck List of cards remaining in the deck.  Initially should
     *                      be a List of the integers 0 - 51.  This List will be
     *                      modified to delete the cards used in the hand.
     * @param randgen       Random number generator used to pick the cards.  In
     *                      production code pass any Random, or a stronger subclass
     *                      such as java.security.SecureRandom; in test code pass
     *                      one shared Random whose seed the test can control.
     * @return Returns a random five card poker hand from the remaining deck.
     */
    public static String createPokerHand(List<Integer> remainingDeck, Random randgen) {
        StringBuilder hand = new StringBuilder();
        for (int i = 0; i < 5; ++i) {
            int r = randgen.nextInt(remainingDeck.size());
            if (i != 0) hand.append(" ");
            hand.append(intToCard(remainingDeck.get(r)));
            remainingDeck.remove(r);
        }
        return hand.toString();
    }

    /**
     * Sends the same message to every email address in a list.  The message
     * service is injected, so the caller decides how each message is actually
     * sent: a real email provider in production, a console service in tests.
     * @param subject  the subject line sent to every address
     * @param msg      the body text sent to every address
     * @param addrList the email addresses to send to, visited one at a time
     * @param service  the MessageService that performs each individual send
     */
    public static void sendSpam(String subject, String msg, Iterator<String> addrList, MessageService service) {
        while (addrList.hasNext()) {
            String address = addrList.next();
            service.sendMessage(subject, msg, address);
        }
    }

    /**
     * Converts the integers 0 - 51 into a card face value and suit.
     *
     * @param i Card value 0 - 51
     * @return Two character card face value and suit.
     */
    private static String intToCard(int i) {
        String faceValues = "A23456789TJQK";
        String suits = "\u2660\u2661\u2662\u2663";
        return faceValues.substring(i % 13, i % 13 + 1) + suits.substring(i / 13, i / 13 + 1);
    }


    /**
     * Entry point for the application.  Prints a greeting only; the refactored
     * methods are exercised by the unit tests in Project9Test.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Project9!");
    }
}
