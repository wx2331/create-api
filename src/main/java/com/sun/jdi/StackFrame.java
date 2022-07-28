package com.sun.jdi;

import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public interface StackFrame extends Mirror, Locatable {
  Location location();
  
  ThreadReference thread();
  
  ObjectReference thisObject();
  
  List<LocalVariable> visibleVariables() throws AbsentInformationException;
  
  LocalVariable visibleVariableByName(String paramString) throws AbsentInformationException;
  
  Value getValue(LocalVariable paramLocalVariable);
  
  Map<LocalVariable, Value> getValues(List<? extends LocalVariable> paramList);
  
  void setValue(LocalVariable paramLocalVariable, Value paramValue) throws InvalidTypeException, ClassNotLoadedException;
  
  List<Value> getArgumentValues();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\StackFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */