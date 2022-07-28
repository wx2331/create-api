/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.constExpr.Expression;
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
/*     */ public class TypedefEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   protected TypedefEntry() {}
/*     */   
/*     */   protected TypedefEntry(TypedefEntry paramTypedefEntry) {
/*  58 */     super(paramTypedefEntry);
/*  59 */     this._arrayInfo = (Vector)paramTypedefEntry._arrayInfo.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   protected TypedefEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  64 */     super(paramSymtabEntry, paramIDLID);
/*  65 */     if (module().equals("")) {
/*  66 */       module(name());
/*  67 */     } else if (!name().equals("")) {
/*  68 */       module(module() + "/" + name());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector arrayInfo() {
/*  76 */     return this._arrayInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addArrayInfo(Expression paramExpression) {
/*  81 */     this._arrayInfo.addElement(paramExpression);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  86 */     return new TypedefEntry(this);
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
/*  97 */     typedefGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReferencable() {
/* 104 */     return type().isReferencable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void isReferencable(boolean paramBoolean) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/* 117 */     return typedefGen;
/*     */   }
/*     */   
/* 120 */   private Vector _arrayInfo = new Vector();
/*     */   static TypedefGen typedefGen;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\TypedefEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */