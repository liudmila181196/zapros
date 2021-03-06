package zapros;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Criteria {
    public String name;
    public int id;
    public int numOfAssesments;
    
    public Criteria(){}
    
    public Criteria(int id, String name, int numOfAssesments){
        this.name = name;
        this.id=id;
        this.numOfAssesments=numOfAssesments;
    }
    //чтение исходных данных
    public ArrayList<Criteria> readFile(String filename){
        ArrayList<Criteria> list = new ArrayList();
        String encoding = System.getProperty("console.encoding", "utf-8");
        System.out.println("Reading criteria.txt");
        try
        {
            InputStream ist= new FileInputStream(filename+"/criteria.txt");
            Scanner sc = new Scanner(ist, encoding);
            //чтение построчно
            String s;
            while((s=sc.nextLine())!=null){
                String[] words = s.split(";");
                list.add(new Criteria(Integer.parseInt(words[0]), 
                        words[1], Integer.parseInt(words[2])));
            }
            sc.close();
        }
         catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return list;
    }
}
