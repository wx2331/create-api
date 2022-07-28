package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public interface ClassType extends ReferenceType {
  public static final int INVOKE_SINGLE_THREADED = 1;
  
  ClassType superclass();
  
  List<InterfaceType> interfaces();
  
  List<InterfaceType> allInterfaces();
  
  List<ClassType> subclasses();
  
  boolean isEnum();
  
  void setValue(Field paramField, Value paramValue) throws InvalidTypeException, ClassNotLoadedException;
  
  Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt) throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException;
  
  ObjectReference newInstance(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt) throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException;
  
  Method concreteMethodByName(String paramString1, String paramString2);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ClassType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */