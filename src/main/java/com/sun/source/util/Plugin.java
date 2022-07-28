package com.sun.source.util;

import jdk.Exported;

@Exported
public interface Plugin {
  String getName();
  
  void init(JavacTask paramJavacTask, String... paramVarArgs);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\Plugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */