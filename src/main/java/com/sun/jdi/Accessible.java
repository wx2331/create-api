package com.sun.jdi;

import jdk.Exported;

@Exported
public interface Accessible {
  int modifiers();
  
  boolean isPrivate();
  
  boolean isPackagePrivate();
  
  boolean isProtected();
  
  boolean isPublic();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\Accessible.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */