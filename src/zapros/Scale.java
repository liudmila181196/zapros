package zapros;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Scale {
    
    
    public ArrayList<ArrayList<String>> buildScale(Data zapros){
        int id=0;
        ArrayList<ArrayList<String>> epsh = new ArrayList<ArrayList<String>>();
        
        while (epsh.size()<zapros.pair_list.size()){
            
            int crit1_id = zapros.answer_list.get(id).pair.crit1;
            int crit2_id = zapros.answer_list.get(id).pair.crit2;
            
            ArrayList<Assesment> assesments1=new ArrayList();
            ArrayList<Assesment> assesments2=new ArrayList();
            
            for(Assesment assesment :zapros.assesment_list){
                if (assesment.criteria_id==crit1_id){
                    assesments1.add(assesment);
                }
                else if (assesment.criteria_id==crit2_id){
                    assesments2.add(assesment);
                }
            }
            int len_assesment=assesments1.size()-1;
            ArrayList<String> scale = new ArrayList();
            scale.add(assesments1.get(0).name);
            scale.add(assesments2.get(0).name);
            
            for (int i=0;i<len_assesment;i++){
                int[] change = get_change_req(zapros.answer_list.get(i), zapros);
                if (change[0]==1) scale.add(assesments1.get(change[1]-1).name);
                else scale.add(assesments2.get(change[1]-1).name);
                id++;
            }
            for (Assesment as: assesments1){
                if (!scale.contains(as.name)) scale.add(as.name);
            }
            for (Assesment as: assesments2){
                if (!scale.contains(as.name)) scale.add(as.name);
            }
            epsh.add(scale);
        }
        return epsh;
    }
    
    public int max_id_ass_by_crit(int id, Data zapros){
        int max = 10000;
        for(Assesment as:zapros.assesment_list){
            if (as.criteria_id==id && max>as.id) max=as.id;
        }
        return max;
    }
    
    public int[] get_change_req(Answer answer, Data zapros){
        int[] change = {0,0};
        int a_ass_start = max_id_ass_by_crit(answer.pair.crit1, zapros);
        int b_ass_start = max_id_ass_by_crit(answer.pair.crit2, zapros);
        if (answer.decision == "first"){
            change[0]=1;
            change[1] = answer.as11>a_ass_start?answer.as11:answer.as12;
        }
        else {
            change[0]=2;
            change[1] = answer.as21>b_ass_start?answer.as21:answer.as22;
        }
        return change;
    }
    
    public ArrayList<String> buildUnifiedScale(ArrayList<ArrayList<String>> scales){
        ArrayList<String> uniScale = new ArrayList<>();
        while (scales.get(0).size()>0){
                ArrayList<String> list = scales.get(0);
                String str = list.get(0);
                uniScale.add(str);
                for (ArrayList<String> l:scales){
                    if(l.contains(str)) l.remove(str);
                }
                Collections.sort(scales, arraySizeComparator);
        }
        return uniScale;
    }
    
    public Comparator <ArrayList<String>> arraySizeComparator = new Comparator <ArrayList<String>>()
    {
        @Override
        public int compare(ArrayList<String> l1, ArrayList<String> l2)
        {
            return -Integer.compare(l1.size(), l2.size());
        }
    };

}
