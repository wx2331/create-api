package com.sun.tools.corba.se.idl;

public interface SymtabFactory {
  AttributeEntry attributeEntry();
  
  AttributeEntry attributeEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID);
  
  ConstEntry constEntry();
  
  ConstEntry constEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
  
  NativeEntry nativeEntry();
  
  NativeEntry nativeEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
  
  EnumEntry enumEntry();
  
  EnumEntry enumEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
  
  ExceptionEntry exceptionEntry();
  
  ExceptionEntry exceptionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
  
  ForwardEntry forwardEntry();
  
  ForwardEntry forwardEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);
  
  ForwardValueEntry forwardValueEntry();
  
  ForwardValueEntry forwardValueEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);
  
  IncludeEntry includeEntry();
  
  IncludeEntry includeEntry(SymtabEntry paramSymtabEntry);
  
  InterfaceEntry interfaceEntry();
  
  InterfaceEntry interfaceEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);
  
  ValueEntry valueEntry();
  
  ValueEntry valueEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);
  
  ValueBoxEntry valueBoxEntry();
  
  ValueBoxEntry valueBoxEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);
  
  MethodEntry methodEntry();
  
  MethodEntry methodEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID);
  
  ModuleEntry moduleEntry();
  
  ModuleEntry moduleEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID);
  
  ParameterEntry parameterEntry();
  
  ParameterEntry parameterEntry(MethodEntry paramMethodEntry, IDLID paramIDLID);
  
  PragmaEntry pragmaEntry();
  
  PragmaEntry pragmaEntry(SymtabEntry paramSymtabEntry);
  
  PrimitiveEntry primitiveEntry();
  
  PrimitiveEntry primitiveEntry(String paramString);
  
  SequenceEntry sequenceEntry();
  
  SequenceEntry sequenceEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
  
  StringEntry stringEntry();
  
  StructEntry structEntry();
  
  StructEntry structEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
  
  TypedefEntry typedefEntry();
  
  TypedefEntry typedefEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
  
  UnionEntry unionEntry();
  
  UnionEntry unionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\SymtabFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */