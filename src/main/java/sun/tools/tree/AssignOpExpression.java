/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ public abstract class AssignOpExpression
/*     */   extends BinaryAssignExpression
/*     */ {
/*     */   protected Type itype;
/*  41 */   final int NOINC = Integer.MAX_VALUE;
/*     */   
/*  43 */   protected FieldUpdater updater = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AssignOpExpression(int paramInt, long paramLong, Expression paramExpression1, Expression paramExpression2) {
/*  49 */     super(paramInt, paramLong, paramExpression1, paramExpression2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void selectType(Environment paramEnvironment, Context paramContext, int paramInt) {
/*  58 */     Type type = null;
/*  59 */     switch (this.op) {
/*     */       case 5:
/*  61 */         if (this.left.type == Type.tString) {
/*  62 */           if (this.right.type == Type.tVoid)
/*     */           
/*     */           { 
/*  65 */             paramEnvironment.error(this.where, "incompatible.type", opNames[this.op], Type.tVoid, Type.tString);
/*     */             
/*  67 */             this.type = Type.tError; }
/*     */           else
/*  69 */           { this.type = this.itype = Type.tString; }  return;
/*     */         } 
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 6:
/*  75 */         if ((paramInt & 0x80) != 0) {
/*  76 */           this.itype = Type.tDouble; break;
/*  77 */         }  if ((paramInt & 0x40) != 0) {
/*  78 */           this.itype = Type.tFloat; break;
/*  79 */         }  if ((paramInt & 0x20) != 0) {
/*  80 */           this.itype = Type.tLong; break;
/*     */         } 
/*  82 */         this.itype = Type.tInt;
/*     */         break;
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*  87 */         if ((paramInt & 0x1) != 0) {
/*  88 */           this.itype = Type.tBoolean; break;
/*  89 */         }  if ((paramInt & 0x20) != 0) {
/*  90 */           this.itype = Type.tLong; break;
/*     */         } 
/*  92 */         this.itype = Type.tInt;
/*     */         break;
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*  97 */         type = Type.tInt;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 102 */         if (this.right.type.inMask(62)) {
/* 103 */           this.right = new ConvertExpression(this.where, Type.tInt, this.right);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 111 */         if (this.left.type == Type.tLong) {
/* 112 */           this.itype = Type.tLong; break;
/*     */         } 
/* 114 */         this.itype = Type.tInt;
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 120 */         throw new CompilerError("Bad assignOp type: " + this.op);
/*     */     } 
/* 122 */     if (type == null) {
/* 123 */       type = this.itype;
/*     */     }
/* 125 */     this.right = convert(paramEnvironment, paramContext, type, this.right);
/*     */ 
/*     */     
/* 128 */     this.type = this.left.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getIncrement() {
/* 136 */     if (this.left.op == 60 && this.type.isType(4) && this.right.op == 65 && (
/* 137 */       this.op == 5 || this.op == 6) && 
/* 138 */       ((IdentifierExpression)this.left).field.isLocal()) {
/* 139 */       int i = ((IntExpression)this.right).value;
/* 140 */       if (this.op == 6)
/* 141 */         i = -i; 
/* 142 */       if (i == (short)i)
/* 143 */         return i; 
/*     */     } 
/* 145 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vset checkValue(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/* 153 */     paramVset = this.left.checkAssignOp(paramEnvironment, paramContext, paramVset, paramHashtable, this);
/* 154 */     paramVset = this.right.checkValue(paramEnvironment, paramContext, paramVset, paramHashtable);
/* 155 */     int i = this.left.type.getTypeMask() | this.right.type.getTypeMask();
/* 156 */     if ((i & 0x2000) != 0) {
/* 157 */       return paramVset;
/*     */     }
/* 159 */     selectType(paramEnvironment, paramContext, i);
/* 160 */     if (!this.type.isType(13)) {
/* 161 */       convert(paramEnvironment, paramContext, this.itype, this.left);
/*     */     }
/* 163 */     this.updater = this.left.getUpdater(paramEnvironment, paramContext);
/* 164 */     return paramVset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression inlineValue(Environment paramEnvironment, Context paramContext) {
/* 172 */     this.left = this.left.inlineValue(paramEnvironment, paramContext);
/* 173 */     this.right = this.right.inlineValue(paramEnvironment, paramContext);
/* 174 */     if (this.updater != null) {
/* 175 */       this.updater = this.updater.inline(paramEnvironment, paramContext);
/*     */     }
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Expression copyInline(Context paramContext) {
/* 184 */     AssignOpExpression assignOpExpression = (AssignOpExpression)clone();
/* 185 */     assignOpExpression.left = this.left.copyInline(paramContext);
/* 186 */     assignOpExpression.right = this.right.copyInline(paramContext);
/* 187 */     if (this.updater != null) {
/* 188 */       assignOpExpression.updater = this.updater.copyInline(paramContext);
/*     */     }
/* 190 */     return assignOpExpression;
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
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 202 */     if (this.updater == null) {
/* 203 */       return (getIncrement() != Integer.MAX_VALUE) ? 3 : (this.right
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 211 */         .costInline(paramInt, paramEnvironment, paramContext) + this.left
/* 212 */         .costInline(paramInt, paramEnvironment, paramContext) + 4);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 217 */     return this.right.costInline(paramInt, paramEnvironment, paramContext) + this.updater
/* 218 */       .costInline(paramInt, paramEnvironment, paramContext, true) + 1;
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
/*     */   void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler, boolean paramBoolean) {
/* 230 */     int i = getIncrement();
/* 231 */     if (i != Integer.MAX_VALUE && this.updater == null) {
/* 232 */       int j = ((LocalMember)((IdentifierExpression)this.left).field).number;
/* 233 */       int[] arrayOfInt = { j, i };
/* 234 */       paramAssembler.add(this.where, 132, arrayOfInt);
/* 235 */       if (paramBoolean) {
/* 236 */         this.left.codeValue(paramEnvironment, paramContext, paramAssembler);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 241 */     if (this.updater == null) {
/*     */       
/* 243 */       int j = this.left.codeLValue(paramEnvironment, paramContext, paramAssembler);
/* 244 */       codeDup(paramEnvironment, paramContext, paramAssembler, j, 0);
/* 245 */       this.left.codeLoad(paramEnvironment, paramContext, paramAssembler);
/* 246 */       codeConversion(paramEnvironment, paramContext, paramAssembler, this.left.type, this.itype);
/* 247 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 248 */       codeOperation(paramEnvironment, paramContext, paramAssembler);
/* 249 */       codeConversion(paramEnvironment, paramContext, paramAssembler, this.itype, this.type);
/* 250 */       if (paramBoolean) {
/* 251 */         codeDup(paramEnvironment, paramContext, paramAssembler, this.type.stackSize(), j);
/*     */       }
/* 253 */       this.left.codeStore(paramEnvironment, paramContext, paramAssembler);
/*     */     } else {
/*     */       
/* 256 */       this.updater.startUpdate(paramEnvironment, paramContext, paramAssembler, false);
/* 257 */       codeConversion(paramEnvironment, paramContext, paramAssembler, this.left.type, this.itype);
/* 258 */       this.right.codeValue(paramEnvironment, paramContext, paramAssembler);
/* 259 */       codeOperation(paramEnvironment, paramContext, paramAssembler);
/* 260 */       codeConversion(paramEnvironment, paramContext, paramAssembler, this.itype, this.type);
/* 261 */       this.updater.finishUpdate(paramEnvironment, paramContext, paramAssembler, paramBoolean);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void codeValue(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 266 */     code(paramEnvironment, paramContext, paramAssembler, true);
/*     */   }
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 269 */     code(paramEnvironment, paramContext, paramAssembler, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream) {
/* 276 */     paramPrintStream.print("(" + opNames[this.op] + " ");
/* 277 */     this.left.print(paramPrintStream);
/* 278 */     paramPrintStream.print(" ");
/* 279 */     this.right.print(paramPrintStream);
/* 280 */     paramPrintStream.print(")");
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\AssignOpExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */