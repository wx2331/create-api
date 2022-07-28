/*     */ package com.sun.tools.javadoc;
/*     */ 
/*     */ import com.sun.javadoc.ClassDoc;
/*     */ import com.sun.javadoc.ExecutableMemberDoc;
/*     */ import com.sun.javadoc.ParamTag;
/*     */ import com.sun.javadoc.Parameter;
/*     */ import com.sun.javadoc.SourcePosition;
/*     */ import com.sun.javadoc.ThrowsTag;
/*     */ import com.sun.javadoc.Type;
/*     */ import com.sun.javadoc.TypeVariable;
/*     */ import com.sun.source.util.TreePath;
/*     */ import com.sun.tools.javac.code.Symbol;
/*     */ import com.sun.tools.javac.code.Type;
/*     */ import com.sun.tools.javac.util.List;
/*     */ import com.sun.tools.javac.util.ListBuffer;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.text.CollationKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ExecutableMemberDocImpl
/*     */   extends MemberDocImpl
/*     */   implements ExecutableMemberDoc
/*     */ {
/*     */   protected final Symbol.MethodSymbol sym;
/*     */   
/*     */   public ExecutableMemberDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol, TreePath paramTreePath) {
/*  63 */     super(paramDocEnv, (Symbol)paramMethodSymbol, paramTreePath);
/*  64 */     this.sym = paramMethodSymbol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecutableMemberDocImpl(DocEnv paramDocEnv, Symbol.MethodSymbol paramMethodSymbol) {
/*  71 */     this(paramDocEnv, paramMethodSymbol, (TreePath)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getFlags() {
/*  78 */     return this.sym.flags();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Symbol.ClassSymbol getContainingClass() {
/*  85 */     return this.sym.enclClass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNative() {
/*  92 */     return Modifier.isNative(getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSynchronized() {
/*  99 */     return Modifier.isSynchronized(getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVarArgs() {
/* 107 */     return ((this.sym.flags() & 0x400000000L) != 0L && !this.env.legacyDoclet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSynthetic() {
/* 115 */     return ((this.sym.flags() & 0x1000L) != 0L);
/*     */   }
/*     */   
/*     */   public boolean isIncluded() {
/* 119 */     return (containingClass().isIncluded() && this.env.shouldDocument(this.sym));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThrowsTag[] throwsTags() {
/* 129 */     return comment().throwsTags();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParamTag[] paramTags() {
/* 139 */     return comment().paramTags();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParamTag[] typeParamTags() {
/* 146 */     return this.env.legacyDoclet ? new ParamTag[0] : 
/*     */       
/* 148 */       comment().typeParamTags();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassDoc[] thrownExceptions() {
/* 158 */     ListBuffer listBuffer = new ListBuffer();
/* 159 */     for (Type type : this.sym.type.getThrownTypes()) {
/* 160 */       type = this.env.types.erasure(type);
/*     */ 
/*     */       
/* 163 */       ClassDocImpl classDocImpl = this.env.getClassDoc((Symbol.ClassSymbol)type.tsym);
/* 164 */       if (classDocImpl != null) listBuffer.append(classDocImpl); 
/*     */     } 
/* 166 */     return (ClassDoc[])listBuffer.toArray((Object[])new ClassDocImpl[listBuffer.length()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type[] thrownExceptionTypes() {
/* 175 */     return TypeMaker.getTypes(this.env, this.sym.type.getThrownTypes());
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
/*     */   public Parameter[] parameters() {
/* 188 */     List list = this.sym.params();
/* 189 */     Parameter[] arrayOfParameter = new Parameter[list.length()];
/*     */     
/* 191 */     byte b = 0;
/* 192 */     for (Symbol.VarSymbol varSymbol : list) {
/* 193 */       arrayOfParameter[b++] = new ParameterImpl(this.env, varSymbol);
/*     */     }
/* 195 */     return arrayOfParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type receiverType() {
/* 205 */     Type type = (this.sym.type.asMethodType()).recvtype;
/* 206 */     return (type != null) ? TypeMaker.getType(this.env, type, false, true) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeVariable[] typeParameters() {
/* 214 */     if (this.env.legacyDoclet) {
/* 215 */       return new TypeVariable[0];
/*     */     }
/* 217 */     TypeVariable[] arrayOfTypeVariable = new TypeVariable[this.sym.type.getTypeArguments().length()];
/* 218 */     TypeMaker.getTypes(this.env, this.sym.type.getTypeArguments(), (Type[])arrayOfTypeVariable);
/* 219 */     return arrayOfTypeVariable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String signature() {
/* 228 */     return makeSignature(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String flatSignature() {
/* 239 */     return makeSignature(false);
/*     */   }
/*     */   
/*     */   private String makeSignature(boolean paramBoolean) {
/* 243 */     StringBuilder stringBuilder = new StringBuilder();
/* 244 */     stringBuilder.append("(");
/* 245 */     for (List list = this.sym.type.getParameterTypes(); list.nonEmpty(); ) {
/* 246 */       Type type = (Type)list.head;
/* 247 */       stringBuilder.append(TypeMaker.getTypeString(this.env, type, paramBoolean));
/* 248 */       list = list.tail;
/* 249 */       if (list.nonEmpty()) {
/* 250 */         stringBuilder.append(", ");
/*     */       }
/*     */     } 
/* 253 */     if (isVarArgs()) {
/* 254 */       int i = stringBuilder.length();
/* 255 */       stringBuilder.replace(i - 2, i, "...");
/*     */     } 
/* 257 */     stringBuilder.append(")");
/* 258 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   protected String typeParametersString() {
/* 262 */     return TypeMaker.typeParametersString(this.env, (Symbol)this.sym, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CollationKey generateKey() {
/* 270 */     String str = name() + flatSignature() + typeParametersString();
/*     */     
/* 272 */     str = str.replace(',', ' ').replace('&', ' ');
/*     */     
/* 274 */     return this.env.doclocale.collator.getCollationKey(str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourcePosition position() {
/* 283 */     if ((this.sym.enclClass()).sourcefile == null) return null; 
/* 284 */     return SourcePositionImpl.make((this.sym.enclClass()).sourcefile, (this.tree == null) ? 0 : this.tree.pos, this.lineMap);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javadoc\ExecutableMemberDocImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */