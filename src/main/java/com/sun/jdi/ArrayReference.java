package com.sun.jdi;

import java.util.List;
import jdk.Exported;

@Exported
public interface ArrayReference extends ObjectReference {
  int length();
  
  Value getValue(int paramInt);
  
  List<Value> getValues();
  
  List<Value> getValues(int paramInt1, int paramInt2);
  
  void setValue(int paramInt, Value paramValue) throws InvalidTypeException, ClassNotLoadedException;
  
  void setValues(List<? extends Value> paramList) throws InvalidTypeException, ClassNotLoadedException;
  
  void setValues(int paramInt1, List<? extends Value> paramList, int paramInt2, int paramInt3) throws InvalidTypeException, ClassNotLoadedException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ArrayReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */