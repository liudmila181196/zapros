package zapros;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;
public class Alternative {
    public String name;
    public int id;
    public ArrayList<Integer> criteria_assesments = new ArrayList<Integer>();
    //Integer assessment1_id;
    //Integer assessment2_id;
    //Integer assessment3_id;
    public float value;
    
    public Alternative(){
        
    }
    
    public Alternative( int id, String name, ArrayList<Integer> criteria_assesments, float value){
        this.criteria_assesments=criteria_assesments;
        this.id=id;
        this.name=name;
        this.value=value;
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
