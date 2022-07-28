package com.sun.tools.internal.xjc.model;

import com.sun.xml.internal.xsom.XSComponent;
import org.xml.sax.Locator;

public interface CCustomizable {
  CCustomizations getCustomizations();
  
  Locator getLocator();
  
  XSComponent getSchemaComponent();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CCustomizable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */