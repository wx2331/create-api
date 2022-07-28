/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.Arguments;
/*     */ import com.sun.tools.corba.se.idl.Factories;
/*     */ import com.sun.tools.corba.se.idl.GenFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Factories
/*     */   extends Factories
/*     */ {
/*     */   public GenFactory genFactory() {
/*  48 */     return new GenFactory();
/*     */   }
/*     */ 
/*     */   
/*     */   public Arguments arguments() {
/*  53 */     return new Arguments();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] languageKeywords() {
/*  59 */     return keywords;
/*     */   }
/*     */   
/*  62 */   static String[] keywords = new String[] { "abstract", "break", "byte", "catch", "class", "continue", "do", "else", "extends", "false", "final", "finally", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "native", "new", "null", "operator", "outer", "package", "private", "protected", "public", "return", "static", "super", "synchronized", "this", "throw", "throws", "transient", "true", "try", "volatile", "while", "+Helper", "+Holder", "+Package", "clone", "equals", "finalize", "getClass", "hashCode", "notify", "notifyAll", "toString", "wait" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   private Helper _helper = null;
/*     */   
/*     */   public Helper helper() {
/*  93 */     if (this._helper == null)
/*  94 */       if (Util.corbaLevel(2.4F, 99.0F)) {
/*  95 */         this._helper = new Helper24();
/*     */       } else {
/*  97 */         this._helper = new Helper();
/*  98 */       }   return this._helper;
/*     */   }
/*     */   
/* 101 */   private ValueFactory _valueFactory = null;
/*     */   
/*     */   public ValueFactory valueFactory() {
/* 104 */     if (this._valueFactory == null && 
/* 105 */       Util.corbaLevel(2.4F, 99.0F)) {
/* 106 */       this._valueFactory = new ValueFactory();
/*     */     }
/* 108 */     return this._valueFactory;
/*     */   }
/*     */   
/* 111 */   private DefaultFactory _defaultFactory = null;
/*     */   
/*     */   public DefaultFactory defaultFactory() {
/* 114 */     if (this._defaultFactory == null && 
/* 115 */       Util.corbaLevel(2.4F, 99.0F)) {
/* 116 */       this._defaultFactory = new DefaultFactory();
/*     */     }
/* 118 */     return this._defaultFactory;
/*     */   }
/*     */   
/* 121 */   private Holder _holder = new Holder();
/*     */   
/*     */   public Holder holder() {
/* 124 */     return this._holder;
/*     */   }
/*     */   
/* 127 */   private Skeleton _skeleton = new Skeleton();
/*     */   
/*     */   public Skeleton skeleton() {
/* 130 */     return this._skeleton;
/*     */   }
/*     */   
/* 133 */   private Stub _stub = new Stub();
/*     */   
/*     */   public Stub stub() {
/* 136 */     return this._stub;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\Factories.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */