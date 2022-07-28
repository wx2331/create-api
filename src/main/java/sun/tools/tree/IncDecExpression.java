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
/*     */ public class IncDecExpression
/*     */   extends UnaryExpression
/*     */ {
/*  40 */   private FieldUpdater updater = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IncDecExpression(int paramInt, long paramLong, Expression paramExpression) {
/*  46 */     super(paramInt, paramLong, paramExpression.type, paramExpression);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  53 */     paramVset = this.right.checkAssignOp(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/*  54 */     if (this.right.type.inMask(254)) {
/*  55 */       this.type = this.right.type;
/*     */     } else {
/*  57 */       if (!this.right.type.isType(13)) {
/*  58 */         paramEnvironment.error(this.where, "invalid.arg.type", this.right.type, opNames[this.op]);
/*     */       }
/*  60 */       this.type = Type.tError;
/*     */     } 
/*  62 */     this.updater = this.right.getUpdater(paramEnvironment, paramContext);
/*  63 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  70 */     return checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inline(Environment paramEnvironment, Context paramContext) {
/*  77 */     return inlineValue(paramEnvironment, paramContext);
/*     */   }
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/*  81 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/*  82 */     if (this.updater != null) {
/*  83 */       this.updater = this.updater.inline(paramEnvironment, paramContext);
/*     */     }
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/*  89 */     if (this.updater == null) {
/*  90 */       if (this.right.op == 60 && this.type.isType(4) && ((IdentifierExpression)this.right).field
/*  91 */         .isLocal())
/*     */       {
/*  93 */         return 3;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 100 */       return this.right.costInline(paramInt, paramEnvironment, paramContext) + 4;
/*     */     } 
/*     */     
/* 103 */     return this.updater.costInline(paramInt, paramEnvironment, paramContext, true) + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void codeIncDecOp(Assembler paramAssembler, boolean paramBoolean) {
/* 113 */     switch (this.type.getTypeCode()) {
/*     */       case 1:
/* 115 */         paramAssembler.add(this.where, 18, new Integer(1));
/* 116 */         paramAssembler.add(this.where, paramBoolean ? 96 : 100);
/* 117 */         paramAssembler.add(this.where, 145);
/*     */         return;
/*     */       case 3:
/* 120 */         paramAssembler.add(this.where, 18, new Integer(1));
/* 121 */         paramAssembler.add(this.where, paramBoolean ? 96 : 100);
/* 122 */         paramAssembler.add(this.where, 147);
/*     */         return;
/*     */       case 2:
/* 125 */         paramAssembler.add(this.where, 18, new Integer(1));
/* 126 */         paramAssembler.add(this.where, paramBoolean ? 96 : 100);
/* 127 */         paramAssembler.add(this.where, 146);
/*     */         return;
/*     */       case 4:
/* 130 */         paramAssembler.add(this.where, 18, new Integer(1));
/* 131 */         paramAssembler.add(this.where, paramBoolean ? 96 : 100);
/*     */         return;
/*     */       case 5:
/* 134 */         paramAssembler.add(this.where, 20, new Long(1L));
/* 135 */         paramAssembler.add(this.where, paramBoolean ? 97 : 101);
/*     */         return;
/*     */       case 6:
/* 138 */         paramAssembler.add(this.where, 18, new Float(1.0F));
/* 139 */         paramAssembler.add(this.where, paramBoolean ? 98 : 102);
/*     */         return;
/*     */       case 7:
/* 142 */         paramAssembler.add(this.where, 20, new Double(1.0D));
/* 143 */         paramAssembler.add(this.where, paramBoolean ? 99 : 103);
/*     */         return;
/*     */     } 
/* 146 */     throw new CompilerError("invalid type");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void codeIncDec(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
/* 153 */     if (this.right.op == 60 && this.type.isType(4) && ((IdentifierExpression)this.right).field
/* 154 */       .isLocal() && this.updater == null) {
/* 155 */       if (paramBoolean3 && !paramBoolean2) {
/* 156 */         this.right.codeLoad(paramEnvironment, paramContext, paramAssembler);
/*     */       }
/* 158 */       int i = ((LocalMember)((IdentifierExpression)this.right).field).number;
/* 159 */       int[] arrayOfInt = { i, paramBoolean1 ? 1 : -1 };
/* 160 */       paramAssembler.add(this.where, 132, arrayOfInt);
/* 161 */       if (paramBoolean3 && paramBoolean2) {
/* 162 */         this.right.codeLoad(paramEnvironment, paramContext, paramAssembler);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 168 */     if (this.updater == null) {
/*     */       
/* 170 */       int i = this.right.codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 171 */       codeDup(paramEnvironment, paramContext, paramAssembler, i, 0);
/* 172 */       this.right.codeLoad(paramEnvironment, paramContext, paramAssembler);
/* 173 */       if (paramBoolean3 && !paramBoolean2) {
/* 174 */         codeDup(paramEnvironment, paramContext, paramAssembler, this.type.stackSize(), i);
/*     */       }
/* 176 */       codeIncDecOp(paramAssembler, paramBoolean1);
/* 177 */       if (paramBoolean3 && paramBoolean2) {
/* 178 */         codeDup(paramEnvironment, paramContext, paramAssembler, this.type.stackSize(), i);
/*     */       }
/* 180 */       this.right.codeStore(paramEnvironment, paramContext, paramAssembler);
/*     */     } else {
/*     */       
/* 183 */       this.updater.startUpdate(paramEnvironment, paramContext, paramAssembler, (paramBoolean3 && !paramBoolean2));
/* 184 */       codeIncDecOp(paramAssembler, paramBoolean1);
/* 185 */       this.updater.finishUpdate(paramEnvironment, paramContext, paramAssembler, (paramBoolean3 && paramBoolean2));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\IncDecExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */