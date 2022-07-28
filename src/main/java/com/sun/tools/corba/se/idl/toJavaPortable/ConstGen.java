/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.ConstEntry;
/*     */ import com.sun.tools.corba.se.idl.ConstGen;
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
/*     */ public class ConstGen
/*     */   implements ConstGen
/*     */ {
/*     */   public void generate(Hashtable paramHashtable, ConstEntry paramConstEntry, PrintWriter paramPrintWriter) {
/*  72 */     this.symbolTable = paramHashtable;
/*  73 */     this.c = paramConstEntry;
/*  74 */     this.stream = paramPrintWriter;
/*  75 */     init();
/*     */     
/*  77 */     if (paramConstEntry.container() instanceof com.sun.tools.corba.se.idl.ModuleEntry) {
/*  78 */       generateConst();
/*  79 */     } else if (this.stream != null) {
/*  80 */       writeConstExpr();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateConst() {
/*  95 */     openStream();
/*  96 */     if (this.stream == null)
/*     */       return; 
/*  98 */     writeHeading();
/*  99 */     writeBody();
/* 100 */     writeClosing();
/* 101 */     closeStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void openStream() {
/* 110 */     this.stream = (PrintWriter)Util.stream((SymtabEntry)this.c, ".java");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeHeading() {
/* 118 */     Util.writePackage(this.stream, (SymtabEntry)this.c);
/* 119 */     Util.writeProlog(this.stream, ((GenFileStream)this.stream).name());
/* 120 */     this.stream.println("public interface " + this.c.name());
/*     */ 
/*     */     
/* 123 */     this.stream.println("{");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeBody() {
/* 131 */     writeConstExpr();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeConstExpr() {
/* 139 */     if (this.c.comment() != null)
/* 140 */       this.c.comment().generate("  ", this.stream); 
/* 141 */     if (this.c.container() instanceof com.sun.tools.corba.se.idl.ModuleEntry) {
/*     */       
/* 143 */       this.stream.print("  public static final " + Util.javaName(this.c.type()) + " value = ");
/*     */     } else {
/* 145 */       this.stream.print("  public static final " + Util.javaName(this.c.type()) + ' ' + this.c.name() + " = ");
/*     */     } 
/* 147 */     writeConstValue(this.c.type());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeConstValue(SymtabEntry paramSymtabEntry) {
/* 155 */     if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.PrimitiveEntry) {
/* 156 */       this.stream.println('(' + Util.javaName(paramSymtabEntry) + ")(" + Util.parseExpression(this.c.value()) + ");");
/* 157 */     } else if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.StringEntry) {
/* 158 */       this.stream.println(Util.parseExpression(this.c.value()) + ';');
/* 159 */     } else if (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.TypedefEntry) {
/*     */       
/* 161 */       while (paramSymtabEntry instanceof com.sun.tools.corba.se.idl.TypedefEntry)
/* 162 */         paramSymtabEntry = paramSymtabEntry.type(); 
/* 163 */       writeConstValue(paramSymtabEntry);
/*     */     } else {
/*     */       
/* 166 */       this.stream.println(Util.parseExpression(this.c.value()) + ';');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClosing() {
/* 174 */     this.stream.println("}");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void closeStream() {
/* 182 */     this.stream.close();
/*     */   }
/*     */   
/* 185 */   protected Hashtable symbolTable = null;
/* 186 */   protected ConstEntry c = null;
/* 187 */   protected PrintWriter stream = null;
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\ConstGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */