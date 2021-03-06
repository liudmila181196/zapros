package zapros;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.*;

public class Scale {
    ArrayList<ArrayList<String>> epsh_sign;//лист знаков сравнения (>, =) для каждой шкалы
    ArrayList<LinkedHashMap<String, Integer[]>> epsh;//шкалы пар критериев, строка - имя оценки, массив хранит id критерия и оценки
    ArrayList<Integer[]> listOfCriterias;
    
    //строим для каждой пары критериев шкалу на основе ответов ЛПР
    public ArrayList<LinkedHashMap<String, Integer[]>> buildScale(Data zapros){
        int id=0;
        
        epsh = new ArrayList<LinkedHashMap<String, Integer[]>>();
        epsh_sign = new ArrayList<ArrayList<String>>();
        listOfCriterias=new ArrayList<>();
        //для каждой пары критериев строим шкалу, их должно быть столько же, сколько пар критериев
        while (epsh.size()<zapros.pair_list.size()){
            
            int crit1_id = zapros.answer_list.get(id).pair.crit1;
            int crit2_id = zapros.answer_list.get(id).pair.crit2;
            Integer[] criterias=new Integer[]{crit1_id, crit2_id};
            
            ArrayList<Assesment> assesments1=new ArrayList();
            ArrayList<Assesment> assesments2=new ArrayList();
            
            //находим оценки для критериев
            for(Assesment assesment :zapros.assesment_list){
                if (assesment.criteria_id==crit1_id){
                    assesments1.add(assesment);
                }
                else if (assesment.criteria_id==crit2_id){
                    assesments2.add(assesment);
                }
            }
            
            int len_assesment=assesments1.size()>assesments2.size()?assesments1.size():assesments2.size();
            
            LinkedHashMap<String, Integer[]> scale = new LinkedHashMap<String, Integer[]>();
            ArrayList<String> scale_sign = new ArrayList<String>();
            
            //заносим в шкалу наилучшие (начальные) оценки критериев
            scale.put(assesments1.get(0).name, new Integer[]{crit1_id,assesments1.get(0).id});
            scale.put(assesments2.get(0).name, new Integer[]{crit2_id,assesments2.get(0).id});
            scale_sign.add(" , ");
            scale_sign.add(" > ");
            
            //создаем шкалу для пары критериев
            for (int i=0;i<len_assesment;i++){
                //возвращаем решение и оценку, отличающуюся от начальной(наилучшей)
                int[] change = get_change_req(zapros.answer_list.get(id), zapros);
                
                //если выбран первый вариант ответа
                if (change[0]==1) {
                    //если в шкале еще нет выбранной оценки, добавляем в шкалу
                    if (!scale.containsKey(assesments2.get(change[1]-1).name)) {
                        scale.put(assesments2.get(change[1]-1).name, new Integer[]{crit1_id,assesments2.get(change[1]-1).id});
                    }
                    scale_sign.add(" > ");
                }
                //если выбран второй вариант ответа
                else if (change[0]==2){
                    //если в шкале еще нет выбранной оценки, добавляем в шкалу
                    if (!scale.containsKey(assesments1.get(change[1]-1).name)) {
                        scale.put(assesments1.get(change[1]-1).name, new Integer[]{crit2_id,assesments1.get(change[1]-1).id});
                    }
                    scale_sign.add(" > ");
                }
                //если выбран вариант "Без разницы"
                else if (change[0]==3){
                    scale_sign.add(" = ");
                    //добавляем друг другу в списки равных оценок
                    assesments1.get(change[1]-1).equals.add(assesments2.get(change[2]-1));
                    assesments2.get(change[2]-1).equals.add(assesments1.get(change[1]-1));
                    //если оценок еще нет в шкале, добавляем
                    if (!scale.containsKey(assesments1.get(change[1]-1).name)) {
                        scale.put(assesments1.get(change[1]-1).name, new Integer[]{crit1_id,assesments1.get(change[1]-1).id});
                    }
                    if (!scale.containsKey(assesments2.get(change[2]-1).name)) {
                        scale.put(assesments2.get(change[2]-1).name, new Integer[]{crit2_id,assesments2.get(change[2]-1).id});
                    }
                }
                id++;
            }
            //если какие то оценки не были добавлены в шкалу, заносим в конец
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
            epsh_sign.add(scale_sign);
            listOfCriterias.add(criterias);
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
        int[] change = {0,0,0};
        int a_ass_start = start_id_ass_by_crit(answer.pair.crit1, zapros);//наилучшая оценка критерия 1
        int b_ass_start = start_id_ass_by_crit(answer.pair.crit2, zapros);//наилучшая оценка критерия 2
        //если выбран первый вариант ответа
        if (answer.decision == "first"){
            change[0]=1;//номер ответа
            change[1] = answer.as11>a_ass_start?answer.as11:answer.as12;//возвращаем ту оценку, что отличается от наилучшей
        }
        else if (answer.decision == "second"){
            change[0]=2;
            change[1] = answer.as21>b_ass_start?answer.as21:answer.as22;
        }
        else if (answer.decision == "not matter"){
            change[0]=3;
            change[1] = answer.as11>a_ass_start?answer.as11:answer.as12;
            change[2] = answer.as21>b_ass_start?answer.as21:answer.as22;
        }
        return change;
    }
    
    //строим единую шкалу
    public String buildUnifiedScale(ArrayList<LinkedHashMap<String, Integer[]>> scale, int method, Data data){
        String unisc="";
        
        
        SortedMap <String, Double> map = new TreeMap<String, Double>();//список названий оценок и их баллов
        SortedSet<Map.Entry<String, Double>> sortedset = new TreeSet<Map.Entry<String, Double>>(orderComp);//список сортируется по баллам
        
        //создаем общий список всех оценок с баллами
        //для запрос 2 ключом является имя оценки
        if (method==2){
            for (HashMap<String, Integer[]> l:scale){
                int i=0;
                int n = l.size();
                double[] as = new double[n];
                double j=1;
                as[n-1]=n;

                //"баллы" за место оценки в шкале, разница между ними тем больше, чем дальше стоит оценка в шкале
                for (int z=n-2;z>=0;z--){
                    as[z] = as[z+1]-j;
                    j=j+0.5;
                }
                for (Map.Entry<String, Integer[]> str :l.entrySet()){
                    //если в мэпе еще нет такой оценки, добавляем с баллами за текущее положение в шкале
                    if (!map.containsKey(str.getKey())) {
                        map.put(str.getKey(), as[i]);
                    }
                    //иначе суммируем баллы
                    else {
                        map.put(str.getKey(), as[i]+map.get(str.getKey()));
                    }
                    i++;
                }
            }            
        }
        //для запрос 3 ключом будет запись в виде R(название_критерия, 1-id_оценки)
        else if (method==3){
            for (HashMap<String, Integer[]> l:scale){
                int i=0;
                int n = l.size();
                double[] as = new double[n];
                double j=1;
                as[n-1]=n;

                //"баллы" за место оценки в шкале, разница между ними тем больше, чем дальше стоит оценка в шкале
                for (int z=n-2;z>=0;z--){
                    as[z] = as[z+1]-j;
                    j=j+0.5;
                }
                for (Map.Entry<String, Integer[]> str :l.entrySet()){
                    if (!map.containsKey(str.getKey())) {
                        map.put(str.getKey(), as[i]);
                    }
                    else {
                        map.put(str.getKey(), as[i]+map.get(str.getKey()));
                    }
                    i++;
                }
            }
        }
        //сортируем список по баллам
        sortedset.addAll(map.entrySet());
        int i=2;
        
        //проставляем ранги для оценок
        for (Map.Entry<String, Double> item :sortedset){
            //находим нужную оценку
            for (Assesment a: data.assesment_list){
                if (a.name.equals(item.getKey())) {
                    if (a.id==1) a.rang=1; //если оценка наилучшая, сразу присваем ранг 1
                    else if (a.rang==0){//если ранг еще не назначен
                        if (!a.equals.isEmpty()){//если у оценки есть равные оценки
                            int rang=0;
                            for (Assesment b:a.equals){
                                if (b.rang>0) rang=b.rang;//находим назначенный ранг у равных оценок
                            }
                            if (rang>0) {//если у равных оценок назначен ранг, назначим его текущей оценке
                                a.rang=rang;
                            }
                            else {
                                a.rang=i;//если у равных оценок не назначен ранг, присваиваем текущее положение в списке
                                i++;
                            }
                        }else {//присваиваем текущее положение в списке
                            a.rang=i;
                            i++;
                        }
                    }
                    //System.out.println(a.criteria_id+" "+a.id+" "+item.getKey()+" "+item.getValue()+" "+a.rang);
                }
            }
        }
        
        SortedMap <String, Integer> map2 = new TreeMap<String, Integer>();//список оценок с названием и рангом
        SortedSet<Map.Entry<String, Integer>> sortedset2 = new TreeSet<Map.Entry<String, Integer>>(orderCompRang);//список сортируется по рангу
        
        //заполняем список оценками с их рангами
        for (Assesment a: data.assesment_list){
            map2.put(a.name, a.rang);
        }
        //сортируем по рангу
        sortedset2.addAll(map2.entrySet());
        
        int rang=0, z=0;
        String sign ="";
        
        //склеиваем все в единую шкалу
        if (method==3){
            for (Map.Entry<String, Integer> item :sortedset2){
                for (Assesment a: data.assesment_list){
                    //находим нужную оценку
                    if (a.name.equals(item.getKey())){
                        //если оценка не наилучшая (не одна из первых)
                        if (z>data.criteria_list.size()-2){
                            if (a.rang==rang && rang!=1) sign=" = ";
                            else if (a.rang!=1) sign=" > ";
                            //записываем в виде R(название_критерия, 1-id_оценки)
                            unisc+=sign+"R("+data.criteria_list.get(a.criteria_id-1).name+", "+1+"-"+a.id+") ";
                        }
                        //иначе перечисляем через запятую
                        else unisc+="R("+data.criteria_list.get(a.criteria_id-1).name+", "+1+"-"+a.id+") "+" , ";
                        rang=a.rang;
                    }
                }
                z++;
            }
        }
        else if (method==2){
            for (Map.Entry<String, Integer> item :sortedset2){
                for (Assesment a: data.assesment_list){
                    //находим нужную оценку
                    if (a.name.equals(item.getKey())){
                        //если оценка не наилучшая (не одна из первых)
                        if (z>data.criteria_list.size()-2){
                            if (a.rang!=1) sign=" > ";
                            //записываем только название оценки
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
    
    //сортировка оценок по рангу
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
    
    //сравнение альтернатив
    public void compareAlternatives(Data data){
        SortedMap <Integer, ArrayList<Integer>> map = new TreeMap<Integer, ArrayList<Integer>>();//список с id альтернативы и списком рангов
        SortedSet<Map.Entry<Integer, ArrayList<Integer>>> sortedset = new TreeSet<Map.Entry<Integer, ArrayList<Integer>>>(orderAternative);//список с сортировкой альтернатив
        
        //создаем список всех альтернатив, заменяем оценки на их ранги
        for (Alternative a:data.alternative_list){
            ArrayList<Integer> listRang = new ArrayList<Integer>();
            int i=1;
            
            for (Integer v: a.criteria_assesments){
                for (Assesment as: data.assesment_list){
                    if (as.criteria_id==i && as.id==v) listRang.add(as.rang);
                }
                i++;
            }
            map.put(a.id, listRang);
        }
        //сортируем альтернативы
        sortedset.addAll(map.entrySet());
        
        //проставляем ранги альтернативам в соответствии их положению в списке
        int i=1;
        for (Map.Entry<Integer, ArrayList<Integer>> item :sortedset){
            for (Alternative a: data.alternative_list){
                if (a.id==item.getKey()) {
                    a.rang=i;
                    a.assesment_rangs=item.getValue();
                    i++;
                }
            }
        }
        
        
        SortedMap <Integer, ArrayList<Integer>> map2 = new TreeMap<Integer, ArrayList<Integer>>();
        //если есть одинаковые альтернативы, проставляем им одинаковые ранги 
        //SortedMap удаляет записи с одинаковым ключом, поэтому проверяем на размер, если  не совпадает, значит что то удалилось
        if (sortedset.size()!=map.size()){
            //добавим все исходные записи (не отсортированные)
            for (Map.Entry<Integer, ArrayList<Integer>> item :map.entrySet()){
                map2.put(item.getKey(), item.getValue());
            }
            //удалим из него отсортированные, останутся удаленные записи
            for (Map.Entry<Integer, ArrayList<Integer>> item :sortedset){
                map2.remove(item.getKey());
            }
            //находим удаленные альтернативы и проставляем им ранг аналогичной альтернативы
            for (Map.Entry<Integer, ArrayList<Integer>> item :map2.entrySet()){
                for (Alternative a: data.alternative_list){
                    if (a.id==item.getKey()) {
                        for (Alternative b: data.alternative_list){
                            if(a.criteria_assesments.equals(b.criteria_assesments) && b.rang!=0) {
                                a.rang=b.rang;
                                break;
                            }
                        }
                        a.assesment_rangs=item.getValue();
                    }
                }
            }
        }
    }
    
    //сравнение альтернатив по рангам оценок
    public Comparator <Map.Entry<Integer, ArrayList<Integer>>> orderAternative = new Comparator<Map.Entry<Integer, ArrayList<Integer>>>() {
                @Override
                public int compare(Map.Entry<Integer, ArrayList<Integer>> e1, Map.Entry<Integer, ArrayList<Integer>> e2) {
                    if (e1.getValue().equals(e2.getValue())) {//если ранги полностью идентичны
                        return 0;
                    }
                    int sum1=0, sum2=0;
                    //посчитаем сумму рангов
                    for (Integer a:e1.getValue()){
                        sum1+=a;
                    }
                    for (Integer a:e2.getValue()){
                        sum2+=a;
                    }
                    if (sum1!=sum2) return sum1-sum2;//если суммы отличаются
                    //иначе ищем, где больше наилучщих рангов
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
