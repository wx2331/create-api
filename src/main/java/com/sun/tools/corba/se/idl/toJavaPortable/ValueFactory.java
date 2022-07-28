/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.MethodEntry;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import com.sun.tools.corba.se.idl.ValueEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
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
/*     */ public class ValueFactory
/*     */   implements AuxGen
/*     */ {
/*     */   protected Hashtable symbolTable;
/*     */   protected SymtabEntry entry;
/*     */   protected GenFileStream stream;
/*     */   protected String factoryClass;
/*     */   protected String factoryType;
/*     */   
/*     */   public void generate(Hashtable paramHashtable, SymtabEntry paramSymtabEntry) {
/*  76 */     this.symbolTable = paramHashtable;
/*  77 */     this.entry = paramSymtabEntry;
/*  78 */     init();
/*  79 */     if (hasFactoryMethods()) {
/*  80 */       openStream();
/*  81 */       if (this.stream == null)
/*     */         return; 
/*  83 */       writeHeading();
/*  84 */       writeBody();
/*  85 */       writeClosing();
/*  86 */       closeStream();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/*  95 */     this.factoryClass = this.entry.name() + "ValueFactory";
/*  96 */     this.factoryType = Util.javaName(this.entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasFactoryMethods() {
/* 104 */     Vector vector = ((ValueEntry)this.entry).initializers();
/* 105 */     if (vector != null && vector.size() > 0) {
/* 106 */       return true;
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openStream() {
/* 116 */     this.stream = Util.stream(this.entry, "ValueFactory.java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 125 */     Util.writePackage((PrintWriter)this.stream, this.entry, (short)0);
/* 126 */     Util.writeProlog((PrintWriter)this.stream, this.stream.name());
/* 127 */     if (this.entry.comment() != null)
/* 128 */       this.entry.comment().generate("", (PrintWriter)this.stream); 
/* 129 */     this.stream.println("public interface " + this.factoryClass + " extends org.omg.CORBA.portable.ValueFactory");
/* 130 */     this.stream.println('{');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody() {
/* 138 */     Vector<MethodEntry> vector = ((ValueEntry)this.entry).initializers();
/* 139 */     if (vector != null)
/*     */     {
/* 141 */       for (byte b = 0; b < vector.size(); b++) {
/*     */         
/* 143 */         MethodEntry methodEntry = vector.elementAt(b);
/* 144 */         methodEntry.valueMethod(true);
/* 145 */         ((MethodGen)methodEntry.generator()).interfaceMethod(this.symbolTable, methodEntry, (PrintWriter)this.stream);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClosing() {
/* 155 */     this.stream.println('}');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeStream() {
/* 163 */     this.stream.close();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\ValueFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */