/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.constExpr.Expression;
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
/*     */ public class StringEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   static StringGen stringGen;
/*     */   
/*     */   protected StringEntry() {
/*  50 */     String str = (String)Parser.overrideNames.get("string");
/*  51 */     if (str == null) {
/*  52 */       name("string");
/*     */     } else {
/*  54 */       name(str);
/*  55 */     }  repositoryID(Util.emptyID);
/*     */   }
/*     */ 
/*     */   
/*     */   protected StringEntry(StringEntry paramStringEntry) {
/*  60 */     super(paramStringEntry);
/*  61 */     this._maxSize = paramStringEntry._maxSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected StringEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  66 */     super(paramSymtabEntry, paramIDLID);
/*  67 */     module("");
/*     */     
/*  69 */     String str = (String)Parser.overrideNames.get("string");
/*  70 */     if (str == null) {
/*  71 */       name("string");
/*     */     } else {
/*  73 */       name(str);
/*  74 */     }  repositoryID(Util.emptyID);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  79 */     return new StringEntry(this);
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
/*  90 */     stringGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/*  98 */     return stringGen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void maxSize(Expression paramExpression) {
/* 105 */     this._maxSize = paramExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression maxSize() {
/* 112 */     return this._maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 117 */   private Expression _maxSize = null;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\StringEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */