/*     */ package com.sun.xml.internal.rngom.digested;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.ast.om.Location;
/*     */ import com.sun.xml.internal.rngom.parse.Context;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DDataPattern
/*     */   extends DPattern
/*     */ {
/*     */   DPattern except;
/*     */   String datatypeLibrary;
/*     */   String type;
/*  63 */   final List<Param> params = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public final class Param
/*     */   {
/*     */     String name;
/*     */     
/*     */     String value;
/*     */     Context context;
/*     */     String ns;
/*     */     Location loc;
/*     */     Annotation anno;
/*     */     
/*     */     public Param(String name, String value, Context context, String ns, Location loc, Annotation anno) {
/*  77 */       this.name = name;
/*  78 */       this.value = value;
/*  79 */       this.context = context;
/*  80 */       this.ns = ns;
/*  81 */       this.loc = loc;
/*  82 */       this.anno = anno;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  86 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getValue() {
/*  90 */       return this.value;
/*     */     }
/*     */     
/*     */     public Context getContext() {
/*  94 */       return this.context;
/*     */     }
/*     */     
/*     */     public String getNs() {
/*  98 */       return this.ns;
/*     */     }
/*     */     
/*     */     public Location getLoc() {
/* 102 */       return this.loc;
/*     */     }
/*     */     
/*     */     public Annotation getAnno() {
/* 106 */       return this.anno;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDatatypeLibrary() {
/* 117 */     return this.datatypeLibrary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/* 127 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Param> getParams() {
/* 137 */     return this.params;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DPattern getExcept() {
/* 146 */     return this.except;
/*     */   }
/*     */   
/*     */   public boolean isNullable() {
/* 150 */     return false;
/*     */   }
/*     */   
/*     */   public Object accept(DPatternVisitor visitor) {
/* 154 */     return visitor.onData(this);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\digested\DDataPattern.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */