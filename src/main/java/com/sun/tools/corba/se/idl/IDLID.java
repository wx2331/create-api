/*     */ package com.sun.tools.corba.se.idl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IDLID
/*     */   extends RepositoryID
/*     */ {
/*     */   private String _prefix;
/*     */   private String _name;
/*     */   private String _version;
/*     */   
/*     */   public IDLID() {
/*  44 */     this._prefix = "";
/*  45 */     this._name = "";
/*  46 */     this._version = "1.0";
/*     */   }
/*     */ 
/*     */   
/*     */   public IDLID(String paramString1, String paramString2, String paramString3) {
/*  51 */     this._prefix = paramString1;
/*  52 */     this._name = paramString2;
/*  53 */     this._version = paramString3;
/*     */   }
/*     */ 
/*     */   
/*     */   public String ID() {
/*  58 */     if (this._prefix.equals("")) {
/*  59 */       return "IDL:" + this._name + ':' + this._version;
/*     */     }
/*  61 */     return "IDL:" + this._prefix + '/' + this._name + ':' + this._version;
/*     */   }
/*     */ 
/*     */   
/*     */   public String prefix() {
/*  66 */     return this._prefix;
/*     */   }
/*     */ 
/*     */   
/*     */   void prefix(String paramString) {
/*  71 */     if (paramString == null) {
/*  72 */       this._prefix = "";
/*     */     } else {
/*  74 */       this._prefix = paramString;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String name() {
/*  79 */     return this._name;
/*     */   }
/*     */ 
/*     */   
/*     */   void name(String paramString) {
/*  84 */     if (paramString == null) {
/*  85 */       this._name = "";
/*     */     } else {
/*  87 */       this._name = paramString;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String version() {
/*  92 */     return this._version;
/*     */   }
/*     */ 
/*     */   
/*     */   void version(String paramString) {
/*  97 */     if (paramString == null) {
/*  98 */       this._version = "";
/*     */     } else {
/* 100 */       this._version = paramString;
/*     */     } 
/*     */   }
/*     */   
/*     */   void appendToName(String paramString) {
/* 105 */     if (paramString != null)
/* 106 */       if (this._name.equals("")) {
/* 107 */         this._name = paramString;
/*     */       } else {
/* 109 */         this._name += '/' + paramString;
/*     */       }  
/*     */   }
/*     */   
/*     */   void replaceName(String paramString) {
/* 114 */     if (paramString == null) {
/* 115 */       this._name = "";
/*     */     } else {
/*     */       
/* 118 */       int i = this._name.lastIndexOf('/');
/* 119 */       if (i < 0) {
/* 120 */         this._name = paramString;
/*     */       } else {
/* 122 */         this._name = this._name.substring(0, i + 1) + paramString;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 128 */     return new IDLID(this._prefix, this._name, this._version);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\IDLID.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */