/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.Instruction;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.asm.SwitchData;
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
/*     */ 
/*     */ 
/*     */ public class SwitchStatement
/*     */   extends Statement
/*     */ {
/*     */   Expression expr;
/*     */   Statement[] args;
/*     */   
/*     */   public SwitchStatement(long paramLong, Expression paramExpression, Statement[] paramArrayOfStatement) {
/*  49 */     super(95, paramLong);
/*  50 */     this.expr = paramExpression;
/*  51 */     this.args = paramArrayOfStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  58 */     checkLabel(paramEnvironment, paramContext);
/*  59 */     CheckContext checkContext = new CheckContext(paramContext, this);
/*  60 */     paramVset = this.expr.checkValue(paramEnvironment, checkContext, reach(paramEnvironment, paramVset), paramHashtable);
/*  61 */     Type type = this.expr.type;
/*     */     
/*  63 */     this.expr = convert(paramEnvironment, checkContext, Type.tInt, this.expr);
/*     */     
/*  65 */     Hashtable<Object, Object> hashtable = new Hashtable<>();
/*  66 */     boolean bool = false;
/*     */ 
/*     */     
/*  69 */     Vset vset = DEAD_END;
/*     */     
/*  71 */     for (byte b = 0; b < this.args.length; b++) {
/*  72 */       Statement statement = this.args[b];
/*     */       
/*  74 */       if (statement.op == 96) {
/*     */         
/*  76 */         vset = statement.check(paramEnvironment, checkContext, vset.join(paramVset.copy()), paramHashtable);
/*     */         
/*  78 */         Expression expression = ((CaseStatement)statement).expr;
/*  79 */         if (expression != null) {
/*  80 */           if (expression instanceof IntegerExpression) {
/*     */             
/*  82 */             Integer integer = (Integer)((IntegerExpression)expression).getValue();
/*  83 */             int i = integer.intValue();
/*  84 */             if (hashtable.get(expression) != null) {
/*  85 */               paramEnvironment.error(statement.where, "duplicate.label", integer);
/*     */             } else {
/*  87 */               boolean bool1; hashtable.put(expression, statement);
/*     */               
/*  89 */               switch (type.getTypeCode()) {
/*     */                 case 1:
/*  91 */                   bool1 = (i != (byte)i) ? true : false; break;
/*     */                 case 3:
/*  93 */                   bool1 = (i != (short)i) ? true : false; break;
/*     */                 case 2:
/*  95 */                   bool1 = (i != (char)i) ? true : false; break;
/*     */                 default:
/*  97 */                   bool1 = false; break;
/*     */               } 
/*  99 */               if (bool1) {
/* 100 */                 paramEnvironment.error(statement.where, "switch.overflow", integer, type);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           }
/* 120 */           else if (!expression.isConstant() || expression
/* 121 */             .getType() != Type.tInt) {
/* 122 */             paramEnvironment.error(statement.where, "const.expr.required");
/*     */           } 
/*     */         } else {
/*     */           
/* 126 */           if (bool) {
/* 127 */             paramEnvironment.error(statement.where, "duplicate.default");
/*     */           }
/* 129 */           bool = true;
/*     */         } 
/*     */       } else {
/* 132 */         vset = statement.checkBlockStatement(paramEnvironment, checkContext, vset, paramHashtable);
/*     */       } 
/*     */     } 
/* 135 */     if (!vset.isDeadEnd()) {
/* 136 */       checkContext.vsBreak = checkContext.vsBreak.join(vset);
/*     */     }
/* 138 */     if (bool)
/* 139 */       paramVset = checkContext.vsBreak; 
/* 140 */     return paramContext.removeAdditionalVars(paramVset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/* 147 */     paramContext = new Context(paramContext, this);
/* 148 */     this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/* 149 */     for (byte b = 0; b < this.args.length; b++) {
/* 150 */       if (this.args[b] != null) {
/* 151 */         this.args[b] = this.args[b].inline(paramEnvironment, paramContext);
/*     */       }
/*     */     } 
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 161 */     SwitchStatement switchStatement = (SwitchStatement)clone();
/* 162 */     switchStatement.expr = this.expr.copyInline(paramContext);
/* 163 */     switchStatement.args = new Statement[this.args.length];
/* 164 */     for (byte b = 0; b < this.args.length; b++) {
/* 165 */       if (this.args[b] != null) {
/* 166 */         switchStatement.args[b] = this.args[b].copyInline(paramContext, paramBoolean);
/*     */       }
/*     */     } 
/* 169 */     return switchStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 176 */     int i = this.expr.costInline(paramInt, paramEnvironment, paramContext);
/* 177 */     for (byte b = 0; b < this.args.length && i < paramInt; b++) {
/* 178 */       if (this.args[b] != null) {
/* 179 */         i += this.args[b].costInline(paramInt, paramEnvironment, paramContext);
/*     */       }
/*     */     } 
/* 182 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 189 */     CodeContext codeContext = new CodeContext(paramContext, this);
/*     */     
/* 191 */     this.expr.codeValue(paramEnvironment, codeContext, paramAssembler);
/*     */     
/* 193 */     SwitchData switchData = new SwitchData();
/* 194 */     boolean bool = false;
/*     */     byte b;
/* 196 */     for (b = 0; b < this.args.length; b++) {
/* 197 */       Statement statement = this.args[b];
/* 198 */       if (statement != null && statement.op == 96) {
/* 199 */         Expression expression = ((CaseStatement)statement).expr;
/* 200 */         if (expression != null) {
/* 201 */           switchData.add(((IntegerExpression)expression).value, new Label());
/*     */         }
/*     */         else {
/*     */           
/* 205 */           bool = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 212 */     if (paramEnvironment.coverage()) {
/* 213 */       switchData.initTableCase();
/*     */     }
/* 215 */     paramAssembler.add(this.where, 170, switchData);
/*     */     
/* 217 */     for (b = 0; b < this.args.length; b++) {
/* 218 */       Statement statement = this.args[b];
/* 219 */       if (statement != null) {
/* 220 */         if (statement.op == 96) {
/* 221 */           Expression expression = ((CaseStatement)statement).expr;
/* 222 */           if (expression != null) {
/* 223 */             paramAssembler.add((Instruction)switchData.get(((IntegerExpression)expression).value));
/*     */             
/* 225 */             switchData.addTableCase(((IntegerExpression)expression).value, statement.where);
/*     */           } else {
/*     */             
/* 228 */             paramAssembler.add((Instruction)switchData.getDefaultLabel());
/*     */             
/* 230 */             switchData.addTableDefault(statement.where);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 235 */           statement.code(paramEnvironment, codeContext, paramAssembler);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 240 */     if (!bool) {
/* 241 */       paramAssembler.add((Instruction)switchData.getDefaultLabel());
/*     */     }
/* 243 */     paramAssembler.add((Instruction)codeContext.breakLabel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 250 */     super.print(paramPrintStream, paramInt);
/* 251 */     paramPrintStream.print("switch (");
/* 252 */     this.expr.print(paramPrintStream);
/* 253 */     paramPrintStream.print(") {\n");
/* 254 */     for (byte b = 0; b < this.args.length; b++) {
/* 255 */       if (this.args[b] != null) {
/* 256 */         printIndent(paramPrintStream, paramInt + 1);
/* 257 */         this.args[b].print(paramPrintStream, paramInt + 1);
/* 258 */         paramPrintStream.print("\n");
/*     */       } 
/*     */     } 
/* 261 */     printIndent(paramPrintStream, paramInt);
/* 262 */     paramPrintStream.print("}");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\SwitchStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */