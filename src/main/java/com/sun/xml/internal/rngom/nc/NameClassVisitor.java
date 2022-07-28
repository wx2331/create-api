package com.sun.xml.internal.rngom.nc;

import javax.xml.namespace.QName;

public interface NameClassVisitor<V> {
  V visitChoice(NameClass paramNameClass1, NameClass paramNameClass2);
  
  V visitNsName(String paramString);
  
  V visitNsNameExcept(String paramString, NameClass paramNameClass);
  
  V visitAnyName();
  
  V visitAnyNameExcept(NameClass paramNameClass);
  
  V visitName(QName paramQName);
  
  V visitNull();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\nc\NameClassVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */