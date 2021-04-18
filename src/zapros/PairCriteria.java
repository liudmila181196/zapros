package zapros;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PairCriteria {
    public int crit1;
    public int crit2;
    
    
    public PairCriteria(){}
    
    public PairCriteria(int crit1, int crit2){
        this.crit1 = crit1;
        this.crit2=crit2;
        
    }
    
    public ArrayList<PairCriteria> readFile(String filename){
        ArrayList<PairCriteria> list = new ArrayList();
        String encoding = System.getProperty("console.encoding", "utf-8");
        System.out.println("Reading pair.txt");
        try
        {
            InputStream ist= new FileInputStream(filename+"/paircriteria.txt");
            Scanner sc = new Scanner(ist, encoding);
            //чтение построчно
            String s;
            while((s=sc.nextLine())!=null){
                String[] words = s.split(";");
                list.add(new PairCriteria(Integer.parseInt(words[0]), Integer.parseInt(words[1])));
            }
            sc.close();
        }
         catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return list;
    }
}
