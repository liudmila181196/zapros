package zapros;

import java.util.ArrayList;

public class Ask {
    public int session;//номер вопроса для пары критериев
    public Answer lastAnswer;//предыдущий ответ
    public ArrayList<Assesment> assesments1=new ArrayList();//все оценки критерия 1
    public ArrayList<Assesment> assesments2=new ArrayList();//все оценки критерия 2
    public PairCriteria pair;
    public int maxSize;
    
    public Ask(){}
    
    //находим оценки критериев
    public Ask (PairCriteria pair, ArrayList<Assesment> assesment_list){
        this.pair = pair;
        for(Assesment assesment :assesment_list){
            if (assesment.criteria_id==pair.crit1){
                assesments1.add(assesment);
            }
            else if (assesment.criteria_id==pair.crit2){
                assesments2.add(assesment);
            }
        }
        maxSize=assesments1.size()>assesments2.size()?assesments1.size():assesments2.size();
        session=0;
    }
    
    //формируем варианты ответа для следующего вопроса
    public Answer nextAnswer(){
        
        //первый вопрос к ЛПР
        if (session==0){
            session=1;
            lastAnswer=new Answer(1, 2, 2, 1, pair);
        }
        //последующие вопросы
        else if (session<maxSize-1){
            session++;
            //если был выбран первый вариант ответа, ухудшаем пару 11, 12
            if (lastAnswer.decision.equals("first")){
                int[] new_ass_arr = change_assesments(lastAnswer.as11, lastAnswer.as12);
                lastAnswer=new Answer(new_ass_arr[0], new_ass_arr[1], lastAnswer.as21, lastAnswer.as22, pair);
            }
            //иначе ухудшаем пару 21, 22
            else {
                int[] new_ass_arr = change_assesments(lastAnswer.as21, lastAnswer.as22);
                lastAnswer=new Answer(lastAnswer.as11, lastAnswer.as12, new_ass_arr[0], new_ass_arr[1], pair);
            }
        }
        //последний вопрос к ЛПР
        else if (session==maxSize-1){
            session++;
            //если одна из оценок наихудшая, ухудшаем наилучшую оценку из пары
            if(lastAnswer.as11==assesments1.size() || lastAnswer.as12==assesments2.size()){
                int[] new_ass_arr = change_assesments(lastAnswer.as21, lastAnswer.as22);
                lastAnswer=new Answer(lastAnswer.as11, lastAnswer.as12, new_ass_arr[0], new_ass_arr[1], pair);
            }
            else if (lastAnswer.as21==assesments1.size() || lastAnswer.as22==assesments2.size()){
                int[] new_ass_arr = change_assesments(lastAnswer.as11, lastAnswer.as12);
                lastAnswer=new Answer(new_ass_arr[0], new_ass_arr[1], lastAnswer.as21, lastAnswer.as22, pair);
            }
        }
        return lastAnswer;
    }
    
    //ухудшаем худшую оценку в паре
    public int[] change_assesments(int as1, int as2 ){
        if (as2==1){
            if (as1<max_assesment(assesments1)){
                as1++;
            }
        }
        else {
            if (as2<max_assesment(assesments2)){
                as2++;
            }
        }
        
        return new int[]{as1, as2};
    }
    
    //максимальная оценка в списке
    public int max_assesment(ArrayList<Assesment> as_list){
        int max=0;
        for (Assesment a: as_list){
            if (a.id>max) max=a.id;
        }
        return max;
    }
}
