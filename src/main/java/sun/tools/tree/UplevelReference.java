/*     */ package sun.tools.tree;
/*     */ 
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Constants;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
/*     */ import sun.tools.java.MemberDefinition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UplevelReference
/*     */   implements Constants
/*     */ {
/*     */   ClassDefinition client;
/*     */   LocalMember target;
/*     */   LocalMember localArgument;
/*     */   MemberDefinition localField;
/*     */   UplevelReference next;
/*     */   
/*     */   public UplevelReference(ClassDefinition paramClassDefinition, LocalMember paramLocalMember) {
/*     */     Identifier identifier1;
/*  88 */     this.client = paramClassDefinition;
/*  89 */     this.target = paramLocalMember;
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (paramLocalMember.getName().equals(idThis)) {
/*  94 */       ClassDefinition classDefinition1 = paramLocalMember.getClassDefinition();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 102 */       byte b1 = 0;
/* 103 */       for (ClassDefinition classDefinition2 = classDefinition1; !classDefinition2.isTopLevel(); classDefinition2 = classDefinition2.getOuterClass())
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 108 */         b1++;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 133 */       identifier1 = Identifier.lookup("this$" + b1);
/*     */     } else {
/* 135 */       identifier1 = Identifier.lookup("val$" + paramLocalMember.getName());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     Identifier identifier2 = identifier1;
/* 142 */     byte b = 0;
/*     */     while (true) {
/* 144 */       boolean bool = (paramClassDefinition.getFirstMatch(identifier1) != null) ? true : false;
/* 145 */       UplevelReference uplevelReference = paramClassDefinition.getReferences();
/* 146 */       for (; uplevelReference != null; uplevelReference = uplevelReference.next) {
/* 147 */         if (uplevelReference.target.getName().equals(identifier1)) {
/* 148 */           bool = true;
/*     */         }
/*     */       } 
/* 151 */       if (!bool) {
/*     */         break;
/*     */       }
/*     */       
/* 155 */       identifier1 = Identifier.lookup(identifier2 + "$" + ++b);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 160 */     this
/*     */ 
/*     */       
/* 163 */       .localArgument = new LocalMember(paramLocalMember.getWhere(), paramClassDefinition, 524304, paramLocalMember.getType(), identifier1);
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
/*     */   public UplevelReference insertInto(UplevelReference paramUplevelReference) {
/* 175 */     if (paramUplevelReference == null || isEarlierThan(paramUplevelReference)) {
/* 176 */       this.next = paramUplevelReference;
/* 177 */       return this;
/*     */     } 
/* 179 */     UplevelReference uplevelReference = paramUplevelReference;
/* 180 */     while (uplevelReference.next != null && !isEarlierThan(uplevelReference.next)) {
/* 181 */       uplevelReference = uplevelReference.next;
/*     */     }
/* 183 */     this.next = uplevelReference.next;
/* 184 */     uplevelReference.next = this;
/* 185 */     return paramUplevelReference;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEarlierThan(UplevelReference paramUplevelReference) {
/* 194 */     if (isClientOuterField())
/* 195 */       return true; 
/* 196 */     if (paramUplevelReference.isClientOuterField()) {
/* 197 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 201 */     LocalMember localMember = paramUplevelReference.target;
/* 202 */     Identifier identifier1 = this.target.getName();
/* 203 */     Identifier identifier2 = localMember.getName();
/* 204 */     int i = identifier1.toString().compareTo(identifier2.toString());
/* 205 */     if (i != 0) {
/* 206 */       return (i < 0);
/*     */     }
/* 208 */     Identifier identifier3 = this.target.getClassDefinition().getName();
/* 209 */     Identifier identifier4 = localMember.getClassDefinition().getName();
/* 210 */     int j = identifier3.toString().compareTo(identifier4.toString());
/* 211 */     return (j < 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final LocalMember getTarget() {
/* 218 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final LocalMember getLocalArgument() {
/* 225 */     return this.localArgument;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MemberDefinition getLocalField() {
/* 232 */     return this.localField;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final MemberDefinition getLocalField(Environment paramEnvironment) {
/* 240 */     if (this.localField == null) {
/* 241 */       makeLocalField(paramEnvironment);
/*     */     }
/* 243 */     return this.localField;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClassDefinition getClient() {
/* 250 */     return this.client;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final UplevelReference getNext() {
/* 257 */     return this.next;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClientOuterField() {
/* 267 */     MemberDefinition memberDefinition = this.client.findOuterMember();
/* 268 */     return (memberDefinition != null && this.localField == memberDefinition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean localArgumentAvailable(Environment paramEnvironment, Context paramContext) {
/* 279 */     MemberDefinition memberDefinition = paramContext.field;
/* 280 */     if (memberDefinition.getClassDefinition() != this.client) {
/* 281 */       throw new CompilerError("localArgumentAvailable");
/*     */     }
/* 283 */     return (memberDefinition.isConstructor() || memberDefinition
/* 284 */       .isVariable() || memberDefinition
/* 285 */       .isInitializer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void noteReference(Environment paramEnvironment, Context paramContext) {
/* 295 */     if (this.localField == null && !localArgumentAvailable(paramEnvironment, paramContext))
/*     */     {
/* 297 */       makeLocalField(paramEnvironment);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void makeLocalField(Environment paramEnvironment) {
/* 303 */     this.client.referencesMustNotBeFrozen();
/* 304 */     int i = 524306;
/* 305 */     this.localField = paramEnvironment.makeMemberDefinition(paramEnvironment, this.localArgument
/* 306 */         .getWhere(), this.client, null, i, this.localArgument
/*     */ 
/*     */         
/* 309 */         .getType(), this.localArgument
/* 310 */         .getName(), null, null, null);
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
/*     */   public Expression makeLocalReference(Environment paramEnvironment, Context paramContext) {
/* 322 */     if (paramContext.field.getClassDefinition() != this.client) {
/* 323 */       throw new CompilerError("makeLocalReference");
/*     */     }
/* 325 */     if (localArgumentAvailable(paramEnvironment, paramContext)) {
/* 326 */       return new IdentifierExpression(0L, this.localArgument);
/*     */     }
/* 328 */     return makeFieldReference(paramEnvironment, paramContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression makeFieldReference(Environment paramEnvironment, Context paramContext) {
/* 337 */     Expression expression = paramContext.findOuterLink(paramEnvironment, 0L, this.localField);
/* 338 */     return new FieldExpression(0L, expression, this.localField);
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
/*     */   public void willCodeArguments(Environment paramEnvironment, Context paramContext) {
/* 351 */     if (!isClientOuterField()) {
/* 352 */       paramContext.noteReference(paramEnvironment, this.target);
/*     */     }
/*     */     
/* 355 */     if (this.next != null) {
/* 356 */       this.next.willCodeArguments(paramEnvironment, paramContext);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void codeArguments(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, long paramLong, MemberDefinition paramMemberDefinition) {
/* 366 */     if (!isClientOuterField()) {
/* 367 */       Expression expression = paramContext.makeReference(paramEnvironment, this.target);
/* 368 */       expression.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */     } 
/*     */     
/* 371 */     if (this.next != null) {
/* 372 */       this.next.codeArguments(paramEnvironment, paramContext, paramAssembler, paramLong, paramMemberDefinition);
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
/*     */   public void codeInitialization(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, long paramLong, MemberDefinition paramMemberDefinition) {
/* 385 */     if (this.localField != null && !isClientOuterField()) {
/* 386 */       Expression expression1 = paramContext.makeReference(paramEnvironment, this.target);
/* 387 */       Expression expression2 = makeFieldReference(paramEnvironment, paramContext);
/* 388 */       expression1 = new AssignExpression(expression1.getWhere(), expression2, expression1);
/* 389 */       expression1.type = this.localField.getType();
/* 390 */       expression1.code(paramEnvironment, paramContext, paramAssembler);
/*     */     } 
/*     */     
/* 393 */     if (this.next != null) {
/* 394 */       this.next.codeInitialization(paramEnvironment, paramContext, paramAssembler, paramLong, paramMemberDefinition);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 399 */     return "[" + this.localArgument + " in " + this.client + "]";
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\UplevelReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */