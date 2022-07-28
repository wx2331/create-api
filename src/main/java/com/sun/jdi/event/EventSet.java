package com.sun.jdi.event;

import com.sun.jdi.Mirror;
import java.util.Set;
import jdk.Exported;

@Exported
public interface EventSet extends Mirror, Set<Event> {
  int suspendPolicy();
  
  EventIterator eventIterator();
  
  void resume();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\EventSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */