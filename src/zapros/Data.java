package zapros;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileInputStream;
public class Data {
    public ArrayList<Criteria> criteria_list;
    public ArrayList<Assesment> assesment_list;
    public ArrayList<Alternative> alternative_list;
    public ArrayList<Answer> answer_list;
    public ArrayList<PairCriteria> pair_list;
    public String question;
    
    public Data(String path){
        //добавить сортировку списков
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
    }
    
    public String readFile(String filename){
        String str = "";
        String encoding = System.getProperty("console.encoding", "utf-8");
        try
        {
            InputStream ist= new FileInputStream(filename+"/question.txt");
            Scanner sc = new Scanner(ist, encoding);
            //чтение построчно
            String s;
            while((s=sc.nextLine())!=null){
                str=s;
            }
            
            sc.close();
        }
         catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return str;
    }
}
