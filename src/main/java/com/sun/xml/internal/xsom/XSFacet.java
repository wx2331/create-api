package com.sun.xml.internal.xsom;

public interface XSFacet extends XSComponent {
  public static final String FACET_LENGTH = "length";
  
  public static final String FACET_MINLENGTH = "minLength";
  
  public static final String FACET_MAXLENGTH = "maxLength";
  
  public static final String FACET_PATTERN = "pattern";
  
  public static final String FACET_ENUMERATION = "enumeration";
  
  public static final String FACET_TOTALDIGITS = "totalDigits";
  
  public static final String FACET_FRACTIONDIGITS = "fractionDigits";
  
  public static final String FACET_MININCLUSIVE = "minInclusive";
  
  public static final String FACET_MAXINCLUSIVE = "maxInclusive";
  
  public static final String FACET_MINEXCLUSIVE = "minExclusive";
  
  public static final String FACET_MAXEXCLUSIVE = "maxExclusive";
  
  public static final String FACET_WHITESPACE = "whiteSpace";
  
  String getName();
  
  XmlString getValue();
  
  boolean isFixed();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\xsom\XSFacet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */