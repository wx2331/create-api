package com.sun.xml.internal.xsom;

import java.util.List;
import java.util.Set;

public interface XSElementDecl extends XSDeclaration, XSTerm {
  XSType getType();
  
  boolean isNillable();
  
  XSElementDecl getSubstAffiliation();
  
  List<XSIdentityConstraint> getIdentityConstraints();
  
  boolean isSubstitutionExcluded(int paramInt);
  
  boolean isSubstitutionDisallowed(int paramInt);
  
  boolean isAbstract();
  
  XSElementDecl[] listSubstitutables();
  
  Set<? extends XSElementDecl> getSubstitutables();
  
  boolean canBeSubstitutedBy(XSElementDecl paramXSElementDecl);
  
  XmlString getDefaultValue();
  
  XmlString getFixedValue();
  
  Boolean getForm();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSElementDecl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */