/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clarify.View;

import clarify.Model.Category;
import clarify.Model.Entry;
import clarify.Util.Database;
import clarify.Util.PageSwitchHelper;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author khang
 */
public class TimeLoggingController implements Initializable {

    PageSwitchHelper p = new PageSwitchHelper();
    Database d = new Database();
    
    @FXML
    private TreeView treeItem;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadTreeView();
        } catch (IOException ex) {
            Logger.getLogger(TimeLoggingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void handleCreateCategory(ActionEvent event) throws IOException {
        p.changeCenter(event, "AddCategory.fxml");
    }
    
    @FXML
    public void handleCreateEntry(ActionEvent event) throws IOException {
        p.changeCenter(event, "AddEntry.fxml");
    }
    
    @FXML
    public void loadTreeView() throws IOException {
        TreeItem dummyRoot = new TreeItem();
        ArrayList<Category> categoryList = getCategory();
        for(Category cat: categoryList) {
            //Creates Categories
            TreeItem newCategory = new TreeItem(cat.getString(cat.getName()));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ColourIcon.fxml"));
            AnchorPane icon = (AnchorPane) loader.load();
            ColourIconController cont = loader.getController();
            cont.setBackground(cat.getString(cat.getColourRGB()));
            newCategory.setGraphic(icon);
            
            //Adds entries to TreeItem
            addEntries(newCategory, cat);
            dummyRoot.getChildren().add(newCategory);
        }
        treeItem.setRoot(dummyRoot);
        treeItem.setShowRoot(false);
    }
    
    public ArrayList<Category> getCategory(){
        System.out.println("getCategory method initialised");
        ArrayList<Category> categories = new ArrayList<>();
        String statement = "SELECT * FROM Categories;";
        try {
            ResultSet rs = d.getResultSet(statement);
            while(rs.next()){
                categories.add(new Category(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        System.out.println("getCategory method executed successfully!");
        }
        catch (SQLException e){
            System.out.println("Could not load categories");
        }
        return categories;
    }
    
    @FXML
    public void addEntries(TreeItem item, Category category){
        System.out.println("addEntries method intialised");
        ArrayList<Entry> entries = new ArrayList<>();
        String statement = "SELECT * FROM ENTRIES WHERE category = '"+category.getInt(category.getId())+"';";
        try {
            ResultSet rs = d.getResultSet(statement);
            while(rs.next()){
                String entryPrompt = "Entry ID: "+rs.getInt(1)+" | "+rs.getString(4);
                TreeItem entry = new TreeItem(entryPrompt);
                item.getChildren().add(entry);
            }
        }
        catch(SQLException e){
            //TODO
        }
    }
    
}