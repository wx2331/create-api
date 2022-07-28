package com.sun.jdi;

import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
public interface ReferenceType extends Type, Comparable<ReferenceType>, Accessible {
  String name();
  
  String genericSignature();
  
  ClassLoaderReference classLoader();
  
  String sourceName() throws AbsentInformationException;
  
  List<String> sourceNames(String paramString) throws AbsentInformationException;
  
  List<String> sourcePaths(String paramString) throws AbsentInformationException;
  
  String sourceDebugExtension() throws AbsentInformationException;
  
  boolean isStatic();
  
  boolean isAbstract();
  
  boolean isFinal();
  
  boolean isPrepared();
  
  boolean isVerified();
  
  boolean isInitialized();
  
  boolean failedToInitialize();
  
  List<Field> fields();
  
  List<Field> visibleFields();
  
  List<Field> allFields();
  
  Field fieldByName(String paramString);
  
  List<Method> methods();
  
  List<Method> visibleMethods();
  
  List<Method> allMethods();
  
  List<Method> methodsByName(String paramString);
  
  List<Method> methodsByName(String paramString1, String paramString2);
  
  List<ReferenceType> nestedTypes();
  
  Value getValue(Field paramField);
  
  Map<Field, Value> getValues(List<? extends Field> paramList);
  
  ClassObjectReference classObject();
  
  List<Location> allLineLocations() throws AbsentInformationException;
  
  List<Location> allLineLocations(String paramString1, String paramString2) throws AbsentInformationException;
  
  List<Location> locationsOfLine(int paramInt) throws AbsentInformationException;
  
  List<Location> locationsOfLine(String paramString1, String paramString2, int paramInt) throws AbsentInformationException;
  
  List<String> availableStrata();
  
  String defaultStratum();
  
  List<ObjectReference> instances(long paramLong);
  
  boolean equals(Object paramObject);
  
  int hashCode();
  
  int majorVersion();
  
  int minorVersion();
  
  int constantPoolCount();
  
  byte[] constantPool();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\jdi\ReferenceType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */