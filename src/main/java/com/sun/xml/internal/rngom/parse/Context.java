package com.sun.xml.internal.rngom.parse;

import java.util.Enumeration;
import org.relaxng.datatype.ValidationContext;

public interface Context extends ValidationContext {
  Enumeration prefixes();
  
  Context copy();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\Context.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */