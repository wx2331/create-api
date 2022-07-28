package com.sun.tools.corba.se.idl;

public interface InterfaceType {
  public static final int NORMAL = 0;
  
  public static final int ABSTRACT = 1;
  
  public static final int LOCAL = 2;
  
  public static final int LOCALSERVANT = 3;
  
  public static final int LOCAL_SIGNATURE_ONLY = 4;
  
  int getInterfaceType();
  
  void setInterfaceType(int paramInt);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\InterfaceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */