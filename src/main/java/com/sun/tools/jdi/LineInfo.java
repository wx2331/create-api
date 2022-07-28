package com.sun.tools.jdi;

import com.sun.jdi.AbsentInformationException;

interface LineInfo {
  String liStratum();
  
  int liLineNumber();
  
  String liSourceName() throws AbsentInformationException;
  
  String liSourcePath() throws AbsentInformationException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\LineInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */