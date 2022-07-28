package org.relaxng.datatype;

public interface Datatype {
  public static final int ID_TYPE_NULL = 0;
  
  public static final int ID_TYPE_ID = 1;
  
  public static final int ID_TYPE_IDREF = 2;
  
  public static final int ID_TYPE_IDREFS = 3;
  
  boolean isValid(String paramString, ValidationContext paramValidationContext);
  
  void checkValid(String paramString, ValidationContext paramValidationContext) throws DatatypeException;
  
  DatatypeStreamingValidator createStreamingValidator(ValidationContext paramValidationContext);
  
  Object createValue(String paramString, ValidationContext paramValidationContext);
  
  boolean sameValue(Object paramObject1, Object paramObject2);
  
  int valueHashCode(Object paramObject);
  
  int getIdType();
  
  boolean isContextDependent();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\Datatype.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */