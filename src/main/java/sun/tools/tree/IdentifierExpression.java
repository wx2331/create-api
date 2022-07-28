/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.LocalVariable;
/*     */ import sun.tools.java.AmbiguousMember;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.ClassNotFound;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.IdentifierToken;
/*     */ import sun.tools.java.MemberDefinition;
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
/*     */ public class IdentifierExpression
/*     */   extends Expression
/*     */ {
/*     */   Identifier id;
/*     */   MemberDefinition field;
/*     */   Expression implementation;
/*     */   
/*     */   public IdentifierExpression(long paramLong, Identifier paramIdentifier) {
/*  49 */     super(60, paramLong, Type.tError);
/*  50 */     this.id = paramIdentifier;
/*     */   }
/*     */   public IdentifierExpression(IdentifierToken paramIdentifierToken) {
/*  53 */     this(paramIdentifierToken.getWhere(), paramIdentifierToken.getName());
/*     */   }
/*     */   public IdentifierExpression(long paramLong, MemberDefinition paramMemberDefinition) {
/*  56 */     super(60, paramLong, paramMemberDefinition.getType());
/*  57 */     this.id = paramMemberDefinition.getName();
/*  58 */     this.field = paramMemberDefinition;
/*     */   }
/*     */   
/*     */   public Expression getImplementation() {
/*  62 */     if (this.implementation != null)
/*  63 */       return this.implementation; 
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Identifier paramIdentifier) {
/*  71 */     return this.id.equals(paramIdentifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Vset assign(Environment paramEnvironment, Context paramContext, Vset paramVset) {
/*  79 */     if (this.field.isLocal()) {
/*  80 */       LocalMember localMember = (LocalMember)this.field;
/*  81 */       if (localMember.scopeNumber < paramContext.frameNumber) {
/*  82 */         paramEnvironment.error(this.where, "assign.to.uplevel", this.id);
/*     */       }
/*  84 */       if (localMember.isFinal())
/*     */       {
/*  86 */         if (!localMember.isBlankFinal()) {
/*  87 */           paramEnvironment.error(this.where, "assign.to.final", this.id);
/*  88 */         } else if (!paramVset.testVarUnassigned(localMember.number)) {
/*  89 */           paramEnvironment.error(this.where, "assign.to.blank.final", this.id);
/*     */         } 
/*     */       }
/*  92 */       paramVset.addVar(localMember.number);
/*  93 */       localMember.writecount++;
/*  94 */     } else if (this.field.isFinal()) {
/*  95 */       paramVset = FieldExpression.checkFinalAssign(paramEnvironment, paramContext, paramVset, this.where, this.field);
/*     */     } 
/*     */     
/*  98 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Vset get(Environment paramEnvironment, Context paramContext, Vset paramVset) {
/* 105 */     if (this.field.isLocal()) {
/* 106 */       LocalMember localMember = (LocalMember)this.field;
/* 107 */       if (localMember.scopeNumber < paramContext.frameNumber && !localMember.isFinal()) {
/* 108 */         paramEnvironment.error(this.where, "invalid.uplevel", this.id);
/*     */       }
/* 110 */       if (!paramVset.testVar(localMember.number)) {
/* 111 */         paramEnvironment.error(this.where, "var.not.initialized", this.id);
/* 112 */         paramVset.addVar(localMember.number);
/*     */       } 
/* 114 */       localMember.readcount++;
/*     */     } else {
/* 116 */       if (!this.field.isStatic() && 
/* 117 */         !paramVset.testVar(paramContext.getThisNumber())) {
/* 118 */         paramEnvironment.error(this.where, "access.inst.before.super", this.id);
/* 119 */         this.implementation = null;
/*     */       } 
/*     */       
/* 122 */       if (this.field.isBlankFinal()) {
/* 123 */         int i = paramContext.getFieldNumber(this.field);
/* 124 */         if (i >= 0 && !paramVset.testVar(i)) {
/* 125 */           paramEnvironment.error(this.where, "var.not.initialized", this.id);
/*     */         }
/*     */       } 
/*     */     } 
/* 129 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean bind(Environment paramEnvironment, Context paramContext) {
/*     */     try {
/* 137 */       this.field = paramContext.getField(paramEnvironment, this.id);
/* 138 */       if (this.field == null) {
/* 139 */         ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/* 140 */         for (; classDefinition != null; classDefinition = classDefinition.getOuterClass()) {
/* 141 */           if (classDefinition.findAnyMethod(paramEnvironment, this.id) != null) {
/* 142 */             paramEnvironment.error(this.where, "invalid.var", this.id, paramContext.field
/* 143 */                 .getClassDeclaration());
/* 144 */             return false;
/*     */           } 
/*     */         } 
/* 147 */         paramEnvironment.error(this.where, "undef.var", this.id);
/* 148 */         return false;
/*     */       } 
/*     */       
/* 151 */       this.type = this.field.getType();
/*     */ 
/*     */       
/* 154 */       if (!paramContext.field.getClassDefinition().canAccess(paramEnvironment, this.field)) {
/* 155 */         paramEnvironment.error(this.where, "no.field.access", this.id, this.field
/* 156 */             .getClassDeclaration(), paramContext.field
/* 157 */             .getClassDeclaration());
/* 158 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 162 */       if (this.field.isLocal()) {
/* 163 */         LocalMember localMember = (LocalMember)this.field;
/* 164 */         if (localMember.scopeNumber < paramContext.frameNumber)
/*     */         {
/* 166 */           this.implementation = paramContext.makeReference(paramEnvironment, localMember);
/*     */         }
/*     */       } else {
/* 169 */         MemberDefinition memberDefinition = this.field;
/*     */         
/* 171 */         if (memberDefinition.reportDeprecated(paramEnvironment)) {
/* 172 */           paramEnvironment.error(this.where, "warn.field.is.deprecated", this.id, memberDefinition
/* 173 */               .getClassDefinition());
/*     */         }
/*     */         
/* 176 */         ClassDefinition classDefinition = memberDefinition.getClassDefinition();
/* 177 */         if (classDefinition != paramContext.field.getClassDefinition()) {
/*     */           
/* 179 */           MemberDefinition memberDefinition1 = paramContext.getApparentField(paramEnvironment, this.id);
/* 180 */           if (memberDefinition1 != null && memberDefinition1 != memberDefinition) {
/* 181 */             ClassDefinition classDefinition1 = paramContext.findScope(paramEnvironment, classDefinition);
/* 182 */             if (classDefinition1 == null) classDefinition1 = memberDefinition.getClassDefinition(); 
/* 183 */             if (memberDefinition1.isLocal()) {
/* 184 */               paramEnvironment.error(this.where, "inherited.hides.local", this.id, classDefinition1
/* 185 */                   .getClassDeclaration());
/*     */             } else {
/* 187 */               paramEnvironment.error(this.where, "inherited.hides.field", this.id, classDefinition1
/* 188 */                   .getClassDeclaration(), memberDefinition1
/* 189 */                   .getClassDeclaration());
/*     */             } 
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
/* 202 */         if (memberDefinition.isStatic()) {
/*     */           
/* 204 */           TypeExpression typeExpression = new TypeExpression(this.where, memberDefinition.getClassDeclaration().getType());
/* 205 */           this.implementation = new FieldExpression(this.where, null, memberDefinition);
/*     */         } else {
/* 207 */           Expression expression = paramContext.findOuterLink(paramEnvironment, this.where, memberDefinition);
/* 208 */           if (expression != null) {
/* 209 */             this.implementation = new FieldExpression(this.where, expression, memberDefinition);
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 215 */       if (!paramContext.canReach(paramEnvironment, this.field)) {
/* 216 */         paramEnvironment.error(this.where, "forward.ref", this.id, this.field
/* 217 */             .getClassDeclaration());
/* 218 */         return false;
/*     */       } 
/* 220 */       return true;
/* 221 */     } catch (ClassNotFound classNotFound) {
/* 222 */       paramEnvironment.error(this.where, "class.not.found", classNotFound.name, paramContext.field);
/* 223 */     } catch (AmbiguousMember ambiguousMember) {
/* 224 */       paramEnvironment.error(this.where, "ambig.field", this.id, ambiguousMember.field1
/* 225 */           .getClassDeclaration(), ambiguousMember.field2
/* 226 */           .getClassDeclaration());
/*     */     } 
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 235 */     if (this.field != null)
/*     */     {
/*     */       
/* 238 */       return paramVset;
/*     */     }
/* 240 */     if (bind(paramEnvironment, paramContext)) {
/* 241 */       paramVset = get(paramEnvironment, paramContext, paramVset);
/* 242 */       paramContext.field.getClassDefinition().addDependency(this.field.getClassDeclaration());
/* 243 */       if (this.implementation != null)
/* 244 */         paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); 
/*     */     } 
/* 246 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkLHS(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 254 */     if (!bind(paramEnvironment, paramContext))
/* 255 */       return paramVset; 
/* 256 */     paramVset = assign(paramEnvironment, paramContext, paramVset);
/* 257 */     if (this.implementation != null)
/* 258 */       paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); 
/* 259 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkAssignOp(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, Expression paramExpression) {
/* 267 */     if (!bind(paramEnvironment, paramContext))
/* 268 */       return paramVset; 
/* 269 */     paramVset = assign(paramEnvironment, paramContext, get(paramEnvironment, paramContext, paramVset));
/* 270 */     if (this.implementation != null)
/* 271 */       paramVset = this.implementation.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable); 
/* 272 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUpdater getAssigner(Environment paramEnvironment, Context paramContext) {
/* 279 */     if (this.implementation != null)
/* 280 */       return this.implementation.getAssigner(paramEnvironment, paramContext); 
/* 281 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FieldUpdater getUpdater(Environment paramEnvironment, Context paramContext) {
/* 288 */     if (this.implementation != null)
/* 289 */       return this.implementation.getUpdater(paramEnvironment, paramContext); 
/* 290 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkAmbigName(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable, UnaryExpression paramUnaryExpression) {
/*     */     
/* 299 */     try { if (paramContext.getField(paramEnvironment, this.id) != null)
/*     */       {
/* 301 */         return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */       } }
/* 303 */     catch (ClassNotFound classNotFound) {  }
/* 304 */     catch (AmbiguousMember ambiguousMember) {}
/*     */ 
/*     */     
/* 307 */     ClassDefinition classDefinition = toResolvedType(paramEnvironment, paramContext, true);
/*     */     
/* 309 */     if (classDefinition != null) {
/* 310 */       paramUnaryExpression.right = new TypeExpression(this.where, classDefinition.getType());
/* 311 */       return paramVset;
/*     */     } 
/*     */     
/* 314 */     this.type = Type.tPackage;
/* 315 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassDefinition toResolvedType(Environment paramEnvironment, Context paramContext, boolean paramBoolean) {
/* 323 */     Identifier identifier = paramContext.resolveName(paramEnvironment, this.id);
/* 324 */     Type type = Type.tClass(identifier);
/* 325 */     if (paramBoolean && !paramEnvironment.classExists(type)) {
/* 326 */       return null;
/*     */     }
/* 328 */     if (paramEnvironment.resolve(this.where, paramContext.field.getClassDefinition(), type)) {
/*     */       try {
/* 330 */         ClassDefinition classDefinition = paramEnvironment.getClassDefinition(type);
/*     */ 
/*     */         
/* 333 */         if (classDefinition.isMember()) {
/* 334 */           ClassDefinition classDefinition1 = paramContext.findScope(paramEnvironment, classDefinition.getOuterClass());
/* 335 */           if (classDefinition1 != classDefinition.getOuterClass()) {
/* 336 */             Identifier identifier1 = paramContext.getApparentClassName(paramEnvironment, this.id);
/* 337 */             if (!identifier1.equals(idNull) && !identifier1.equals(identifier)) {
/* 338 */               paramEnvironment.error(this.where, "inherited.hides.type", this.id, classDefinition1
/* 339 */                   .getClassDeclaration());
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 344 */         if (!classDefinition.getLocalName().equals(this.id.getFlatName().getName())) {
/* 345 */           paramEnvironment.error(this.where, "illegal.mangled.name", this.id, classDefinition);
/*     */         }
/*     */         
/* 348 */         return classDefinition;
/* 349 */       } catch (ClassNotFound classNotFound) {}
/*     */     }
/*     */     
/* 352 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Type toType(Environment paramEnvironment, Context paramContext) {
/* 360 */     ClassDefinition classDefinition = toResolvedType(paramEnvironment, paramContext, false);
/* 361 */     if (classDefinition != null) {
/* 362 */       return classDefinition.getType();
/*     */     }
/* 364 */     return Type.tError;
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
/*     */   public boolean isConstant() {
/* 391 */     if (this.implementation != null)
/* 392 */       return this.implementation.isConstant(); 
/* 393 */     if (this.field != null) {
/* 394 */       return this.field.isConstant();
/*     */     }
/* 396 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/* 403 */     return null;
/*     */   }
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 406 */     if (this.implementation != null)
/* 407 */       return this.implementation.inlineValue(paramEnvironment, paramContext); 
/* 408 */     if (this.field == null) {
/* 409 */       return this;
/*     */     }
/*     */     try {
/* 412 */       if (this.field.isLocal()) {
/* 413 */         if (this.field.isInlineable(paramEnvironment, false)) {
/* 414 */           Expression expression = (Expression)this.field.getValue(paramEnvironment);
/* 415 */           return (expression == null) ? this : expression.inlineValue(paramEnvironment, paramContext);
/*     */         } 
/* 417 */         return this;
/*     */       } 
/* 419 */       return this;
/* 420 */     } catch (ClassNotFound classNotFound) {
/* 421 */       throw new CompilerError(classNotFound);
/*     */     } 
/*     */   }
/*     */   public Expression inlineLHS(Environment paramEnvironment, Context paramContext) {
/* 425 */     if (this.implementation != null)
/* 426 */       return this.implementation.inlineLHS(paramEnvironment, paramContext); 
/* 427 */     return this;
/*     */   }
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 431 */     if (this.implementation != null) {
/* 432 */       return this.implementation.copyInline(paramContext);
/*     */     }
/* 434 */     IdentifierExpression identifierExpression = (IdentifierExpression)super.copyInline(paramContext);
/* 435 */     if (this.field != null && this.field.isLocal()) {
/* 436 */       identifierExpression.field = ((LocalMember)this.field).getCurrentInlineCopy(paramContext);
/*     */     }
/* 438 */     return identifierExpression;
/*     */   }
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 442 */     if (this.implementation != null)
/* 443 */       return this.implementation.costInline(paramInt, paramEnvironment, paramContext); 
/* 444 */     return super.costInline(paramInt, paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int codeLValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 451 */     return 0;
/*     */   }
/*     */   void codeLoad(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 454 */     paramAssembler.add(this.where, 21 + this.type.getTypeCodeOffset(), new Integer(((LocalMember)this.field).number));
/*     */   }
/*     */   
/*     */   void codeStore(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 458 */     LocalMember localMember = (LocalMember)this.field;
/* 459 */     paramAssembler.add(this.where, 54 + this.type.getTypeCodeOffset(), new LocalVariable(localMember, localMember.number));
/*     */   }
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 463 */     codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 464 */     codeLoad(paramEnvironment, paramContext, paramAssembler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 471 */     paramPrintStream.print(this.id + "#" + ((this.field != null) ? this.field.hashCode() : 0));
/* 472 */     if (this.implementation != null) {
/* 473 */       paramPrintStream.print("/IMPL=");
/* 474 */       this.implementation.print(paramPrintStream);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\IdentifierExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */