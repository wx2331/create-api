package com.sun.xml.internal.xsom;

import java.util.Collection;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

public interface XSSchemaSet {
  XSSchema getSchema(String paramString);
  
  XSSchema getSchema(int paramInt);
  
  int getSchemaSize();
  
  Iterator<XSSchema> iterateSchema();
  
  Collection<XSSchema> getSchemas();
  
  XSType getType(String paramString1, String paramString2);
  
  XSSimpleType getSimpleType(String paramString1, String paramString2);
  
  XSAttributeDecl getAttributeDecl(String paramString1, String paramString2);
  
  XSElementDecl getElementDecl(String paramString1, String paramString2);
  
  XSModelGroupDecl getModelGroupDecl(String paramString1, String paramString2);
  
  XSAttGroupDecl getAttGroupDecl(String paramString1, String paramString2);
  
  XSComplexType getComplexType(String paramString1, String paramString2);
  
  XSIdentityConstraint getIdentityConstraint(String paramString1, String paramString2);
  
  Iterator<XSElementDecl> iterateElementDecls();
  
  Iterator<XSType> iterateTypes();
  
  Iterator<XSAttributeDecl> iterateAttributeDecls();
  
  Iterator<XSAttGroupDecl> iterateAttGroupDecls();
  
  Iterator<XSModelGroupDecl> iterateModelGroupDecls();
  
  Iterator<XSSimpleType> iterateSimpleTypes();
  
  Iterator<XSComplexType> iterateComplexTypes();
  
  Iterator<XSNotation> iterateNotations();
  
  Iterator<XSIdentityConstraint> iterateIdentityConstraints();
  
  XSComplexType getAnyType();
  
  XSSimpleType getAnySimpleType();
  
  XSContentType getEmpty();
  
  Collection<XSComponent> select(String paramString, NamespaceContext paramNamespaceContext);
  
  XSComponent selectSingle(String paramString, NamespaceContext paramNamespaceContext);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSSchemaSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */