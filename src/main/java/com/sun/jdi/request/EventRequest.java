package com.sun.jdi.request;

import com.sun.jdi.Mirror;
import jdk.Exported;

@Exported
public interface EventRequest extends Mirror {
  public static final int SUSPEND_NONE = 0;
  
  public static final int SUSPEND_EVENT_THREAD = 1;
  
  public static final int SUSPEND_ALL = 2;
  
  boolean isEnabled();
  
  void setEnabled(boolean paramBoolean);
  
  void enable();
  
  void disable();
  
  void addCountFilter(int paramInt);
  
  void setSuspendPolicy(int paramInt);
  
  int suspendPolicy();
  
  void putProperty(Object paramObject1, Object paramObject2);
  
  Object getProperty(Object paramObject);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\request\EventRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */