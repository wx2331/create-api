package com.sun.tools.internal.xjc.reader.xmlschema.bindinfo;

import java.util.Collection;
import javax.xml.namespace.QName;
import org.xml.sax.Locator;

public interface BIDeclaration {
  void setParent(BindInfo paramBindInfo);
  
  QName getName();
  
  Locator getLocation();
  
  void markAsAcknowledged();
  
  boolean isAcknowledged();
  
  void onSetOwner();
  
  Collection<BIDeclaration> getChildren();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\xmlschema\bindinfo\BIDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */