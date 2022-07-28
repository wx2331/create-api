package org.relaxng.datatype;

public interface DatatypeLibrary {
  DatatypeBuilder createDatatypeBuilder(String paramString) throws DatatypeException;
  
  Datatype createDatatype(String paramString) throws DatatypeException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\org\relaxng\datatype\DatatypeLibrary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */