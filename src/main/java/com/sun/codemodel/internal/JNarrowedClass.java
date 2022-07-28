/*     */ package com.sun.codemodel.internal;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ class JNarrowedClass
/*     */   extends JClass
/*     */ {
/*     */   final JClass basis;
/*     */   private final List<JClass> args;
/*     */   
/*     */   JNarrowedClass(JClass basis, JClass arg) {
/*  53 */     this(basis, Collections.singletonList(arg));
/*     */   }
/*     */   
/*     */   JNarrowedClass(JClass basis, List<JClass> args) {
/*  57 */     super(basis.owner());
/*  58 */     this.basis = basis;
/*  59 */     assert !(basis instanceof JNarrowedClass);
/*  60 */     this.args = args;
/*     */   }
/*     */ 
/*     */   
/*     */   public JClass narrow(JClass clazz) {
/*  65 */     List<JClass> newArgs = new ArrayList<>(this.args);
/*  66 */     newArgs.add(clazz);
/*  67 */     return new JNarrowedClass(this.basis, newArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   public JClass narrow(JClass... clazz) {
/*  72 */     List<JClass> newArgs = new ArrayList<>(this.args);
/*  73 */     newArgs.addAll(Arrays.asList(clazz));
/*  74 */     return new JNarrowedClass(this.basis, newArgs);
/*     */   }
/*     */   
/*     */   public String name() {
/*  78 */     StringBuilder buf = new StringBuilder();
/*  79 */     buf.append(this.basis.name());
/*  80 */     buf.append('<');
/*  81 */     boolean first = true;
/*  82 */     for (JClass c : this.args) {
/*  83 */       if (first) {
/*  84 */         first = false;
/*     */       } else {
/*  86 */         buf.append(',');
/*  87 */       }  buf.append(c.name());
/*     */     } 
/*  89 */     buf.append('>');
/*  90 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String fullName() {
/*  94 */     StringBuilder buf = new StringBuilder();
/*  95 */     buf.append(this.basis.fullName());
/*  96 */     buf.append('<');
/*  97 */     boolean first = true;
/*  98 */     for (JClass c : this.args) {
/*  99 */       if (first) {
/* 100 */         first = false;
/*     */       } else {
/* 102 */         buf.append(',');
/* 103 */       }  buf.append(c.fullName());
/*     */     } 
/* 105 */     buf.append('>');
/* 106 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String binaryName() {
/* 111 */     StringBuilder buf = new StringBuilder();
/* 112 */     buf.append(this.basis.binaryName());
/* 113 */     buf.append('<');
/* 114 */     boolean first = true;
/* 115 */     for (JClass c : this.args) {
/* 116 */       if (first) {
/* 117 */         first = false;
/*     */       } else {
/* 119 */         buf.append(',');
/* 120 */       }  buf.append(c.binaryName());
/*     */     } 
/* 122 */     buf.append('>');
/* 123 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void generate(JFormatter f) {
/* 128 */     f.t(this.basis).p('<').g((Collection)this.args).p('￿');
/*     */   }
/*     */ 
/*     */   
/*     */   void printLink(JFormatter f) {
/* 133 */     this.basis.printLink(f);
/* 134 */     f.p("{@code <}");
/* 135 */     boolean first = true;
/* 136 */     for (JClass c : this.args) {
/* 137 */       if (first) {
/* 138 */         first = false;
/*     */       } else {
/* 140 */         f.p(',');
/* 141 */       }  c.printLink(f);
/*     */     } 
/* 143 */     f.p("{@code >}");
/*     */   }
/*     */   
/*     */   public JPackage _package() {
/* 147 */     return this.basis._package();
/*     */   }
/*     */   
/*     */   public JClass _extends() {
/* 151 */     JClass base = this.basis._extends();
/* 152 */     if (base == null) return base; 
/* 153 */     return base.substituteParams(this.basis.typeParams(), this.args);
/*     */   }
/*     */   
/*     */   public Iterator<JClass> _implements() {
/* 157 */     return new Iterator<JClass>() {
/* 158 */         private final Iterator<JClass> core = JNarrowedClass.this.basis._implements();
/*     */         public void remove() {
/* 160 */           this.core.remove();
/*     */         }
/*     */         public JClass next() {
/* 163 */           return ((JClass)this.core.next()).substituteParams(JNarrowedClass.this.basis.typeParams(), JNarrowedClass.this.args);
/*     */         }
/*     */         public boolean hasNext() {
/* 166 */           return this.core.hasNext();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public JClass erasure() {
/* 173 */     return this.basis;
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/* 177 */     return this.basis.isInterface();
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 181 */     return this.basis.isAbstract();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 186 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 196 */     if (!(obj instanceof JNarrowedClass)) return false; 
/* 197 */     return fullName().equals(((JClass)obj).fullName());
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 202 */     return fullName().hashCode();
/*     */   }
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/*     */     int j;
/* 206 */     JClass b = this.basis.substituteParams(variables, bindings);
/* 207 */     boolean different = (b != this.basis);
/*     */     
/* 209 */     List<JClass> clazz = new ArrayList<>(this.args.size());
/* 210 */     for (int i = 0; i < clazz.size(); i++) {
/* 211 */       JClass c = ((JClass)this.args.get(i)).substituteParams(variables, bindings);
/* 212 */       clazz.set(i, c);
/* 213 */       j = different | ((c != this.args.get(i)) ? 1 : 0);
/*     */     } 
/*     */     
/* 216 */     if (j != 0) {
/* 217 */       return new JNarrowedClass(b, clazz);
/*     */     }
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<JClass> getTypeParameters() {
/* 224 */     return this.args;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\codemodel\internal\JNarrowedClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */