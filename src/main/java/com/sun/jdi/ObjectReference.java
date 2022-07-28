package com.sun.jdi;

import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public interface ObjectReference extends Value {
  public static final int INVOKE_SINGLE_THREADED = 1;
  
  public static final int INVOKE_NONVIRTUAL = 2;
  
  ReferenceType referenceType();
  
  Value getValue(Field paramField);
  
  Map<Field, Value> getValues(List<? extends Field> paramList);
  
  void setValue(Field paramField, Value paramValue) throws InvalidTypeException, ClassNotLoadedException;
  
  Value invokeMethod(ThreadReference paramThreadReference, Method paramMethod, List<? extends Value> paramList, int paramInt) throws InvalidTypeException, ClassNotLoadedException, IncompatibleThreadStateException, InvocationException;
  
  void disableCollection();
  
  void enableCollection();
  
  boolean isCollected();
  
  long uniqueID();
  
  List<ThreadReference> waitingThreads() throws IncompatibleThreadStateException;
  
  ThreadReference owningThread() throws IncompatibleThreadStateException;
  
  int entryCount() throws IncompatibleThreadStateException;
  
  List<ObjectReference> referringObjects(long paramLong);
  
  boolean equals(Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ObjectReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */