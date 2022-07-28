/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.ForwardValueEntry;
/*     */ import com.sun.tools.corba.se.idl.ForwardValueGen;
/*     */ import com.sun.tools.corba.se.idl.GenFileStream;
/*     */ import com.sun.tools.corba.se.idl.SymtabEntry;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ForwardValueGen
/*     */   implements ForwardValueGen, JavaGenerator
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, ForwardValueEntry paramForwardValueEntry, PrintWriter paramPrintWriter) {
/*  72 */     this.symbolTable = paramHashtable;
/*  73 */     this.v = paramForwardValueEntry;
/*     */     
/*  75 */     openStream();
/*  76 */     if (this.stream == null)
/*     */       return; 
/*  78 */     generateHelper();
/*  79 */     generateHolder();
/*  80 */     generateStub();
/*  81 */     writeHeading();
/*  82 */     writeBody();
/*  83 */     writeClosing();
/*  84 */     closeStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openStream() {
/*  92 */     this.stream = (PrintWriter)Util.stream((SymtabEntry)this.v, ".java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHelper() {
/* 100 */     ((Factories)Compile.compiler.factories()).helper().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateHolder() {
/* 108 */     ((Factories)Compile.compiler.factories()).holder().generate(this.symbolTable, (SymtabEntry)this.v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateStub() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 123 */     Util.writePackage(this.stream, (SymtabEntry)this.v);
/* 124 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/*     */     
/* 126 */     if (this.v.comment() != null) {
/* 127 */       this.v.comment().generate("", this.stream);
/*     */     }
/* 129 */     this.stream.print("public class " + this.v.name() + " implements org.omg.CORBA.portable.IDLEntity");
/*     */ 
/*     */     
/* 132 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClosing() {
/* 147 */     this.stream.println("} // class " + this.v.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeStream() {
/* 155 */     this.stream.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int helperType(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 163 */     return paramInt;
/*     */   }
/*     */   
/*     */   public int type(int paramInt, String paramString1, TCOffsets paramTCOffsets, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 167 */     paramPrintWriter.println(paramString1 + paramString2 + " = " + Util.helperName(paramSymtabEntry, true) + ".type ();");
/* 168 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void helperRead(String paramString, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 173 */     paramPrintWriter.println("    " + paramString + " value = new " + paramString + " ();");
/* 174 */     read(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/* 175 */     paramPrintWriter.println("    return value;");
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 180 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void helperWrite(SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 185 */     write(0, "    ", "value", paramSymtabEntry, paramPrintWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(int paramInt, String paramString1, String paramString2, SymtabEntry paramSymtabEntry, PrintWriter paramPrintWriter) {
/* 190 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeAbstract() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 203 */   protected Hashtable symbolTable = null;
/* 204 */   protected ForwardValueEntry v = null;
/* 205 */   protected PrintWriter stream = null;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\ForwardValueGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */