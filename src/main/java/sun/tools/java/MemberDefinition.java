/*      */ package sun.tools.java;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import sun.tools.asm.Assembler;
/*      */ import sun.tools.tree.Context;
/*      */ import sun.tools.tree.Expression;
/*      */ import sun.tools.tree.Node;
/*      */ import sun.tools.tree.Statement;
/*      */ import sun.tools.tree.Vset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MemberDefinition
/*      */   implements Constants
/*      */ {
/*      */   protected long where;
/*      */   protected int modifiers;
/*      */   protected Type type;
/*      */   protected String documentation;
/*      */   protected IdentifierToken[] expIds;
/*      */   protected ClassDeclaration[] exp;
/*      */   protected Node value;
/*      */   protected ClassDefinition clazz;
/*      */   protected Identifier name;
/*      */   protected ClassDefinition innerClass;
/*      */   protected MemberDefinition nextMember;
/*      */   protected MemberDefinition nextMatch;
/*      */   protected MemberDefinition accessPeer;
/*      */   protected boolean superAccessMethod;
/*      */   private static Map proxyCache;
/*      */   static final int PUBLIC_ACCESS = 1;
/*      */   static final int PROTECTED_ACCESS = 2;
/*      */   static final int PACKAGE_ACCESS = 3;
/*      */   static final int PRIVATE_ACCESS = 4;
/*      */   
/*      */   public MemberDefinition(long paramLong, ClassDefinition paramClassDefinition, int paramInt, Type paramType, Identifier paramIdentifier, IdentifierToken[] paramArrayOfIdentifierToken, Node paramNode) {
/*   70 */     if (paramArrayOfIdentifierToken == null) {
/*   71 */       paramArrayOfIdentifierToken = new IdentifierToken[0];
/*      */     }
/*   73 */     this.where = paramLong;
/*   74 */     this.clazz = paramClassDefinition;
/*   75 */     this.modifiers = paramInt;
/*   76 */     this.type = paramType;
/*   77 */     this.name = paramIdentifier;
/*   78 */     this.expIds = paramArrayOfIdentifierToken;
/*   79 */     this.value = paramNode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MemberDefinition(ClassDefinition paramClassDefinition) {
/*   92 */     this(paramClassDefinition.getWhere(), paramClassDefinition
/*   93 */         .getOuterClass(), paramClassDefinition
/*   94 */         .getModifiers(), paramClassDefinition
/*   95 */         .getType(), paramClassDefinition
/*   96 */         .getName().getFlatName().getName(), (IdentifierToken[])null, (Node)null);
/*      */     
/*   98 */     this.innerClass = paramClassDefinition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MemberDefinition makeProxyMember(MemberDefinition paramMemberDefinition, ClassDefinition paramClassDefinition, Environment paramEnvironment) {
/*  128 */     if (proxyCache == null) {
/*  129 */       proxyCache = new HashMap<>();
/*      */     }
/*      */     
/*  132 */     String str = paramMemberDefinition.toString() + "@" + paramClassDefinition.toString();
/*      */     
/*  134 */     MemberDefinition memberDefinition = (MemberDefinition)proxyCache.get(str);
/*      */     
/*  136 */     if (memberDefinition != null) {
/*  137 */       return memberDefinition;
/*      */     }
/*      */ 
/*      */     
/*  141 */     memberDefinition = new MemberDefinition(paramMemberDefinition.getWhere(), paramClassDefinition, paramMemberDefinition.getModifiers(), paramMemberDefinition.getType(), paramMemberDefinition.getName(), paramMemberDefinition.getExceptionIds(), null);
/*      */     
/*  143 */     memberDefinition.exp = paramMemberDefinition.getExceptions(paramEnvironment);
/*  144 */     proxyCache.put(str, memberDefinition);
/*      */     
/*  146 */     return memberDefinition;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getWhere() {
/*  153 */     return this.where;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ClassDeclaration getClassDeclaration() {
/*  160 */     return this.clazz.getClassDeclaration();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resolveTypeStructure(Environment paramEnvironment) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassDeclaration getDefiningClassDeclaration() {
/*  173 */     return getClassDeclaration();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ClassDefinition getClassDefinition() {
/*  180 */     return this.clazz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ClassDefinition getTopClass() {
/*  187 */     return this.clazz.getTopClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getModifiers() {
/*  194 */     return this.modifiers;
/*      */   }
/*      */   public final void subModifiers(int paramInt) {
/*  197 */     this.modifiers &= paramInt ^ 0xFFFFFFFF;
/*      */   }
/*      */   public final void addModifiers(int paramInt) {
/*  200 */     this.modifiers |= paramInt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Type getType() {
/*  207 */     return this.type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Identifier getName() {
/*  214 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vector getArguments() {
/*  221 */     return isMethod() ? new Vector() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassDeclaration[] getExceptions(Environment paramEnvironment) {
/*  228 */     if (this.expIds != null && this.exp == null)
/*  229 */       if (this.expIds.length == 0) {
/*  230 */         this.exp = new ClassDeclaration[0];
/*      */       } else {
/*      */         
/*  233 */         throw new CompilerError("getExceptions " + this);
/*      */       }  
/*  235 */     return this.exp;
/*      */   }
/*      */   
/*      */   public final IdentifierToken[] getExceptionIds() {
/*  239 */     return this.expIds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassDefinition getInnerClass() {
/*  246 */     return this.innerClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUplevelValue() {
/*  254 */     if (!isSynthetic() || !isVariable() || isStatic()) {
/*  255 */       return false;
/*      */     }
/*  257 */     String str = this.name.toString();
/*  258 */     return (str.startsWith("val$") || str
/*  259 */       .startsWith("loc$") || str
/*  260 */       .startsWith("this$"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAccessMethod() {
/*  273 */     return (isSynthetic() && isMethod() && this.accessPeer != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MemberDefinition getAccessMethodTarget() {
/*  281 */     if (isAccessMethod()) {
/*  282 */       for (MemberDefinition memberDefinition = this.accessPeer; memberDefinition != null; memberDefinition = memberDefinition.accessPeer) {
/*      */         
/*  284 */         if (!memberDefinition.isAccessMethod()) {
/*  285 */           return memberDefinition;
/*      */         }
/*      */       } 
/*      */     }
/*  289 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAccessMethodTarget(MemberDefinition paramMemberDefinition) {
/*  294 */     if (getAccessMethodTarget() != paramMemberDefinition) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  301 */       if (this.accessPeer != null || paramMemberDefinition.accessPeer != null) {
/*  302 */         throw new CompilerError("accessPeer");
/*      */       }
/*  304 */       this.accessPeer = paramMemberDefinition;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MemberDefinition getAccessUpdateMember() {
/*  312 */     if (isAccessMethod()) {
/*  313 */       for (MemberDefinition memberDefinition = this.accessPeer; memberDefinition != null; memberDefinition = memberDefinition.accessPeer) {
/*  314 */         if (memberDefinition.isAccessMethod()) {
/*  315 */           return memberDefinition;
/*      */         }
/*      */       } 
/*      */     }
/*  319 */     return null;
/*      */   }
/*      */   
/*      */   public void setAccessUpdateMember(MemberDefinition paramMemberDefinition) {
/*  323 */     if (getAccessUpdateMember() != paramMemberDefinition) {
/*  324 */       if (!isAccessMethod() || paramMemberDefinition
/*  325 */         .getAccessMethodTarget() != getAccessMethodTarget()) {
/*  326 */         throw new CompilerError("accessPeer");
/*      */       }
/*  328 */       paramMemberDefinition.accessPeer = this.accessPeer;
/*  329 */       this.accessPeer = paramMemberDefinition;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isSuperAccessMethod() {
/*  338 */     return this.superAccessMethod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setIsSuperAccessMethod(boolean paramBoolean) {
/*  346 */     this.superAccessMethod = paramBoolean;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBlankFinal() {
/*  354 */     return (isFinal() && !isSynthetic() && getValue() == null);
/*      */   }
/*      */   
/*      */   public boolean isNeverNull() {
/*  358 */     if (isUplevelValue())
/*      */     {
/*  360 */       return !this.name.toString().startsWith("val$");
/*      */     }
/*  362 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Node getValue(Environment paramEnvironment) throws ClassNotFound {
/*  369 */     return this.value;
/*      */   }
/*      */   public final Node getValue() {
/*  372 */     return this.value;
/*      */   }
/*      */   public final void setValue(Node paramNode) {
/*  375 */     this.value = paramNode;
/*      */   }
/*      */   public Object getInitialValue() {
/*  378 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final MemberDefinition getNextMember() {
/*  385 */     return this.nextMember;
/*      */   }
/*      */   public final MemberDefinition getNextMatch() {
/*  388 */     return this.nextMatch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDocumentation() {
/*  395 */     return this.documentation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void check(Environment paramEnvironment) throws ClassNotFound {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Vset check(Environment paramEnvironment, Context paramContext, Vset paramVset) throws ClassNotFound {
/*  408 */     return paramVset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void code(Environment paramEnvironment, Assembler paramAssembler) throws ClassNotFound {
/*  415 */     throw new CompilerError("code");
/*      */   }
/*      */   public void codeInit(Environment paramEnvironment, Context paramContext, Assembler paramAssembler) throws ClassNotFound {
/*  418 */     throw new CompilerError("codeInit");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean reportDeprecated(Environment paramEnvironment) {
/*  425 */     return (isDeprecated() || this.clazz.reportDeprecated(paramEnvironment));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean canReach(Environment paramEnvironment, MemberDefinition paramMemberDefinition) {
/*  433 */     if (paramMemberDefinition.isLocal() || !paramMemberDefinition.isVariable() || (!isVariable() && !isInitializer()))
/*  434 */       return true; 
/*  435 */     if (getClassDeclaration().equals(paramMemberDefinition.getClassDeclaration()) && 
/*  436 */       isStatic() == paramMemberDefinition.isStatic()) {
/*      */ 
/*      */       
/*  439 */       while ((paramMemberDefinition = paramMemberDefinition.getNextMember()) != null && paramMemberDefinition != this);
/*  440 */       return (paramMemberDefinition != null);
/*      */     } 
/*  442 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getAccessLevel() {
/*  476 */     if (isPublic())
/*  477 */       return 1; 
/*  478 */     if (isProtected())
/*  479 */       return 2; 
/*  480 */     if (isPackagePrivate())
/*  481 */       return 3; 
/*  482 */     if (isPrivate()) {
/*  483 */       return 4;
/*      */     }
/*  485 */     throw new CompilerError("getAccessLevel()");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void reportError(Environment paramEnvironment, String paramString, ClassDeclaration paramClassDeclaration, MemberDefinition paramMemberDefinition) {
/*  497 */     if (paramClassDeclaration == null) {
/*      */ 
/*      */ 
/*      */       
/*  501 */       paramEnvironment.error(getWhere(), paramString, this, 
/*  502 */           getClassDeclaration(), paramMemberDefinition
/*  503 */           .getClassDeclaration());
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  509 */       paramEnvironment.error(paramClassDeclaration.getClassDefinition().getWhere(), paramString, this, 
/*      */ 
/*      */ 
/*      */           
/*  513 */           getClassDeclaration(), paramMemberDefinition
/*  514 */           .getClassDeclaration());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean sameReturnType(MemberDefinition paramMemberDefinition) {
/*  523 */     if (!isMethod() || !paramMemberDefinition.isMethod()) {
/*  524 */       throw new CompilerError("sameReturnType: not method");
/*      */     }
/*      */     
/*  527 */     Type type1 = getType().getReturnType();
/*  528 */     Type type2 = paramMemberDefinition.getType().getReturnType();
/*      */     
/*  530 */     return (type1 == type2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkOverride(Environment paramEnvironment, MemberDefinition paramMemberDefinition) {
/*  548 */     return checkOverride(paramEnvironment, paramMemberDefinition, (ClassDeclaration)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkOverride(Environment paramEnvironment, MemberDefinition paramMemberDefinition, ClassDeclaration paramClassDeclaration) {
/*  562 */     boolean bool = true;
/*      */ 
/*      */     
/*  565 */     if (!isMethod()) {
/*  566 */       throw new CompilerError("checkOverride(), expected method");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  571 */     if (isSynthetic()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  579 */       if (paramMemberDefinition.isFinal() || paramMemberDefinition
/*  580 */         .isConstructor() || paramMemberDefinition
/*  581 */         .isStatic() || !isStatic());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  593 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  598 */     if (getName() != paramMemberDefinition.getName() || 
/*  599 */       !getType().equalArguments(paramMemberDefinition.getType()))
/*      */     {
/*  601 */       throw new CompilerError("checkOverride(), signature mismatch");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  606 */     if (paramMemberDefinition.isStatic() && !isStatic()) {
/*  607 */       reportError(paramEnvironment, "override.static.with.instance", paramClassDeclaration, paramMemberDefinition);
/*  608 */       bool = false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  613 */     if (!paramMemberDefinition.isStatic() && isStatic()) {
/*  614 */       reportError(paramEnvironment, "hide.instance.with.static", paramClassDeclaration, paramMemberDefinition);
/*  615 */       bool = false;
/*      */     } 
/*      */ 
/*      */     
/*  619 */     if (paramMemberDefinition.isFinal()) {
/*  620 */       reportError(paramEnvironment, "override.final.method", paramClassDeclaration, paramMemberDefinition);
/*  621 */       bool = false;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  630 */     if (paramMemberDefinition.reportDeprecated(paramEnvironment) && !isDeprecated() && this instanceof sun.tools.javac.SourceMember)
/*      */     {
/*  632 */       reportError(paramEnvironment, "warn.override.is.deprecated", paramClassDeclaration, paramMemberDefinition);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  637 */     if (getAccessLevel() > paramMemberDefinition.getAccessLevel()) {
/*  638 */       reportError(paramEnvironment, "override.more.restrictive", paramClassDeclaration, paramMemberDefinition);
/*  639 */       bool = false;
/*      */     } 
/*      */ 
/*      */     
/*  643 */     if (!sameReturnType(paramMemberDefinition));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  657 */     if (!exceptionsFit(paramEnvironment, paramMemberDefinition)) {
/*  658 */       reportError(paramEnvironment, "override.incompatible.exceptions", paramClassDeclaration, paramMemberDefinition);
/*      */       
/*  660 */       bool = false;
/*      */     } 
/*      */     
/*  663 */     return bool;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkMeet(Environment paramEnvironment, MemberDefinition paramMemberDefinition, ClassDeclaration paramClassDeclaration) {
/*  691 */     if (!isMethod()) {
/*  692 */       throw new CompilerError("checkMeet(), expected method");
/*      */     }
/*      */ 
/*      */     
/*  696 */     if (!isAbstract() && !paramMemberDefinition.isAbstract()) {
/*  697 */       throw new CompilerError("checkMeet(), no abstract method");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  704 */     if (!isAbstract())
/*  705 */       return checkOverride(paramEnvironment, paramMemberDefinition, paramClassDeclaration); 
/*  706 */     if (!paramMemberDefinition.isAbstract()) {
/*  707 */       return paramMemberDefinition.checkOverride(paramEnvironment, this, paramClassDeclaration);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  714 */     if (getName() != paramMemberDefinition.getName() || 
/*  715 */       !getType().equalArguments(paramMemberDefinition.getType()))
/*      */     {
/*  717 */       throw new CompilerError("checkMeet(), signature mismatch");
/*      */     }
/*      */ 
/*      */     
/*  721 */     if (!sameReturnType(paramMemberDefinition)) {
/*      */       
/*  723 */       paramEnvironment.error(paramClassDeclaration.getClassDefinition().getWhere(), "meet.different.return", this, 
/*      */           
/*  725 */           getClassDeclaration(), paramMemberDefinition
/*  726 */           .getClassDeclaration());
/*  727 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  734 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean couldOverride(Environment paramEnvironment, MemberDefinition paramMemberDefinition) {
/*  750 */     if (!isMethod()) {
/*  751 */       throw new CompilerError("coulcOverride(), expected method");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  764 */     if (!paramMemberDefinition.isAbstract()) {
/*  765 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  769 */     if (getAccessLevel() > paramMemberDefinition.getAccessLevel()) {
/*  770 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  774 */     if (!exceptionsFit(paramEnvironment, paramMemberDefinition)) {
/*  775 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  782 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean exceptionsFit(Environment paramEnvironment, MemberDefinition paramMemberDefinition) {
/*  791 */     ClassDeclaration[] arrayOfClassDeclaration1 = getExceptions(paramEnvironment);
/*  792 */     ClassDeclaration[] arrayOfClassDeclaration2 = paramMemberDefinition.getExceptions(paramEnvironment);
/*      */ 
/*      */     
/*      */     byte b;
/*      */     
/*  797 */     label24: for (b = 0; b < arrayOfClassDeclaration1.length; b++) {
/*      */       try {
/*  799 */         ClassDefinition classDefinition = arrayOfClassDeclaration1[b].getClassDefinition(paramEnvironment);
/*  800 */         for (byte b1 = 0; b1 < arrayOfClassDeclaration2.length; b1++) {
/*  801 */           if (classDefinition.subClassOf(paramEnvironment, arrayOfClassDeclaration2[b1])) {
/*      */             continue label24;
/*      */           }
/*      */         } 
/*  805 */         if (!classDefinition.subClassOf(paramEnvironment, paramEnvironment
/*  806 */             .getClassDeclaration(idJavaLangError)))
/*      */         {
/*  808 */           if (!classDefinition.subClassOf(paramEnvironment, paramEnvironment
/*  809 */               .getClassDeclaration(idJavaLangRuntimeException)))
/*      */           {
/*      */ 
/*      */ 
/*      */             
/*  814 */             return false; } 
/*      */         }
/*  816 */       } catch (ClassNotFound classNotFound) {
/*      */         
/*  818 */         paramEnvironment.error(getWhere(), "class.not.found", classNotFound.name, paramMemberDefinition
/*  819 */             .getClassDeclaration());
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  824 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPublic() {
/*  833 */     return ((this.modifiers & 0x1) != 0);
/*      */   }
/*      */   public final boolean isPrivate() {
/*  836 */     return ((this.modifiers & 0x2) != 0);
/*      */   }
/*      */   public final boolean isProtected() {
/*  839 */     return ((this.modifiers & 0x4) != 0);
/*      */   }
/*      */   public final boolean isPackagePrivate() {
/*  842 */     return ((this.modifiers & 0x7) == 0);
/*      */   }
/*      */   public final boolean isFinal() {
/*  845 */     return ((this.modifiers & 0x10) != 0);
/*      */   }
/*      */   public final boolean isStatic() {
/*  848 */     return ((this.modifiers & 0x8) != 0);
/*      */   }
/*      */   public final boolean isSynchronized() {
/*  851 */     return ((this.modifiers & 0x20) != 0);
/*      */   }
/*      */   public final boolean isAbstract() {
/*  854 */     return ((this.modifiers & 0x400) != 0);
/*      */   }
/*      */   public final boolean isNative() {
/*  857 */     return ((this.modifiers & 0x100) != 0);
/*      */   }
/*      */   public final boolean isVolatile() {
/*  860 */     return ((this.modifiers & 0x40) != 0);
/*      */   }
/*      */   public final boolean isTransient() {
/*  863 */     return ((this.modifiers & 0x80) != 0);
/*      */   }
/*      */   public final boolean isMethod() {
/*  866 */     return this.type.isType(12);
/*      */   }
/*      */   public final boolean isVariable() {
/*  869 */     return (!this.type.isType(12) && this.innerClass == null);
/*      */   }
/*      */   public final boolean isSynthetic() {
/*  872 */     return ((this.modifiers & 0x80000) != 0);
/*      */   }
/*      */   public final boolean isDeprecated() {
/*  875 */     return ((this.modifiers & 0x40000) != 0);
/*      */   }
/*      */   public final boolean isStrict() {
/*  878 */     return ((this.modifiers & 0x200000) != 0);
/*      */   }
/*      */   public final boolean isInnerClass() {
/*  881 */     return (this.innerClass != null);
/*      */   }
/*      */   public final boolean isInitializer() {
/*  884 */     return getName().equals(idClassInit);
/*      */   }
/*      */   public final boolean isConstructor() {
/*  887 */     return getName().equals(idInit);
/*      */   }
/*      */   public boolean isLocal() {
/*  890 */     return false;
/*      */   }
/*      */   public boolean isInlineable(Environment paramEnvironment, boolean paramBoolean) throws ClassNotFound {
/*  893 */     return ((isStatic() || isPrivate() || isFinal() || isConstructor() || paramBoolean) && 
/*  894 */       !isSynchronized() && !isNative());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isConstant() {
/*  901 */     if (isFinal() && isVariable() && this.value != null) {
/*      */       
/*      */       try {
/*      */         
/*  905 */         this.modifiers &= 0xFFFFFFEF;
/*  906 */         return ((Expression)this.value).isConstant();
/*      */       } finally {
/*  908 */         this.modifiers |= 0x10;
/*      */       } 
/*      */     }
/*  911 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  918 */     Identifier identifier = getClassDefinition().getName();
/*  919 */     if (isInitializer())
/*  920 */       return isStatic() ? "static {}" : "instance {}"; 
/*  921 */     if (isConstructor()) {
/*  922 */       StringBuffer stringBuffer = new StringBuffer();
/*  923 */       stringBuffer.append(identifier);
/*  924 */       stringBuffer.append('(');
/*  925 */       Type[] arrayOfType = getType().getArgumentTypes();
/*  926 */       for (byte b = 0; b < arrayOfType.length; b++) {
/*  927 */         if (b > 0) {
/*  928 */           stringBuffer.append(',');
/*      */         }
/*  930 */         stringBuffer.append(arrayOfType[b].toString());
/*      */       } 
/*  932 */       stringBuffer.append(')');
/*  933 */       return stringBuffer.toString();
/*  934 */     }  if (isInnerClass()) {
/*  935 */       return getInnerClass().toString();
/*      */     }
/*  937 */     return this.type.typeString(getName().toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void print(PrintStream paramPrintStream) {
/*  944 */     if (isPublic()) {
/*  945 */       paramPrintStream.print("public ");
/*      */     }
/*  947 */     if (isPrivate()) {
/*  948 */       paramPrintStream.print("private ");
/*      */     }
/*  950 */     if (isProtected()) {
/*  951 */       paramPrintStream.print("protected ");
/*      */     }
/*  953 */     if (isFinal()) {
/*  954 */       paramPrintStream.print("final ");
/*      */     }
/*  956 */     if (isStatic()) {
/*  957 */       paramPrintStream.print("static ");
/*      */     }
/*  959 */     if (isSynchronized()) {
/*  960 */       paramPrintStream.print("synchronized ");
/*      */     }
/*  962 */     if (isAbstract()) {
/*  963 */       paramPrintStream.print("abstract ");
/*      */     }
/*  965 */     if (isNative()) {
/*  966 */       paramPrintStream.print("native ");
/*      */     }
/*  968 */     if (isVolatile()) {
/*  969 */       paramPrintStream.print("volatile ");
/*      */     }
/*  971 */     if (isTransient()) {
/*  972 */       paramPrintStream.print("transient ");
/*      */     }
/*  974 */     paramPrintStream.println(toString() + ";");
/*      */   }
/*      */   
/*      */   public void cleanup(Environment paramEnvironment) {
/*  978 */     this.documentation = null;
/*  979 */     if (isMethod() && this.value != null) {
/*  980 */       int i = 0;
/*  981 */       if (isPrivate() || isInitializer()) {
/*  982 */         this.value = (Node)Statement.empty;
/*      */       
/*      */       }
/*  985 */       else if ((i = ((Statement)this.value).costInline(Statement.MAXINLINECOST, null, null)) >= Statement.MAXINLINECOST) {
/*      */ 
/*      */         
/*  988 */         this.value = (Node)Statement.empty;
/*      */       } else {
/*      */         try {
/*  991 */           if (!isInlineable((Environment)null, true)) {
/*  992 */             this.value = (Node)Statement.empty;
/*      */           }
/*      */         }
/*  995 */         catch (ClassNotFound classNotFound) {}
/*      */       } 
/*  997 */       if (this.value != Statement.empty && paramEnvironment.dump()) {
/*  998 */         paramEnvironment.output("[after cleanup of " + getName() + ", " + i + " expression cost units remain]");
/*      */       }
/*      */     }
/* 1001 */     else if (isVariable() && (
/* 1002 */       isPrivate() || !isFinal() || this.type.isType(9))) {
/* 1003 */       this.value = null;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\tools\java\MemberDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */