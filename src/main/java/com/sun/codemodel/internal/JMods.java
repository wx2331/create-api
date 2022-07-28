/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JMods
/*     */   implements JGenerable
/*     */ {
/*  39 */   private static int VAR = 8;
/*  40 */   private static int FIELD = 799;
/*     */ 
/*     */   
/*  43 */   private static int METHOD = 255;
/*     */   
/*  45 */   private static int CLASS = 63;
/*     */   
/*  47 */   private static int INTERFACE = 1;
/*     */   
/*     */   private int mods;
/*     */   
/*     */   private JMods(int mods) {
/*  52 */     this.mods = mods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getValue() {
/*  59 */     return this.mods;
/*     */   }
/*     */   
/*     */   private static void check(int mods, int legal, String what) {
/*  63 */     if ((mods & (legal ^ 0xFFFFFFFF)) != 0) {
/*  64 */       throw new IllegalArgumentException("Illegal modifiers for " + what + ": " + (new JMods(mods))
/*     */           
/*  66 */           .toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static JMods forVar(int mods) {
/*  72 */     check(mods, VAR, "variable");
/*  73 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   static JMods forField(int mods) {
/*  77 */     check(mods, FIELD, "field");
/*  78 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   static JMods forMethod(int mods) {
/*  82 */     check(mods, METHOD, "method");
/*  83 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   static JMods forClass(int mods) {
/*  87 */     check(mods, CLASS, "class");
/*  88 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   static JMods forInterface(int mods) {
/*  92 */     check(mods, INTERFACE, "class");
/*  93 */     return new JMods(mods);
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  97 */     return ((this.mods & 0x20) != 0);
/*     */   }
/*     */   
/*     */   public boolean isNative() {
/* 101 */     return ((this.mods & 0x40) != 0);
/*     */   }
/*     */   
/*     */   public boolean isSynchronized() {
/* 105 */     return ((this.mods & 0x80) != 0);
/*     */   }
/*     */   
/*     */   public void setSynchronized(boolean newValue) {
/* 109 */     setFlag(128, newValue);
/*     */   }
/*     */   
/*     */   public void setPrivate() {
/* 113 */     setFlag(1, false);
/* 114 */     setFlag(2, false);
/* 115 */     setFlag(4, true);
/*     */   }
/*     */   
/*     */   public void setProtected() {
/* 119 */     setFlag(1, false);
/* 120 */     setFlag(2, true);
/* 121 */     setFlag(4, false);
/*     */   }
/*     */   
/*     */   public void setPublic() {
/* 125 */     setFlag(1, true);
/* 126 */     setFlag(2, false);
/* 127 */     setFlag(4, false);
/*     */   }
/*     */   
/*     */   public void setFinal(boolean newValue) {
/* 131 */     setFlag(8, newValue);
/*     */   }
/*     */   
/*     */   private void setFlag(int bit, boolean newValue) {
/* 135 */     this.mods = this.mods & (bit ^ 0xFFFFFFFF) | (newValue ? bit : 0);
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 139 */     if ((this.mods & 0x1) != 0) f.p("public"); 
/* 140 */     if ((this.mods & 0x2) != 0) f.p("protected"); 
/* 141 */     if ((this.mods & 0x4) != 0) f.p("private"); 
/* 142 */     if ((this.mods & 0x8) != 0) f.p("final"); 
/* 143 */     if ((this.mods & 0x10) != 0) f.p("static"); 
/* 144 */     if ((this.mods & 0x20) != 0) f.p("abstract"); 
/* 145 */     if ((this.mods & 0x40) != 0) f.p("native"); 
/* 146 */     if ((this.mods & 0x80) != 0) f.p("synchronized"); 
/* 147 */     if ((this.mods & 0x100) != 0) f.p("transient"); 
/* 148 */     if ((this.mods & 0x200) != 0) f.p("volatile");
/*     */   
/*     */   }
/*     */   
/*     */   public String toString() {
/* 153 */     StringWriter s = new StringWriter();
/* 154 */     JFormatter f = new JFormatter(new PrintWriter(s));
/* 155 */     generate(f);
/* 156 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JMods.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */