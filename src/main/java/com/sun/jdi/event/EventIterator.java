package com.sun.jdi.event;

import java.util.Iterator;
import jdk.Exported;

@Exported
public interface EventIterator extends Iterator<Event> {
  Event nextEvent();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\EventIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */