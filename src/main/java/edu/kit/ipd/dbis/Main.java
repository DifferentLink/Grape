package edu.kit.ipd.dbis;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Grape's main class.
 */
public class Main {

    /**
     * Grape's main method.
     * @param args arguments provided when run using the command line.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hello World!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Hello World!");
        frame.getContentPane().add(label);

        frame.pack();
        frame.setVisible(true);
    }
}
