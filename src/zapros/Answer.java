package zapros;
import java.util.ArrayList;

//класс с ответами
public class Answer {
    public PairCriteria pair;//пара критериев
    public int as11;//оценка 11 (id оценки)
    public int as12;//оценка 12 (id оценки)
    public int as21;//оценка 21 (id оценки)
    public int as22;//оценка 22 (id оценки)
    public String decision;//ответ ЛПР
    
    
    public String getDecision(){
        return this.decision;
    }
    
    public int getAs11(){
        return this.as11;
    }
    
    public int getAs12(){
        return this.as12;
    }
    
    public int getAs21(){
        return this.as21;
    }
    
    public int getAs22(){
        return this.as22;
    }
    
    public int getCrit1(){
        return this.pair.crit1;
    }
    
    public int getCrit2(){
        return this.pair.crit2;
    }
    
    public Answer(){};
    
    public Answer(int as11, int as12, int as21, int as22, PairCriteria pair){
        this.as11=as11;
        this.as12=as12;
        this.as21=as21;
        this.as22=as22;
        decision="";
        this.pair = pair;
    }
    
    
}
