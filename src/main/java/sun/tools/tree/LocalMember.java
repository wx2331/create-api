/*     */ package sun.tools.tree;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.tools.java.ClassDefinition;
/*     */ import sun.tools.java.CompilerError;
/*     */ import sun.tools.java.Environment;
/*     */ import sun.tools.java.Identifier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LocalMember
/*     */   extends MemberDefinition
/*     */ {
/*  45 */   int number = -1;
/*     */ 
/*     */   
/*     */   int readcount;
/*     */ 
/*     */   
/*     */   int writecount;
/*     */ 
/*     */   
/*     */   int scopeNumber;
/*     */ 
/*     */   
/*     */   LocalMember originalOfCopy;
/*     */ 
/*     */   
/*     */   LocalMember prev;
/*     */ 
/*     */   
/*     */   public int getScopeNumber() {
/*  64 */     return this.scopeNumber;
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
/*     */   public LocalMember(long paramLong, ClassDefinition paramClassDefinition, int paramInt, Type paramType, Identifier paramIdentifier) {
/*  83 */     super(paramLong, paramClassDefinition, paramInt, paramType, paramIdentifier, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalMember(ClassDefinition paramClassDefinition) {
/*  90 */     super(paramClassDefinition);
/*     */ 
/*     */     
/*  93 */     this.name = paramClassDefinition.getLocalName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   LocalMember(MemberDefinition paramMemberDefinition) {
/* 100 */     this(0L, (ClassDefinition)null, 0, paramMemberDefinition.getType(), idClass);
/*     */     
/* 102 */     this.accessPeer = paramMemberDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final MemberDefinition getMember() {
/* 109 */     return (this.name == idClass) ? this.accessPeer : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLocal() {
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalMember copyInline(Context paramContext) {
/* 127 */     LocalMember localMember = new LocalMember(this.where, this.clazz, this.modifiers, this.type, this.name);
/* 128 */     localMember.readcount = this.readcount;
/* 129 */     localMember.writecount = this.writecount;
/*     */     
/* 131 */     localMember.originalOfCopy = this;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     localMember.addModifiers(131072);
/* 138 */     if (this.accessPeer != null && (this.accessPeer
/* 139 */       .getModifiers() & 0x20000) == 0) {
/* 140 */       throw new CompilerError("local copyInline");
/*     */     }
/* 142 */     this.accessPeer = localMember;
/*     */     
/* 144 */     return localMember;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalMember getCurrentInlineCopy(Context paramContext) {
/* 154 */     MemberDefinition memberDefinition = this.accessPeer;
/* 155 */     if (memberDefinition != null && (memberDefinition.getModifiers() & 0x20000) != 0) {
/* 156 */       return (LocalMember)memberDefinition;
/*     */     }
/*     */     
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LocalMember[] copyArguments(Context paramContext, MemberDefinition paramMemberDefinition) {
/* 166 */     Vector vector = paramMemberDefinition.getArguments();
/* 167 */     LocalMember[] arrayOfLocalMember = new LocalMember[vector.size()];
/* 168 */     vector.copyInto((Object[])arrayOfLocalMember);
/* 169 */     for (byte b = 0; b < arrayOfLocalMember.length; b++) {
/* 170 */       arrayOfLocalMember[b] = arrayOfLocalMember[b].copyInline(paramContext);
/*     */     }
/* 172 */     return arrayOfLocalMember;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void doneWithArguments(Context paramContext, LocalMember[] paramArrayOfLocalMember) {
/* 179 */     for (byte b = 0; b < paramArrayOfLocalMember.length; b++) {
/* 180 */       if ((paramArrayOfLocalMember[b]).originalOfCopy.accessPeer == paramArrayOfLocalMember[b]) {
/* 181 */         (paramArrayOfLocalMember[b]).originalOfCopy.accessPeer = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInlineable(Environment paramEnvironment, boolean paramBoolean) {
/* 192 */     return ((getModifiers() & 0x100000) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUsed() {
/* 199 */     return (this.readcount != 0 || this.writecount != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   LocalMember getAccessVar() {
/* 204 */     return (LocalMember)this.accessPeer;
/*     */   }
/*     */   void setAccessVar(LocalMember paramLocalMember) {
/* 207 */     this.accessPeer = paramLocalMember;
/*     */   }
/*     */   
/*     */   MemberDefinition getAccessVarMember() {
/* 211 */     return this.accessPeer;
/*     */   }
/*     */   void setAccessVarMember(MemberDefinition paramMemberDefinition) {
/* 214 */     this.accessPeer = paramMemberDefinition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getValue(Environment paramEnvironment) {
/* 222 */     return getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumber(Context paramContext) {
/* 229 */     return this.number;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\tree\LocalMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */