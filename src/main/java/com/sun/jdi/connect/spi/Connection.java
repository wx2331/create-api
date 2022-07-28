package com.sun.jdi.connect.spi;

import java.io.IOException;
import jdk.Exported;

@Exported
public abstract class Connection {
  public abstract byte[] readPacket() throws IOException;
  
  public abstract void writePacket(byte[] paramArrayOfbyte) throws IOException;
  
  public abstract void close() throws IOException;
  
  public abstract boolean isOpen();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\connect\spi\Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */