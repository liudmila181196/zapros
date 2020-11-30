package zapros;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class PairCriteria {
    public int crit1;
    public int crit2;
    public boolean uses;
    
    public PairCriteria(){}
    
    public PairCriteria(int crit1, int crit2, boolean uses){
        this.crit1 = crit1;
        this.crit2=crit2;
        this.uses=uses;
    }
    
    public ArrayList<PairCriteria> readFile(String filename){
        ArrayList<PairCriteria> list = new ArrayList();
        String encoding = System.getProperty("console.encoding", "utf-8");
        try
        {
            InputStream ist= new FileInputStream(filename+"/paircriteria.txt");
            Scanner sc = new Scanner(ist, encoding);
            //чтение построчно
            String s;
            while((s=sc.nextLine())!=null){
                String[] words = s.split(";");
                list.add(new PairCriteria(Integer.parseInt(words[0]), Integer.parseInt(words[1]), Boolean.parseBoolean(words[2])));
            }
            sc.close();
        }
         catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        return list;
    }
}
