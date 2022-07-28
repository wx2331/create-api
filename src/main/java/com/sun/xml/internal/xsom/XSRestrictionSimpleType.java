package com.sun.xml.internal.xsom;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface XSRestrictionSimpleType extends XSSimpleType {
  Iterator<XSFacet> iterateDeclaredFacets();
  
  Collection<? extends XSFacet> getDeclaredFacets();
  
  XSFacet getDeclaredFacet(String paramString);
  
  List<XSFacet> getDeclaredFacets(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSRestrictionSimpleType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */