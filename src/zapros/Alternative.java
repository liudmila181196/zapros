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
    public int rang;
    public ArrayList<Integer> criteria_rangs = new ArrayList<Integer>();//ранги оценок
    
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
        return this.criteria_rangs;
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
    
    public ArrayList<Alternative> readFile(String filename){
        ArrayList<Alternative> list = new ArrayList();
        String encoding = System.getProperty("console.encoding", "utf-8");
        try
        {
            InputStream ist= new FileInputStream(filename+"/alternative.txt");
            Scanner sc = new Scanner(ist, encoding);
            //чтение построчно
            String s;
            while((s=sc.nextLine())!=null){
                String[] words = s.split(";");
                ArrayList<Integer> int_list = new ArrayList();
                int_list.add(Integer.parseInt(words[2]));
                int_list.add(Integer.parseInt(words[3]));
                int_list.add(Integer.parseInt(words[4]));
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
