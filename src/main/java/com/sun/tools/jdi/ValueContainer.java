package com.sun.tools.jdi;

import com.sun.jdi.ClassNotLoadedException;
import com.sun.jdi.Type;

interface ValueContainer {
  Type type() throws ClassNotLoadedException;
  
  Type findType(String paramString) throws ClassNotLoadedException;
  
  String typeName();
  
  String signature();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ValueContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */