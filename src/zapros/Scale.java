package zapros;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.*;

public class Scale {
    //ArrayList<ArrayList<String>> epsh_str;
    //ArrayList<ArrayList<Integer[]>> epsh_int;
    ArrayList<ArrayList<String>> epsh_sign;//лист знаков сравнения (>, =) для каждой шкалы
    ArrayList<LinkedHashMap<String, Integer[]>> epsh;//шкалы сравнения пар критериев
    
    //строим для каждой пары критериев шкалу на основе ответов ЛПР
    public ArrayList<LinkedHashMap<String, Integer[]>> buildScale(Data zapros, int variant){
        int id=0;
        //epsh_str = new ArrayList<ArrayList<String>>();
        //epsh_int = new ArrayList<ArrayList<Integer[]>>();
        epsh = new ArrayList<LinkedHashMap<String, Integer[]>>();
        epsh_sign = new ArrayList<ArrayList<String>>();
        
        //для каждой пары критериев
        while (epsh.size()<zapros.pair_list.size()){
            
            int crit1_id = zapros.answer_list.get(id).pair.crit1;
            int crit2_id = zapros.answer_list.get(id).pair.crit2;
            
            ArrayList<Assesment> assesments1=new ArrayList();
            ArrayList<Assesment> assesments2=new ArrayList();
            
            //оценки для критериев
            for(Assesment assesment :zapros.assesment_list){
                if (assesment.criteria_id==crit1_id){
                    assesments1.add(assesment);
                }
                else if (assesment.criteria_id==crit2_id){
                    assesments2.add(assesment);
                }
            }
            
            int len_assesment=assesments1.size();
            
            //ArrayList<Answer> ans_list = new ArrayList<Answer>();
            //ans_list = zapros.answer_list.
            
            LinkedHashMap<String, Integer[]> scale = new LinkedHashMap<String, Integer[]>();
            ArrayList<String> scale_sign = new ArrayList<String>();
            
            scale.put(assesments1.get(0).name, new Integer[]{crit1_id,assesments1.get(0).id});
            scale.put(assesments2.get(0).name, new Integer[]{crit2_id,assesments2.get(0).id});
            scale_sign.add(" , ");
            scale_sign.add(" > ");
            
            //создаем шкалу для пары критериев
            for (int i=0;i<len_assesment;i++){
                int[] change = get_change_req(zapros.answer_list.get(id), zapros);
                if (change[0]==1) {
                    if (!scale.containsKey(assesments1.get(change[1]-1).name)) {
                        scale.put(assesments1.get(change[1]-1).name, new Integer[]{crit1_id,assesments1.get(change[1]-1).id});
                    }
                    scale_sign.add(" > ");
                }
                else if (change[0]==2){
                    if (!scale.containsKey(assesments2.get(change[1]-1).name)) {
                        scale.put(assesments2.get(change[1]-1).name, new Integer[]{crit2_id,assesments2.get(change[1]-1).id});
                    }
                    scale_sign.add(" > ");
                }
                else if (change[0]==3){
                    scale_sign.add(" = ");
                    assesments1.get(change[1]-1).equals.add(assesments2.get(change[1]-1));
                    assesments2.get(change[1]-1).equals.add(assesments1.get(change[1]-1));
                    if (!scale.containsKey(assesments1.get(change[1]-1).name)) {
                        scale.put(assesments1.get(change[1]-1).name, new Integer[]{crit1_id,assesments1.get(change[1]-1).id});
                    }
                    if (!scale.containsKey(assesments2.get(change[1]-1).name)) {
                        scale.put(assesments2.get(change[1]-1).name, new Integer[]{crit2_id,assesments2.get(change[1]-1).id});
                    }
                }
                id++;
            }
            for (Assesment as: assesments1){
                if (!scale.containsKey(as.name)) {
                    scale.put(as.name,new Integer[]{as.criteria_id,as.id});
                    scale_sign.add(">");
                }
            }
            for (Assesment as: assesments2){
                if (!scale.containsKey(as.name)) {
                    scale.put(as.name, new Integer[]{as.criteria_id,as.id});
                    scale_sign.add(">");
                }
            }
            epsh.add(scale);
            //epsh_sign.add(new ArrayList<String>());
            epsh_sign.add(scale_sign);
        }
        return epsh;
    }
    //находим номер начальной(наилучшей) оценки критерия(по id критерия) 
    public int start_id_ass_by_crit(int id, Data zapros){
        int min = zapros.assesment_list.get(0).id;
        for(Assesment as:zapros.assesment_list){
            if (as.criteria_id==id && min>as.id) min=as.id;
        }
        return min;
    }
    //возвращаем решение и оценку, отличающуюся от начальной(наилучшей)
    public int[] get_change_req(Answer answer, Data zapros){
        int[] change = {0,0};
        int a_ass_start = start_id_ass_by_crit(answer.pair.crit1, zapros);
        int b_ass_start = start_id_ass_by_crit(answer.pair.crit2, zapros);
        if (answer.decision == "first"){
            change[0]=1;
            change[1] = answer.as11>a_ass_start?answer.as11:answer.as12;
        }
        else if (answer.decision == "second"){
            change[0]=2;
            change[1] = answer.as21>b_ass_start?answer.as21:answer.as22;
        }
        else if (answer.decision == "not matter"){
            change[0]=3;
            change[1] = answer.as21>b_ass_start?answer.as21:answer.as22;
        }
        return change;
    }
    
    //строим единую шкалу
    public String buildUnifiedScale(ArrayList<LinkedHashMap<String, Integer[]>> scale, int method, Data data){
        ArrayList<String> uniScale = new ArrayList<>();
        String unisc="";
        int n = scale.get(0).size();
        double[] as = new double[n];
        double j=1;
        as[n-1]=n;
        
        //"баллы" за место оценки в шкале
        for (int i=n-2;i>=0;i--){
            as[i] = as[i+1]-j;
            j=j+0.5;
            //System.out.println(as[i]);
        }
        
        SortedMap <String, Double> map = new TreeMap<String, Double>();
        SortedSet<Map.Entry<String, Double>> sortedset = new TreeSet<Map.Entry<String, Double>>(orderComp);
        SortedMap <String, Double> map2 = new TreeMap<String, Double>();
        SortedSet<Map.Entry<String, Double>> sortedset2 = new TreeSet<Map.Entry<String, Double>>(orderComp);
        
        //создаем общий список всех оценок с баллами
        if (method==2){
            for (HashMap<String, Integer[]> l:scale){
                int i=0;
                for (Map.Entry<String, Integer[]> str :l.entrySet()){
                    if (!map.containsKey(str.getKey())) {
                        map.put(str.getKey(),as[i]);
                        map2.put(str.getKey(), as[i]);
                    }
                    else {
                        map.put(str.getKey(), as[i]+map.get(str.getKey()));
                        map2.put(str.getKey(), as[i]+map2.get(str.getKey()));
                    }
                    i++;
                }
            }            
        }
        else if (method==3){
            for (HashMap<String, Integer[]> l:scale){
                int i=0;
                for (Map.Entry<String, Integer[]> str :l.entrySet()){
                    String s = "R("+data.criteria_list.get(str.getValue()[0]-1).name+", "+1+"-"+str.getValue()[1]+") ";
                    if (!map.containsKey(s)) {
                        map.put(s,as[i]);
                        map2.put(str.getKey(), as[i]);
                    }
                    else {
                        map.put(s, as[i]+map.get(s));
                        map2.put(str.getKey(), as[i]+map2.get(str.getKey()));
                    }
                    i++;
                }
            }
        }
        sortedset.addAll(map.entrySet());
        /*for (Map.Entry<String, Double> item :sortedset){
            System.out.println(item.getKey()+" "+item.getValue());
            uniScale.add(item.getKey());
            
        }*/
        
        sortedset2.addAll(map2.entrySet());
        int i=2;
        //проставляем ранги для оценок
        for (Map.Entry<String, Double> item :sortedset2){
            for (Assesment a: data.assesment_list){
                if (a.name.equals(item.getKey())) {
                    if (a.id==1) a.rang=1;
                    else if (a.rang==0){
                        if (!a.equals.isEmpty()){
                            int rang=0;
                            for (Assesment b:a.equals){
                                if (b.rang>0) rang=b.rang;
                            }
                            if (rang>0) {
                                a.rang=rang;
                            }
                            else {
                                a.rang=i;
                                i++;
                            }
                        }else {
                            a.rang=i;
                            i++;
                        }
                    }
                    System.out.println(a.criteria_id+" "+a.id+" "+item.getKey()+" "+item.getValue()+" "+a.rang);
                }
            }
        }
        SortedMap <String, Integer> map3 = new TreeMap<String, Integer>();
        SortedSet<Map.Entry<String, Integer>> sortedset3 = new TreeSet<Map.Entry<String, Integer>>(orderCompRang);
        for (Assesment a: data.assesment_list){
            map3.put(a.name, a.rang);
        }
        sortedset3.addAll(map3.entrySet());
        int rang=0, z=0;
        String sign ="";
        
        //склеиваем все в единую шкалу
        if (method==3){
            for (Map.Entry<String, Integer> item :sortedset3){
                for (Assesment a: data.assesment_list){
                    if (a.name.equals(item.getKey())){
                        if (z>data.criteria_list.size()-2){
                            if (a.rang==rang && rang!=1) sign=" = ";
                            else if (a.rang!=1) sign=" > ";

                            unisc+=sign+"R("+data.criteria_list.get(a.criteria_id-1).name+", "+1+"-"+a.id+") ";
                        }
                        else unisc+="R("+data.criteria_list.get(a.criteria_id-1).name+", "+1+"-"+a.id+") "+" , ";
                        rang=a.rang;
                    }
                }
                z++;
            }
        }
        else if (method==2){
            for (Map.Entry<String, Integer> item :sortedset3){
                for (Assesment a: data.assesment_list){
                    if (a.name.equals(item.getKey())){
                        if (z>data.criteria_list.size()-2){
                            if (a.rang!=1) sign=" > ";

                            unisc+=sign+" "+a.name+" ";
                        }
                        else unisc+=" "+a.name+" , ";
                    }
                }
                z++;
            }
        }
        return unisc;
    }
    
    //сортировка по рангу
    public Comparator <Map.Entry<String, Integer>> orderCompRang = new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                    int i=0;
                    if (e1.getValue().compareTo(e2.getValue())==0) i=e1.getKey().compareTo(e2.getKey());
                    else i=e1.getValue().compareTo(e2.getValue());
                    return i;
                }
    };
    
    //сортировка по баллам
    public Comparator <Map.Entry<String, Double>> orderComp = new Comparator<Map.Entry<String, Double>>() {
                @Override
                public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
                    int i=0;
                    if (e1.getValue().compareTo(e2.getValue())==0) i=e1.getKey().compareTo(e2.getKey());
                    else i=e1.getValue().compareTo(e2.getValue());
                    return i;
                }
    };
    
    /*
    public Comparator <Map.Entry<Double, Integer[]>> orderCompInt = new Comparator<Map.Entry<Double, Integer[]>>() {
                @Override
                public int compare(Map.Entry<Double, Integer[]> e1, Map.Entry<Double, Integer[]> e2) {
                    int i=0;
                    if (e1.getKey().compareTo(e2.getKey())==0) i=e1.getValue()[0].compareTo(e2.getValue()[0]);
                    else i=e1.getKey().compareTo(e2.getKey());
                    return i;
                }
    };*/
    
    //сравнение альтернатив
    public void compareAlternatives(Data data){
        SortedMap <Integer, ArrayList<Integer>> map = new TreeMap<Integer, ArrayList<Integer>>();
        SortedSet<Map.Entry<Integer, ArrayList<Integer>>> sortedset = new TreeSet<Map.Entry<Integer, ArrayList<Integer>>>(orderAternative);
        
        //создаем список всех альтернатив, заменяем оценки на их ранги
        for (Alternative a:data.alternative_list){
            ArrayList<Integer> sum = new ArrayList<Integer>();
            int i=1;
            for (Integer v: a.criteria_assesments){
                for (Assesment as: data.assesment_list){
                    if (as.criteria_id==i && as.id==v) sum.add(as.rang);
                }
                i++;
            }
            map.put(a.id, sum);
        }
        sortedset.addAll(map.entrySet());
        
        //проставляем ранги альтернативам
        int i=1;
        for (Map.Entry<Integer, ArrayList<Integer>> item :sortedset){
            for (Alternative a: data.alternative_list){
                if (a.id==item.getKey()) {
                    a.rang=i;
                    a.criteria_rangs=item.getValue();
                    i++;
                }
            }
        }
        
        //если есть одинаковые альтернативы, проставляем им одинаковые ранги
        SortedMap <Integer, ArrayList<Integer>> map2 = new TreeMap<Integer, ArrayList<Integer>>();
        if (sortedset.size()!=map.size()){
            for (Map.Entry<Integer, ArrayList<Integer>> item :map.entrySet()){
                map2.put(item.getKey(), item.getValue());
            }
            for (Map.Entry<Integer, ArrayList<Integer>> item :sortedset){
                map2.remove(item.getKey());
            }
            for (Map.Entry<Integer, ArrayList<Integer>> item :map2.entrySet()){
                for (Alternative a: data.alternative_list){
                    if (a.id==item.getKey()) {
                        for (Alternative b: data.alternative_list){
                            if(a.criteria_assesments.equals(b.criteria_assesments) && b.rang!=0) {
                                a.rang=b.rang;
                                break;
                            }
                        }
                        a.criteria_rangs=item.getValue();
                    }
                }
            }
        }
    }
    
    //сравнение альтернатив по рангам оценок
    public Comparator <Map.Entry<Integer, ArrayList<Integer>>> orderAternative = new Comparator<Map.Entry<Integer, ArrayList<Integer>>>() {
                @Override
                public int compare(Map.Entry<Integer, ArrayList<Integer>> e1, Map.Entry<Integer, ArrayList<Integer>> e2) {
                    if (e1.getValue().equals(e2.getValue())) {
                        return 0;
                    }
                    int sum1=0, sum2=0;
                    
                    for (Integer a:e1.getValue()){
                        sum1+=a;
                    }
                    for (Integer a:e2.getValue()){
                        sum2+=a;
                    }
                    if (sum1!=sum2) return sum1-sum2;
                    else {
                        sum1=0; sum2=0;
                        for (int j=0; j<e1.getValue().size();j++){
                            if (e1.getValue().get(j)<e2.getValue().get(j)) sum1++;
                            else sum2++;
                        }
                        if (sum1!=sum2) return sum2-sum1;
                        else return 0;
                    }
                }
    };
}
