package com.sun.xml.internal.xsom;

import org.relaxng.datatype.ValidationContext;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

public interface ForeignAttributes extends Attributes {
  ValidationContext getContext();
  
  Locator getLocator();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\ForeignAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */