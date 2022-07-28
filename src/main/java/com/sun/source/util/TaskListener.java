package com.sun.source.util;

import jdk.Exported;

@Exported
public interface TaskListener {
  void started(TaskEvent paramTaskEvent);
  
  void finished(TaskEvent paramTaskEvent);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\TaskListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */