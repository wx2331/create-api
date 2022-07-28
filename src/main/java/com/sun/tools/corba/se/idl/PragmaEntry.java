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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PragmaEntry
/*     */   extends SymtabEntry
/*     */ {
/*     */   static PragmaGen pragmaGen;
/*     */   private String _data;
/*     */   
/*     */   protected PragmaEntry() {
/* 102 */     this._data = null; repositoryID(Util.emptyID); } protected PragmaEntry(SymtabEntry paramSymtabEntry) { super(paramSymtabEntry, new IDLID()); this._data = null; module(paramSymtabEntry.name()); name(""); } protected PragmaEntry(PragmaEntry paramPragmaEntry) { super(paramPragmaEntry); this._data = null; }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     return new PragmaEntry(this);
/*     */   }
/*     */   
/*     */   public void generate(Hashtable paramHashtable, PrintWriter paramPrintWriter) {
/*     */     pragmaGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */   
/*     */   public Generator generator() {
/*     */     return pragmaGen;
/*     */   }
/*     */   
/*     */   public String data() {
/*     */     return this._data;
/*     */   }
/*     */   
/*     */   public void data(String paramString) {
/*     */     this._data = paramString;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\PragmaEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */