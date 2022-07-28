package com.sun.tools.internal.xjc.api;

import com.sun.codemodel.internal.JType;
import javax.xml.namespace.QName;

public interface Property {
  String name();
  
  JType type();
  
  QName elementName();
  
  QName rawName();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */