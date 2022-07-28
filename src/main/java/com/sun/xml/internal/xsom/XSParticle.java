package com.sun.xml.internal.xsom;

import java.math.BigInteger;

public interface XSParticle extends XSContentType {
  public static final int UNBOUNDED = -1;
  
  BigInteger getMinOccurs();
  
  BigInteger getMaxOccurs();
  
  boolean isRepeated();
  
  XSTerm getTerm();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSParticle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */