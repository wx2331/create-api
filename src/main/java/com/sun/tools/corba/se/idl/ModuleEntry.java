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
/*     */ public class ModuleEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   protected ModuleEntry() {}
/*     */   
/*     */   protected ModuleEntry(ModuleEntry paramModuleEntry) {
/*  56 */     super(paramModuleEntry);
/*  57 */     this._contained = (Vector)paramModuleEntry._contained.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ModuleEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  62 */     super(paramSymtabEntry, paramIDLID);
/*     */     
/*  64 */     if (module().equals("")) {
/*  65 */       module(name());
/*  66 */     } else if (!name().equals("")) {
/*  67 */       module(module() + "/" + name());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  72 */     return new ModuleEntry(this);
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
/*  83 */     moduleGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/*  91 */     return moduleGen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addContained(SymtabEntry paramSymtabEntry) {
/*  99 */     this._contained.addElement(paramSymtabEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector contained() {
/* 107 */     return this._contained;
/*     */   }
/*     */   
/* 110 */   private Vector _contained = new Vector();
/*     */   static ModuleGen moduleGen;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\ModuleEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */