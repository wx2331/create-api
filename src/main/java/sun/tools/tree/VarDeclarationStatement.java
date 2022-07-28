/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.LocalVariable;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VarDeclarationStatement
/*     */   extends Statement
/*     */ {
/*     */   LocalMember field;
/*     */   Expression expr;
/*     */   
/*     */   public VarDeclarationStatement(long paramLong, Expression paramExpression) {
/*  48 */     super(108, paramLong);
/*  49 */     this.expr = paramExpression;
/*     */   }
/*     */   public VarDeclarationStatement(long paramLong, LocalMember paramLocalMember, Expression paramExpression) {
/*  52 */     super(108, paramLong);
/*  53 */     this.field = paramLocalMember;
/*  54 */     this.expr = paramExpression;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Vset checkDeclaration(Environment paramEnvironment, Context paramContext, Vset paramVset, int paramInt, Type paramType, Hashtable paramHashtable) {
/*  61 */     if (this.labels != null) {
/*  62 */       paramEnvironment.error(this.where, "declaration.with.label", this.labels[0]);
/*     */     }
/*  64 */     if (this.field != null) {
/*  65 */       if (paramContext.getLocalClass(this.field.getName()) != null && this.field
/*  66 */         .isInnerClass()) {
/*  67 */         paramEnvironment.error(this.where, "local.class.redefined", this.field.getName());
/*     */       }
/*     */       
/*  70 */       paramContext.declare(paramEnvironment, this.field);
/*  71 */       if (this.field.isInnerClass()) {
/*  72 */         ClassDefinition classDefinition = this.field.getInnerClass();
/*     */         try {
/*  74 */           paramVset = classDefinition.checkLocalClass(paramEnvironment, paramContext, paramVset, null, null, null);
/*     */         }
/*  76 */         catch (ClassNotFound classNotFound) {
/*  77 */           paramEnvironment.error(this.where, "class.not.found", classNotFound.name, opNames[this.op]);
/*     */         } 
/*  79 */         return paramVset;
/*     */       } 
/*  81 */       paramVset.addVar(this.field.number);
/*  82 */       return (this.expr != null) ? this.expr.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable) : paramVset;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     Expression expression = this.expr;
/*     */     
/*  93 */     if (expression.op == 1) {
/*  94 */       this.expr = ((AssignExpression)expression).right;
/*  95 */       expression = ((AssignExpression)expression).left;
/*     */     } else {
/*  97 */       this.expr = null;
/*     */     } 
/*     */     
/* 100 */     boolean bool = paramType.isType(13);
/* 101 */     while (expression.op == 48) {
/* 102 */       ArrayAccessExpression arrayAccessExpression = (ArrayAccessExpression)expression;
/* 103 */       if (arrayAccessExpression.index != null) {
/* 104 */         paramEnvironment.error(arrayAccessExpression.index.where, "array.dim.in.type");
/* 105 */         bool = true;
/*     */       } 
/* 107 */       expression = arrayAccessExpression.right;
/* 108 */       paramType = Type.tArray(paramType);
/*     */     } 
/* 110 */     if (expression.op == 60) {
/* 111 */       Identifier identifier = ((IdentifierExpression)expression).id;
/* 112 */       if (paramContext.getLocalField(identifier) != null) {
/* 113 */         paramEnvironment.error(this.where, "local.redefined", identifier);
/*     */       }
/*     */       
/* 116 */       this.field = new LocalMember(expression.where, paramContext.field.getClassDefinition(), paramInt, paramType, identifier);
/* 117 */       paramContext.declare(paramEnvironment, this.field);
/*     */       
/* 119 */       if (this.expr != null) {
/* 120 */         paramVset = this.expr.checkInitializer(paramEnvironment, paramContext, paramVset, paramType, paramHashtable);
/* 121 */         this.expr = convert(paramEnvironment, paramContext, paramType, this.expr);
/* 122 */         this.field.setValue(this.expr);
/* 123 */         if (this.field.isConstant())
/*     */         {
/*     */ 
/*     */           
/* 127 */           this.field.addModifiers(1048576);
/*     */         }
/* 129 */         paramVset.addVar(this.field.number);
/* 130 */       } else if (bool) {
/* 131 */         paramVset.addVar(this.field.number);
/*     */       } else {
/* 133 */         paramVset.addVarUnassigned(this.field.number);
/*     */       } 
/* 135 */       return paramVset;
/*     */     } 
/* 137 */     paramEnvironment.error(expression.where, "invalid.decl");
/* 138 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/* 145 */     if (this.field.isInnerClass()) {
/* 146 */       ClassDefinition classDefinition = this.field.getInnerClass();
/* 147 */       classDefinition.inlineLocalClass(paramEnvironment);
/* 148 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 153 */     if (paramEnvironment.opt() && !this.field.isUsed()) {
/* 154 */       return (new ExpressionStatement(this.where, this.expr)).inline(paramEnvironment, paramContext);
/*     */     }
/*     */     
/* 157 */     paramContext.declare(paramEnvironment, this.field);
/*     */     
/* 159 */     if (this.expr != null) {
/* 160 */       this.expr = this.expr.inlineValue(paramEnvironment, paramContext);
/* 161 */       this.field.setValue(this.expr);
/* 162 */       if (paramEnvironment.opt() && this.field.writecount == 0) {
/* 163 */         if (this.expr.op == 60) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 172 */           IdentifierExpression identifierExpression = (IdentifierExpression)this.expr;
/* 173 */           if (identifierExpression.field.isLocal() && (paramContext = paramContext.getInlineContext()) != null && ((LocalMember)identifierExpression.field).number < paramContext.varNumber) {
/*     */ 
/*     */             
/* 176 */             this.field.setValue(this.expr);
/* 177 */             this.field.addModifiers(1048576);
/*     */           } 
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 189 */         if (this.expr.isConstant() || this.expr.op == 82 || this.expr.op == 83) {
/*     */           
/* 191 */           this.field.setValue(this.expr);
/* 192 */           this.field.addModifiers(1048576);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 212 */     VarDeclarationStatement varDeclarationStatement = (VarDeclarationStatement)clone();
/* 213 */     if (this.expr != null) {
/* 214 */       varDeclarationStatement.expr = this.expr.copyInline(paramContext);
/*     */     }
/* 216 */     return varDeclarationStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 223 */     if (this.field != null && this.field.isInnerClass()) {
/* 224 */       return paramInt;
/*     */     }
/* 226 */     return (this.expr != null) ? this.expr.costInline(paramInt, paramEnvironment, paramContext) : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 233 */     if (this.expr != null && !this.expr.type.isType(11)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 247 */       paramContext.declare(paramEnvironment, this.field);
/* 248 */       this.expr.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */       
/* 250 */       paramAssembler.add(this.where, 54 + this.field.getType().getTypeCodeOffset(), new LocalVariable(this.field, this.field.number));
/*     */     } else {
/*     */       
/* 253 */       paramContext.declare(paramEnvironment, this.field);
/* 254 */       if (this.expr != null)
/*     */       {
/* 256 */         this.expr.code(paramEnvironment, paramContext, paramAssembler);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 265 */     paramPrintStream.print("local ");
/* 266 */     if (this.field != null) {
/* 267 */       paramPrintStream.print(this.field + "#" + this.field.hashCode());
/* 268 */       if (this.expr != null) {
/* 269 */         paramPrintStream.print(" = ");
/* 270 */         this.expr.print(paramPrintStream);
/*     */       } 
/*     */     } else {
/* 273 */       this.expr.print(paramPrintStream);
/* 274 */       paramPrintStream.print(";");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\VarDeclarationStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */