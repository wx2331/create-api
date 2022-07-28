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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IncludeEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   static IncludeGen includeGen;
/*     */   private Vector includeList;
/*     */   private String _absFilename;
/*     */   
/*     */   protected IncludeEntry() {
/* 121 */     this.includeList = new Vector();
/*     */ 
/*     */     
/* 124 */     this._absFilename = null; repositoryID(Util.emptyID); } protected IncludeEntry(SymtabEntry paramSymtabEntry) { super(paramSymtabEntry, new IDLID()); this.includeList = new Vector(); this._absFilename = null; module(paramSymtabEntry.name()); name(""); } protected IncludeEntry(IncludeEntry paramIncludeEntry) { super(paramIncludeEntry); this.includeList = new Vector(); this._absFilename = null; }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     return new IncludeEntry(this);
/*     */   }
/*     */   
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter) {
/*     */     includeGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */   
/*     */   public Generator generator() {
/*     */     return includeGen;
/*     */   }
/*     */   
/*     */   public void absFilename(String paramString) {
/*     */     this._absFilename = paramString;
/*     */   }
/*     */   
/*     */   public String absFilename() {
/*     */     return this._absFilename;
/*     */   }
/*     */   
/*     */   public void addInclude(IncludeEntry paramIncludeEntry) {
/*     */     this.includeList.addElement(paramIncludeEntry);
/*     */   }
/*     */   
/*     */   public Vector includes() {
/*     */     return this.includeList;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\IncludeEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */