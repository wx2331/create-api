package com.sun.javadoc;

import java.io.File;

public interface SourcePosition {
  File file();
  
  int line();
  
  int column();
  
  String toString();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\SourcePosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */