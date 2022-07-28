/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParameterEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   public static final int In = 0;
/*     */   public static final int Inout = 1;
/*     */   public static final int Out = 2;
/*     */   
/*     */   protected ParameterEntry() {}
/*     */   
/*     */   protected ParameterEntry(ParameterEntry paramParameterEntry) {
/*  61 */     super(paramParameterEntry);
/*  62 */     this._passType = paramParameterEntry._passType;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ParameterEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  67 */     super(paramSymtabEntry, paramIDLID);
/*  68 */     if (module().equals("")) {
/*  69 */       module(name());
/*  70 */     } else if (!name().equals("")) {
/*  71 */       module(module() + "/" + name());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  76 */     return new ParameterEntry(this);
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
/*  87 */     parameterGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/*  95 */     return parameterGen;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void passType(int paramInt) {
/* 101 */     if (paramInt >= 0 && paramInt <= 2) {
/* 102 */       this._passType = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int passType() {
/* 108 */     return this._passType;
/*     */   }
/*     */   
/* 111 */   private int _passType = 0;
/*     */   static ParameterGen parameterGen;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\ParameterEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */