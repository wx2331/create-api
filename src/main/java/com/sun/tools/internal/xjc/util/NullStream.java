package com.sun.tools.internal.xjc.util;

import java.io.IOException;
import java.io.OutputStream;

public class NullStream extends OutputStream {
  public void write(int b) throws IOException {}
  
  public void close() throws IOException {}
  
  public void flush() throws IOException {}
  
  public void write(byte[] b, int off, int len) throws IOException {}
  
  public void write(byte[] b) throws IOException {}
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xj\\util\NullStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */