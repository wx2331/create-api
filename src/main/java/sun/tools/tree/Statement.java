/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
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
/*     */ public class Statement
/*     */   extends Node
/*     */ {
/*  41 */   public static final Vset DEAD_END = Vset.DEAD_END;
/*  42 */   Identifier[] labels = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Statement(int paramInt, long paramLong) {
/*  48 */     super(paramInt, paramLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   public static final Statement empty = new Statement(105, 0L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static final int MAXINLINECOST = Integer.getInteger("javac.maxinlinecost", 30)
/*  61 */     .intValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Statement insertStatement(Statement paramStatement1, Statement paramStatement2) {
/*  68 */     if (paramStatement2 == null) {
/*  69 */       paramStatement2 = paramStatement1;
/*  70 */     } else if (paramStatement2 instanceof CompoundStatement) {
/*     */       
/*  72 */       ((CompoundStatement)paramStatement2).insertStatement(paramStatement1);
/*     */     } else {
/*  74 */       Statement[] arrayOfStatement = { paramStatement1, paramStatement2 };
/*  75 */       paramStatement2 = new CompoundStatement(paramStatement1.getWhere(), arrayOfStatement);
/*     */     } 
/*  77 */     return paramStatement2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLabel(Environment paramEnvironment, Expression paramExpression) {
/*  84 */     if (paramExpression.op == 60) {
/*  85 */       if (this.labels == null) {
/*  86 */         this.labels = new Identifier[1];
/*     */       }
/*     */       else {
/*     */         
/*  90 */         Identifier[] arrayOfIdentifier = new Identifier[this.labels.length + 1];
/*  91 */         System.arraycopy(this.labels, 0, arrayOfIdentifier, 1, this.labels.length);
/*  92 */         this.labels = arrayOfIdentifier;
/*     */       } 
/*  94 */       this.labels[0] = ((IdentifierExpression)paramExpression).id;
/*     */     } else {
/*  96 */       paramEnvironment.error(paramExpression.where, "invalid.label");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkMethod(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 105 */     CheckContext checkContext = new CheckContext(paramContext, new Statement(47, 0L));
/* 106 */     paramContext = checkContext;
/*     */     
/* 108 */     paramVset = check(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */ 
/*     */     
/* 111 */     if (!paramContext.field.getType().getReturnType().isType(11))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 123 */       if (!paramVset.isDeadEnd()) {
/* 124 */         paramEnvironment.error(paramContext.field.getWhere(), "return.required.at.end", paramContext.field);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/* 129 */     paramVset = paramVset.join(checkContext.vsBreak);
/*     */     
/* 131 */     return paramVset;
/*     */   }
/*     */   Vset checkDeclaration(Environment paramEnvironment, Context paramContext, Vset paramVset, int paramInt, Type paramType, Hashtable paramHashtable) {
/* 134 */     throw new CompilerError("checkDeclaration");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkLabel(Environment paramEnvironment, Context paramContext) {
/* 143 */     if (this.labels != null) {
/* 144 */       byte b; label20: for (b = 0; b < this.labels.length; b++) {
/*     */         
/* 146 */         for (int i = b + 1; i < this.labels.length; i++) {
/* 147 */           if (this.labels[b] == this.labels[i]) {
/* 148 */             paramEnvironment.error(this.where, "nested.duplicate.label", this.labels[b]);
/*     */ 
/*     */             
/*     */             continue label20;
/*     */           } 
/*     */         } 
/*     */         
/* 155 */         CheckContext checkContext = (CheckContext)paramContext.getLabelContext(this.labels[b]);
/*     */         
/* 157 */         if (checkContext != null)
/*     */         {
/* 159 */           if (checkContext.frameNumber == paramContext.frameNumber) {
/* 160 */             paramEnvironment.error(this.where, "nested.duplicate.label", this.labels[b]);
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 168 */     throw new CompilerError("check");
/*     */   }
/*     */ 
/*     */   
/*     */   Vset checkBlockStatement(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 173 */     return check(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */   
/*     */   Vset reach(Environment paramEnvironment, Vset paramVset) {
/* 177 */     if (paramVset.isDeadEnd()) {
/* 178 */       paramEnvironment.error(this.where, "stat.not.reached");
/* 179 */       paramVset = paramVset.clearDeadEnd();
/*     */     } 
/* 181 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement eliminate(Environment paramEnvironment, Statement paramStatement) {
/* 195 */     if (paramStatement != null && this.labels != null) {
/* 196 */       Statement[] arrayOfStatement = { paramStatement };
/* 197 */       paramStatement = new CompoundStatement(this.where, arrayOfStatement);
/* 198 */       paramStatement.labels = this.labels;
/*     */     } 
/* 200 */     return paramStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 208 */     throw new CompilerError("code");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeFinally(Environment paramEnvironment, Context paramContext1, Assembler paramAssembler, Context paramContext2, Type paramType) {
/* 219 */     Integer integer = null;
/* 220 */     boolean bool1 = false;
/* 221 */     boolean bool2 = false;
/*     */     Context context;
/* 223 */     for (context = paramContext1; context != null && context != paramContext2; context = context.prev) {
/* 224 */       if (context.node != null)
/*     */       {
/* 226 */         if (context.node.op == 126) {
/* 227 */           bool1 = true;
/* 228 */         } else if (context.node.op == 103 && ((CodeContext)context).contLabel != null) {
/*     */ 
/*     */           
/* 231 */           bool1 = true;
/* 232 */           FinallyStatement finallyStatement = (FinallyStatement)context.node;
/* 233 */           if (!finallyStatement.finallyCanFinish) {
/* 234 */             bool2 = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 241 */     if (!bool1) {
/*     */       return;
/*     */     }
/*     */     
/* 245 */     if (paramType != null) {
/*     */       
/* 247 */       ClassDefinition classDefinition = paramContext1.field.getClassDefinition();
/* 248 */       if (!bool2) {
/*     */ 
/*     */         
/* 251 */         LocalMember localMember = paramContext1.getLocalField(idFinallyReturnValue);
/* 252 */         integer = new Integer(localMember.number);
/* 253 */         paramAssembler.add(this.where, 54 + paramType.getTypeCodeOffset(), integer);
/*     */       } else {
/*     */         
/* 256 */         switch (paramContext1.field.getType().getReturnType().getTypeCode()) { case 11:
/*     */             break;
/*     */           case 5:
/*     */           case 7:
/* 260 */             paramAssembler.add(this.where, 88); break;
/*     */           default:
/* 262 */             paramAssembler.add(this.where, 87);
/*     */             break; }
/*     */       
/*     */       } 
/*     */     } 
/* 267 */     for (context = paramContext1; context != null && context != paramContext2; context = context.prev) {
/* 268 */       if (context.node != null)
/*     */       {
/* 270 */         if (context.node.op == 126) {
/* 271 */           paramAssembler.add(this.where, 168, ((CodeContext)context).contLabel);
/* 272 */         } else if (context.node.op == 103 && ((CodeContext)context).contLabel != null) {
/*     */           
/* 274 */           FinallyStatement finallyStatement = (FinallyStatement)context.node;
/* 275 */           Label label = ((CodeContext)context).contLabel;
/* 276 */           if (finallyStatement.finallyCanFinish) {
/* 277 */             paramAssembler.add(this.where, 168, label);
/*     */           } else {
/*     */             
/* 280 */             paramAssembler.add(this.where, 167, label);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/* 286 */     if (integer != null) {
/* 287 */       paramAssembler.add(this.where, 21 + paramType.getTypeCodeOffset(), integer);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLabel(Identifier paramIdentifier) {
/* 295 */     Identifier[] arrayOfIdentifier = this.labels;
/* 296 */     if (arrayOfIdentifier != null) {
/* 297 */       for (int i = arrayOfIdentifier.length; --i >= 0;) {
/* 298 */         if (arrayOfIdentifier[i].equals(paramIdentifier)) {
/* 299 */           return true;
/*     */         }
/*     */       } 
/*     */     }
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression firstConstructor() {
/* 310 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 317 */     return (Statement)clone();
/*     */   }
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 321 */     return paramInt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void printIndent(PrintStream paramPrintStream, int paramInt) {
/* 329 */     for (byte b = 0; b < paramInt; b++)
/* 330 */       paramPrintStream.print("    "); 
/*     */   }
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 334 */     if (this.labels != null)
/* 335 */       for (int i = this.labels.length; --i >= 0;)
/* 336 */         paramPrintStream.print(this.labels[i] + ": ");  
/*     */   }
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 340 */     print(paramPrintStream, 0);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\Statement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */