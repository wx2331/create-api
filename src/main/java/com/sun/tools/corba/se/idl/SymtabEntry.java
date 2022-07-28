/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
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
/*     */ public class SymtabEntry
/*     */ {
/*     */   public SymtabEntry() {
/* 356 */     this._container = null;
/* 357 */     this._module = "";
/* 358 */     this._name = "";
/* 359 */     this._typeName = "";
/* 360 */     this._type = null;
/* 361 */     this._sourceFile = null;
/* 362 */     this._info = null;
/* 363 */     this._repID = new IDLID("", "", "1.0");
/* 364 */     this._emit = setEmit;
/* 365 */     this._comment = null;
/*     */     
/* 367 */     this._isReferencable = true; initDynamicVars(); } SymtabEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) { this._container = null; this._module = ""; this._name = ""; this._typeName = ""; this._type = null; this._sourceFile = null; this._info = null; this._repID = new IDLID("", "", "1.0"); this._emit = setEmit; this._comment = null; this._isReferencable = true; this._module = paramSymtabEntry._module; this._name = paramSymtabEntry._name; this._type = paramSymtabEntry._type; this._typeName = paramSymtabEntry._typeName; this._sourceFile = paramSymtabEntry._sourceFile; this._info = paramSymtabEntry._info; this._repID = (RepositoryID)paramIDLID.clone(); ((IDLID)this._repID).appendToName(this._name); if (paramSymtabEntry instanceof InterfaceEntry || paramSymtabEntry instanceof ModuleEntry || paramSymtabEntry instanceof StructEntry || paramSymtabEntry instanceof UnionEntry || (paramSymtabEntry instanceof SequenceEntry && this instanceof SequenceEntry)) { this._container = paramSymtabEntry; } else { this._container = paramSymtabEntry._container; }  initDynamicVars(); this._comment = paramSymtabEntry._comment; } SymtabEntry(SymtabEntry paramSymtabEntry) { this._container = null; this._module = ""; this._name = ""; this._typeName = ""; this._type = null; this._sourceFile = null; this._info = null; this._repID = new IDLID("", "", "1.0"); this._emit = setEmit; this._comment = null; this._isReferencable = true;
/*     */     this._module = paramSymtabEntry._module;
/*     */     this._name = paramSymtabEntry._name;
/*     */     this._type = paramSymtabEntry._type;
/*     */     this._typeName = paramSymtabEntry._typeName;
/*     */     this._sourceFile = paramSymtabEntry._sourceFile;
/*     */     this._info = paramSymtabEntry._info;
/*     */     this._repID = (RepositoryID)paramSymtabEntry._repID.clone();
/*     */     this._container = paramSymtabEntry._container;
/*     */     if (this._type instanceof ForwardEntry)
/*     */       ((ForwardEntry)this._type).types.addElement(this); 
/*     */     initDynamicVars();
/*     */     this._comment = paramSymtabEntry._comment; }
/*     */ 
/*     */   
/*     */   void initDynamicVars() {
/*     */     this._dynamicVars = new Vector(maxKey + 1);
/*     */     for (byte b = 0; b <= maxKey; b++)
/*     */       this._dynamicVars.addElement(null); 
/*     */   }
/*     */   
/*     */   public Object clone() {
/*     */     return new SymtabEntry(this);
/*     */   }
/*     */   
/*     */   public final String fullName() {
/*     */     return this._module.equals("") ? this._name : (this._module + '/' + this._name);
/*     */   }
/*     */   
/*     */   public String module() {
/*     */     return this._module;
/*     */   }
/*     */   
/*     */   public void module(String paramString) {
/*     */     if (paramString == null) {
/*     */       this._module = "";
/*     */     } else {
/*     */       this._module = paramString;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String name() {
/*     */     return this._name;
/*     */   }
/*     */   
/*     */   public void name(String paramString) {
/*     */     if (paramString == null) {
/*     */       this._name = "";
/*     */     } else {
/*     */       this._name = paramString;
/*     */     } 
/*     */     if (this._repID instanceof IDLID)
/*     */       ((IDLID)this._repID).replaceName(paramString); 
/*     */   }
/*     */   
/*     */   public String typeName() {
/*     */     return this._typeName;
/*     */   }
/*     */   
/*     */   protected void typeName(String paramString) {
/*     */     this._typeName = paramString;
/*     */   }
/*     */   
/*     */   public SymtabEntry type() {
/*     */     return this._type;
/*     */   }
/*     */   
/*     */   public void type(SymtabEntry paramSymtabEntry) {
/*     */     if (paramSymtabEntry == null) {
/*     */       typeName("");
/*     */     } else {
/*     */       typeName(paramSymtabEntry.fullName());
/*     */     } 
/*     */     this._type = paramSymtabEntry;
/*     */     if (this._type instanceof ForwardEntry)
/*     */       ((ForwardEntry)this._type).types.addElement(this); 
/*     */   }
/*     */   
/*     */   public IncludeEntry sourceFile() {
/*     */     return this._sourceFile;
/*     */   }
/*     */   
/*     */   public void sourceFile(IncludeEntry paramIncludeEntry) {
/*     */     this._sourceFile = paramIncludeEntry;
/*     */   }
/*     */   
/*     */   public SymtabEntry container() {
/*     */     return this._container;
/*     */   }
/*     */   
/*     */   public void container(SymtabEntry paramSymtabEntry) {
/*     */     if (paramSymtabEntry instanceof InterfaceEntry || paramSymtabEntry instanceof ModuleEntry)
/*     */       this._container = paramSymtabEntry; 
/*     */   }
/*     */   
/*     */   public RepositoryID repositoryID() {
/*     */     return this._repID;
/*     */   }
/*     */   
/*     */   public void repositoryID(RepositoryID paramRepositoryID) {
/*     */     this._repID = paramRepositoryID;
/*     */   }
/*     */   
/*     */   public boolean emit() {
/*     */     return (this._emit && this._isReferencable);
/*     */   }
/*     */   
/*     */   public void emit(boolean paramBoolean) {
/*     */     this._emit = paramBoolean;
/*     */   }
/*     */   
/*     */   public Comment comment() {
/*     */     return this._comment;
/*     */   }
/*     */   
/*     */   public void comment(Comment paramComment) {
/*     */     this._comment = paramComment;
/*     */   }
/*     */   
/*     */   public boolean isReferencable() {
/*     */     return this._isReferencable;
/*     */   }
/*     */   
/*     */   public void isReferencable(boolean paramBoolean) {
/*     */     this._isReferencable = paramBoolean;
/*     */   }
/*     */   
/*     */   static Stack includeStack = new Stack();
/*     */   
/*     */   static void enteringInclude() {
/*     */     includeStack.push(new Boolean(setEmit));
/*     */     setEmit = false;
/*     */   }
/*     */   
/*     */   static void exitingInclude() {
/*     */     setEmit = ((Boolean)includeStack.pop()).booleanValue();
/*     */   }
/*     */   
/*     */   public static int getVariableKey() {
/*     */     return ++maxKey;
/*     */   }
/*     */   
/*     */   public void dynamicVariable(int paramInt, Object paramObject) throws NoSuchFieldException {
/*     */     if (paramInt > maxKey)
/*     */       throw new NoSuchFieldException(Integer.toString(paramInt)); 
/*     */     if (paramInt >= this._dynamicVars.size())
/*     */       growVars(); 
/*     */     this._dynamicVars.setElementAt(paramObject, paramInt);
/*     */   }
/*     */   
/*     */   public Object dynamicVariable(int paramInt) throws NoSuchFieldException {
/*     */     if (paramInt > maxKey)
/*     */       throw new NoSuchFieldException(Integer.toString(paramInt)); 
/*     */     if (paramInt >= this._dynamicVars.size())
/*     */       growVars(); 
/*     */     return this._dynamicVars.elementAt(paramInt);
/*     */   }
/*     */   
/*     */   void growVars() {
/*     */     int i = maxKey - this._dynamicVars.size() + 1;
/*     */     for (byte b = 0; b < i; b++)
/*     */       this._dynamicVars.addElement(null); 
/*     */   }
/*     */   
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter) {}
/*     */   
/*     */   public Generator generator() {
/*     */     return null;
/*     */   }
/*     */   
/*     */   static boolean setEmit = true;
/*     */   static int maxKey = -1;
/*     */   private SymtabEntry _container;
/*     */   private String _module;
/*     */   private String _name;
/*     */   private String _typeName;
/*     */   private SymtabEntry _type;
/*     */   private IncludeEntry _sourceFile;
/*     */   private Object _info;
/*     */   private RepositoryID _repID;
/*     */   private boolean _emit;
/*     */   private Comment _comment;
/*     */   private Vector _dynamicVars;
/*     */   private boolean _isReferencable;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\SymtabEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */