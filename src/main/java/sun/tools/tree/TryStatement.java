/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.CatchData;
/*     */ import sun.tools.asm.Instruction;
/*     */ import sun.tools.asm.TryData;
/*     */ import sun.tools.java.ClassDeclaration;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TryStatement
/*     */   extends Statement
/*     */ {
/*     */   Statement body;
/*     */   Statement[] args;
/*     */   long arrayCloneWhere;
/*     */   
/*     */   public TryStatement(long paramLong, Statement paramStatement, Statement[] paramArrayOfStatement) {
/*  52 */     super(101, paramLong);
/*  53 */     this.body = paramStatement;
/*  54 */     this.args = paramArrayOfStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  61 */     checkLabel(paramEnvironment, paramContext);
/*     */     try {
/*  63 */       paramVset = reach(paramEnvironment, paramVset);
/*  64 */       Hashtable<Object, Object> hashtable = new Hashtable<>();
/*  65 */       CheckContext checkContext = new CheckContext(paramContext, this);
/*     */ 
/*     */ 
/*     */       
/*  69 */       Vset vset1 = this.body.check(paramEnvironment, checkContext, paramVset.copy(), hashtable);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  77 */       Vset vset2 = Vset.firstDAandSecondDU(paramVset, vset1.copy().join(checkContext.vsTryExit));
/*     */       byte b1;
/*  79 */       for (b1 = 0; b1 < this.args.length; b1++)
/*     */       {
/*     */         
/*  82 */         vset1 = vset1.join(this.args[b1].check(paramEnvironment, checkContext, vset2.copy(), paramHashtable));
/*     */       }
/*     */ 
/*     */       
/*  86 */       for (b1 = 1; b1 < this.args.length; b1++) {
/*  87 */         CatchStatement catchStatement = (CatchStatement)this.args[b1];
/*  88 */         if (catchStatement.field != null) {
/*     */ 
/*     */           
/*  91 */           Type type = catchStatement.field.getType();
/*  92 */           ClassDefinition classDefinition = paramEnvironment.getClassDefinition(type);
/*     */           
/*  94 */           for (byte b = 0; b < b1; b++) {
/*  95 */             CatchStatement catchStatement1 = (CatchStatement)this.args[b];
/*  96 */             if (catchStatement1.field != null) {
/*     */ 
/*     */               
/*  99 */               Type type1 = catchStatement1.field.getType();
/* 100 */               ClassDeclaration classDeclaration = paramEnvironment.getClassDeclaration(type1);
/* 101 */               if (classDefinition.subClassOf(paramEnvironment, classDeclaration)) {
/* 102 */                 paramEnvironment.error((this.args[b1]).where, "catch.not.reached"); break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 108 */       ClassDeclaration classDeclaration1 = paramEnvironment.getClassDeclaration(idJavaLangError);
/* 109 */       ClassDeclaration classDeclaration2 = paramEnvironment.getClassDeclaration(idJavaLangRuntimeException);
/*     */ 
/*     */       
/* 112 */       for (byte b2 = 0; b2 < this.args.length; b2++) {
/* 113 */         CatchStatement catchStatement = (CatchStatement)this.args[b2];
/* 114 */         if (catchStatement.field != null) {
/*     */ 
/*     */           
/* 117 */           Type type = catchStatement.field.getType();
/* 118 */           if (type.isType(10)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 124 */             ClassDefinition classDefinition = paramEnvironment.getClassDefinition(type);
/*     */ 
/*     */             
/* 127 */             if (!classDefinition.subClassOf(paramEnvironment, classDeclaration1) && !classDefinition.superClassOf(paramEnvironment, classDeclaration1) && 
/* 128 */               !classDefinition.subClassOf(paramEnvironment, classDeclaration2) && !classDefinition.superClassOf(paramEnvironment, classDeclaration2)) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 133 */               boolean bool = false;
/* 134 */               for (Enumeration<ClassDeclaration> enumeration1 = hashtable.keys(); enumeration1.hasMoreElements(); ) {
/* 135 */                 ClassDeclaration classDeclaration = enumeration1.nextElement();
/* 136 */                 if (classDefinition.superClassOf(paramEnvironment, classDeclaration) || classDefinition.subClassOf(paramEnvironment, classDeclaration)) {
/* 137 */                   bool = true;
/*     */                   break;
/*     */                 } 
/*     */               } 
/* 141 */               if (!bool && this.arrayCloneWhere != 0L && classDefinition
/* 142 */                 .getName().toString().equals("java.lang.CloneNotSupportedException")) {
/* 143 */                 paramEnvironment.error(this.arrayCloneWhere, "warn.array.clone.supported", classDefinition.getName());
/*     */               }
/*     */               
/* 146 */               if (!bool)
/* 147 */                 paramEnvironment.error(catchStatement.where, "catch.not.thrown", classDefinition.getName()); 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 152 */       for (Enumeration<ClassDeclaration> enumeration = hashtable.keys(); enumeration.hasMoreElements(); ) {
/* 153 */         ClassDeclaration classDeclaration = enumeration.nextElement();
/* 154 */         ClassDefinition classDefinition = classDeclaration.getClassDefinition(paramEnvironment);
/* 155 */         boolean bool = true;
/* 156 */         for (byte b = 0; b < this.args.length; b++) {
/* 157 */           CatchStatement catchStatement = (CatchStatement)this.args[b];
/* 158 */           if (catchStatement.field != null) {
/*     */ 
/*     */             
/* 161 */             Type type = catchStatement.field.getType();
/* 162 */             if (!type.isType(13))
/*     */             {
/* 164 */               if (classDefinition.subClassOf(paramEnvironment, paramEnvironment.getClassDeclaration(type))) {
/* 165 */                 bool = false; break;
/*     */               }  } 
/*     */           } 
/*     */         } 
/* 169 */         if (bool) {
/* 170 */           paramHashtable.put(classDeclaration, hashtable.get(classDeclaration));
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 181 */       return paramContext.removeAdditionalVars(vset1.join(checkContext.vsBreak));
/* 182 */     } catch (ClassNotFound classNotFound) {
/* 183 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, opNames[this.op]);
/* 184 */       return paramVset;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/* 192 */     if (this.body != null) {
/* 193 */       this.body = this.body.inline(paramEnvironment, new Context(paramContext, this));
/*     */     }
/* 195 */     if (this.body == null) {
/* 196 */       return null;
/*     */     }
/* 198 */     for (byte b = 0; b < this.args.length; b++) {
/* 199 */       if (this.args[b] != null) {
/* 200 */         this.args[b] = this.args[b].inline(paramEnvironment, new Context(paramContext, this));
/*     */       }
/*     */     } 
/* 203 */     return (this.args.length == 0) ? eliminate(paramEnvironment, this.body) : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 210 */     TryStatement tryStatement = (TryStatement)clone();
/* 211 */     if (this.body != null) {
/* 212 */       tryStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/* 214 */     tryStatement.args = new Statement[this.args.length];
/* 215 */     for (byte b = 0; b < this.args.length; b++) {
/* 216 */       if (this.args[b] != null) {
/* 217 */         tryStatement.args[b] = this.args[b].copyInline(paramContext, paramBoolean);
/*     */       }
/*     */     } 
/* 220 */     return tryStatement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 265 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 272 */     CodeContext codeContext = new CodeContext(paramContext, this);
/*     */     
/* 274 */     TryData tryData = new TryData(); byte b;
/* 275 */     for (b = 0; b < this.args.length; b++) {
/* 276 */       Type type = ((CatchStatement)this.args[b]).field.getType();
/* 277 */       if (type.isType(10)) {
/* 278 */         tryData.add(paramEnvironment.getClassDeclaration(type));
/*     */       } else {
/* 280 */         tryData.add(type);
/*     */       } 
/*     */     } 
/* 283 */     paramAssembler.add(this.where, -3, tryData);
/* 284 */     if (this.body != null) {
/* 285 */       this.body.code(paramEnvironment, codeContext, paramAssembler);
/*     */     }
/*     */     
/* 288 */     paramAssembler.add((Instruction)tryData.getEndLabel());
/* 289 */     paramAssembler.add(this.where, 167, codeContext.breakLabel);
/*     */     
/* 291 */     for (b = 0; b < this.args.length; b++) {
/* 292 */       CatchData catchData = tryData.getCatch(b);
/* 293 */       paramAssembler.add((Instruction)catchData.getLabel());
/* 294 */       this.args[b].code(paramEnvironment, codeContext, paramAssembler);
/* 295 */       paramAssembler.add(this.where, 167, codeContext.breakLabel);
/*     */     } 
/*     */     
/* 298 */     paramAssembler.add((Instruction)codeContext.breakLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 305 */     super.print(paramPrintStream, paramInt);
/* 306 */     paramPrintStream.print("try ");
/* 307 */     if (this.body != null) {
/* 308 */       this.body.print(paramPrintStream, paramInt);
/*     */     } else {
/* 310 */       paramPrintStream.print("<empty>");
/*     */     } 
/* 312 */     for (byte b = 0; b < this.args.length; b++) {
/* 313 */       paramPrintStream.print(" ");
/* 314 */       this.args[b].print(paramPrintStream, paramInt);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\TryStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */