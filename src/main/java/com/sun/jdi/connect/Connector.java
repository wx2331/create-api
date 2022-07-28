package com.sun.jdi.connect;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public interface Connector {
  String name();
  
  String description();
  
  Transport transport();
  
  Map<String, Argument> defaultArguments();
  
  @Exported
  public static interface Argument extends Serializable {
    String name();
    
    String label();
    
    String description();
    
    String value();
    
    void setValue(String param1String);
    
    boolean isValid(String param1String);
    
    boolean mustSpecify();
  }
  
  @Exported
  public static interface BooleanArgument extends Argument {
    void setValue(boolean param1Boolean);
    
    boolean isValid(String param1String);
    
    String stringValueOf(boolean param1Boolean);
    
    boolean booleanValue();
  }
  
  @Exported
  public static interface IntegerArgument extends Argument {
    void setValue(int param1Int);
    
    boolean isValid(String param1String);
    
    boolean isValid(int param1Int);
    
    String stringValueOf(int param1Int);
    
    int intValue();
    
    int max();
    
    int min();
  }
  
  @Exported
  public static interface SelectedArgument extends Argument {
    List<String> choices();
    
    boolean isValid(String param1String);
  }
  
  @Exported
  public static interface StringArgument extends Argument {
    boolean isValid(String param1String);
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\connect\Connector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */