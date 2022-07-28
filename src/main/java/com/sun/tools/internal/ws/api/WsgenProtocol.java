package com.sun.tools.internal.ws.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WsgenProtocol {
  String token();
  
  String lexical();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\api\WsgenProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */