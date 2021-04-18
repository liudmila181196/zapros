package zapros;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileInputStream;
import javafx.scene.control.Alert;

//класс, хранящий все данные
public class Data {
    public ArrayList<Criteria> criteria_list;//лист со всеми критериями
    public ArrayList<Assesment> assesment_list;//лист со всеми оценками
    public ArrayList<Alternative> alternative_list;//лист со всеми альтернативами
    public ArrayList<Answer> answer_list;//лист со всеми ответами
    public ArrayList<PairCriteria> pair_list;//лист со всеми парами критериев
    public String question;//вопрос к ЛПР
    public Boolean isDataOk = false;
    
    //считывание исходных данных
    public Data(String path){
        Alternative alt = new Alternative();
        this.alternative_list=alt.readFile(path);
        Assesment assesment=new Assesment();
        this.assesment_list = assesment.readFile(path);
        Criteria crit = new Criteria();
        this.criteria_list = crit.readFile(path);
        PairCriteria pair = new PairCriteria();
        this.pair_list = pair.readFile(path);
        answer_list = new ArrayList();
        this.question = readFile(path);
        if (alternative_list.isEmpty()||assesment_list.isEmpty()||criteria_list.isEmpty()||pair_list.isEmpty()||question.isEmpty())
        {
            showAlert();
        }
        else isDataOk =true;
        
        
        
    }
    
    //чтение файла с вопросом к ЛПР
    public String readFile(String filename){
        String str = "";
        String encoding = System.getProperty("console.encoding", "utf-8");
        System.out.println("Reading question.txt");
        try
        {
            InputStream ist= new FileInputStream(filename+"/question.txt");
            Scanner sc = new Scanner(ist, encoding);
            //чтение построчно
            String s;
            while((s=sc.nextLine())!=null){
                str+=s;
            }
            
            sc.close();
        }
         catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return str;
    }
    
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка!");
 
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Один из исходных файлов пуст!");
 
        alert.showAndWait();
    }
}
