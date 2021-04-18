package zapros;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Assesment {
    public String name;
    public int criteria_id;
    public int id;
    public int rang;
    public ArrayList<Assesment> equals = new ArrayList<Assesment>();//список равных оценок
    
    public Assesment(){}
    
    public Assesment(int criteria_id, int id, String name){
        this.name = name;
        this.criteria_id = criteria_id;
        this.id=id;
        this.rang=0;
    }
    //чтение исходных данных
    public ArrayList<Assesment> readFile(String filename){
        ArrayList<Assesment> list = new ArrayList();
        String encoding = System.getProperty("console.encoding", "utf-8");
        System.out.println("Reading assesment.txt");
        try
        {
            InputStream ist= new FileInputStream(filename+"/assesment.txt");
            Scanner sc = new Scanner(ist, encoding);
            String s;
            while((s=sc.nextLine())!=null){
                String[] words = s.split(";");
                String name = words[2];
                list.add(new Assesment(Integer.parseInt(words[0]), 
                        Integer.parseInt(words[1]), 
                        name));
            }
            sc.close();
        }
         catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return list;
    }
}
