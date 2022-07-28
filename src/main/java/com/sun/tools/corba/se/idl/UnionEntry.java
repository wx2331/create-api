/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.constExpr.Expression;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Enumeration;
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
/*     */ public class UnionEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   protected UnionEntry() {}
/*     */   
/*     */   protected UnionEntry(UnionEntry paramUnionEntry) {
/*  59 */     super(paramUnionEntry);
/*  60 */     if (!name().equals("")) {
/*     */       
/*  62 */       module(module() + name());
/*  63 */       name("");
/*     */     } 
/*  65 */     this._branches = (Vector)paramUnionEntry._branches.clone();
/*  66 */     this._defaultBranch = paramUnionEntry._defaultBranch;
/*  67 */     this._contained = paramUnionEntry._contained;
/*     */   }
/*     */ 
/*     */   
/*     */   protected UnionEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  72 */     super(paramSymtabEntry, paramIDLID);
/*  73 */     if (module().equals("")) {
/*  74 */       module(name());
/*  75 */     } else if (!name().equals("")) {
/*  76 */       module(module() + "/" + name());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  81 */     return new UnionEntry(this);
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
/*  92 */     unionGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/* 100 */     return unionGen;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBranch(UnionBranch paramUnionBranch) {
/* 105 */     this._branches.addElement(paramUnionBranch);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector branches() {
/* 111 */     return this._branches;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void defaultBranch(TypedefEntry paramTypedefEntry) {
/* 119 */     this._defaultBranch = paramTypedefEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedefEntry defaultBranch() {
/* 127 */     return this._defaultBranch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addContained(SymtabEntry paramSymtabEntry) {
/* 132 */     this._contained.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector contained() {
/* 154 */     return this._contained;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean has(Expression paramExpression) {
/* 159 */     Enumeration enumeration = this._branches.elements();
/* 160 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 162 */       Enumeration<Expression> enumeration1 = ((UnionBranch)enumeration.nextElement()).labels.elements();
/* 163 */       while (enumeration1.hasMoreElements()) {
/*     */         
/* 165 */         Expression expression = enumeration1.nextElement();
/* 166 */         if (expression.equals(paramExpression) || expression.value().equals(paramExpression.value()))
/* 167 */           return true; 
/*     */       } 
/*     */     } 
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean has(TypedefEntry paramTypedefEntry) {
/* 175 */     Enumeration<UnionBranch> enumeration = this._branches.elements();
/* 176 */     while (enumeration.hasMoreElements()) {
/*     */       
/* 178 */       UnionBranch unionBranch = enumeration.nextElement();
/* 179 */       if (!unionBranch.typedef.equals(paramTypedefEntry) && unionBranch.typedef.name().equals(paramTypedefEntry.name()))
/* 180 */         return true; 
/*     */     } 
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   
/* 186 */   private Vector _branches = new Vector();
/* 187 */   private TypedefEntry _defaultBranch = null;
/* 188 */   private Vector _contained = new Vector();
/*     */   static UnionGen unionGen;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\UnionEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */