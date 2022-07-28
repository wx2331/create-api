package com.sun.jdi.event;

import com.sun.jdi.Mirror;
import jdk.Exported;

@Exported
public interface EventQueue extends Mirror {
  EventSet remove() throws InterruptedException;
  
  EventSet remove(long paramLong) throws InterruptedException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\EventQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */