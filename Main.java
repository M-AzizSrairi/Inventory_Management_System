package com.inventory.JavaProject;

import javax.swing.*;
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new SaleGUI("Sale Entry");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // Center the frame
            frame.setVisible(true);
        });
    }
}