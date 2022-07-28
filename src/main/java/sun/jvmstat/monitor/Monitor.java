package sun.jvmstat.monitor;

public interface Monitor {
  String getName();
  
  String getBaseName();
  
  Units getUnits();
  
  Variability getVariability();
  
  boolean isVector();
  
  int getVectorLength();
  
  boolean isSupported();
  
  Object getValue();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\jvmstat\monitor\Monitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */