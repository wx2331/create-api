/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
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
/*     */ public class MethodEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   protected MethodEntry() {}
/*     */   
/*     */   protected MethodEntry(MethodEntry paramMethodEntry) {
/*  58 */     super(paramMethodEntry);
/*  59 */     this._exceptionNames = (Vector)paramMethodEntry._exceptionNames.clone();
/*  60 */     this._exceptions = (Vector)paramMethodEntry._exceptions.clone();
/*  61 */     this._contexts = (Vector)paramMethodEntry._contexts.clone();
/*  62 */     this._parameters = (Vector)paramMethodEntry._parameters.clone();
/*  63 */     this._oneway = paramMethodEntry._oneway;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MethodEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID) {
/*  68 */     super(paramInterfaceEntry, paramIDLID);
/*  69 */     if (module().equals("")) {
/*  70 */       module(name());
/*  71 */     } else if (!name().equals("")) {
/*  72 */       module(module() + "/" + name());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  77 */     return new MethodEntry(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter) {
/*  88 */     methodGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/*  96 */     return methodGen;
/*     */   }
/*     */ 
/*     */   
/*     */   public void type(SymtabEntry paramSymtabEntry) {
/* 101 */     super.type(paramSymtabEntry);
/* 102 */     if (paramSymtabEntry == null) {
/* 103 */       typeName("void");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addException(ExceptionEntry paramExceptionEntry) {
/* 109 */     this._exceptions.addElement(paramExceptionEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector exceptions() {
/* 115 */     return this._exceptions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExceptionName(String paramString) {
/* 121 */     this._exceptionNames.addElement(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector exceptionNames() {
/* 130 */     return this._exceptionNames;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContext(String paramString) {
/* 136 */     this._contexts.addElement(paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector contexts() {
/* 142 */     return this._contexts;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParameter(ParameterEntry paramParameterEntry) {
/* 148 */     this._parameters.addElement(paramParameterEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector parameters() {
/* 156 */     return this._parameters;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void oneway(boolean paramBoolean) {
/* 162 */     this._oneway = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean oneway() {
/* 168 */     return this._oneway;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void valueMethod(boolean paramBoolean) {
/* 174 */     this._valueMethod = paramBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean valueMethod() {
/* 180 */     return this._valueMethod;
/*     */   }
/*     */ 
/*     */   
/*     */   void exceptionsAddElement(ExceptionEntry paramExceptionEntry) {
/* 185 */     addException(paramExceptionEntry);
/* 186 */     addExceptionName(paramExceptionEntry.fullName());
/*     */   }
/*     */   
/* 189 */   private Vector _exceptionNames = new Vector();
/* 190 */   private Vector _exceptions = new Vector();
/* 191 */   private Vector _contexts = new Vector();
/* 192 */   private Vector _parameters = new Vector();
/*     */   private boolean _oneway = false;
/*     */   private boolean _valueMethod = false;
/*     */   static MethodGen methodGen;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\MethodEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */