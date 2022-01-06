package in.vnl.EventProcess;

import java.util.Comparator;

public class EventDataComparator implements Comparator<EventData>{ 
    
    // Overriding compare()method of Comparator  
                // for ascending order of type,then on the basis of insertion time 
    public int compare(EventData d1, EventData d2) { 
        if (d1.getType() != d2.getType())
            return  d1.getType() - d2.getType();
        return d1.getDate().compareTo(d2.getDate());
    }
}