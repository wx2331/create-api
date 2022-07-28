package com.sun.jdi;

import jdk.Exported;

@Exported
public interface Type extends Mirror {
  String signature();
  
  String name();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\Type.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */