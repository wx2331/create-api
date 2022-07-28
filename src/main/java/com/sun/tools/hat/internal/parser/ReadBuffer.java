package com.sun.tools.hat.internal.parser;

import java.io.IOException;

public interface ReadBuffer {
  void get(long paramLong, byte[] paramArrayOfbyte) throws IOException;
  
  char getChar(long paramLong) throws IOException;
  
  byte getByte(long paramLong) throws IOException;
  
  short getShort(long paramLong) throws IOException;
  
  int getInt(long paramLong) throws IOException;
  
  long getLong(long paramLong) throws IOException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\hat\internal\parser\ReadBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */