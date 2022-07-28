package com.sun.codemodel.internal;

public interface JGenerifiable {
  JTypeVar generify(String paramString);
  
  JTypeVar generify(String paramString, Class<?> paramClass);
  
  JTypeVar generify(String paramString, JClass paramJClass);
  
  JTypeVar[] typeParams();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JGenerifiable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */