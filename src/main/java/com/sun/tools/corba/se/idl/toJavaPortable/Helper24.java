/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Helper24
/*     */   extends Helper
/*     */ {
/*     */   protected void writeHeading() {
/*  77 */     Util.writePackage((PrintWriter)this.stream, this.entry, (short)2);
/*  78 */     Util.writeProlog((PrintWriter)this.stream, this.stream.name());
/*     */ 
/*     */     
/*  81 */     if (this.entry.comment() != null) {
/*  82 */       this.entry.comment().generate("", (PrintWriter)this.stream);
/*     */     }
/*  84 */     if (this.entry instanceof com.sun.tools.corba.se.idl.ValueBoxEntry) {
/*  85 */       this.stream.print("public final class " + this.helperClass);
/*  86 */       this.stream.println(" implements org.omg.CORBA.portable.BoxedValueHelper");
/*     */     } else {
/*     */       
/*  89 */       this.stream.println("abstract public class " + this.helperClass);
/*  90 */     }  this.stream.println('{');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInstVars() {
/* 100 */     this.stream.println("  private static String  _id = \"" + Util.stripLeadingUnderscoresFromID(this.entry.repositoryID().ID()) + "\";");
/* 101 */     if (this.entry instanceof ValueEntry) {
/*     */       
/* 103 */       this.stream.println();
/* 104 */       if (this.entry instanceof com.sun.tools.corba.se.idl.ValueBoxEntry) {
/* 105 */         this.stream.println("  private static " + this.helperClass + " _instance = new " + this.helperClass + " ();");
/* 106 */         this.stream.println();
/*     */       } 
/*     */     } 
/* 109 */     this.stream.println();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeValueHelperInterface() {
/* 120 */     if (this.entry instanceof com.sun.tools.corba.se.idl.ValueBoxEntry) {
/* 121 */       writeGetID();
/* 122 */     } else if (this.entry instanceof ValueEntry) {
/* 123 */       writeHelperFactories();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHelperFactories() {
/* 132 */     Vector<MethodEntry> vector = ((ValueEntry)this.entry).initializers();
/* 133 */     if (vector != null) {
/*     */       
/* 135 */       this.stream.println();
/* 136 */       for (byte b = 0; b < vector.size(); b++) {
/*     */         
/* 138 */         MethodEntry methodEntry = vector.elementAt(b);
/* 139 */         methodEntry.valueMethod(true);
/* 140 */         ((MethodGen24)methodEntry.generator()).helperFactoryMethod(this.symbolTable, methodEntry, this.entry, (PrintWriter)this.stream);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeCtors() {
/* 151 */     if (this.entry instanceof com.sun.tools.corba.se.idl.ValueBoxEntry) {
/* 152 */       this.stream.println("  public " + this.helperClass + "()");
/* 153 */       this.stream.println("  {");
/* 154 */       this.stream.println("  }");
/* 155 */       this.stream.println();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\Helper24.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */