package com.sun.xml.internal.xsom.impl;

import com.sun.xml.internal.xsom.XSAttGroupDecl;
import com.sun.xml.internal.xsom.XSAttributeDecl;
import com.sun.xml.internal.xsom.XSComplexType;
import com.sun.xml.internal.xsom.XSContentType;
import com.sun.xml.internal.xsom.XSElementDecl;
import com.sun.xml.internal.xsom.XSIdentityConstraint;
import com.sun.xml.internal.xsom.XSSimpleType;
import com.sun.xml.internal.xsom.XSTerm;
import com.sun.xml.internal.xsom.XSType;

public abstract class Ref {
  public static interface Type {
    XSType getType();
  }
  
  public static interface Term {
    XSTerm getTerm();
  }
  
  public static interface SimpleType extends Type {
    XSSimpleType getType();
  }
  
  public static interface IdentityConstraint {
    XSIdentityConstraint get();
  }
  
  public static interface Element extends Term {
    XSElementDecl get();
  }
  
  public static interface ContentType {
    XSContentType getContentType();
  }
  
  public static interface ComplexType extends Type {
    XSComplexType getType();
  }
  
  public static interface Attribute {
    XSAttributeDecl getAttribute();
  }
  
  public static interface AttGroup {
    XSAttGroupDecl get();
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\impl\Ref.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */