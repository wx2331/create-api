package com.sun.tools.internal.xjc.model;

import com.sun.tools.internal.xjc.model.nav.NClass;
import com.sun.tools.internal.xjc.model.nav.NType;
import com.sun.xml.internal.bind.v2.model.core.NonElement;

public interface CNonElement extends NonElement<NType, NClass>, TypeUse, CTypeInfo {
  @Deprecated
  CNonElement getInfo();
  
  @Deprecated
  boolean isCollection();
  
  @Deprecated
  CAdapter getAdapterUse();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CNonElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */