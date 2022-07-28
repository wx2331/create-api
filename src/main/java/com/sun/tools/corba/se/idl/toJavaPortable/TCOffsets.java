/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.EnumEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.TypedefEntry;
/*     */ import java.util.Enumeration;
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
/*     */ public class TCOffsets
/*     */ {
/*     */   public int offset(String paramString) {
/*  63 */     Integer integer = (Integer)this.tcs.get(paramString);
/*  64 */     return (integer == null) ? -1 : integer.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(SymtabEntry paramSymtabEntry) {
/*  72 */     if (paramSymtabEntry == null) {
/*  73 */       this.offset += 8;
/*     */     } else {
/*     */       
/*  76 */       this.tcs.put(paramSymtabEntry.fullName(), new Integer(this.offset));
/*  77 */       this.offset += 4;
/*  78 */       String str = Util.stripLeadingUnderscoresFromID(paramSymtabEntry.repositoryID().ID());
/*  79 */       if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.InterfaceEntry) {
/*  80 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name());
/*  81 */       } else if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.StructEntry) {
/*  82 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name()) + 4;
/*  83 */       } else if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.UnionEntry) {
/*  84 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name()) + 12;
/*  85 */       } else if (paramSymtabEntry instanceof EnumEntry) {
/*     */         
/*  87 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name()) + 4;
/*  88 */         Enumeration<String> enumeration = ((EnumEntry)paramSymtabEntry).elements().elements();
/*  89 */         while (enumeration.hasMoreElements()) {
/*  90 */           this.offset += alignStrLen(enumeration.nextElement());
/*     */         }
/*  92 */       } else if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry) {
/*  93 */         this.offset += 4;
/*  94 */       } else if (paramSymtabEntry instanceof TypedefEntry) {
/*     */         
/*  96 */         this.offset += alignStrLen(str) + alignStrLen(paramSymtabEntry.name());
/*  97 */         if (((TypedefEntry)paramSymtabEntry).arrayInfo().size() != 0) {
/*  98 */           this.offset += 8;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int alignStrLen(String paramString) {
/* 111 */     int i = paramString.length() + 1;
/* 112 */     int j = 4 - i % 4;
/* 113 */     if (j == 4) j = 0; 
/* 114 */     return i + j + 4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMember(SymtabEntry paramSymtabEntry) {
/* 122 */     this.offset += alignStrLen(paramSymtabEntry.name());
/* 123 */     if (((TypedefEntry)paramSymtabEntry).arrayInfo().size() != 0) {
/* 124 */       this.offset += 4;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int currentOffset() {
/* 132 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bumpCurrentOffset(int paramInt) {
/* 140 */     this.offset += paramInt;
/*     */   }
/*     */   
/* 143 */   private Hashtable tcs = new Hashtable<>();
/* 144 */   private int offset = 0;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\TCOffsets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */