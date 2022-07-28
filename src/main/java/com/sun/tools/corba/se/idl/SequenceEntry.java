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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SequenceEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   static SequenceGen sequenceGen;
/*     */   private Expression _maxSize;
/*     */   private Vector _contained;
/*     */   
/*     */   protected SequenceEntry() {
/* 139 */     this._maxSize = null;
/* 140 */     this._contained = new Vector(); repositoryID(Util.emptyID); } protected SequenceEntry(SequenceEntry paramSequenceEntry) { super(paramSequenceEntry); this._maxSize = null; this._contained = new Vector(); this._maxSize = paramSequenceEntry._maxSize; } protected SequenceEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) { super(paramSymtabEntry, paramIDLID); this._maxSize = null; this._contained = new Vector();
/*     */     if (!(paramSymtabEntry instanceof SequenceEntry))
/*     */       if (module().equals("")) {
/*     */         module(name());
/*     */       } else if (!name().equals("")) {
/*     */         module(module() + "/" + name());
/*     */       }  
/*     */     repositoryID(Util.emptyID); }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     return new SequenceEntry(this);
/*     */   }
/*     */   
/*     */   public boolean isReferencable() {
/*     */     return type().isReferencable();
/*     */   }
/*     */   
/*     */   public void isReferencable(boolean paramBoolean) {}
/*     */   
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter) {
/*     */     sequenceGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */   
/*     */   public Generator generator() {
/*     */     return sequenceGen;
/*     */   }
/*     */   
/*     */   public void maxSize(Expression paramExpression) {
/*     */     this._maxSize = paramExpression;
/*     */   }
/*     */   
/*     */   public Expression maxSize() {
/*     */     return this._maxSize;
/*     */   }
/*     */   
/*     */   public void addContained(SymtabEntry paramSymtabEntry) {
/*     */     this._contained.addElement(paramSymtabEntry);
/*     */   }
/*     */   
/*     */   public Vector contained() {
/*     */     return this._contained;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\SequenceEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */