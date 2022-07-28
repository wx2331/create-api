package com.sun.xml.internal.xsom;

import com.sun.xml.internal.xsom.parser.SchemaDocument;
import java.util.Iterator;
import java.util.Map;

public interface XSSchema extends XSComponent {
  String getTargetNamespace();
  
  Map<String, XSAttributeDecl> getAttributeDecls();
  
  Iterator<XSAttributeDecl> iterateAttributeDecls();
  
  XSAttributeDecl getAttributeDecl(String paramString);
  
  Map<String, XSElementDecl> getElementDecls();
  
  Iterator<XSElementDecl> iterateElementDecls();
  
  XSElementDecl getElementDecl(String paramString);
  
  Map<String, XSAttGroupDecl> getAttGroupDecls();
  
  Iterator<XSAttGroupDecl> iterateAttGroupDecls();
  
  XSAttGroupDecl getAttGroupDecl(String paramString);
  
  Map<String, XSModelGroupDecl> getModelGroupDecls();
  
  Iterator<XSModelGroupDecl> iterateModelGroupDecls();
  
  XSModelGroupDecl getModelGroupDecl(String paramString);
  
  Map<String, XSType> getTypes();
  
  Iterator<XSType> iterateTypes();
  
  XSType getType(String paramString);
  
  Map<String, XSSimpleType> getSimpleTypes();
  
  Iterator<XSSimpleType> iterateSimpleTypes();
  
  XSSimpleType getSimpleType(String paramString);
  
  Map<String, XSComplexType> getComplexTypes();
  
  Iterator<XSComplexType> iterateComplexTypes();
  
  XSComplexType getComplexType(String paramString);
  
  Map<String, XSNotation> getNotations();
  
  Iterator<XSNotation> iterateNotations();
  
  XSNotation getNotation(String paramString);
  
  Map<String, XSIdentityConstraint> getIdentityConstraints();
  
  XSIdentityConstraint getIdentityConstraint(String paramString);
  
  SchemaDocument getSourceDocument();
  
  XSSchemaSet getRoot();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSSchema.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */