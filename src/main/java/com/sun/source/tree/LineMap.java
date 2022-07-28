package com.sun.source.tree;

import jdk.Exported;

@Exported
public interface LineMap {
  long getStartPosition(long paramLong);
  
  long getPosition(long paramLong1, long paramLong2);
  
  long getLineNumber(long paramLong);
  
  long getColumnNumber(long paramLong);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\LineMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */