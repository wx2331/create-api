/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.CompilerError;
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
/*     */ 
/*     */ public class ArrayExpression
/*     */   extends NaryExpression
/*     */ {
/*     */   public ArrayExpression(long paramLong, Expression[] paramArrayOfExpression) {
/*  44 */     super(57, paramLong, Type.tError, (Expression)null, paramArrayOfExpression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  51 */     paramEnvironment.error(this.where, "invalid.array.expr");
/*  52 */     return paramVset;
/*     */   }
/*     */   public Vset checkInitializer(Environment paramEnvironment, Context paramContext, Vset paramVset, Type paramType, Hashtable paramHashtable) {
/*  55 */     if (!paramType.isType(9)) {
/*  56 */       if (!paramType.isType(13)) {
/*  57 */         paramEnvironment.error(this.where, "invalid.array.init", paramType);
/*     */       }
/*  59 */       return paramVset;
/*     */     } 
/*  61 */     this.type = paramType;
/*  62 */     paramType = paramType.getElementType();
/*  63 */     for (byte b = 0; b < this.args.length; b++) {
/*  64 */       paramVset = this.args[b].checkInitializer(paramEnvironment, paramContext, paramVset, paramType, paramHashtable);
/*  65 */       this.args[b] = convert(paramEnvironment, paramContext, paramType, this.args[b]);
/*     */     } 
/*  67 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/*  74 */     Expression expression = null;
/*  75 */     for (byte b = 0; b < this.args.length; b++) {
/*  76 */       this.args[b] = this.args[b].inline(paramEnvironment, paramContext);
/*  77 */       if (this.args[b] != null) {
/*  78 */         expression = (expression == null) ? this.args[b] : new CommaExpression(this.where, expression, this.args[b]);
/*     */       }
/*     */     } 
/*  81 */     return expression;
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  84 */     for (byte b = 0; b < this.args.length; b++) {
/*  85 */       this.args[b] = this.args[b].inlineValue(paramEnvironment, paramContext);
/*     */     }
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/*  94 */     boolean bool = false;
/*  95 */     paramAssembler.add(this.where, 18, new Integer(this.args.length));
/*  96 */     switch (this.type.getElementType().getTypeCode()) { case 0:
/*  97 */         paramAssembler.add(this.where, 188, new Integer(4)); break;
/*  98 */       case 1: paramAssembler.add(this.where, 188, new Integer(8)); break;
/*  99 */       case 3: paramAssembler.add(this.where, 188, new Integer(9)); break;
/* 100 */       case 2: paramAssembler.add(this.where, 188, new Integer(5)); break;
/* 101 */       case 4: paramAssembler.add(this.where, 188, new Integer(10)); break;
/* 102 */       case 5: paramAssembler.add(this.where, 188, new Integer(11)); break;
/* 103 */       case 6: paramAssembler.add(this.where, 188, new Integer(6)); break;
/* 104 */       case 7: paramAssembler.add(this.where, 188, new Integer(7));
/*     */         break;
/*     */       case 9:
/* 107 */         paramAssembler.add(this.where, 189, this.type.getElementType());
/*     */         break;
/*     */       
/*     */       case 10:
/* 111 */         paramAssembler.add(this.where, 189, paramEnvironment.getClassDeclaration(this.type.getElementType()));
/*     */         break;
/*     */       
/*     */       default:
/* 115 */         throw new CompilerError("codeValue"); }
/*     */ 
/*     */     
/* 118 */     for (byte b = 0; b < this.args.length; b++) {
/*     */ 
/*     */ 
/*     */       
/* 122 */       if (!this.args[b].equalsDefault()) {
/*     */         
/* 124 */         paramAssembler.add(this.where, 89);
/* 125 */         paramAssembler.add(this.where, 18, new Integer(b));
/* 126 */         this.args[b].codeValue(paramEnvironment, paramContext, paramAssembler);
/* 127 */         switch (this.type.getElementType().getTypeCode()) {
/*     */           case 0:
/*     */           case 1:
/* 130 */             paramAssembler.add(this.where, 84);
/*     */             break;
/*     */           case 2:
/* 133 */             paramAssembler.add(this.where, 85);
/*     */             break;
/*     */           case 3:
/* 136 */             paramAssembler.add(this.where, 86);
/*     */             break;
/*     */           default:
/* 139 */             paramAssembler.add(this.where, 79 + this.type.getElementType().getTypeCodeOffset());
/*     */             break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\ArrayExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */