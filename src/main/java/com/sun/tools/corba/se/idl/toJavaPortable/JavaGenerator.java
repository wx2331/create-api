package com.sun.tools.corba.se.idl.toJavaPortable;

import com.sun.tools.corba.se.idl.SymtabEntry;
import java.io.PrintWriter;

public interface JavaGenerator {
  int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);
  
  void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);
  
  void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);
  
  int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);
  
  int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);
  
  int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\JavaGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */