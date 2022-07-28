package com.sun.jdi.request;

import jdk.Exported;

@Exported
public interface ClassUnloadRequest extends EventRequest {
  void addClassFilter(String paramString);
  
  void addClassExclusionFilter(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\ClassUnloadRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */