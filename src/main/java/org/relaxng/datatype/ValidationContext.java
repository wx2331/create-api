package org.relaxng.datatype;

public interface ValidationContext {
  String resolveNamespacePrefix(String paramString);
  
  String getBaseUri();
  
  boolean isUnparsedEntity(String paramString);
  
  boolean isNotation(String paramString);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\ValidationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */