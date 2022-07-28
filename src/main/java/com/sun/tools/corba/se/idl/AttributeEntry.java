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
/*     */ public class AttributeEntry
/*     */   extends MethodEntry
/*     */ {
/*     */   static AttributeGen attributeGen;
/*     */   
/*     */   protected AttributeEntry() {}
/*     */   
/*     */   protected AttributeEntry(AttributeEntry paramAttributeEntry) {
/*  57 */     super(paramAttributeEntry);
/*  58 */     this._readOnly = paramAttributeEntry._readOnly;
/*     */   }
/*     */ 
/*     */   
/*     */   protected AttributeEntry(InterfaceEntry paramInterfaceEntry, IDLID paramIDLID) {
/*  63 */     super(paramInterfaceEntry, paramIDLID);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() {
/*  68 */     return new AttributeEntry(this);
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
/*  79 */     attributeGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/*  87 */     return attributeGen;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean readOnly() {
/*  93 */     return this._readOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void readOnly(boolean paramBoolean) {
/*  99 */     this._readOnly = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean _readOnly = false;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\AttributeEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */