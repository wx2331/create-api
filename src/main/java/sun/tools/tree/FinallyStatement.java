/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import sun.tools.asm.Assembler;
/*     */ import sun.tools.asm.CatchData;
/*     */ import sun.tools.asm.Instruction;
/*     */ import sun.tools.asm.Label;
/*     */ import sun.tools.asm.TryData;
/*     */ import sun.tools.java.ClassDefinition;
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
/*     */ public class FinallyStatement
/*     */   extends Statement
/*     */ {
/*     */   Statement body;
/*     */   Statement finalbody;
/*     */   boolean finallyCanFinish;
/*     */   boolean needReturnSlot;
/*     */   Statement init;
/*     */   LocalMember tryTemp;
/*     */   
/*     */   public FinallyStatement(long paramLong, Statement paramStatement1, Statement paramStatement2) {
/*  55 */     super(103, paramLong);
/*  56 */     this.body = paramStatement1;
/*  57 */     this.finalbody = paramStatement2;
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
/*     */   Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset, Hashtable paramHashtable) {
/*  72 */     paramVset = reach(paramEnvironment, paramVset);
/*  73 */     Hashtable<Object, Object> hashtable = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 168 */     CheckContext checkContext1 = new CheckContext(paramContext, this);
/*     */     
/* 170 */     Vset vset1 = this.body.check(paramEnvironment, checkContext1, paramVset.copy(), hashtable).join(checkContext1.vsBreak);
/*     */     
/* 172 */     CheckContext checkContext2 = new CheckContext(paramContext, this);
/*     */     
/* 174 */     checkContext2.vsContinue = null;
/* 175 */     Vset vset2 = this.finalbody.check(paramEnvironment, checkContext2, paramVset, paramHashtable);
/* 176 */     this.finallyCanFinish = !vset2.isDeadEnd();
/* 177 */     vset2 = vset2.join(checkContext2.vsBreak);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     if (this.finallyCanFinish)
/*     */     {
/* 185 */       for (Enumeration<Object> enumeration = hashtable.keys(); enumeration.hasMoreElements(); ) {
/* 186 */         Object object = enumeration.nextElement();
/* 187 */         paramHashtable.put(object, hashtable.get(object));
/*     */       } 
/*     */     }
/* 190 */     return paramContext.removeAdditionalVars(vset1.addDAandJoinDU(vset2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement inline(Environment paramEnvironment, Context paramContext) {
/* 197 */     if (this.tryTemp != null) {
/* 198 */       paramContext = new Context(paramContext, this);
/* 199 */       paramContext.declare(paramEnvironment, this.tryTemp);
/*     */     } 
/* 201 */     if (this.init != null) {
/* 202 */       this.init = this.init.inline(paramEnvironment, paramContext);
/*     */     }
/* 204 */     if (this.body != null) {
/* 205 */       this.body = this.body.inline(paramEnvironment, paramContext);
/*     */     }
/* 207 */     if (this.finalbody != null) {
/* 208 */       this.finalbody = this.finalbody.inline(paramEnvironment, paramContext);
/*     */     }
/* 210 */     if (this.body == null) {
/* 211 */       return eliminate(paramEnvironment, this.finalbody);
/*     */     }
/* 213 */     if (this.finalbody == null) {
/* 214 */       return eliminate(paramEnvironment, this.body);
/*     */     }
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Statement copyInline(Context paramContext, boolean paramBoolean) {
/* 223 */     FinallyStatement finallyStatement = (FinallyStatement)clone();
/* 224 */     if (this.tryTemp != null) {
/* 225 */       finallyStatement.tryTemp = this.tryTemp.copyInline(paramContext);
/*     */     }
/* 227 */     if (this.init != null) {
/* 228 */       finallyStatement.init = this.init.copyInline(paramContext, paramBoolean);
/*     */     }
/* 230 */     if (this.body != null) {
/* 231 */       finallyStatement.body = this.body.copyInline(paramContext, paramBoolean);
/*     */     }
/* 233 */     if (this.finalbody != null) {
/* 234 */       finallyStatement.finalbody = this.finalbody.copyInline(paramContext, paramBoolean);
/*     */     }
/* 236 */     return finallyStatement;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int costInline(int paramInt, Environment paramEnvironment, Context paramContext) {
/* 243 */     int i = 4;
/* 244 */     if (this.init != null) {
/* 245 */       i += this.init.costInline(paramInt, paramEnvironment, paramContext);
/* 246 */       if (i >= paramInt) return i; 
/*     */     } 
/* 248 */     if (this.body != null) {
/* 249 */       i += this.body.costInline(paramInt, paramEnvironment, paramContext);
/* 250 */       if (i >= paramInt) return i; 
/*     */     } 
/* 252 */     if (this.finalbody != null) {
/* 253 */       i += this.finalbody.costInline(paramInt, paramEnvironment, paramContext);
/*     */     }
/* 255 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void code(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) {
/* 262 */     paramContext = new Context(paramContext);
/* 263 */     Integer integer1 = null, integer2 = null;
/* 264 */     Label label = new Label();
/*     */     
/* 266 */     if (this.tryTemp != null) {
/* 267 */       paramContext.declare(paramEnvironment, this.tryTemp);
/*     */     }
/* 269 */     if (this.init != null) {
/* 270 */       CodeContext codeContext1 = new CodeContext(paramContext, this);
/* 271 */       this.init.code(paramEnvironment, codeContext1, paramAssembler);
/*     */     } 
/*     */     
/* 274 */     if (this.finallyCanFinish) {
/*     */       
/* 276 */       ClassDefinition classDefinition = paramContext.field.getClassDefinition();
/*     */       
/* 278 */       if (this.needReturnSlot) {
/* 279 */         Type type = paramContext.field.getType().getReturnType();
/* 280 */         LocalMember localMember = new LocalMember(0L, classDefinition, 0, type, idFinallyReturnValue);
/*     */ 
/*     */         
/* 283 */         paramContext.declare(paramEnvironment, localMember);
/* 284 */         Environment.debugOutput("Assigning return slot to " + localMember.number);
/*     */       } 
/*     */ 
/*     */       
/* 288 */       LocalMember localMember1 = new LocalMember(this.where, classDefinition, 0, Type.tObject, null);
/* 289 */       LocalMember localMember2 = new LocalMember(this.where, classDefinition, 0, Type.tInt, null);
/* 290 */       integer1 = new Integer(paramContext.declare(paramEnvironment, localMember1));
/* 291 */       integer2 = new Integer(paramContext.declare(paramEnvironment, localMember2));
/*     */     } 
/*     */     
/* 294 */     TryData tryData = new TryData();
/* 295 */     tryData.add(null);
/*     */ 
/*     */     
/* 298 */     CodeContext codeContext = new CodeContext(paramContext, this);
/* 299 */     paramAssembler.add(this.where, -3, tryData);
/* 300 */     this.body.code(paramEnvironment, codeContext, paramAssembler);
/* 301 */     paramAssembler.add((Instruction)codeContext.breakLabel);
/* 302 */     paramAssembler.add((Instruction)tryData.getEndLabel());
/*     */ 
/*     */     
/* 305 */     if (this.finallyCanFinish) {
/* 306 */       paramAssembler.add(this.where, 168, codeContext.contLabel);
/* 307 */       paramAssembler.add(this.where, 167, label);
/*     */     } else {
/*     */       
/* 310 */       paramAssembler.add(this.where, 167, codeContext.contLabel);
/*     */     } 
/*     */ 
/*     */     
/* 314 */     CatchData catchData = tryData.getCatch(0);
/* 315 */     paramAssembler.add((Instruction)catchData.getLabel());
/* 316 */     if (this.finallyCanFinish) {
/* 317 */       paramAssembler.add(this.where, 58, integer1);
/* 318 */       paramAssembler.add(this.where, 168, codeContext.contLabel);
/* 319 */       paramAssembler.add(this.where, 25, integer1);
/* 320 */       paramAssembler.add(this.where, 191);
/*     */     } else {
/*     */       
/* 323 */       paramAssembler.add(this.where, 87);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     paramAssembler.add((Instruction)codeContext.contLabel);
/* 330 */     codeContext.contLabel = null;
/* 331 */     codeContext.breakLabel = label;
/* 332 */     if (this.finallyCanFinish) {
/* 333 */       paramAssembler.add(this.where, 58, integer2);
/* 334 */       this.finalbody.code(paramEnvironment, codeContext, paramAssembler);
/* 335 */       paramAssembler.add(this.where, 169, integer2);
/*     */     } else {
/* 337 */       this.finalbody.code(paramEnvironment, codeContext, paramAssembler);
/*     */     } 
/* 339 */     paramAssembler.add((Instruction)label);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(PrintStream paramPrintStream, int paramInt) {
/* 346 */     super.print(paramPrintStream, paramInt);
/* 347 */     paramPrintStream.print("try ");
/* 348 */     if (this.body != null) {
/* 349 */       this.body.print(paramPrintStream, paramInt);
/*     */     } else {
/* 351 */       paramPrintStream.print("<empty>");
/*     */     } 
/* 353 */     paramPrintStream.print(" finally ");
/* 354 */     if (this.finalbody != null) {
/* 355 */       this.finalbody.print(paramPrintStream, paramInt);
/*     */     } else {
/* 357 */       paramPrintStream.print("<empty>");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\FinallyStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */