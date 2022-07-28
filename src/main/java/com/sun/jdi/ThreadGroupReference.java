package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public interface ThreadGroupReference extends ObjectReference {
  String name();
  
  ThreadGroupReference parent();
  
  void suspend();
  
  void resume();
  
  List<ThreadReference> threads();
  
  List<ThreadGroupReference> threadGroups();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ThreadGroupReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */