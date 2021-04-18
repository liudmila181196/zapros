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
import javafx.stage.DirectoryChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.geometry.Pos; 
import java.util.Random;
import java.util.*;
import java.io.InputStream;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ZAPROS extends Application {
    GridPane AskView;
    VBox SettingsView;
    VBox AnswerView;
    VBox ScaleView;
    VBox AlterView;
    VBox root;
    Stage primaryStage;
    Scene sceneAnswerView;
    Scene sceneSettingsView;
    Scene sceneScaleView;
    Scene sceneAlterView;
    Scene scene;
    
    Locale locale = new Locale("ru", "RU");
    ResourceBundle rb = ResourceBundle.getBundle("zapros.resources.text", locale);
    
    Ask ask = new Ask();
    Answer answer= new Answer();
    int countOfPairs;
    ToggleGroup group ;
    RadioButton selectionAnswer;
    RadioButton selectionZapros;
    Data zapros;
    String path = "src/zapros/data_1/";
    Label mainlabel;
    
    Button btnStart;
    Button btnAnswerView;
    Button btnScaleView;
    Button btnAlterView;
    Button btnOk = new Button(rb.getString("homapagebtn"), getImageView("home-page"));
    Button btnReturnToAnswerView;
    Button btnReturnToScaleView;
    Button btnSettings;
    Button btnRandomAnswer;
    
    static final int WIDTH = 1400;//ширина окна
    static final int HEIGHT = 700;//высота окна
    
    long time;
    long time2;
    
    public static String styleHead = "-fx-font-size: 2em; ";
    
    
    @Override
    public void start(Stage primaryStage) {
        selectionZapros = new RadioButton();
        selectionZapros.setId("2");
        
        
        
        btnStart = new Button(rb.getString("startbtn"), getImageView("start"));
        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                AskView = new GridPane();
                zapros=new Data(path);
                if (zapros.isDataOk){
                    ask();
                
                    Scene sceneAsk = new Scene (AskView, WIDTH, HEIGHT);
                    primaryStage.setScene(sceneAsk);
                    primaryStage.show();
                }
                
            }
        });
        
        btnSettings = new Button(rb.getString("settings.mainlabel"), getImageView("settings"));
        btnSettings.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                SettingsView = new VBox();
                createSettingsView();
                sceneSettingsView = new Scene (SettingsView, WIDTH, HEIGHT);
                primaryStage.setScene(sceneSettingsView);
                primaryStage.show();
            }
        });
        
        btnAnswerView = new Button(rb.getString("showanswersbtn"), getImageView("next"));
        btnAnswerView.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                AnswerView = new VBox();
                createAnswerView();
                sceneAnswerView = new Scene (AnswerView, WIDTH, HEIGHT);
                primaryStage.setScene(sceneAnswerView);
                primaryStage.show();
            }
        });
        
        btnScaleView = new Button(rb.getString("scaleviewbtn"), getImageView("next"));
        btnScaleView.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                time = System.nanoTime();
                ScaleView = new VBox();
                createScaleView();
                sceneScaleView = new Scene (ScaleView, WIDTH, HEIGHT);
                primaryStage.setScene(sceneScaleView);
                primaryStage.show();
            }
        });
        
        btnAlterView = new Button(rb.getString("alterviewbtn"), getImageView("next"));
        btnAlterView.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                time2 = System.nanoTime();
                AlterView = new VBox();
                createAlterView();
                Scene sceneAlterView = new Scene (AlterView, WIDTH, HEIGHT);
                primaryStage.setScene(sceneAlterView);
                primaryStage.show();
            }
        });
        
        
        btnReturnToAnswerView = new Button(rb.getString("ReturnToAnswerViewbtn"), getImageView("back"));
        
        btnReturnToAnswerView.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                primaryStage.setScene(sceneAnswerView);
                primaryStage.show();
            }
        });
        
        btnReturnToScaleView = new Button(rb.getString("ReturnToScaleViewbtn"), getImageView("back"));
        
        btnReturnToScaleView.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                primaryStage.setScene(sceneScaleView);
                primaryStage.show();
            }
        });
        
        btnRandomAnswer = new Button(rb.getString("btnRandomAnswer"), getImageView("shuffle"));
        
        btnRandomAnswer.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                zapros=new Data(path);
                if (zapros.isDataOk){
                    randomAsk();
                    AnswerView = new VBox();
                    createAnswerView();
                    sceneAnswerView = new Scene (AnswerView, WIDTH, HEIGHT);
                    primaryStage.setScene(sceneAnswerView);
                    primaryStage.show();
                }
            }
        });
        
        mainlabel = new Label(rb.getString("mainmenu"));
        mainlabel.setStyle(styleHead);
        
        root = new VBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(mainlabel, btnStart, btnSettings, btnRandomAnswer);
        
        scene = new Scene(root, WIDTH, HEIGHT);
        
        primaryStage.setTitle("ZAPROS");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                updateMainView();
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        
    }
    
    public void updateMainView(){
        mainlabel.setText(rb.getString("mainmenu"));
        btnStart.setText(rb.getString("startbtn"));
        btnSettings.setText(rb.getString("settings.mainlabel"));
        btnRandomAnswer.setText(rb.getString("btnRandomAnswer"));
    }
    
    //сцена с альтернативами
    public void createAlterView(){
        Scale scale = new Scale();
        scale.compareAlternatives(zapros);
        String uncomp=rb.getString("uncomparable")+": "+scale.incomparable;
        time2 = (System.nanoTime() - time2);
        double timed = (double) time2/1000000.0;
        ObservableList<Alternative> data = FXCollections.observableArrayList(zapros.alternative_list); 
        TableView<Alternative> table = new TableView<Alternative>(data);
        table.setPrefWidth(500);
        table.setPrefHeight(500);
        
        // столбцы вывода оценок
        TableColumn<Alternative, Integer> idColumn = new TableColumn<Alternative, Integer>("Id");
        TableColumn<Alternative, String> nameColumn = new TableColumn<Alternative, String>(rb.getString("nameColumn"));
        TableColumn<Alternative, ArrayList<Integer>> asColumn = new TableColumn<Alternative, ArrayList<Integer>>(rb.getString("assesmentsColumn"));
        
        // определяем фабрику для столбцов
        idColumn.setCellValueFactory(new PropertyValueFactory<Alternative, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Alternative, String>("name"));
        asColumn.setCellValueFactory(new PropertyValueFactory<Alternative, ArrayList<Integer>>("criteria_assesments"));
        
        table.getColumns().add(idColumn);
        table.getColumns().add(nameColumn);
        table.getColumns().add(asColumn);
        
        if (Integer.parseInt(selectionZapros.getId())==3){
            TableColumn<Alternative, ArrayList<Integer>> asrangColumn = new TableColumn<Alternative, ArrayList<Integer>>(rb.getString("ranksColumn"));
            asrangColumn.setCellValueFactory(new PropertyValueFactory<Alternative, ArrayList<Integer>>("criteria_rangs"));
            table.getColumns().add(asrangColumn);
        }
        TableColumn<Alternative, Integer> rangColumn = new TableColumn<Alternative, Integer>(rb.getString("relativerank"));
        rangColumn.setCellValueFactory(new PropertyValueFactory<Alternative, Integer>("rang"));
        table.getColumns().add(rangColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //заголовок
        Label label = new Label(rb.getString("alterview.alterlist"));
        label.setStyle(styleHead);
        Button btnSave = new Button(rb.getString("btnSave"), getImageView("upload"));
        
        Label timeLabel = new Label(rb.getString("timelabel")+" "+String.format("%.6f",timed)+" "+rb.getString("timemeasure"));
        Label uncompLabel = new Label(uncomp);
        
        AlterView.getChildren().addAll(label,table, timeLabel, uncompLabel, btnSave, btnReturnToScaleView, btnOk);
        AlterView.setPadding(new Insets(50));
        AlterView.setSpacing(10);
        
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               try
            {
                PrintWriter pw = new PrintWriter(path+"/result_alternatives.txt");
                pw.close();
                FileWriter writer = new FileWriter(path+"/result_alternatives.txt", true);
               // запись всей строки
                for (Alternative a:zapros.alternative_list){
                    writer.write(a.id+";"+a.name+";"+a.rang+"\n");
                    writer.flush();
                }
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            } 
            }
        });
    }
    
    //сцена настроек
    public void createSettingsView(){
        Label label = new Label(rb.getString("settings.mainlabel"));
        label.setStyle(styleHead);
        Label l = new Label(rb.getString("settings.methodlabel"));
        RadioButton zapros2 = new RadioButton(rb.getString("settings.zapros2"));
        RadioButton zapros3 = new RadioButton(rb.getString("settings.zapros3"));
        zapros2.setId("2");
        zapros3.setId("3");
        zapros2.setSelected(true);
        ToggleGroup group = new ToggleGroup();
        zapros2.setToggleGroup(group);
        zapros3.setToggleGroup(group);
        
        Button btnSetZapros = new Button(rb.getString("settings.SetZaprosbtn"), getImageView("ok"));
        btnSetZapros.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                selectionZapros = (RadioButton) group.getSelectedToggle();
            }
        });
        
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        
        Label lDir = new Label(rb.getString("settings.download"));
        Button btnChooseDir = new Button(rb.getString("settings.downloadbtn"), getImageView("download"));
 
        btnChooseDir.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                File dir = directoryChooser.showDialog(primaryStage);
                if (dir != null) {
                    path = dir.getAbsolutePath();
                }
            }
        });
        
        Label llanguage = new Label(rb.getString("settings.locale"));
        Button btnLocale =new Button(rb.getString("settings.localebtn"));
        
        
        btnLocale.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                if (locale.getCountry().equals("RU")) {
                    locale = new Locale("en", "US");
                    btnLocale.setText("English");
                    rb = ResourceBundle.getBundle("zapros.resources.text", locale);
                    updateButtonsText();
                }
                else {
                    locale = new Locale("ru", "RU");
                    btnLocale.setText("Русский");
                    rb = ResourceBundle.getBundle("zapros.resources.text", locale);
                    updateButtonsText();
                }
            }
        });
        
        SettingsView.getChildren().addAll(label, l, zapros2, zapros3, btnSetZapros, lDir,  btnChooseDir, llanguage, btnLocale, btnOk);
        SettingsView.setPadding(new Insets(50));
        SettingsView.setSpacing(10);
        SettingsView.setAlignment(Pos.CENTER);
    }
    
    public void updateButtonsText(){
        btnStart.setText(rb.getString("startbtn"));
        btnAnswerView.setText(rb.getString("showanswersbtn"));
        btnScaleView.setText(rb.getString("scaleviewbtn"));
        btnAlterView.setText(rb.getString("alterviewbtn"));
        btnOk.setText(rb.getString("homapagebtn"));
        btnReturnToAnswerView.setText(rb.getString("ReturnToAnswerViewbtn"));
        btnReturnToScaleView.setText(rb.getString("ReturnToScaleViewbtn"));
        btnSettings.setText(rb.getString("settings.mainlabel"));
        btnRandomAnswer.setText(rb.getString("btnRandomAnswer"));
    }
    
    final Random random = new Random();
    //рандомные ответы
    public void randomAsk(){
        ArrayList<String> answers = new ArrayList<>();
        answers.add("first");
        answers.add("second");
        answers.add("not matter");
        countOfPairs = zapros.pair_list.size();
        
        ask = new Ask(zapros.pair_list.get(zapros.pair_list.size()-countOfPairs), zapros.assesment_list);
        answer = ask.nextAnswer();
        
        
        while(countOfPairs>0){
            if (selectionZapros.getId()=="2")
                answer.decision= answers.get(random.nextInt(2));//в ЗАПРОС 2
            else answer.decision= answers.get(random.nextInt(3));
            zapros.answer_list.add(answer);
            if (ask.session<ask.maxSize) {
                answer = ask.nextAnswer();
            }
            else {
                countOfPairs--;
                if (countOfPairs>0){
                    ask = new Ask(zapros.pair_list.get(countOfPairs), zapros.assesment_list);
                    answer = ask.nextAnswer();
                }
            }
            
        } 
    }
    
    //сцена со шкалами
    public void createScaleView(){
        
        ObservableList<Answer> data = FXCollections.observableArrayList(); 

        for(int i = 1; i<zapros.answer_list.size(); i++){
            data.add(zapros.answer_list.get(i));
        }
        Scale scale = new Scale();
        ArrayList<LinkedHashMap<String, Integer[]>> scales = scale.buildScale(zapros);
        
        String uniscale = scale.buildUnifiedScale(scales, Integer.parseInt(selectionZapros.getId()), zapros);
        time = (System.nanoTime() - time);
        double timed = (double) time/1000000.0;
        ArrayList <String> stringScales = new ArrayList<String>();
        
        int i=0;
        for (LinkedHashMap<String, Integer[]> sc: scales){
            String s = rb.getString("scaleForCriterias")+" ";
            s+=zapros.criteria_list.get(scale.listOfCriterias.get(i)[0]-1).name + " и " +zapros.criteria_list.get(scale.listOfCriterias.get(i)[1]-1).name+" : ";
            stringScales.add(s);
            s="";
            int j=0;
            if (Integer.parseInt(selectionZapros.getId())==2){
                for (Map.Entry<String, Integer[]> str:sc.entrySet()){
                    
                    if (j>0) s+=str.getKey()+" -> ";
                    else s+=str.getKey()+" , ";
                    j++;
            }
                s=s.substring(0, s.length()-4);
            }
            
            if (Integer.parseInt(selectionZapros.getId())==3){
                for (Map.Entry<String, Integer[]> str:sc.entrySet()){
                    if (j<sc.size()-1)
                        s+="R("+zapros.criteria_list.get(str.getValue()[0]-1).name+
                                ", "+1+"-"+str.getValue()[1]+") "+
                                scale.epsh_sign.get(i).get(j)+" ";
                    else s+="R("+zapros.criteria_list.get(str.getValue()[0]-1).name+", "+1+"-"+str.getValue()[1]+") ";
                    j++;
                }         
            }
            i++;
            stringScales.add(s);
        }
        
        String s = rb.getString("unifiedOrdinalScale")+" ";
        
        stringScales.add(s);
        stringScales.add(uniscale);
        if (Integer.parseInt(selectionZapros.getId())==3){
            stringScales.add(rb.getString("ranksColumn")+": ");
            for (Assesment a: zapros.assesment_list){
                stringScales.add("R("+zapros.criteria_list.get(a.criteria_id-1).name+
                                ", "+1+"-"+a.id+") => "+a.rang);
            }
        }
        
        
        ObservableList<String> langs = FXCollections.observableArrayList(stringScales);
        ListView<String> langsListView = new ListView<String>(langs);
        
        Label labelScale = new Label(rb.getString("scales"));
        labelScale.setStyle(styleHead);
        
        Button btnSave = new Button(rb.getString("btnSave"), getImageView("upload"));
        
        Label timeLabel = new Label(rb.getString("timelabel")+" "+String.format("%.6f",timed)+" "+rb.getString("timemeasure"));
        
        ScaleView.getChildren().addAll(labelScale,langsListView, timeLabel, btnAlterView, btnSave, btnReturnToAnswerView);
        ScaleView.setPadding(new Insets(50));
        ScaleView.setSpacing(10);
        
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               try
            {
                PrintWriter pw = new PrintWriter(path+"/result_scales.txt");
                pw.close();
                FileWriter writer = new FileWriter(path+"/result_scales.txt", true);
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
        
    }
    
    //сцена с ответами
    public void createAnswerView(){
        
        ObservableList<Answer> data = FXCollections.observableArrayList(zapros.answer_list); 
        TableView<Answer> table = new TableView<Answer>(data);
        table.setPrefWidth(500);
        table.setPrefHeight(500);
        // столбцы вывода оценок
        TableColumn<Answer, Integer> crit1Column = new TableColumn<Answer, Integer>(rb.getString("criteria")+" 1");
        TableColumn<Answer, Integer> crit2Column = new TableColumn<Answer, Integer>(rb.getString("criteria")+" 2");
        TableColumn<Answer, Integer> as11Column = new TableColumn<Answer, Integer>(rb.getString("assesment")+" 11");
        TableColumn<Answer, Integer> as12Column = new TableColumn<Answer, Integer>(rb.getString("assesment")+" 12");
        TableColumn<Answer, Integer> as21Column = new TableColumn<Answer, Integer>(rb.getString("assesment")+" 21");
        TableColumn<Answer, Integer> as22Column = new TableColumn<Answer, Integer>(rb.getString("assesment")+" 22");
        TableColumn<Answer, String> answerColumn = new TableColumn<Answer, String>(rb.getString("answer"));

        // определяем фабрику для столбцов
        crit1Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("crit1"));
        crit2Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("crit2"));
        as11Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("as11"));
        as12Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("as12"));
        as21Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("as21"));
        as22Column.setCellValueFactory(new PropertyValueFactory<Answer, Integer>("as22"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<Answer, String>("decision"));
        table.getColumns().add(crit1Column);
        table.getColumns().add(crit2Column);
        table.getColumns().add(as11Column);
        table.getColumns().add(as12Column);
        table.getColumns().add(as21Column);
        table.getColumns().add(as22Column);
        table.getColumns().add(answerColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //заголовок
        Label label = new Label(rb.getString("answerlist"));
        label.setStyle(styleHead);
        Button btnSave = new Button(rb.getString("btnSave"), getImageView("upload"));
        
        AnswerView.getChildren().addAll(label,table, btnScaleView, btnSave);
        AnswerView.setPadding(new Insets(50));
        AnswerView.setSpacing(10);
        
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
               try
            {
                PrintWriter pw = new PrintWriter(path+"/result_answers.txt");
                pw.close();
                FileWriter writer = new FileWriter(path+"/result_answers.txt", true);
               // запись всей строки
                for (Answer a:zapros.answer_list){
                    writer.write(a.pair.crit1+";"+a.pair.crit2+";"+a.as11+";"+a.as12+";"+a.as21+";"+a.as22+";"+a.decision+"\n");
                    writer.flush();
                }
                
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            } 
            }
        });
       
    }
    
    public int calculateNumOfQuestions(){
        int n=0;
        for (PairCriteria p:zapros.pair_list){
            int numOfAssesments1=zapros.criteria_list.get(p.crit1-1).numOfAssesments;
            int numOfAssesments2=zapros.criteria_list.get(p.crit2-1).numOfAssesments;
            n+=numOfAssesments1>numOfAssesments2?numOfAssesments1:numOfAssesments2;
        }
        return n;
    }
    
    int num=1;
    //сцена с вопросами к ЛПР
    public void ask(){
        num=1;
        Button decisionBtn= new Button(rb.getString("answerthequestion"), getImageView("ok"));
        countOfPairs = zapros.pair_list.size();
        int numOfQuestions = calculateNumOfQuestions();
        
        
        ask = new Ask(zapros.pair_list.get(zapros.pair_list.size()-countOfPairs), zapros.assesment_list);
        answer = ask.nextAnswer();
        createAskField();
        group = updateAskField(ask, answer, numOfQuestions, num);
        AskView.add(decisionBtn, 1, 4); 
        
        decisionBtn.setOnAction(event -> {
            num++;
            selectionAnswer = (RadioButton) group.getSelectedToggle();
            if(selectionAnswer!=null){
                answer.decision= selectionAnswer.getId();
                zapros.answer_list.add(answer);
                //System.out.println("Answer: "+answer.decision+", session: "+ask.session);
                if (ask.session<ask.maxSize) {
                    answer = ask.nextAnswer();
                    group = updateAskField(ask, answer, numOfQuestions, num);
                    selectionAnswer = (RadioButton) group.getSelectedToggle();
                }
                else {
                    countOfPairs--;
                    if (countOfPairs>0){
                        ask = new Ask(zapros.pair_list.get(countOfPairs), zapros.assesment_list);
                        answer = ask.nextAnswer();
                        group = updateAskField(ask, answer, numOfQuestions, num);
                    }
                    else{
                        AskView.add(btnAnswerView, 1, 5);
                        decisionBtn.setDisable(true);
                    }
                }
            }
        });
        
    }
    
    
    //создаем пустую сцену для вопросов
    public void createAskField(){
        //Label question = new Label(zapros.question);
        AskView.getRowConstraints().add(new RowConstraints(50));
        AskView.getRowConstraints().add(new RowConstraints(50));
        AskView.getRowConstraints().add(new RowConstraints(50));
        AskView.getRowConstraints().add(new RowConstraints(50));
        AskView.getColumnConstraints().add(new ColumnConstraints(50));
    }
    
    //обновляем вопрос к ЛПР
    public ToggleGroup updateAskField(Ask ask, Answer answer, int numOfQuestions, int num){
        String question1, question2;
        Label question = new Label(num+"/"+numOfQuestions+" "+zapros.question);
        
        removeNodeByRowColumnIndex(1,1,AskView);
        removeNodeByRowColumnIndex(1,2,AskView);
        removeNodeByRowColumnIndex(1,3,AskView);
        removeNodeByRowColumnIndex(1,0, AskView);
        
        question1 = ask.assesments1.get(answer.as11-1).name+" и "+ask.assesments2.get(answer.as12-1).name;
        question2 = ask.assesments1.get(answer.as21-1).name+" и "+ask.assesments2.get(answer.as22-1).name;
        
        RadioButton ans1 = new RadioButton(question1);
        RadioButton ans2 = new RadioButton(question2);
        RadioButton ans3 = new RadioButton(rb.getString("notmatter"));
        ans1.setSelected(true);
        ans1.setId("first");
        ans2.setId("second");
        ans3.setId("not matter");
        ToggleGroup group = new ToggleGroup();
        ans1.setToggleGroup(group);
        ans2.setToggleGroup(group);
        ans3.setToggleGroup(group);
        
        AskView.add(question, 1,0);
        AskView.add(ans1, 1, 1);
        AskView.add(ans2, 1, 2);
        if (selectionZapros.getId()=="3") AskView.add(ans3, 1, 3);
        return group;  
    }
    
    //удаление узла по координатам
    public void removeNodeByRowColumnIndex(final int column,final int row,GridPane gridPane) {
        ObservableList<Node> childrens = gridPane.getChildren();
        for(Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                gridPane.getChildren().remove(node);
                break;
            }
        }
    }
    /*
    private void showAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка!");
 
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Выберите ответ!");
 
        alert.showAndWait();
    }*/
    
    //выбор директории исходных файлов
    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Directory");
 
        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
    
    public ImageView getImageView(String imageName){
       InputStream input = getClass().getResourceAsStream("/zapros/icon/"+imageName+".png");
       Image image = new Image(input, 30,30, true, true);
       ImageView imageView = new ImageView(image);
       //imageView.setFitHeight(30);
       //imageView.setPreserveRatio(true);
       return imageView;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
