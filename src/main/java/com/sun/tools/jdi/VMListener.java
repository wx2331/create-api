package com.sun.tools.jdi;

import java.util.EventListener;

interface VMListener extends EventListener {
  boolean vmSuspended(VMAction paramVMAction);
  
  boolean vmNotSuspended(VMAction paramVMAction);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\VMListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */