package com.sun.tools.classfile;

public interface Dependency {
  Location getOrigin();
  
  Location getTarget();
  
  public static interface Filter {
    boolean accepts(Dependency param1Dependency);
  }
  
  public static interface Finder {
    Iterable<? extends Dependency> findDependencies(ClassFile param1ClassFile);
  }
  
  public static interface Location {
    String getName();
    
    String getClassName();
    
    String getPackageName();
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\classfile\Dependency.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */