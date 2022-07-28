package org.relaxng.datatype;

public interface DatatypeStreamingValidator {
  void addCharacters(char[] paramArrayOfchar, int paramInt1, int paramInt2);
  
  boolean isValid();
  
  void checkValid() throws DatatypeException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\DatatypeStreamingValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */