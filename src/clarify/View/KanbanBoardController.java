/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clarify.View;

import clarify.Model.Task;
import clarify.Util.Database;
import clarify.Util.PageSwitchHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * FXML Controller class for KanbanBoard
 * TODO:
 * - Figure out what's going to be displayed on task
 * - Load tasks into listView
 * - Sort tasks based on date
 * @author khang
 */
public class KanbanBoardController implements Initializable {

    PageSwitchHelper p = new PageSwitchHelper();
    Database d = new Database();

    @FXML
    private ListView<Task> todayList;
    private ObservableList<Task> obsToday;
    
    @FXML
    private ListView<Task> tmrList;
    private ObservableList<Task> obsTmr;
    
    @FXML
    private ListView<Task> weekList;
    private ObservableList<Task> obsWeek;
    
    @FXML
    private ListView<Task> compList;
    private ObservableList<Task> obsComp;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        getTodayTask();
        todayList.setItems(this.obsToday);
        todayList.setCellFactory(taskListView -> new TaskNoteController());
               
        getTomorrowTask();
        tmrList.setItems(this.obsTmr);
        tmrList.setCellFactory(taskListView -> new TaskNoteController());
        
        getWeeklyTask();
        weekList.setItems(this.obsWeek);
        weekList.setCellFactory(taskListView -> new TaskNoteController());
        
        getCompTask();
        compList.setItems(this.obsComp);
        compList.setCellFactory(taskListView -> new TaskNoteController());
        
    }
    
    public void getTodayTask(){
        System.out.println("Today ListView loaded");
        List<Task> task = new ArrayList<>();
        try {
            ResultSet rs = d.getResultSet("SELECT * FROM TASKS"
                    + " WHERE DATE(due_date) = DATE('now', 'localtime')"
                    + " AND status < 2;");
            while (rs.next()){
                task.add(new Task(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7)));
            }
        }
        catch (SQLException e){
            System.out.println("SQL error");
        }
        this.obsToday = FXCollections.observableArrayList(task);
    }
    
    public void getTomorrowTask(){
        System.out.println("Tomorrow ListView loaded");
        List<Task> task = new ArrayList<>();
        try {
            ResultSet rs = d.getResultSet("SELECT * FROM TASKS"
                    + " WHERE DATE(due_date) = DATE('now', '+1 day', 'localtime')"
                    + " AND status < 2;");
            while (rs.next()){
                task.add(new Task(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7)));  
            }
        }
        catch (SQLException e){
            System.out.println("SQL error");
        }
        this.obsTmr = FXCollections.observableArrayList(task);
    }
    
    public void getWeeklyTask(){
        System.out.println("Weekly ListView loaded");
        List<Task> task = new ArrayList<>();
        try {
            ResultSet rs = d.getResultSet("SELECT * FROM TASKS"
                    + " WHERE DATE(due_date) > DATE('now', '+1 day', 'localtime')"
                    + " AND DATE(due_date) < DATE('now', '+7 day', 'localtime')"
                    + " AND status < 2;");
            while (rs.next()){
                task.add(new Task(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7)));  
            }
        }
        catch (SQLException e){
            System.out.println("SQL error");
        }
        this.obsWeek = FXCollections.observableArrayList(task);
    }

    public void getCompTask(){
        System.out.println("Completed ListView loaded");
        List<Task> task = new ArrayList<>();
        try {
            ResultSet rs = d.getResultSet("SELECT * FROM TASKS"
                    + " WHERE DATE(due_date) = DATE('now', 'localtime')"
                    + " AND status = 2;");
            while (rs.next()){
                task.add(new Task(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7)));
            }
        }
        catch (SQLException e){
            System.out.println("SQL error");
        }
        this.obsComp = FXCollections.observableArrayList(task);
    }
    
    public void handleAddTaskBtn(ActionEvent event) throws IOException {
        p.changeCenter(event, "/clarify/View/AddTask.fxml");
    }

}

//SELECT * FROM TASKS
//WHERE DATE(due_date) = DATE('now', '+1 day');