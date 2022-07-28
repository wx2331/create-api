/*     */ package com.sun.xml.internal.rngom.binary.visitor;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.binary.Pattern;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChildElementFinder
/*     */   extends PatternWalker
/*     */ {
/*  64 */   private final Set children = new HashSet();
/*     */ 
/*     */   
/*     */   public static class Element
/*     */   {
/*     */     public final NameClass nc;
/*     */     
/*     */     public final Pattern content;
/*     */     
/*     */     public Element(NameClass nc, Pattern content) {
/*  74 */       this.nc = nc;
/*  75 */       this.content = content;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/*  79 */       if (this == o) return true; 
/*  80 */       if (!(o instanceof Element)) return false;
/*     */       
/*  82 */       Element element = (Element)o;
/*     */       
/*  84 */       if ((this.content != null) ? !this.content.equals(element.content) : (element.content != null)) return false; 
/*  85 */       if ((this.nc != null) ? !this.nc.equals(element.nc) : (element.nc != null)) return false;
/*     */       
/*  87 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  92 */       int result = (this.nc != null) ? this.nc.hashCode() : 0;
/*  93 */       result = 29 * result + ((this.content != null) ? this.content.hashCode() : 0);
/*  94 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set getChildren() {
/* 102 */     return this.children;
/*     */   }
/*     */   
/*     */   public void visitElement(NameClass nc, Pattern content) {
/* 106 */     this.children.add(new Element(nc, content));
/*     */   }
/*     */   
/*     */   public void visitAttribute(NameClass ns, Pattern value) {}
/*     */   
/*     */   public void visitList(Pattern p) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\visitor\ChildElementFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */