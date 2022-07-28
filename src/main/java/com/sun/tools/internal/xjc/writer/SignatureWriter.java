/*     */ package com.sun.tools.internal.xjc.writer;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.FieldOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SignatureWriter
/*     */ {
/*     */   private final Collection<? extends ClassOutline> classes;
/*     */   private final Map<JDefinedClass, ClassOutline> classSet;
/*     */   private final Writer out;
/*     */   private int indent;
/*     */   
/*     */   public static void write(Outline model, Writer out) throws IOException {
/*  59 */     (new SignatureWriter(model, out)).dump();
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
/*     */   private SignatureWriter(Outline model, Writer out) {
/*  73 */     this.classSet = new HashMap<>();
/*     */ 
/*     */     
/*  76 */     this.indent = 0; this.out = out; this.classes = model.getClasses();
/*     */     for (ClassOutline ci : this.classes)
/*  78 */       this.classSet.put(ci.ref, ci);  } private void printIndent() throws IOException { for (int i = 0; i < this.indent; i++)
/*  79 */       this.out.write("  ");  }
/*     */   
/*     */   private void println(String s) throws IOException {
/*  82 */     printIndent();
/*  83 */     this.out.write(s);
/*  84 */     this.out.write(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dump() throws IOException {
/*  90 */     Set<JPackage> packages = new TreeSet<>(new Comparator<JPackage>() {
/*     */           public int compare(JPackage lhs, JPackage rhs) {
/*  92 */             return lhs.name().compareTo(rhs.name());
/*     */           }
/*     */         });
/*  95 */     for (ClassOutline ci : this.classes) {
/*  96 */       packages.add(ci._package()._package());
/*     */     }
/*  98 */     for (JPackage pkg : packages) {
/*  99 */       dump(pkg);
/*     */     }
/* 101 */     this.out.flush();
/*     */   }
/*     */   
/*     */   private void dump(JPackage pkg) throws IOException {
/* 105 */     println("package " + pkg.name() + " {");
/* 106 */     this.indent++;
/* 107 */     dumpChildren((JClassContainer)pkg);
/* 108 */     this.indent--;
/* 109 */     println("}");
/*     */   }
/*     */   
/*     */   private void dumpChildren(JClassContainer cont) throws IOException {
/* 113 */     Iterator<JDefinedClass> itr = cont.classes();
/* 114 */     while (itr.hasNext()) {
/* 115 */       JDefinedClass cls = itr.next();
/* 116 */       ClassOutline ci = this.classSet.get(cls);
/* 117 */       if (ci != null)
/* 118 */         dump(ci); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void dump(ClassOutline ci) throws IOException {
/* 123 */     JDefinedClass cls = ci.implClass;
/*     */     
/* 125 */     StringBuilder buf = new StringBuilder();
/* 126 */     buf.append("interface ");
/* 127 */     buf.append(cls.name());
/*     */     
/* 129 */     boolean first = true;
/* 130 */     Iterator<JClass> itr = cls._implements();
/* 131 */     while (itr.hasNext()) {
/* 132 */       if (first) {
/* 133 */         buf.append(" extends ");
/* 134 */         first = false;
/*     */       } else {
/* 136 */         buf.append(", ");
/*     */       } 
/* 138 */       buf.append(printName((JType)itr.next()));
/*     */     } 
/* 140 */     buf.append(" {");
/* 141 */     println(buf.toString());
/* 142 */     this.indent++;
/*     */ 
/*     */     
/* 145 */     for (FieldOutline fo : ci.getDeclaredFields()) {
/* 146 */       String type = printName(fo.getRawType());
/* 147 */       println(type + ' ' + fo.getPropertyInfo().getName(true) + ';');
/*     */     } 
/*     */     
/* 150 */     dumpChildren((JClassContainer)cls);
/*     */     
/* 152 */     this.indent--;
/* 153 */     println("}");
/*     */   }
/*     */ 
/*     */   
/*     */   private String printName(JType t) {
/* 158 */     String name = t.fullName();
/* 159 */     if (name.startsWith("java.lang."))
/* 160 */       name = name.substring(10); 
/* 161 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\writer\SignatureWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */