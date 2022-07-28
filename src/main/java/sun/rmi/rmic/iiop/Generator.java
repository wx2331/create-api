/*     */ package sun.rmi.rmic.iiop;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.HashSet;
/*     */ import sun.rmi.rmic.BatchEnvironment;
/*     */ import sun.rmi.rmic.Generator;
/*     */ import sun.rmi.rmic.IndentingWriter;
/*     */ import sun.rmi.rmic.Main;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassFile;
/*     */ import sun.tools.java.ClassPath;
/*     */ import sun.tools.java.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Generator
/*     */   implements Generator, Constants
/*     */ {
/*     */   protected boolean alwaysGenerate = false;
/*  74 */   protected BatchEnvironment env = null;
/*  75 */   protected ContextStack contextStack = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean trace = false;
/*     */ 
/*     */   
/*     */   protected boolean idl = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean parseArgs(String[] paramArrayOfString, Main paramMain) {
/*  87 */     for (byte b = 0; b < paramArrayOfString.length; b++) {
/*  88 */       if (paramArrayOfString[b] != null) {
/*  89 */         if (paramArrayOfString[b].equalsIgnoreCase("-always") || paramArrayOfString[b]
/*  90 */           .equalsIgnoreCase("-alwaysGenerate")) {
/*  91 */           this.alwaysGenerate = true;
/*  92 */           paramArrayOfString[b] = null;
/*  93 */         } else if (paramArrayOfString[b].equalsIgnoreCase("-xtrace")) {
/*  94 */           this.trace = true;
/*  95 */           paramArrayOfString[b] = null;
/*     */         } 
/*     */       }
/*     */     } 
/*  99 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean parseNonConforming(ContextStack paramContextStack);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract CompoundType getTopType(ClassDefinition paramClassDefinition, ContextStack paramContextStack);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract OutputType[] getOutputTypesFor(CompoundType paramCompoundType, HashSet paramHashSet);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract String getFileNameExtensionFor(OutputType paramOutputType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void writeOutputFor(OutputType paramOutputType, HashSet paramHashSet, IndentingWriter paramIndentingWriter) throws IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean requireNewInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresGeneration(File paramFile, Type paramType) {
/* 160 */     boolean bool = this.alwaysGenerate;
/*     */     
/* 162 */     if (!bool) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 169 */       ClassPath classPath = this.env.getClassPath();
/* 170 */       String str = paramType.getQualifiedName().replace('.', File.separatorChar);
/*     */ 
/*     */ 
/*     */       
/* 174 */       ClassFile classFile = classPath.getFile(str + ".source");
/*     */       
/* 176 */       if (classFile == null)
/*     */       {
/*     */ 
/*     */         
/* 180 */         classFile = classPath.getFile(str + ".class");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 185 */       if (classFile != null) {
/*     */ 
/*     */ 
/*     */         
/* 189 */         long l = classFile.lastModified();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 195 */         String str1 = IDLNames.replace(paramFile.getName(), ".java", ".class");
/* 196 */         String str2 = paramFile.getParent();
/* 197 */         File file = new File(str2, str1);
/*     */ 
/*     */ 
/*     */         
/* 201 */         if (file.exists())
/*     */         {
/*     */ 
/*     */           
/* 205 */           long l1 = file.lastModified();
/*     */ 
/*     */ 
/*     */           
/* 209 */           bool = (l1 < l);
/*     */         
/*     */         }
/*     */         else
/*     */         {
/*     */           
/* 215 */           bool = true;
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 221 */         bool = true;
/*     */       } 
/*     */     } 
/*     */     
/* 225 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Generator newInstance() {
/* 234 */     Generator generator = null;
/*     */     try {
/* 236 */       generator = (Generator)getClass().newInstance();
/*     */     }
/* 238 */     catch (Exception exception) {}
/*     */     
/* 240 */     return generator;
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
/*     */   
/*     */   public void generate(BatchEnvironment paramBatchEnvironment, ClassDefinition paramClassDefinition, File paramFile) {
/* 262 */     this.env = (BatchEnvironment)paramBatchEnvironment;
/* 263 */     this.contextStack = new ContextStack(this.env);
/* 264 */     this.contextStack.setTrace(this.trace);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     this.env.setParseNonConforming(parseNonConforming(this.contextStack));
/*     */ 
/*     */ 
/*     */     
/* 274 */     CompoundType compoundType = getTopType(paramClassDefinition, this.contextStack);
/* 275 */     if (compoundType != null) {
/*     */       
/* 277 */       Generator generator = this;
/*     */ 
/*     */ 
/*     */       
/* 281 */       if (requireNewInstance())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 286 */         generator = newInstance();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 291 */       generator.generateOutputFiles(compoundType, this.env, paramFile);
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void generateOutputFiles(CompoundType paramCompoundType, BatchEnvironment paramBatchEnvironment, File paramFile) {
/* 306 */     HashSet hashSet = paramBatchEnvironment.alreadyChecked;
/*     */ 
/*     */ 
/*     */     
/* 310 */     OutputType[] arrayOfOutputType = getOutputTypesFor(paramCompoundType, hashSet);
/*     */ 
/*     */ 
/*     */     
/* 314 */     for (byte b = 0; b < arrayOfOutputType.length; b++) {
/* 315 */       OutputType outputType = arrayOfOutputType[b];
/* 316 */       String str = outputType.getName();
/* 317 */       File file = getFileFor(outputType, paramFile);
/* 318 */       boolean bool = false;
/*     */ 
/*     */ 
/*     */       
/* 322 */       if (requiresGeneration(file, outputType.getType())) {
/*     */ 
/*     */ 
/*     */         
/* 326 */         if (file.getName().endsWith(".java")) {
/* 327 */           bool = compileJavaSourceFile(outputType);
/*     */ 
/*     */ 
/*     */           
/* 331 */           if (bool) {
/* 332 */             paramBatchEnvironment.addGeneratedFile(file);
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 339 */           IndentingWriter indentingWriter = new IndentingWriter(new OutputStreamWriter(new FileOutputStream(file)), 4, 2147483647);
/*     */ 
/*     */           
/* 342 */           long l = 0L;
/* 343 */           if (paramBatchEnvironment.verbose()) {
/* 344 */             l = System.currentTimeMillis();
/*     */           }
/*     */           
/* 347 */           writeOutputFor(arrayOfOutputType[b], hashSet, indentingWriter);
/* 348 */           indentingWriter.close();
/*     */           
/* 350 */           if (paramBatchEnvironment.verbose()) {
/* 351 */             long l1 = System.currentTimeMillis() - l;
/* 352 */             paramBatchEnvironment.output(Main.getText("rmic.generated", file.getPath(), Long.toString(l1)));
/*     */           } 
/* 354 */           if (bool) {
/* 355 */             paramBatchEnvironment.parseFile(new ClassFile(file));
/*     */           }
/* 357 */         } catch (IOException iOException) {
/* 358 */           paramBatchEnvironment.error(0L, "cant.write", file.toString());
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           return;
/*     */         } 
/* 365 */       } else if (paramBatchEnvironment.verbose()) {
/* 366 */         paramBatchEnvironment.output(Main.getText("rmic.previously.generated", file.getPath()));
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getFileFor(OutputType paramOutputType, File paramFile) {
/* 386 */     Identifier identifier = getOutputId(paramOutputType);
/* 387 */     File file = null;
/* 388 */     if (this.idl) {
/* 389 */       file = Util.getOutputDirectoryForIDL(identifier, paramFile, this.env);
/*     */     } else {
/* 391 */       file = Util.getOutputDirectoryForStub(identifier, paramFile, this.env);
/*     */     } 
/* 393 */     String str = paramOutputType.getName() + getFileNameExtensionFor(paramOutputType);
/* 394 */     return new File(file, str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Identifier getOutputId(OutputType paramOutputType) {
/* 403 */     return paramOutputType.getType().getIdentifier();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean compileJavaSourceFile(OutputType paramOutputType) {
/* 412 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public class OutputType
/*     */   {
/*     */     private String name;
/*     */     
/*     */     private Type type;
/*     */ 
/*     */     
/*     */     public OutputType(String param1String, Type param1Type) {
/* 424 */       this.name = param1String;
/* 425 */       this.type = param1Type;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 429 */       return this.name;
/*     */     }
/*     */     
/*     */     public Type getType() {
/* 433 */       return this.type;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\iiop\Generator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */