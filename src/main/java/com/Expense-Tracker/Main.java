package com.Expense-Tracker;
import com.Expense-Tracker.gui.MainGui;
import com.Expense-Tracker.util.DatabaseConnection;
import javax.swing.SwingUtilities;
import java.sql.*;
public class Main {
    databaseConnection dbConnection = new databaseConnection();
    try{
        Connection conn = DriverManager.getConnection(dbConnection.URL, dbConnection.USER, dbConnection.PASSWORD);
        System.out.println("Connected to the database successfully!");
    } catch (SQLException e) {
        System.out.println("Connection failed: " + e.getMessage());
    } 

    try{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    SwingUtilities.invokeLater(() -> {
        try{
            new MainGui().setVisible(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    });
}
