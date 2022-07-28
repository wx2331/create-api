package com.sun.tools.internal.xjc.api;

import java.util.List;
import javax.xml.namespace.QName;

public interface Mapping {
  QName getElement();
  
  TypeAndAnnotation getType();
  
  List<? extends Property> getWrapperStyleDrilldown();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\api\Mapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */