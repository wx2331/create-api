/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
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
/*     */ public class ForwardEntry
/*     */   extends SymtabEntry
/*     */   implements InterfaceType
/*     */ {
/*     */   static ForwardGen forwardGen;
/*     */   
/*     */   protected ForwardEntry() {}
/*     */   
/*     */   protected ForwardEntry(ForwardEntry paramForwardEntry) {
/*  57 */     super(paramForwardEntry);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ForwardEntry(SymtabEntry paramSymtabEntry, IDLID paramIDLID) {
/*  62 */     super(paramSymtabEntry, paramIDLID);
/*  63 */     if (module().equals("")) {
/*  64 */       module(name());
/*  65 */     } else if (!name().equals("")) {
/*  66 */       module(module() + "/" + name());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  71 */     return new ForwardEntry(this);
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
/*  82 */     forwardGen.generate(paramHashtable, this, paramPrintWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Generator generator() {
/*  90 */     return forwardGen;
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean replaceForwardDecl(InterfaceEntry paramInterfaceEntry) {
/*  95 */     boolean bool = true;
/*     */ 
/*     */     
/*     */     try {
/*  99 */       ForwardEntry forwardEntry = (ForwardEntry)Parser.symbolTable.get(paramInterfaceEntry.fullName());
/* 100 */       if (forwardEntry != null) {
/*     */ 
/*     */         
/* 103 */         bool = (paramInterfaceEntry.getInterfaceType() == forwardEntry.getInterfaceType()) ? true : false;
/* 104 */         forwardEntry.type(paramInterfaceEntry);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 109 */         paramInterfaceEntry.forwardedDerivers = forwardEntry.derivers;
/* 110 */         Enumeration<InterfaceEntry> enumeration = forwardEntry.derivers.elements();
/* 111 */         while (enumeration.hasMoreElements()) {
/* 112 */           ((InterfaceEntry)enumeration.nextElement()).replaceForwardDecl(forwardEntry, paramInterfaceEntry);
/*     */         }
/*     */         
/* 115 */         enumeration = forwardEntry.types.elements();
/* 116 */         while (enumeration.hasMoreElements()) {
/* 117 */           ((SymtabEntry)enumeration.nextElement()).type(paramInterfaceEntry);
/*     */         }
/*     */       } 
/* 120 */     } catch (Exception exception) {}
/*     */     
/* 122 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInterfaceType() {
/* 130 */     return this._type;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInterfaceType(int paramInt) {
/* 135 */     this._type = paramInt;
/*     */   }
/*     */ 
/*     */   
/* 139 */   Vector derivers = new Vector();
/* 140 */   Vector types = new Vector();
/* 141 */   private int _type = 0;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\ForwardEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */