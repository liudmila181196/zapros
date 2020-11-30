/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zapros;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Label;
import javafx.scene.layout.RowConstraints;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.geometry.Pos; 

public class ZAPROS extends Application {
    GridPane AskView = new GridPane();
    VBox AnswersView = new VBox();
    VBox ScaleView = new VBox();
    Stage primaryStage;
    
    Boolean isDecisionMade=false;
    Ask ask = new Ask();
    Answer answer= new Answer();
    int countOfPairs;
    ToggleGroup group ;
    RadioButton selection;
    Data zapros;
    Button btnAnswerView;
    Button btnScaleView;
    Button btnOk = new Button("OK");
    String path = "src/zapros/data_1/";
    
    @Override
    public void start(Stage primaryStage) {
        Button btnStart = new Button();
        btnStart.setText("Старт");
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                zapros=new Data(path);
                ask();
                
                Scene sceneAsk = new Scene (AskView, 600, 300);
                primaryStage.setScene(sceneAsk);
                primaryStage.show();
            }
        });
        btnAnswerView = new Button();
        btnAnswerView.setText("Посмотреть ответы");
        btnAnswerView.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                createAnswersView();
                Scene sceneAnswerView = new Scene (AnswersView, 600, 300);
                primaryStage.setScene(sceneAnswerView);
                primaryStage.show();
            }
        });
        
        btnScaleView = new Button();
        btnScaleView.setText("Результат");
        btnScaleView.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                createScaleView();
                Scene sceneScaleView = new Scene (ScaleView, 600, 300);
                primaryStage.setScene(sceneScaleView);
                primaryStage.show();
            }
        });
        
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        
        
        Button btnChooseDir = new Button("Загрузить");
 
        btnChooseDir.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                File dir = directoryChooser.showDialog(primaryStage);
                if (dir != null) {
                    path = dir.getAbsolutePath();
                }
            }
        });

        VBox root = new VBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(btnStart, btnChooseDir);
        
        Scene scene = new Scene(root, 600, 300);
        
        primaryStage.setTitle("ZAPROS");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
        
    }
    
    
    public void createScaleView(){
        
        ObservableList<Answer> data = FXCollections.observableArrayList(); 

        for(int i = 1; i<zapros.answer_list.size(); i++){
            data.add(zapros.answer_list.get(i));
        }
        Scale scale = new Scale();
        ArrayList<ArrayList<String>> scales = scale.buildScale(zapros);
        ArrayList<ArrayList<String>> scalescopy = new ArrayList<ArrayList<String>>();
        int i=0;
        for (ArrayList<String> list:scales){
            
            ArrayList<String> listcopy = new ArrayList<String>();
            for (String str:list){
                listcopy.add(str);
            }
            scalescopy.add(new ArrayList<String>());
            scalescopy.set(i, listcopy);
            i++;
        }
        ArrayList<String> uniScale = scale.buildUnifiedScale(scalescopy);
        ArrayList <String> stringScales = new ArrayList<String>();
        i=0;
        for (ArrayList<String> sc: scales){
            String s = "Шкала для критериев: ";
            s+=zapros.criteria_list.get(zapros.pair_list.get(i).crit1-1).name + " и " +zapros.criteria_list.get(zapros.pair_list.get(i).crit2-1).name+" : ";
            i++;
            
            for (String str:sc){
                s+=str+" -> ";
            }
            s=s.substring(0, s.length()-4);
            stringScales.add(s);
        }
        
        String s = "Единая Порядковая Шкала: ";
        for (String str: uniScale){
                s+=str+" -> ";
        }
        s=s.substring(0, s.length()-4);
        stringScales.add(s);
        
        ObservableList<String> langs = FXCollections.observableArrayList(stringScales);
        ListView<String> langsListView = new ListView<String>(langs);
        // столбцы
        //заголовок
        Label labelScore = new Label("Шкалы");
        //кнопка возврата в меню
        Button btnSave = new Button("Сохранить результат");
        //страница рекордов
        ScaleView.getChildren().addAll(labelScore,langsListView, btnOk, btnSave);
        ScaleView.setPadding(new Insets(50));
        ScaleView.setSpacing(10);
        
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               try
            {
                FileWriter writer = new FileWriter(path+"/result.txt", true);
               // запись всей строки
                for (String s:stringScales){
                    writer.write(s+";\n");
                    writer.flush();
                }
                
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            } 
            }
        });
        //AnswersView.setMargin(okBtn2, new Insets(10,WIDTH/2-BTN_WIDTH/2,10,WIDTH/2-BTN_WIDTH/2));
        //AnswersView.setMargin(labelScore, new Insets(10,WIDTH/4,10,WIDTH/4));
    }
    
    public void createAnswersView(){
        
        ObservableList<Answer> data = FXCollections.observableArrayList(zapros.answer_list); 
        TableView<Answer> table = new TableView<Answer>(data);
        table.setPrefWidth(500);
        table.setPrefHeight(500);
        // столбцы вывода оценок
        //TableColumn<Answer, String> crit1Column = new TableColumn<Answer, String>("Критерий 1");
        //TableColumn<Answer, String> crit2Column = new TableColumn<Answer, String>("Критерий 2");
        TableColumn<Answer, Integer> as11Column = new TableColumn<Answer, Integer>("Оценка 11");
        TableColumn<Answer, Integer> as12Column = new TableColumn<Answer, Integer>("Оценка 12");
        TableColumn<Answer, Integer> as21Column = new TableColumn<Answer, Integer>("Оценка 21");
        TableColumn<Answer, Integer> as22Column = new TableColumn<Answer, Integer>("Оценка 22");
        TableColumn<Answer, String> answerColumn = new TableColumn<Answer, String>("Ответ");

        // определяем фабрику для столбцов
        //crit1Column.setCellValueFactory(new PropertyValueFactory<Answer, String>("pair.crit1"));
        //crit2Column.setCellValueFactory(new PropertyValueFactory<Answer, String>("pair.crit2"));
        as11Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("as11"));
        as12Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("as12"));
        as21Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("as21"));
        as22Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("as22"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<Answer, String>("decision"));
        //table.getColumns().add(crit1Column);
        //table.getColumns().add(crit2Column);
        table.getColumns().add(as11Column);
        table.getColumns().add(as12Column);
        table.getColumns().add(as21Column);
        table.getColumns().add(as22Column);
        table.getColumns().add(answerColumn);
        //заголовок
        Label labelScore = new Label("Список ответов");
        //кнопка возврата в меню
        //Button okBtn2 = new Button("OK");
        //страница рекордов
        AnswersView.getChildren().addAll(labelScore,table, btnScaleView);
        AnswersView.setPadding(new Insets(50));
        AnswersView.setSpacing(10);
        
        //AnswersView.setMargin(okBtn2, new Insets(10,WIDTH/2-BTN_WIDTH/2,10,WIDTH/2-BTN_WIDTH/2));
        //AnswersView.setMargin(labelScore, new Insets(10,WIDTH/4,10,WIDTH/4));
    }

    public void ask(){
        Button decisionBtn= new Button("Ответить");
        countOfPairs = zapros.pair_list.size();
        
        ask = new Ask(zapros.pair_list.get(zapros.pair_list.size()-countOfPairs), zapros.assesment_list);
        answer = ask.nextAnswer();
        createAskField();
        group = updateAskField(ask, answer);
        AskView.add(decisionBtn, 1, 4); 

        decisionBtn.setOnAction(event -> {
            if(countOfPairs>0){
                selection = (RadioButton) group.getSelectedToggle();
                if(selection!=null){
                    answer.decision= selection.getId();
                    zapros.answer_list.add(answer);
                    System.out.println("Answer: "+answer.decision+", session: "+ask.session);
                    if (ask.session<2) {
                        answer = ask.nextAnswer();
                        group = updateAskField(ask, answer);
                        selection = (RadioButton) group.getSelectedToggle();
                    }
                    else {
                        countOfPairs--;
                        ask = new Ask(zapros.pair_list.get(countOfPairs), zapros.assesment_list);
                        answer = ask.nextAnswer();
                        group = updateAskField(ask, answer);
                    }
                } else showAlert();
            }
            else{
                AskView.add(btnAnswerView, 1, 5);
                AskView.getChildren().remove(decisionBtn);
            }
            
        });
        
    }
    
    public void createAskField(){
        Label question = new Label(zapros.question);
        AskView.getRowConstraints().add(new RowConstraints(50));
        AskView.getRowConstraints().add(new RowConstraints(50));
        AskView.getRowConstraints().add(new RowConstraints(50));
        AskView.getRowConstraints().add(new RowConstraints(50));
        AskView.getColumnConstraints().add(new ColumnConstraints(50));
        
        
        RadioButton ans1 = new RadioButton("1");
        RadioButton ans2 = new RadioButton("2");
        RadioButton ans3 = new RadioButton("Все равно");
        ans1.setId("first");
        ans2.setId("second");
        ans3.setId("not matter");
        ToggleGroup group = new ToggleGroup();
        ans1.setToggleGroup(group);
        ans2.setToggleGroup(group);
        ans3.setToggleGroup(group);
        
        AskView.add(question,1,0);
        AskView.add(ans1, 1, 1);
        AskView.add(ans2, 1, 2);
        AskView.add(ans3, 1, 3);
        
        
    }
    
    public ToggleGroup updateAskField(Ask ask, Answer answer){
        String question1, question2;
        removeNodeByRowColumnIndex(1,1,AskView);
        removeNodeByRowColumnIndex(1,2,AskView);
        removeNodeByRowColumnIndex(1,3,AskView);
        
        question1 = ask.assesments1.get(answer.as11-1).name+" и "+ask.assesments2.get(answer.as12-1).name;
        question2 = ask.assesments1.get(answer.as21-1).name+" и "+ask.assesments2.get(answer.as22-1).name;
        
        RadioButton ans1 = new RadioButton(question1);
        RadioButton ans2 = new RadioButton(question2);
        RadioButton ans3 = new RadioButton("Без разницы");
        ans1.setId("first");
        ans2.setId("second");
        ans3.setId("not matter");
        ToggleGroup group = new ToggleGroup();
        ans1.setToggleGroup(group);
        ans2.setToggleGroup(group);
        ans3.setToggleGroup(group);
        
        AskView.add(ans1, 1, 1);
        AskView.add(ans2, 1, 2);
        AskView.add(ans3, 1, 3);
        return group;  
    }
    public void removeNodeByRowColumnIndex(final int column,final int row,GridPane gridPane) {
        ObservableList<Node> childrens = gridPane.getChildren();
        for(Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                gridPane.getChildren().remove(node);
                break;
            }
        }
    }

    private void showAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка!");
 
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Выберите ответ!");
 
        alert.showAndWait();
    }
    
    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Directory");
 
        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
