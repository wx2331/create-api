package com.sun.javadoc;

public interface ParameterizedType extends Type {
  ClassDoc asClassDoc();
  
  Type[] typeArguments();
  
  Type superclassType();
  
  Type[] interfaceTypes();
  
  Type containingType();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\ParameterizedType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */