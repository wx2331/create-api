package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public interface ClassLoaderReference extends ObjectReference {
  List<ReferenceType> definedClasses();
  
  List<ReferenceType> visibleClasses();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ClassLoaderReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */