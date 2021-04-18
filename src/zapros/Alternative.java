package zapros;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;
import java.io.PrintWriter;

public class Alternative {
    public String name;
    public int id;
    public ArrayList<Integer> criteria_assesments = new ArrayList<Integer>();//оценки исходные
    public int rang;//относительный ранг
    public ArrayList<Integer> assesment_rangs = new ArrayList<Integer>();//ранги оценок
    
    public Alternative(){
        
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getId(){
        return this.id;
    }
    
    public ArrayList<Integer> getCriteria_assesments(){
        return this.criteria_assesments;
    }
    
    public ArrayList<Integer> getCriteria_rangs(){
        return this.assesment_rangs;
    }
    
    public int getRang(){
        return this.rang;
    }
    
    public Alternative( int id, String name, ArrayList<Integer> criteria_assesments, int rang){
        this.criteria_assesments=criteria_assesments;
        this.id=id;
        this.name=name;
        this.rang=rang;
    }
    
    //чтение исходных данных
    public ArrayList<Alternative> readFile(String filename){
        ArrayList<Alternative> list = new ArrayList();
        String encoding = System.getProperty("console.encoding", "utf-8");
        System.out.println("Reading alternative.txt");
        try
        {
            InputStream ist= new FileInputStream(filename+"/alternative.txt");
            Scanner sc = new Scanner(ist, encoding);
            //чтение построчно
            String s;
            while((s=sc.nextLine())!=null){
                String[] words = s.split(";");
                ArrayList<Integer> int_list = new ArrayList();
                for (int i=2; i<words.length;i++){
                    int_list.add(Integer.parseInt(words[i]));
                }
                String name = words[1];
                list.add(new Alternative(Integer.parseInt(words[0]), 
                        name, 
                        int_list,
                        0));
            }
            sc.close();
        }
         catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return list;
    }
    
    
}
