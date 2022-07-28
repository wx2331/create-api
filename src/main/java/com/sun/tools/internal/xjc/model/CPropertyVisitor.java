package com.sun.tools.internal.xjc.model;

public interface CPropertyVisitor<V> {
  V onElement(CElementPropertyInfo paramCElementPropertyInfo);
  
  V onAttribute(CAttributePropertyInfo paramCAttributePropertyInfo);
  
  V onValue(CValuePropertyInfo paramCValuePropertyInfo);
  
  V onReference(CReferencePropertyInfo paramCReferencePropertyInfo);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\model\CPropertyVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */