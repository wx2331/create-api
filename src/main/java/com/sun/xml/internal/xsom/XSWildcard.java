package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.visitor.XSWildcardFunction;
import com.sun.xml.internal.xsom.visitor.XSWildcardVisitor;
import java.util.Collection;
import java.util.Iterator;

public interface XSWildcard extends XSComponent, XSTerm {
  public static final int LAX = 1;
  
  public static final int STRTICT = 2;
  
  public static final int SKIP = 3;
  
  int getMode();
  
  boolean acceptsNamespace(String paramString);
  
  void visit(XSWildcardVisitor paramXSWildcardVisitor);
  
  <T> T apply(XSWildcardFunction<T> paramXSWildcardFunction);
  
  public static interface Any extends XSWildcard {}
  
  public static interface Other extends XSWildcard {
    String getOtherNamespace();
  }
  
  public static interface Union extends XSWildcard {
    Iterator<String> iterateNamespaces();
    
    Collection<String> getNamespaces();
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSWildcard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */