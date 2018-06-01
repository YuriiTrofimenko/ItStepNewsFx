package org.tyaa.itstepnewsfx;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.textfield.CustomTextField;
import org.tyaa.itstepnewsfx.connector.JsonFetchr;
import org.tyaa.itstepnewsfx.connector.JsonParser;
import org.tyaa.itstepnewsfx.entity.News;
import org.tyaa.itstepnewsfx.global.Global;

public class FXMLController implements Initializable {
    
    /*@FXML
    private Label label;*/
    
    @FXML
    TableView<News> newsTable;

    @FXML
    TableColumn<News, Integer> idCol;
    @FXML
    TableColumn<News, String> titleCol;
    @FXML
    TableColumn<News, String> contentCol;

    @FXML
    CustomTextField titleTextField;
    @FXML
    TextArea contentTextField;
    
    private ObservableList<News> news
            = FXCollections.observableArrayList();
    
    private boolean actionResult = false;
    
    @FXML
    private void addNewsAction(ActionEvent event) {
        
        Task task
            = new Task() {
                @Override
                protected Object call() throws Exception {

                    String jsonString = "";
                    JsonFetchr jsonFetcher = new JsonFetchr();
                    JsonParser jsonParser = new JsonParser();

                    /*System.out.println("https://it-step-news-1.appspot.com/news?action=create"
                                + "&title="
                                + titleTextField.getText()
                                + "&content="
                                + contentTextField.getText());*/
                    
                    jsonString = jsonFetcher.fetchByUrl(
                            "https://it-step-news-1.appspot.com/news?action=create"
                                + "&title="
                                + URLEncoder.encode(titleTextField.getText(), "UTF-8")
                                + "&content="
                                + URLEncoder.encode(contentTextField.getText(), "UTF-8")
                        );
                    
                    actionResult = jsonParser.parseActionResult(jsonString);
                    //System.out.println(jsonString);
                    //Global.news.addAll(jsonParser.parseNews(jsonString));
                    /*for (News news : Global.news) {
                        System.out.println(news.title);
                    }*/
                    return true;
                }
        };
        
        task.setOnSucceeded(ev -> {
            
            if (actionResult) {
                populateTable();
            } else {
            
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setContentText("Не удалось добавить новость");
                errorAlert.showAndWait();
            }
        });
        
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    
    @FXML
    private void delNewsAction(ActionEvent event) {
        //System.out.println("You clicked me!");
        //label.setText("Hello World!");
    }
    
    @FXML
    private void updNewsAction(ActionEvent event) {
        
        populateTable();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        newsTable.setPlaceholder(new Label("Загрузка..."));
        
        idCol.setCellValueFactory(new PropertyValueFactory<News, Integer>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<News, String>("title"));
        contentCol.setCellValueFactory(new PropertyValueFactory<News, String>("content"));

        // заполняем таблицу данными
        newsTable.setItems(news);
        populateTable();
    }
    
    private void populateTable() {
    
        Global.news.clear();
        news.clear();
        Task task
                = new Task() {
            @Override
            protected Object call() throws Exception {

                String jsonString = "";
                JsonFetchr jsonFetcher = new JsonFetchr();
                JsonParser jsonParser = new JsonParser();
                //News achievement = null;
                //List<News> achievements = new ArrayList<>();

                jsonString = jsonFetcher.fetchByUrl(
                        "https://it-step-news-1.appspot.com/news?action=fetch-all-news"
                    );
                //System.out.println(jsonString);
                Global.news.addAll(jsonParser.parseNews(jsonString));
                /*for (News news : Global.news) {
                    System.out.println(news.title);
                }*/
                return true;
            }
        };
        
        task.setOnSucceeded(ev -> {
            
            news.addAll(Global.news);
            /*for (News newsItem : news) {
                System.out.println(newsItem.title);
            }*/
        });
        
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
