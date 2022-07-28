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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   static ConstGen constGen;
/*     */   
/*     */   protected ConstEntry() {}
/*     */   
/*     */   protected ConstEntry(ConstEntry paramConstEntry) {
/*  57 */     super(paramConstEntry);
/*  58 */     if (module().equals("")) {
/*  59 */       module(name());
/*  60 */     } else if (!name().equals("")) {
/*  61 */       module(module() + "/" + name());
/*  62 */     }  this._value = paramConstEntry._value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ConstEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  68 */     super(paramSymtabEntry, paramIDLID);
/*  69 */     if (module().equals("")) {
/*  70 */       module(name());
/*  71 */     } else if (!name().equals("")) {
/*  72 */       module(module() + "/" + name());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  78 */     return new ConstEntry(this);
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
/*  89 */     constGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/*  97 */     return constGen;
/*     */   }
/*     */ 
/*     */   
/*     */   public Expression value() {
/* 102 */     return this._value;
/*     */   }
/*     */ 
/*     */   
/*     */   public void value(Expression paramExpression) {
/* 107 */     this._value = paramExpression;
/*     */   }
/*     */ 
/*     */   
/* 111 */   private Expression _value = null;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\ConstEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */