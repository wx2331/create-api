/*     */ package com.sun.xml.internal.rngom.binary.visitor;
/*     */ 
/*     */ import com.sun.xml.internal.rngom.binary.Pattern;
/*     */ import com.sun.xml.internal.rngom.nc.NameClass;
/*     */ import org.relaxng.datatype.Datatype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternWalker
/*     */   implements PatternVisitor
/*     */ {
/*     */   public void visitEmpty() {}
/*     */   
/*     */   public void visitNotAllowed() {}
/*     */   
/*     */   public void visitError() {}
/*     */   
/*     */   public void visitGroup(Pattern p1, Pattern p2) {
/*  69 */     visitBinary(p1, p2);
/*     */   }
/*     */   
/*     */   protected void visitBinary(Pattern p1, Pattern p2) {
/*  73 */     p1.accept(this);
/*  74 */     p2.accept(this);
/*     */   }
/*     */   
/*     */   public void visitInterleave(Pattern p1, Pattern p2) {
/*  78 */     visitBinary(p1, p2);
/*     */   }
/*     */   
/*     */   public void visitChoice(Pattern p1, Pattern p2) {
/*  82 */     visitBinary(p1, p2);
/*     */   }
/*     */   
/*     */   public void visitOneOrMore(Pattern p) {
/*  86 */     p.accept(this);
/*     */   }
/*     */   
/*     */   public void visitElement(NameClass nc, Pattern content) {
/*  90 */     content.accept(this);
/*     */   }
/*     */   
/*     */   public void visitAttribute(NameClass ns, Pattern value) {
/*  94 */     value.accept(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitData(Datatype dt) {}
/*     */ 
/*     */   
/*     */   public void visitDataExcept(Datatype dt, Pattern except) {}
/*     */ 
/*     */   
/*     */   public void visitValue(Datatype dt, Object obj) {}
/*     */ 
/*     */   
/*     */   public void visitText() {}
/*     */   
/*     */   public void visitList(Pattern p) {
/* 110 */     p.accept(this);
/*     */   }
/*     */   
/*     */   public void visitAfter(Pattern p1, Pattern p2) {}
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\visitor\PatternWalker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */