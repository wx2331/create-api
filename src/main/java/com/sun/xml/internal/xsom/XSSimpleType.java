package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.visitor.XSSimpleTypeFunction;
import com.sun.xml.internal.xsom.visitor.XSSimpleTypeVisitor;
import java.util.List;

public interface XSSimpleType extends XSType, XSContentType {
  XSSimpleType getSimpleBaseType();
  
  XSVariety getVariety();
  
  XSSimpleType getPrimitiveType();
  
  boolean isPrimitive();
  
  XSListSimpleType getBaseListType();
  
  XSUnionSimpleType getBaseUnionType();
  
  boolean isFinal(XSVariety paramXSVariety);
  
  XSSimpleType getRedefinedBy();
  
  XSFacet getFacet(String paramString);
  
  List<XSFacet> getFacets(String paramString);
  
  void visit(XSSimpleTypeVisitor paramXSSimpleTypeVisitor);
  
  <T> T apply(XSSimpleTypeFunction<T> paramXSSimpleTypeFunction);
  
  boolean isRestriction();
  
  boolean isList();
  
  boolean isUnion();
  
  XSRestrictionSimpleType asRestriction();
  
  XSListSimpleType asList();
  
  XSUnionSimpleType asUnion();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSSimpleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */