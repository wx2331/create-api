package com.sun.tools.jdi;

import java.util.EventListener;

interface ThreadListener extends EventListener {
  boolean threadResumable(ThreadAction paramThreadAction);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ThreadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */