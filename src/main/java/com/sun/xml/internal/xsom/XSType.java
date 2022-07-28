package com.sun.xml.internal.xsom;

public interface XSType extends XSDeclaration {
  public static final int EXTENSION = 1;
  
  public static final int RESTRICTION = 2;
  
  public static final int SUBSTITUTION = 4;
  
  XSType getBaseType();
  
  int getDerivationMethod();
  
  boolean isSimpleType();
  
  boolean isComplexType();
  
  XSType[] listSubstitutables();
  
  XSType getRedefinedBy();
  
  int getRedefinedCount();
  
  XSSimpleType asSimpleType();
  
  XSComplexType asComplexType();
  
  boolean isDerivedFrom(XSType paramXSType);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */