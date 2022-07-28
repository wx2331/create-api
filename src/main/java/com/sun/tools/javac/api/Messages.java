package com.sun.tools.javac.api;

import java.util.Locale;
import java.util.MissingResourceException;

public interface Messages {
  void add(String paramString) throws MissingResourceException;
  
  String getLocalizedString(Locale paramLocale, String paramString, Object... paramVarArgs);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\api\Messages.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */