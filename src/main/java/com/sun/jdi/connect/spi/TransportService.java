package com.sun.jdi.connect.spi;

import java.io.IOException;
import jdk.Exported;

@Exported
public abstract class TransportService {
  public abstract String name();
  
  public abstract String description();
  
  public abstract Capabilities capabilities();
  
  public abstract Connection attach(String paramString, long paramLong1, long paramLong2) throws IOException;
  
  public abstract ListenKey startListening(String paramString) throws IOException;
  
  public abstract ListenKey startListening() throws IOException;
  
  public abstract void stopListening(ListenKey paramListenKey) throws IOException;
  
  public abstract Connection accept(ListenKey paramListenKey, long paramLong1, long paramLong2) throws IOException;
  
  @Exported
  public static abstract class Capabilities {
    public abstract boolean supportsMultipleConnections();
    
    public abstract boolean supportsAttachTimeout();
    
    public abstract boolean supportsAcceptTimeout();
    
    public abstract boolean supportsHandshakeTimeout();
  }
  
  @Exported
  public static abstract class ListenKey {
    public abstract String address();
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\connect\spi\TransportService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */