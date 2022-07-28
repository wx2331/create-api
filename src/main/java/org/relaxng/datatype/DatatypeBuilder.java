package org.relaxng.datatype;

public interface DatatypeBuilder {
  void addParameter(String paramString1, String paramString2, ValidationContext paramValidationContext) throws DatatypeException;
  
  Datatype createDatatype() throws DatatypeException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\DatatypeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */