package com.sun.jdi.event;

import jdk.Exported;

@Exported
public interface ClassUnloadEvent extends Event {
  String className();
  
  String classSignature();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\event\ClassUnloadEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */