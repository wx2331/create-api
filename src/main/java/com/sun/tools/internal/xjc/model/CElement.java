package com.sun.tools.internal.xjc.model;

import com.sun.tools.internal.xjc.model.nav.NClass;
import com.sun.tools.internal.xjc.model.nav.NType;
import com.sun.xml.internal.bind.v2.model.core.Element;

public interface CElement extends CTypeInfo, Element<NType, NClass> {
  void setAbstract();
  
  boolean isAbstract();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */