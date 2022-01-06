package in.vnl.msgapp;
import java.util.Timer;
public class SingleTimer {
     
    private static SingleTimer myObj;
    private Timer timer=new Timer();
    private SingleTimer(){
      
    }
    public static SingleTimer getInstance(){
        if(myObj == null){
            myObj = new SingleTimer();
        }
        return myObj;
    }
     
    public Timer getTimer(){
        return timer;
    }
}