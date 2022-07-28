/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import sun.rmi.rmic.BatchEnvironment;
/*     */ import sun.rmi.rmic.Generator;
/*     */ import sun.rmi.rmic.IndentingWriter;
/*     */ import sun.rmi.rmic.Main;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrintGenerator
/*     */   implements Generator, Constants
/*     */ {
/*     */   private static final int JAVA = 0;
/*     */   private static final int IDL = 1;
/*     */   private static final int BOTH = 2;
/*     */   private int whatToPrint;
/*     */   private boolean global = false;
/*     */   private boolean qualified = false;
/*     */   private boolean trace = false;
/*     */   private boolean valueMethods = false;
/*     */   private IndentingWriter out;
/*     */   
/*     */   public PrintGenerator() {
/*  66 */     OutputStreamWriter outputStreamWriter = new OutputStreamWriter(System.out);
/*  67 */     this.out = new IndentingWriter(outputStreamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain) {
/*  77 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  78 */       if (paramArrayOfString[b] != null) {
/*  79 */         String str = paramArrayOfString[b].toLowerCase();
/*  80 */         if (str.equals("-xprint")) {
/*  81 */           this.whatToPrint = 0;
/*  82 */           paramArrayOfString[b] = null;
/*  83 */           if (b + 1 < paramArrayOfString.length) {
/*  84 */             if (paramArrayOfString[b + 1].equalsIgnoreCase("idl")) {
/*  85 */               paramArrayOfString[++b] = null;
/*  86 */               this.whatToPrint = 1;
/*  87 */             } else if (paramArrayOfString[b + 1].equalsIgnoreCase("both")) {
/*  88 */               paramArrayOfString[++b] = null;
/*  89 */               this.whatToPrint = 2;
/*     */             } 
/*     */           }
/*  92 */         } else if (str.equals("-xglobal")) {
/*  93 */           this.global = true;
/*  94 */           paramArrayOfString[b] = null;
/*  95 */         } else if (str.equals("-xqualified")) {
/*  96 */           this.qualified = true;
/*  97 */           paramArrayOfString[b] = null;
/*  98 */         } else if (str.equals("-xtrace")) {
/*  99 */           this.trace = true;
/* 100 */           paramArrayOfString[b] = null;
/* 101 */         } else if (str.equals("-xvaluemethods")) {
/* 102 */           this.valueMethods = true;
/* 103 */           paramArrayOfString[b] = null;
/*     */         } 
/*     */       } 
/*     */     } 
/* 107 */     return true;
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
/*     */   public void generate(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile) {
/* 123 */     BatchEnvironment batchEnvironment = (BatchEnvironment)paramBatchEnvironment;
/* 124 */     ContextStack contextStack = new ContextStack(batchEnvironment);
/* 125 */     contextStack.setTrace(this.trace);
/*     */     
/* 127 */     if (this.valueMethods) {
/* 128 */       batchEnvironment.setParseNonConforming(true);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 133 */     CompoundType compoundType = CompoundType.forCompound(paramClassDefinition, contextStack);
/*     */     
/* 135 */     if (compoundType != null)
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 141 */         Type[] arrayOfType = compoundType.collectMatching(33554432);
/*     */         
/* 143 */         for (byte b = 0; b < arrayOfType.length; b++) {
/*     */           
/* 145 */           this.out.pln("\n-----------------------------------------------------------\n");
/*     */           
/* 147 */           Type type = arrayOfType[b];
/*     */           
/* 149 */           switch (this.whatToPrint) { case 0:
/* 150 */               type.println(this.out, this.qualified, false, false);
/*     */               break;
/*     */             case 1:
/* 153 */               type.println(this.out, this.qualified, true, this.global);
/*     */               break;
/*     */             case 2:
/* 156 */               type.println(this.out, this.qualified, false, false);
/* 157 */               type.println(this.out, this.qualified, true, this.global);
/*     */               break;
/*     */             default:
/* 160 */               throw new CompilerError("Unknown type!"); }
/*     */ 
/*     */         
/*     */         } 
/* 164 */         this.out.flush();
/*     */       }
/* 166 */       catch (IOException iOException) {
/* 167 */         throw new CompilerError("PrintGenerator caught " + iOException);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\PrintGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */