package clarify.Util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

    public static Connection conn;

    //Opens connection to the database
    public static void openConnection() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection("jdbc:sqlite:INFS2605.db");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    //Executes result set based on @param
    public ResultSet getResultSet(String sqlstatement) throws SQLException {
        openConnection();
        java.sql.Statement statement = conn.createStatement();
        ResultSet RS = statement.executeQuery(sqlstatement);
        return RS;
    }

    //Inserts @param on the database
    public void insertStatement(String insert_query) throws SQLException {
        java.sql.Statement stmt = null;
        openConnection();
        try {
            conn.setAutoCommit(false);
            System.out.println("Database opened successfully");
            stmt = conn.createStatement();
            System.out.println("The query was: " + insert_query);
            stmt.executeUpdate(insert_query);
            stmt.close();
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        stmt.close();
    }

    //This method creates the Categories table
    public void createCategoriesTable() {
        PreparedStatement createLoginTable = null;
        ResultSet rs = null;
        openConnection();
        try {
            System.out.println("Checking Categories table");
            DatabaseMetaData dbmd = conn.getMetaData();
            rs = dbmd.getTables(null, null, "Categories", null);
            if (!rs.next()) {
                //Limiting the length of category names to otherwise gg
                String ps1 = "CREATE TABLE Categories "
                        + "(cat_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "category_name TEXT NOT NULL, "
                        + "colour TEXT NOT NULL);";
                System.out.println("Categories table created");
                
                /* Inserts demo data - NOTE: We use rgb code as the data for colour
                #ffffff = White, #2196F3 = Light Blue */
                ArrayList<String> stList = new ArrayList<String>();
                stList.add(ps1);
                stList.add("INSERT INTO Categories(cat_id, category_name, colour) "
                    + "VALUES ('1', 'Home', '#000000');");
                stList.add("INSERT INTO Categories(cat_id, category_name, colour) "
                    + "VALUES ('2', 'Work', '#2196F3');");
                for (String st: stList){
                    insertStatement(st);
                }
            } else {
                System.out.println("Categories table exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createEntriesTable() {
        ResultSet rs = null;
        openConnection();
        try {
            System.out.println("Checking ENTRIES table ");
            DatabaseMetaData dbmd = conn.getMetaData();
            rs = dbmd.getTables(null, null, "ENTRIES", null);
            if (!rs.next()) {
                ArrayList<String> statementString = new ArrayList<String>();
                statementString.add("CREATE TABLE ENTRIES "
                        + " (ent_id INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ",startTime TEXT NOT NULL"
                        + ",endTime TEXT NOT NULL"
                        + ",entryDescription TEXT NOT NULL"
                        + ",category INTEGER NOT NULL"
                        + ",task INTEGER NOT NULL"
                        + ",FOREIGN KEY(category) REFERENCES Categories(cat_id)"
                        + ",FOREIGN KEY(task) REFERENCES TASKS(task_id)"
                        + ");");
                System.out.println("Entries table created");

                statementString.add(
                        "INSERT INTO ENTRIES (startTime, endTime, entryDescription, category, task)"
                        + " VALUES ('2019-10-29 12:00:00', '2019-10-29 13:00:00', 'First entry', '1', '1');");
                statementString.add(
                        "INSERT INTO ENTRIES (startTime, endTime, entryDescription, category, task)"
                        + " VALUES ('2019-10-31 10:00:00', '2019-10-31 14:00:00', 'Another entry', '2', '1');");
                
                 statementString.add(
                        "INSERT INTO ENTRIES (startTime, endTime, entryDescription, category, task)"
                        + " VALUES ('2019-10-29 12:00:00', '2019-10-29 13:00:00', 'Study for exam', '1', '1');");
                statementString.add(
                        "INSERT INTO ENTRIES (startTime, endTime, entryDescription, category, task)"
                        + " VALUES ('2019-10-31 10:00:00', '2019-10-31 14:00:00', 'Work out', '2', '2');");

                statementString.add(
                        "INSERT INTO ENTRIES (startTime, endTime, entryDescription, category, task)"
                        + " VALUES ('2019-10-31 10:00:00', '2019-10-31 14:00:00', 'Watch Lectures', '2', '2');");

                statementString.add(
                        "INSERT INTO ENTRIES (startTime, endTime, entryDescription, category, task)"
                        + " VALUES ('2019-10-30 10:00:00', '2019-10-30 14:00:00', 'Do homework', '2', '2');");

                statementString.add(
                        "INSERT INTO ENTRIES (startTime, endTime, entryDescription, category, task)"
                        + "VALUES ('2019-10-30 10:00:00', '2019-10-30 10:50:00', 'Eat', '2', '1');");

                statementString.add(
                        "INSERT INTO ENTRIES (startTime, endTime, entryDescription, category, task)"
                        + " VALUES ('2019-10-31 10:00:00', '2019-10-31 10:01:00', 'Lowest', '2', '1');");

                for (String thisStatement : statementString) {
                    try {
                        insertStatement(thisStatement);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("ENTRIES TABLE already Exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTaskTable() {
        ResultSet rs = null;
        openConnection();
        try {
            System.out.println("Checking TASKS table ");
            DatabaseMetaData dbmd = conn.getMetaData();
            rs = dbmd.getTables(null, null, "TASKS", null);
            if (!rs.next()) {
                ArrayList<String> statementString = new ArrayList<String>();
                statementString.add("CREATE TABLE TASKS "
                        + "(task_id INTEGER PRIMARY KEY AUTOINCREMENT"
                        + ",task_title TEXT NOT NULL"
                        + ",task_desc TEXT"
                        + ",do_date TEXT"
                        + ",due_date TEXT"
                        + ",priority INTEGER NOT NULL"
                        + ",status INTEGER NOT NULL"
                        + ");");
                System.out.println("Tasks table created");
                
                statementString.add(
                        "INSERT INTO TASKS (task_title, task_desc, do_date, due_date, priority, status) "
                        + "VALUES ('Complete Wireframes', 'Draw up wireframes for 2605', '2019-11-04 12:00:00', '2019-11-04 12:00:00', '70', '0');");
                statementString.add(
                        "INSERT INTO TASKS (task_title, task_desc, do_date, due_date, priority, status) "
                        + "VALUES ('Write code for database', 'Create tables for Data Capture', '2019-11-04 12:00:00','2019-11-04 12:00:00','100', '0');");

                for (String thisStatement : statementString) {
                    try {
                        insertStatement(thisStatement);
                    } catch (SQLException e) {

                    }
                }
            } else {
                System.out.println("Tasks TABLE already Exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
