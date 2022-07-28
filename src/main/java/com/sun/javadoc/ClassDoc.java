package com.sun.javadoc;

public interface ClassDoc extends ProgramElementDoc, Type {
  boolean isAbstract();
  
  boolean isSerializable();
  
  boolean isExternalizable();
  
  MethodDoc[] serializationMethods();
  
  FieldDoc[] serializableFields();
  
  boolean definesSerializableFields();
  
  ClassDoc superclass();
  
  Type superclassType();
  
  boolean subclassOf(ClassDoc paramClassDoc);
  
  ClassDoc[] interfaces();
  
  Type[] interfaceTypes();
  
  TypeVariable[] typeParameters();
  
  ParamTag[] typeParamTags();
  
  FieldDoc[] fields();
  
  FieldDoc[] fields(boolean paramBoolean);
  
  FieldDoc[] enumConstants();
  
  MethodDoc[] methods();
  
  MethodDoc[] methods(boolean paramBoolean);
  
  ConstructorDoc[] constructors();
  
  ConstructorDoc[] constructors(boolean paramBoolean);
  
  ClassDoc[] innerClasses();
  
  ClassDoc[] innerClasses(boolean paramBoolean);
  
  ClassDoc findClass(String paramString);
  
  @Deprecated
  ClassDoc[] importedClasses();
  
  @Deprecated
  PackageDoc[] importedPackages();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\javadoc\ClassDoc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */