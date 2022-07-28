/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public final class JBlock
/*     */   implements JGenerable, JStatement
/*     */ {
/*  47 */   private final List<Object> content = new ArrayList();
/*     */ 
/*     */   
/*     */   private boolean bracesRequired = true;
/*     */ 
/*     */   
/*     */   private boolean indentRequired = true;
/*     */ 
/*     */   
/*     */   private int pos;
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock() {
/*  61 */     this(true, true);
/*     */   }
/*     */   
/*     */   public JBlock(boolean bracesRequired, boolean indentRequired) {
/*  65 */     this.bracesRequired = bracesRequired;
/*  66 */     this.indentRequired = indentRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Object> getContents() {
/*  74 */     return Collections.unmodifiableList(this.content);
/*     */   }
/*     */   
/*     */   private <T> T insert(T statementOrDeclaration) {
/*  78 */     this.content.add(this.pos, statementOrDeclaration);
/*  79 */     this.pos++;
/*  80 */     return statementOrDeclaration;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int pos() {
/*  92 */     return this.pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int pos(int newPos) {
/* 106 */     int r = this.pos;
/* 107 */     if (newPos > this.content.size() || newPos < 0)
/* 108 */       throw new IllegalArgumentException(); 
/* 109 */     this.pos = newPos;
/*     */     
/* 111 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 119 */     return this.content.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar decl(JType type, String name) {
/* 134 */     return decl(0, type, name, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar decl(JType type, String name, JExpression init) {
/* 152 */     return decl(0, type, name, init);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JVar decl(int mods, JType type, String name, JExpression init) {
/* 173 */     JVar v = new JVar(JMods.forVar(mods), type, name, init);
/* 174 */     insert(v);
/* 175 */     this.bracesRequired = true;
/* 176 */     this.indentRequired = true;
/* 177 */     return v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock assign(JAssignmentTarget lhs, JExpression exp) {
/* 190 */     insert(new JAssignment(lhs, exp));
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   public JBlock assignPlus(JAssignmentTarget lhs, JExpression exp) {
/* 195 */     insert(new JAssignment(lhs, exp, "+"));
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation invoke(JExpression expr, String method) {
/* 212 */     JInvocation i = new JInvocation(expr, method);
/* 213 */     insert(i);
/* 214 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation invoke(JExpression expr, JMethod method) {
/* 230 */     return insert(new JInvocation(expr, method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation staticInvoke(JClass type, String method) {
/* 237 */     return insert(new JInvocation(type, method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation invoke(String method) {
/* 249 */     return insert(new JInvocation((JExpression)null, method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JInvocation invoke(JMethod method) {
/* 261 */     return insert(new JInvocation((JExpression)null, method));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock add(JStatement s) {
/* 273 */     insert(s);
/* 274 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JConditional _if(JExpression expr) {
/* 286 */     return insert(new JConditional(expr));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JForLoop _for() {
/* 295 */     return insert(new JForLoop());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JWhileLoop _while(JExpression test) {
/* 304 */     return insert(new JWhileLoop(test));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSwitch _switch(JExpression test) {
/* 311 */     return insert(new JSwitch(test));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JDoLoop _do(JExpression test) {
/* 320 */     return insert(new JDoLoop(test));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTryBlock _try() {
/* 329 */     return insert(new JTryBlock());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _return() {
/* 336 */     insert(new JReturn(null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _return(JExpression exp) {
/* 343 */     insert(new JReturn(exp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _throw(JExpression exp) {
/* 350 */     insert(new JThrow(exp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _break() {
/* 357 */     _break(null);
/*     */   }
/*     */   
/*     */   public void _break(JLabel label) {
/* 361 */     insert(new JBreak(label));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JLabel label(String name) {
/* 369 */     JLabel l = new JLabel(name);
/* 370 */     insert(l);
/* 371 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void _continue(JLabel label) {
/* 378 */     insert(new JContinue(label));
/*     */   }
/*     */   
/*     */   public void _continue() {
/* 382 */     _continue(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JBlock block() {
/* 389 */     JBlock b = new JBlock();
/* 390 */     b.bracesRequired = false;
/* 391 */     b.indentRequired = false;
/* 392 */     return insert(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JStatement directStatement(final String source) {
/* 407 */     JStatement s = new JStatement() {
/*     */         public void state(JFormatter f) {
/* 409 */           f.p(source).nl();
/*     */         }
/*     */       };
/* 412 */     add(s);
/* 413 */     return s;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 417 */     if (this.bracesRequired)
/* 418 */       f.p('{').nl(); 
/* 419 */     if (this.indentRequired)
/* 420 */       f.i(); 
/* 421 */     generateBody(f);
/* 422 */     if (this.indentRequired)
/* 423 */       f.o(); 
/* 424 */     if (this.bracesRequired)
/* 425 */       f.p('}'); 
/*     */   }
/*     */   
/*     */   void generateBody(JFormatter f) {
/* 429 */     for (Object o : this.content) {
/* 430 */       if (o instanceof JDeclaration) {
/* 431 */         f.d((JDeclaration)o); continue;
/*     */       } 
/* 433 */       f.s((JStatement)o);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JForEach forEach(JType varType, String name, JExpression collection) {
/* 445 */     return insert(new JForEach(varType, name, collection));
/*     */   }
/*     */   
/*     */   public void state(JFormatter f) {
/* 449 */     f.g(this);
/* 450 */     if (this.bracesRequired)
/* 451 */       f.nl(); 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */