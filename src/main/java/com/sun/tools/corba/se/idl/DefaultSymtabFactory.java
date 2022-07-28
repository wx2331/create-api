/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSymtabFactory
/*     */   implements SymtabFactory
/*     */ {
/*     */   public AttributeEntry attributeEntry() {
/*  48 */     return new AttributeEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public AttributeEntry attributeEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID) {
/*  53 */     return new AttributeEntry(paramInterfaceEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public ConstEntry constEntry() {
/*  58 */     return new ConstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConstEntry constEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  63 */     return new ConstEntry(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeEntry nativeEntry() {
/*  68 */     return new NativeEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeEntry nativeEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  73 */     return new NativeEntry(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumEntry enumEntry() {
/*  78 */     return new EnumEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumEntry enumEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  83 */     return new EnumEntry(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public ExceptionEntry exceptionEntry() {
/*  88 */     return new ExceptionEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public ExceptionEntry exceptionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  93 */     return new ExceptionEntry(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public ForwardEntry forwardEntry() {
/*  98 */     return new ForwardEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public ForwardEntry forwardEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID) {
/* 103 */     return new ForwardEntry(paramModuleEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public ForwardValueEntry forwardValueEntry() {
/* 108 */     return new ForwardValueEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public ForwardValueEntry forwardValueEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID) {
/* 113 */     return new ForwardValueEntry(paramModuleEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public IncludeEntry includeEntry() {
/* 118 */     return new IncludeEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public IncludeEntry includeEntry(SymtabEntry paramSymtabEntry) {
/* 123 */     return new IncludeEntry(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceEntry interfaceEntry() {
/* 128 */     return new InterfaceEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceEntry interfaceEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID) {
/* 133 */     return new InterfaceEntry(paramModuleEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueEntry valueEntry() {
/* 138 */     return new ValueEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueEntry valueEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID) {
/* 143 */     return new ValueEntry(paramModuleEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueBoxEntry valueBoxEntry() {
/* 148 */     return new ValueBoxEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueBoxEntry valueBoxEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID) {
/* 153 */     return new ValueBoxEntry(paramModuleEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodEntry methodEntry() {
/* 158 */     return new MethodEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodEntry methodEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID) {
/* 163 */     return new MethodEntry(paramInterfaceEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public ModuleEntry moduleEntry() {
/* 168 */     return new ModuleEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public ModuleEntry moduleEntry(ModuleEntry paramModuleEntry, IDLID paramIDLID) {
/* 173 */     return new ModuleEntry(paramModuleEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public ParameterEntry parameterEntry() {
/* 178 */     return new ParameterEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public ParameterEntry parameterEntry(MethodEntry paramMethodEntry, IDLID paramIDLID) {
/* 183 */     return new ParameterEntry(paramMethodEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public PragmaEntry pragmaEntry() {
/* 188 */     return new PragmaEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public PragmaEntry pragmaEntry(SymtabEntry paramSymtabEntry) {
/* 193 */     return new PragmaEntry(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   public PrimitiveEntry primitiveEntry() {
/* 198 */     return new PrimitiveEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrimitiveEntry primitiveEntry(String paramString) {
/* 209 */     return new PrimitiveEntry(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public SequenceEntry sequenceEntry() {
/* 214 */     return new SequenceEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public SequenceEntry sequenceEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/* 219 */     return new SequenceEntry(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public StringEntry stringEntry() {
/* 224 */     return new StringEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public StructEntry structEntry() {
/* 229 */     return new StructEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public StructEntry structEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/* 234 */     return new StructEntry(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedefEntry typedefEntry() {
/* 239 */     return new TypedefEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedefEntry typedefEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/* 244 */     return new TypedefEntry(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnionEntry unionEntry() {
/* 249 */     return new UnionEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnionEntry unionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/* 254 */     return new UnionEntry(paramSymtabEntry, paramIDLID);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\DefaultSymtabFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */