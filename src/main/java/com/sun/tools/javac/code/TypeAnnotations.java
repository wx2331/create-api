/*      */ package com.sun.tools.javac.code;
/*      */
/*      */ import com.sun.source.tree.MemberReferenceTree;
/*      */ import com.sun.source.tree.Tree;
/*      */ import com.sun.tools.javac.comp.Annotate;
/*      */ import com.sun.tools.javac.comp.Attr;
/*      */ import com.sun.tools.javac.comp.AttrContext;
/*      */ import com.sun.tools.javac.comp.Env;
/*      */ import com.sun.tools.javac.tree.JCTree;
/*      */ import com.sun.tools.javac.tree.TreeScanner;
/*      */ import com.sun.tools.javac.util.Assert;
/*      */ import com.sun.tools.javac.util.Context;
/*      */ import com.sun.tools.javac.util.List;
/*      */ import com.sun.tools.javac.util.ListBuffer;
/*      */ import com.sun.tools.javac.util.Log;
/*      */ import com.sun.tools.javac.util.Names;
/*      */ import com.sun.tools.javac.util.Options;
/*      */ import javax.lang.model.element.Element;
/*      */ import javax.lang.model.element.ElementKind;
/*      */ import javax.lang.model.type.TypeKind;
/*      */ import javax.tools.JavaFileObject;
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
/*      */ public class TypeAnnotations
/*      */ {
/*   85 */   protected static final Context.Key<TypeAnnotations> typeAnnosKey = new Context.Key(); final Log log; final Names names; final Symtab syms; final Annotate annotate;
/*      */   final Attr attr;
/*      */
/*      */   public static TypeAnnotations instance(Context paramContext) {
/*   89 */     TypeAnnotations typeAnnotations = (TypeAnnotations)paramContext.get(typeAnnosKey);
/*   90 */     if (typeAnnotations == null)
/*   91 */       typeAnnotations = new TypeAnnotations(paramContext);
/*   92 */     return typeAnnotations;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   protected TypeAnnotations(Context paramContext) {
/*  102 */     paramContext.put(typeAnnosKey, this);
/*  103 */     this.names = Names.instance(paramContext);
/*  104 */     this.log = Log.instance(paramContext);
/*  105 */     this.syms = Symtab.instance(paramContext);
/*  106 */     this.annotate = Annotate.instance(paramContext);
/*  107 */     this.attr = Attr.instance(paramContext);
/*  108 */     Options options = Options.instance(paramContext);
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
/*      */   public void organizeTypeAnnotationsSignatures(final Env<AttrContext> env, final JCTree.JCClassDecl tree) {
/*  121 */     this.annotate.afterRepeated(new Annotate.Worker()
/*      */         {
/*      */           public void run() {
/*  124 */             JavaFileObject javaFileObject = TypeAnnotations.this.log.useSource(env.toplevel.sourcefile);
/*      */
/*      */             try {
/*  127 */               (new TypeAnnotationPositions(true)).scan((JCTree)tree);
/*      */             } finally {
/*  129 */               TypeAnnotations.this.log.useSource(javaFileObject);
/*      */             }
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */   public void validateTypeAnnotationsSignatures(final Env<AttrContext> env, final JCTree.JCClassDecl tree) {
/*  136 */     this.annotate.validate(new Annotate.Worker()
/*      */         {
/*      */           public void run() {
/*  139 */             JavaFileObject javaFileObject = TypeAnnotations.this.log.useSource(env.toplevel.sourcefile);
/*      */
/*      */             try {
/*  142 */               TypeAnnotations.this.attr.validateTypeAnnotations((JCTree)tree, true);
/*      */             } finally {
/*  144 */               TypeAnnotations.this.log.useSource(javaFileObject);
/*      */             }
/*      */           }
/*      */         });
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public void organizeTypeAnnotationsBodies(JCTree.JCClassDecl paramJCClassDecl) {
/*  155 */     (new TypeAnnotationPositions(false)).scan((JCTree)paramJCClassDecl);
/*      */   }
/*      */
/*  158 */   public enum AnnotationType { DECLARATION, TYPE, BOTH; }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   public AnnotationType annotationType(Attribute.Compound paramCompound, Symbol paramSymbol) {
/*  166 */     Attribute.Compound compound = paramCompound.type.tsym.attribute(this.syms.annotationTargetType.tsym);
/*  167 */     if (compound == null) {
/*  168 */       return inferTargetMetaInfo(paramCompound, paramSymbol);
/*      */     }
/*  170 */     Attribute attribute = compound.member(this.names.value);
/*  171 */     if (!(attribute instanceof Attribute.Array)) {
/*  172 */       Assert.error("annotationType(): bad @Target argument " + attribute + " (" + attribute
/*  173 */           .getClass() + ")");
/*  174 */       return AnnotationType.DECLARATION;
/*      */     }
/*  176 */     Attribute.Array array = (Attribute.Array)attribute;
/*  177 */     boolean bool1 = false, bool2 = false;
/*  178 */     for (Attribute attribute1 : array.values) {
/*  179 */       if (!(attribute1 instanceof Attribute.Enum)) {
/*  180 */         Assert.error("annotationType(): unrecognized Attribute kind " + attribute1 + " (" + attribute1
/*  181 */             .getClass() + ")");
/*  182 */         bool1 = true;
/*      */       } else {
/*      */
/*  185 */         Attribute.Enum enum_ = (Attribute.Enum)attribute1;
/*  186 */         if (enum_.value.name == this.names.TYPE) {
/*  187 */           if (paramSymbol.kind == 2)
/*  188 */             bool1 = true;
/*  189 */         } else if (enum_.value.name == this.names.FIELD) {
/*  190 */           if (paramSymbol.kind == 4 && paramSymbol.owner.kind != 16)
/*      */           {
/*  192 */             bool1 = true; }
/*  193 */         } else if (enum_.value.name == this.names.METHOD) {
/*  194 */           if (paramSymbol.kind == 16 &&
/*  195 */             !paramSymbol.isConstructor())
/*  196 */             bool1 = true;
/*  197 */         } else if (enum_.value.name == this.names.PARAMETER) {
/*  198 */           if (paramSymbol.kind == 4 && paramSymbol.owner.kind == 16 && (paramSymbol
/*      */
/*  200 */             .flags() & 0x200000000L) != 0L)
/*  201 */             bool1 = true;
/*  202 */         } else if (enum_.value.name == this.names.CONSTRUCTOR) {
/*  203 */           if (paramSymbol.kind == 16 && paramSymbol
/*  204 */             .isConstructor())
/*  205 */             bool1 = true;
/*  206 */         } else if (enum_.value.name == this.names.LOCAL_VARIABLE) {
/*  207 */           if (paramSymbol.kind == 4 && paramSymbol.owner.kind == 16 && (paramSymbol
/*      */
/*  209 */             .flags() & 0x200000000L) == 0L)
/*  210 */             bool1 = true;
/*  211 */         } else if (enum_.value.name == this.names.ANNOTATION_TYPE) {
/*  212 */           if (paramSymbol.kind == 2 && (paramSymbol
/*  213 */             .flags() & 0x2000L) != 0L)
/*  214 */             bool1 = true;
/*  215 */         } else if (enum_.value.name == this.names.PACKAGE) {
/*  216 */           if (paramSymbol.kind == 1)
/*  217 */             bool1 = true;
/*  218 */         } else if (enum_.value.name == this.names.TYPE_USE) {
/*  219 */           if (paramSymbol.kind == 2 || paramSymbol.kind == 4 || (paramSymbol.kind == 16 &&
/*      */
/*  221 */             !paramSymbol.isConstructor() &&
/*  222 */             !paramSymbol.type.getReturnType().hasTag(TypeTag.VOID)) || (paramSymbol.kind == 16 && paramSymbol
/*  223 */             .isConstructor()))
/*  224 */             bool2 = true;
/*  225 */         } else if (enum_.value.name != this.names.TYPE_PARAMETER) {
/*      */
/*      */
/*      */
/*      */
/*      */
/*  231 */           Assert.error("annotationType(): unrecognized Attribute name " + enum_.value.name + " (" + enum_.value.name
/*  232 */               .getClass() + ")");
/*  233 */           bool1 = true;
/*      */         }
/*      */       }
/*  236 */     }  if (bool1 && bool2)
/*  237 */       return AnnotationType.BOTH;
/*  238 */     if (bool2) {
/*  239 */       return AnnotationType.TYPE;
/*      */     }
/*  241 */     return AnnotationType.DECLARATION;
/*      */   }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */   private static AnnotationType inferTargetMetaInfo(Attribute.Compound paramCompound, Symbol paramSymbol) {
/*  249 */     return AnnotationType.DECLARATION;
/*      */   }
/*      */   private class TypeAnnotationPositions extends TreeScanner
/*      */   {
/*      */     private final boolean sigOnly;
/*      */     private ListBuffer<JCTree> frames;
/*      */     private boolean isInClass; private JCTree.JCLambda currentLambda; protected void push(JCTree param1JCTree) { this.frames = this.frames.prepend(param1JCTree); } protected JCTree pop() { return (JCTree)this.frames.next(); } private JCTree peek2() { return (JCTree)(this.frames.toList()).tail.head; } public void scan(JCTree param1JCTree) { push(param1JCTree); super.scan(param1JCTree); pop(); } private void separateAnnotationsKinds(JCTree param1JCTree, Type param1Type, Symbol param1Symbol, TypeAnnotationPosition param1TypeAnnotationPosition) { // Byte code:
/*      */       //   0: aload_3
/*      */       //   1: invokevirtual getRawAttributes : ()Lcom/sun/tools/javac/util/List;
/*      */       //   4: astore #5
/*      */       //   6: new com/sun/tools/javac/util/ListBuffer
/*      */       //   9: dup
/*      */       //   10: invokespecial <init> : ()V
/*      */       //   13: astore #6
/*      */       //   15: new com/sun/tools/javac/util/ListBuffer
/*      */       //   18: dup
/*      */       //   19: invokespecial <init> : ()V
/*      */       //   22: astore #7
/*      */       //   24: new com/sun/tools/javac/util/ListBuffer
/*      */       //   27: dup
/*      */       //   28: invokespecial <init> : ()V
/*      */       //   31: astore #8
/*      */       //   33: aload #5
/*      */       //   35: invokevirtual iterator : ()Ljava/util/Iterator;
/*      */       //   38: astore #9
/*      */       //   40: aload #9
/*      */       //   42: invokeinterface hasNext : ()Z
/*      */       //   47: ifeq -> 176
/*      */       //   50: aload #9
/*      */       //   52: invokeinterface next : ()Ljava/lang/Object;
/*      */       //   57: checkcast com/sun/tools/javac/code/Attribute$Compound
/*      */       //   60: astore #10
/*      */       //   62: getstatic com/sun/tools/javac/code/TypeAnnotations$3.$SwitchMap$com$sun$tools$javac$code$TypeAnnotations$AnnotationType : [I
/*      */       //   65: aload_0
/*      */       //   66: getfield this$0 : Lcom/sun/tools/javac/code/TypeAnnotations;
/*      */       //   69: aload #10
/*      */       //   71: aload_3
/*      */       //   72: invokevirtual annotationType : (Lcom/sun/tools/javac/code/Attribute$Compound;Lcom/sun/tools/javac/code/Symbol;)Lcom/sun/tools/javac/code/TypeAnnotations$AnnotationType;
/*      */       //   75: invokevirtual ordinal : ()I
/*      */       //   78: iaload
/*      */       //   79: tableswitch default -> 173, 1 -> 104, 2 -> 115, 3 -> 144
/*      */       //   104: aload #6
/*      */       //   106: aload #10
/*      */       //   108: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */       //   111: pop
/*      */       //   112: goto -> 173
/*      */       //   115: aload #6
/*      */       //   117: aload #10
/*      */       //   119: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */       //   122: pop
/*      */       //   123: aload_0
/*      */       //   124: aload #10
/*      */       //   126: aload #4
/*      */       //   128: invokespecial toTypeCompound : (Lcom/sun/tools/javac/code/Attribute$Compound;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)Lcom/sun/tools/javac/code/Attribute$TypeCompound;
/*      */       //   131: astore #11
/*      */       //   133: aload #7
/*      */       //   135: aload #11
/*      */       //   137: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */       //   140: pop
/*      */       //   141: goto -> 173
/*      */       //   144: aload_0
/*      */       //   145: aload #10
/*      */       //   147: aload #4
/*      */       //   149: invokespecial toTypeCompound : (Lcom/sun/tools/javac/code/Attribute$Compound;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)Lcom/sun/tools/javac/code/Attribute$TypeCompound;
/*      */       //   152: astore #11
/*      */       //   154: aload #7
/*      */       //   156: aload #11
/*      */       //   158: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */       //   161: pop
/*      */       //   162: aload #8
/*      */       //   164: aload #11
/*      */       //   166: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */       //   169: pop
/*      */       //   170: goto -> 173
/*      */       //   173: goto -> 40
/*      */       //   176: aload_3
/*      */       //   177: invokevirtual resetAnnotations : ()V
/*      */       //   180: aload_3
/*      */       //   181: aload #6
/*      */       //   183: invokevirtual toList : ()Lcom/sun/tools/javac/util/List;
/*      */       //   186: invokevirtual setDeclarationAttributes : (Lcom/sun/tools/javac/util/List;)V
/*      */       //   189: aload #7
/*      */       //   191: invokevirtual isEmpty : ()Z
/*      */       //   194: ifeq -> 198
/*      */       //   197: return
/*      */       //   198: aload #7
/*      */       //   200: invokevirtual toList : ()Lcom/sun/tools/javac/util/List;
/*      */       //   203: astore #9
/*      */       //   205: aload_2
/*      */       //   206: ifnonnull -> 235
/*      */       //   209: aload_3
/*      */       //   210: invokevirtual getEnclosingElement : ()Lcom/sun/tools/javac/code/Symbol;
/*      */       //   213: invokevirtual asType : ()Lcom/sun/tools/javac/code/Type;
/*      */       //   216: astore_2
/*      */       //   217: aload_0
/*      */       //   218: aload_1
/*      */       //   219: aload_2
/*      */       //   220: aload #9
/*      */       //   222: aload #9
/*      */       //   224: invokespecial typeWithAnnotations : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/code/Type;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/util/List;)Lcom/sun/tools/javac/code/Type;
/*      */       //   227: astore_2
/*      */       //   228: aload_3
/*      */       //   229: aload #9
/*      */       //   231: invokevirtual appendUniqueTypeAttributes : (Lcom/sun/tools/javac/util/List;)V
/*      */       //   234: return
/*      */       //   235: aload_0
/*      */       //   236: aload_1
/*      */       //   237: aload_2
/*      */       //   238: aload #9
/*      */       //   240: aload #8
/*      */       //   242: invokevirtual toList : ()Lcom/sun/tools/javac/util/List;
/*      */       //   245: invokespecial typeWithAnnotations : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/code/Type;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/util/List;)Lcom/sun/tools/javac/code/Type;
/*      */       //   248: astore_2
/*      */       //   249: aload_3
/*      */       //   250: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   253: getstatic javax/lang/model/element/ElementKind.METHOD : Ljavax/lang/model/element/ElementKind;
/*      */       //   256: if_acmpne -> 273
/*      */       //   259: aload_3
/*      */       //   260: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   263: invokevirtual asMethodType : ()Lcom/sun/tools/javac/code/Type$MethodType;
/*      */       //   266: aload_2
/*      */       //   267: putfield restype : Lcom/sun/tools/javac/code/Type;
/*      */       //   270: goto -> 438
/*      */       //   273: aload_3
/*      */       //   274: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   277: getstatic javax/lang/model/element/ElementKind.PARAMETER : Ljavax/lang/model/element/ElementKind;
/*      */       //   280: if_acmpne -> 433
/*      */       //   283: aload_3
/*      */       //   284: aload_2
/*      */       //   285: putfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   288: aload_3
/*      */       //   289: invokevirtual getQualifiedName : ()Lcom/sun/tools/javac/util/Name;
/*      */       //   292: aload_0
/*      */       //   293: getfield this$0 : Lcom/sun/tools/javac/code/TypeAnnotations;
/*      */       //   296: getfield names : Lcom/sun/tools/javac/util/Names;
/*      */       //   299: getfield _this : Lcom/sun/tools/javac/util/Name;
/*      */       //   302: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   305: ifeq -> 325
/*      */       //   308: aload_3
/*      */       //   309: getfield owner : Lcom/sun/tools/javac/code/Symbol;
/*      */       //   312: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   315: invokevirtual asMethodType : ()Lcom/sun/tools/javac/code/Type$MethodType;
/*      */       //   318: aload_2
/*      */       //   319: putfield recvtype : Lcom/sun/tools/javac/code/Type;
/*      */       //   322: goto -> 438
/*      */       //   325: aload_3
/*      */       //   326: getfield owner : Lcom/sun/tools/javac/code/Symbol;
/*      */       //   329: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   332: invokevirtual asMethodType : ()Lcom/sun/tools/javac/code/Type$MethodType;
/*      */       //   335: astore #10
/*      */       //   337: aload_3
/*      */       //   338: getfield owner : Lcom/sun/tools/javac/code/Symbol;
/*      */       //   341: checkcast com/sun/tools/javac/code/Symbol$MethodSymbol
/*      */       //   344: getfield params : Lcom/sun/tools/javac/util/List;
/*      */       //   347: astore #11
/*      */       //   349: aload #10
/*      */       //   351: getfield argtypes : Lcom/sun/tools/javac/util/List;
/*      */       //   354: astore #12
/*      */       //   356: new com/sun/tools/javac/util/ListBuffer
/*      */       //   359: dup
/*      */       //   360: invokespecial <init> : ()V
/*      */       //   363: astore #13
/*      */       //   365: aload #11
/*      */       //   367: invokevirtual nonEmpty : ()Z
/*      */       //   370: ifeq -> 420
/*      */       //   373: aload #11
/*      */       //   375: getfield head : Ljava/lang/Object;
/*      */       //   378: aload_3
/*      */       //   379: if_acmpne -> 392
/*      */       //   382: aload #13
/*      */       //   384: aload_2
/*      */       //   385: invokevirtual add : (Ljava/lang/Object;)Z
/*      */       //   388: pop
/*      */       //   389: goto -> 403
/*      */       //   392: aload #13
/*      */       //   394: aload #12
/*      */       //   396: getfield head : Ljava/lang/Object;
/*      */       //   399: invokevirtual add : (Ljava/lang/Object;)Z
/*      */       //   402: pop
/*      */       //   403: aload #12
/*      */       //   405: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   408: astore #12
/*      */       //   410: aload #11
/*      */       //   412: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   415: astore #11
/*      */       //   417: goto -> 365
/*      */       //   420: aload #10
/*      */       //   422: aload #13
/*      */       //   424: invokevirtual toList : ()Lcom/sun/tools/javac/util/List;
/*      */       //   427: putfield argtypes : Lcom/sun/tools/javac/util/List;
/*      */       //   430: goto -> 438
/*      */       //   433: aload_3
/*      */       //   434: aload_2
/*      */       //   435: putfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   438: aload_3
/*      */       //   439: aload #9
/*      */       //   441: invokevirtual appendUniqueTypeAttributes : (Lcom/sun/tools/javac/util/List;)V
/*      */       //   444: aload_3
/*      */       //   445: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   448: getstatic javax/lang/model/element/ElementKind.PARAMETER : Ljavax/lang/model/element/ElementKind;
/*      */       //   451: if_acmpeq -> 484
/*      */       //   454: aload_3
/*      */       //   455: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   458: getstatic javax/lang/model/element/ElementKind.LOCAL_VARIABLE : Ljavax/lang/model/element/ElementKind;
/*      */       //   461: if_acmpeq -> 484
/*      */       //   464: aload_3
/*      */       //   465: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   468: getstatic javax/lang/model/element/ElementKind.RESOURCE_VARIABLE : Ljavax/lang/model/element/ElementKind;
/*      */       //   471: if_acmpeq -> 484
/*      */       //   474: aload_3
/*      */       //   475: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   478: getstatic javax/lang/model/element/ElementKind.EXCEPTION_PARAMETER : Ljavax/lang/model/element/ElementKind;
/*      */       //   481: if_acmpne -> 495
/*      */       //   484: aload_3
/*      */       //   485: getfield owner : Lcom/sun/tools/javac/code/Symbol;
/*      */       //   488: aload_3
/*      */       //   489: invokevirtual getRawTypeAttributes : ()Lcom/sun/tools/javac/util/List;
/*      */       //   492: invokevirtual appendUniqueTypeAttributes : (Lcom/sun/tools/javac/util/List;)V
/*      */       //   495: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #288	-> 0
/*      */       //   #289	-> 6
/*      */       //   #290	-> 15
/*      */       //   #291	-> 24
/*      */       //   #293	-> 33
/*      */       //   #294	-> 62
/*      */       //   #296	-> 104
/*      */       //   #297	-> 112
/*      */       //   #299	-> 115
/*      */       //   #300	-> 123
/*      */       //   #301	-> 133
/*      */       //   #302	-> 141
/*      */       //   #305	-> 144
/*      */       //   #306	-> 154
/*      */       //   #308	-> 162
/*      */       //   #309	-> 170
/*      */       //   #312	-> 173
/*      */       //   #314	-> 176
/*      */       //   #315	-> 180
/*      */       //   #317	-> 189
/*      */       //   #318	-> 197
/*      */       //   #321	-> 198
/*      */       //   #323	-> 205
/*      */       //   #327	-> 209
/*      */       //   #331	-> 217
/*      */       //   #336	-> 228
/*      */       //   #337	-> 234
/*      */       //   #341	-> 235
/*      */       //   #343	-> 249
/*      */       //   #344	-> 259
/*      */       //   #345	-> 273
/*      */       //   #346	-> 283
/*      */       //   #347	-> 288
/*      */       //   #348	-> 308
/*      */       //   #351	-> 325
/*      */       //   #352	-> 337
/*      */       //   #353	-> 349
/*      */       //   #354	-> 356
/*      */       //   #355	-> 365
/*      */       //   #356	-> 373
/*      */       //   #357	-> 382
/*      */       //   #359	-> 392
/*      */       //   #361	-> 403
/*      */       //   #362	-> 410
/*      */       //   #364	-> 420
/*      */       //   #365	-> 430
/*      */       //   #367	-> 433
/*      */       //   #370	-> 438
/*      */       //   #372	-> 444
/*      */       //   #373	-> 455
/*      */       //   #374	-> 465
/*      */       //   #375	-> 475
/*      */       //   #378	-> 484
/*      */       //   #380	-> 495 } private Type typeWithAnnotations(JCTree param1JCTree, Type param1Type, List<Attribute.TypeCompound> param1List1, List<Attribute.TypeCompound> param1List2) { if (param1List1.isEmpty())
/*      */         return param1Type;  if (param1Type.hasTag(TypeTag.ARRAY)) { Type type4; Type.ArrayType arrayType1 = (Type.ArrayType)param1Type.unannotatedType(); Type.ArrayType arrayType2 = new Type.ArrayType(null, arrayType1.tsym); if (param1Type.isAnnotated()) { type4 = arrayType2.annotatedType(param1Type.getAnnotationMirrors()); } else { type4 = arrayType2; }  JCTree.JCArrayTypeTree jCArrayTypeTree = arrayTypeTree(param1JCTree); ListBuffer listBuffer1 = new ListBuffer(); listBuffer1 = listBuffer1.append(TypeAnnotationPosition.TypePathEntry.ARRAY); while (arrayType1.elemtype.hasTag(TypeTag.ARRAY)) { if (arrayType1.elemtype.isAnnotated()) { Type type = arrayType1.elemtype; arrayType1 = (Type.ArrayType)type.unannotatedType(); Type.ArrayType arrayType = arrayType2; arrayType2 = new Type.ArrayType(null, arrayType1.tsym); arrayType.elemtype = arrayType2.annotatedType(arrayType1.elemtype.getAnnotationMirrors()); } else { arrayType1 = (Type.ArrayType)arrayType1.elemtype; arrayType2.elemtype = new Type.ArrayType(null, arrayType1.tsym); arrayType2 = (Type.ArrayType)arrayType2.elemtype; }  jCArrayTypeTree = arrayTypeTree((JCTree)jCArrayTypeTree.elemtype); listBuffer1 = listBuffer1.append(TypeAnnotationPosition.TypePathEntry.ARRAY); }  Type type5 = typeWithAnnotations((JCTree)jCArrayTypeTree.elemtype, arrayType1.elemtype, param1List1, param1List2); arrayType2.elemtype = type5; Attribute.TypeCompound typeCompound = (Attribute.TypeCompound)param1List1.get(0); TypeAnnotationPosition typeAnnotationPosition = typeCompound.position; typeAnnotationPosition.location = typeAnnotationPosition.location.prependList(listBuffer1.toList()); param1JCTree.type = type4; return type4; }  if (param1Type.hasTag(TypeTag.TYPEVAR))
/*      */         return param1Type;  if (param1Type.getKind() == TypeKind.UNION) { JCTree.JCTypeUnion jCTypeUnion = (JCTree.JCTypeUnion)param1JCTree; JCTree.JCExpression jCExpression = (JCTree.JCExpression)jCTypeUnion.alternatives.get(0); Type type = typeWithAnnotations((JCTree)jCExpression, jCExpression.type, param1List1, param1List2); jCExpression.type = type; return param1Type; }  Type type1 = param1Type; Element element = param1Type.asElement(); JCTree jCTree = param1JCTree; while (element != null && element.getKind() != ElementKind.PACKAGE && type1 != null && type1.getKind() != TypeKind.NONE && type1.getKind() != TypeKind.ERROR && (jCTree.getKind() == Tree.Kind.MEMBER_SELECT || jCTree.getKind() == Tree.Kind.PARAMETERIZED_TYPE || jCTree.getKind() == Tree.Kind.ANNOTATED_TYPE)) { JCTree.JCExpression jCExpression2; JCTree jCTree1; if (jCTree.getKind() == Tree.Kind.MEMBER_SELECT) { type1 = type1.getEnclosingType(); element = element.getEnclosingElement(); jCExpression2 = ((JCTree.JCFieldAccess)jCTree).getExpression(); continue; }  if (jCExpression2.getKind() == Tree.Kind.PARAMETERIZED_TYPE) { jCTree1 = ((JCTree.JCTypeApply)jCExpression2).getType(); continue; }  JCTree.JCExpression jCExpression1 = ((JCTree.JCAnnotatedType)jCTree1).getUnderlyingType(); }  if (type1 != null && type1.hasTag(TypeTag.NONE)) { switch (param1List2.size()) { case 0:
/*      */             return param1Type;
/*      */           case 1:
/*      */             TypeAnnotations.this.log.error(param1JCTree.pos(), "cant.type.annotate.scoping.1", new Object[] { param1List2 }); }
/*      */          TypeAnnotations.this.log.error(param1JCTree.pos(), "cant.type.annotate.scoping", new Object[] { param1List2 }); }
/*      */        ListBuffer listBuffer = new ListBuffer(); Type type2 = type1; while (element != null && element.getKind() != ElementKind.PACKAGE && type2 != null && type2.getKind() != TypeKind.NONE && type2.getKind() != TypeKind.ERROR) { type2 = type2.getEnclosingType(); element = element.getEnclosingElement(); if (type2 != null && type2.getKind() != TypeKind.NONE)
/*      */           listBuffer = listBuffer.append(TypeAnnotationPosition.TypePathEntry.INNER_TYPE);  }
/*      */        if (listBuffer.nonEmpty()) { Attribute.TypeCompound typeCompound = (Attribute.TypeCompound)param1List1.get(0); TypeAnnotationPosition typeAnnotationPosition = typeCompound.position; typeAnnotationPosition.location = typeAnnotationPosition.location.appendList(listBuffer.toList()); }
/*  265 */        Type type3 = typeWithAnnotations(param1Type, type1, param1List1); param1JCTree.type = type3; return type3; } TypeAnnotationPositions(boolean param1Boolean) { this.frames = new ListBuffer();
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
/*      */
/*      */
/*      */
/* 1028 */       this.isInClass = false;
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
/* 1115 */       this.currentLambda = null; this.sigOnly = param1Boolean; }
/*      */     private JCTree.JCArrayTypeTree arrayTypeTree(JCTree param1JCTree) { if (param1JCTree.getKind() == Tree.Kind.ARRAY_TYPE)
/*      */         return (JCTree.JCArrayTypeTree)param1JCTree;  if (param1JCTree.getKind() == Tree.Kind.ANNOTATED_TYPE)
/* 1118 */         return (JCTree.JCArrayTypeTree)((JCTree.JCAnnotatedType)param1JCTree).underlyingType;  Assert.error("Could not determine array type from type tree: " + param1JCTree); return null; } public void visitLambda(JCTree.JCLambda param1JCLambda) { JCTree.JCLambda jCLambda = this.currentLambda;
/*      */
/* 1120 */       try { this.currentLambda = param1JCLambda;
/*      */
/* 1122 */         byte b = 0;
/* 1123 */         for (JCTree.JCVariableDecl jCVariableDecl : param1JCLambda.params) {
/* 1124 */           if (!jCVariableDecl.mods.annotations.isEmpty()) {
/*      */
/*      */
/* 1127 */             TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1128 */             typeAnnotationPosition.type = TargetType.METHOD_FORMAL_PARAMETER;
/* 1129 */             typeAnnotationPosition.parameter_index = b;
/* 1130 */             typeAnnotationPosition.pos = jCVariableDecl.vartype.pos;
/* 1131 */             typeAnnotationPosition.onLambda = param1JCLambda;
/* 1132 */             separateAnnotationsKinds((JCTree)jCVariableDecl.vartype, jCVariableDecl.sym.type, jCVariableDecl.sym, typeAnnotationPosition);
/*      */           }
/* 1134 */           b++;
/*      */         }
/*      */
/* 1137 */         push((JCTree)param1JCLambda);
/* 1138 */         scan(param1JCLambda.body);
/* 1139 */         scan(param1JCLambda.params);
/* 1140 */         pop(); }
/*      */       finally
/* 1142 */       { this.currentLambda = jCLambda; }  } private Type typeWithAnnotations(Type param1Type1, final Type stopAt, List<Attribute.TypeCompound> param1List) { Type.Visitor<Type, List<Attribute.TypeCompound>> visitor = new Type.Visitor<Type, List<Attribute.TypeCompound>>() { public Type visitClassType(Type.ClassType param2ClassType, List<Attribute.TypeCompound> param2List) { if (param2ClassType == stopAt || param2ClassType.getEnclosingType() == Type.noType) return param2ClassType.annotatedType(param2List);  Type.ClassType classType = new Type.ClassType(param2ClassType.getEnclosingType().<Type, List<Attribute.TypeCompound>>accept(this, param2List), param2ClassType.typarams_field, param2ClassType.tsym); classType.all_interfaces_field = param2ClassType.all_interfaces_field; classType.allparams_field = param2ClassType.allparams_field; classType.interfaces_field = param2ClassType.interfaces_field; classType.rank_field = param2ClassType.rank_field; classType.supertype_field = param2ClassType.supertype_field; return classType; } public Type visitAnnotatedType(Type.AnnotatedType param2AnnotatedType, List<Attribute.TypeCompound> param2List) { return ((Type)param2AnnotatedType.unannotatedType().<Type, List<Attribute.TypeCompound>>accept(this, param2List)).annotatedType(param2AnnotatedType.getAnnotationMirrors()); } public Type visitWildcardType(Type.WildcardType param2WildcardType, List<Attribute.TypeCompound> param2List) { return param2WildcardType.annotatedType(param2List); } public Type visitArrayType(Type.ArrayType param2ArrayType, List<Attribute.TypeCompound> param2List) { return new Type.ArrayType(param2ArrayType.elemtype.<Type, List<Attribute.TypeCompound>>accept(this, param2List), param2ArrayType.tsym); } public Type visitMethodType(Type.MethodType param2MethodType, List<Attribute.TypeCompound> param2List) { return param2MethodType; } public Type visitPackageType(Type.PackageType param2PackageType, List<Attribute.TypeCompound> param2List) { return param2PackageType; }
/*      */           public Type visitTypeVar(Type.TypeVar param2TypeVar, List<Attribute.TypeCompound> param2List) { return param2TypeVar.annotatedType(param2List); }
/*      */           public Type visitCapturedType(Type.CapturedType param2CapturedType, List<Attribute.TypeCompound> param2List) { return param2CapturedType.annotatedType(param2List); }
/*      */           public Type visitForAll(Type.ForAll param2ForAll, List<Attribute.TypeCompound> param2List) { return param2ForAll; }
/*      */           public Type visitUndetVar(Type.UndetVar param2UndetVar, List<Attribute.TypeCompound> param2List) { return param2UndetVar; }
/*      */           public Type visitErrorType(Type.ErrorType param2ErrorType, List<Attribute.TypeCompound> param2List) { return param2ErrorType.annotatedType(param2List); }
/*      */           public Type visitType(Type param2Type, List<Attribute.TypeCompound> param2List) { return param2Type.annotatedType(param2List); } }
/*      */         ; return param1Type1.<Type, List<Attribute.TypeCompound>>accept(visitor, param1List); }
/*      */     private Attribute.TypeCompound toTypeCompound(Attribute.Compound param1Compound, TypeAnnotationPosition param1TypeAnnotationPosition) { return new Attribute.TypeCompound(param1Compound, param1TypeAnnotationPosition); }
/*      */     private void resolveFrame(JCTree param1JCTree1, JCTree param1JCTree2, List<JCTree> param1List, TypeAnnotationPosition param1TypeAnnotationPosition) { // Byte code:
/*      */       //   0: getstatic com/sun/tools/javac/code/TypeAnnotations$3.$SwitchMap$com$sun$source$tree$Tree$Kind : [I
/*      */       //   3: aload_2
/*      */       //   4: invokevirtual getKind : ()Lcom/sun/source/tree/Tree$Kind;
/*      */       //   7: invokevirtual ordinal : ()I
/*      */       //   10: iaload
/*      */       //   11: tableswitch default -> 2255, 1 -> 108, 2 -> 155, 3 -> 173, 4 -> 346, 5 -> 364, 6 -> 364, 7 -> 364, 8 -> 364, 9 -> 515, 10 -> 656, 11 -> 856, 12 -> 1174, 13 -> 1310, 14 -> 1587, 15 -> 1811, 16 -> 1932, 17 -> 1966, 18 -> 2020, 19 -> 2171, 20 -> 2171, 21 -> 2221
/*      */       //   108: aload_2
/*      */       //   109: checkcast com/sun/tools/javac/tree/JCTree$JCTypeCast
/*      */       //   112: astore #5
/*      */       //   114: aload #4
/*      */       //   116: getstatic com/sun/tools/javac/code/TargetType.CAST : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   119: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   122: aload #5
/*      */       //   124: getfield clazz : Lcom/sun/tools/javac/tree/JCTree;
/*      */       //   127: getstatic com/sun/tools/javac/tree/JCTree$Tag.TYPEINTERSECTION : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   130: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   133: ifeq -> 139
/*      */       //   136: goto -> 145
/*      */       //   139: aload #4
/*      */       //   141: iconst_0
/*      */       //   142: putfield type_index : I
/*      */       //   145: aload #4
/*      */       //   147: aload_2
/*      */       //   148: getfield pos : I
/*      */       //   151: putfield pos : I
/*      */       //   154: return
/*      */       //   155: aload #4
/*      */       //   157: getstatic com/sun/tools/javac/code/TargetType.INSTANCEOF : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   160: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   163: aload #4
/*      */       //   165: aload_2
/*      */       //   166: getfield pos : I
/*      */       //   169: putfield pos : I
/*      */       //   172: return
/*      */       //   173: aload_2
/*      */       //   174: checkcast com/sun/tools/javac/tree/JCTree$JCNewClass
/*      */       //   177: astore #6
/*      */       //   179: aload #6
/*      */       //   181: getfield def : Lcom/sun/tools/javac/tree/JCTree$JCClassDecl;
/*      */       //   184: ifnull -> 291
/*      */       //   187: aload #6
/*      */       //   189: getfield def : Lcom/sun/tools/javac/tree/JCTree$JCClassDecl;
/*      */       //   192: astore #7
/*      */       //   194: aload #7
/*      */       //   196: getfield extending : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   199: aload_1
/*      */       //   200: if_acmpne -> 220
/*      */       //   203: aload #4
/*      */       //   205: getstatic com/sun/tools/javac/code/TargetType.CLASS_EXTENDS : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   208: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   211: aload #4
/*      */       //   213: iconst_m1
/*      */       //   214: putfield type_index : I
/*      */       //   217: goto -> 288
/*      */       //   220: aload #7
/*      */       //   222: getfield implementing : Lcom/sun/tools/javac/util/List;
/*      */       //   225: aload_1
/*      */       //   226: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   229: ifeq -> 257
/*      */       //   232: aload #4
/*      */       //   234: getstatic com/sun/tools/javac/code/TargetType.CLASS_EXTENDS : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   237: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   240: aload #4
/*      */       //   242: aload #7
/*      */       //   244: getfield implementing : Lcom/sun/tools/javac/util/List;
/*      */       //   247: aload_1
/*      */       //   248: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   251: putfield type_index : I
/*      */       //   254: goto -> 288
/*      */       //   257: new java/lang/StringBuilder
/*      */       //   260: dup
/*      */       //   261: invokespecial <init> : ()V
/*      */       //   264: ldc 'Could not determine position of tree '
/*      */       //   266: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   269: aload_1
/*      */       //   270: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   273: ldc ' within frame '
/*      */       //   275: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   278: aload_2
/*      */       //   279: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   282: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   285: invokestatic error : (Ljava/lang/String;)V
/*      */       //   288: goto -> 336
/*      */       //   291: aload #6
/*      */       //   293: getfield typeargs : Lcom/sun/tools/javac/util/List;
/*      */       //   296: aload_1
/*      */       //   297: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   300: ifeq -> 328
/*      */       //   303: aload #4
/*      */       //   305: getstatic com/sun/tools/javac/code/TargetType.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   308: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   311: aload #4
/*      */       //   313: aload #6
/*      */       //   315: getfield typeargs : Lcom/sun/tools/javac/util/List;
/*      */       //   318: aload_1
/*      */       //   319: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   322: putfield type_index : I
/*      */       //   325: goto -> 336
/*      */       //   328: aload #4
/*      */       //   330: getstatic com/sun/tools/javac/code/TargetType.NEW : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   333: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   336: aload #4
/*      */       //   338: aload_2
/*      */       //   339: getfield pos : I
/*      */       //   342: putfield pos : I
/*      */       //   345: return
/*      */       //   346: aload #4
/*      */       //   348: getstatic com/sun/tools/javac/code/TargetType.NEW : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   351: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   354: aload #4
/*      */       //   356: aload_2
/*      */       //   357: getfield pos : I
/*      */       //   360: putfield pos : I
/*      */       //   363: return
/*      */       //   364: aload #4
/*      */       //   366: aload_2
/*      */       //   367: getfield pos : I
/*      */       //   370: putfield pos : I
/*      */       //   373: aload_2
/*      */       //   374: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */       //   377: getfield extending : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   380: aload_1
/*      */       //   381: if_acmpne -> 401
/*      */       //   384: aload #4
/*      */       //   386: getstatic com/sun/tools/javac/code/TargetType.CLASS_EXTENDS : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   389: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   392: aload #4
/*      */       //   394: iconst_m1
/*      */       //   395: putfield type_index : I
/*      */       //   398: goto -> 514
/*      */       //   401: aload_2
/*      */       //   402: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */       //   405: getfield implementing : Lcom/sun/tools/javac/util/List;
/*      */       //   408: aload_1
/*      */       //   409: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   412: ifeq -> 442
/*      */       //   415: aload #4
/*      */       //   417: getstatic com/sun/tools/javac/code/TargetType.CLASS_EXTENDS : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   420: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   423: aload #4
/*      */       //   425: aload_2
/*      */       //   426: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */       //   429: getfield implementing : Lcom/sun/tools/javac/util/List;
/*      */       //   432: aload_1
/*      */       //   433: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   436: putfield type_index : I
/*      */       //   439: goto -> 514
/*      */       //   442: aload_2
/*      */       //   443: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */       //   446: getfield typarams : Lcom/sun/tools/javac/util/List;
/*      */       //   449: aload_1
/*      */       //   450: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   453: ifeq -> 483
/*      */       //   456: aload #4
/*      */       //   458: getstatic com/sun/tools/javac/code/TargetType.CLASS_TYPE_PARAMETER : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   461: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   464: aload #4
/*      */       //   466: aload_2
/*      */       //   467: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */       //   470: getfield typarams : Lcom/sun/tools/javac/util/List;
/*      */       //   473: aload_1
/*      */       //   474: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   477: putfield parameter_index : I
/*      */       //   480: goto -> 514
/*      */       //   483: new java/lang/StringBuilder
/*      */       //   486: dup
/*      */       //   487: invokespecial <init> : ()V
/*      */       //   490: ldc 'Could not determine position of tree '
/*      */       //   492: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   495: aload_1
/*      */       //   496: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   499: ldc ' within frame '
/*      */       //   501: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   504: aload_2
/*      */       //   505: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   508: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   511: invokestatic error : (Ljava/lang/String;)V
/*      */       //   514: return
/*      */       //   515: aload_2
/*      */       //   516: checkcast com/sun/tools/javac/tree/JCTree$JCMethodDecl
/*      */       //   519: astore #7
/*      */       //   521: aload #4
/*      */       //   523: aload_2
/*      */       //   524: getfield pos : I
/*      */       //   527: putfield pos : I
/*      */       //   530: aload #7
/*      */       //   532: getfield thrown : Lcom/sun/tools/javac/util/List;
/*      */       //   535: aload_1
/*      */       //   536: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   539: ifeq -> 567
/*      */       //   542: aload #4
/*      */       //   544: getstatic com/sun/tools/javac/code/TargetType.THROWS : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   547: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   550: aload #4
/*      */       //   552: aload #7
/*      */       //   554: getfield thrown : Lcom/sun/tools/javac/util/List;
/*      */       //   557: aload_1
/*      */       //   558: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   561: putfield type_index : I
/*      */       //   564: goto -> 655
/*      */       //   567: aload #7
/*      */       //   569: getfield restype : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   572: aload_1
/*      */       //   573: if_acmpne -> 587
/*      */       //   576: aload #4
/*      */       //   578: getstatic com/sun/tools/javac/code/TargetType.METHOD_RETURN : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   581: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   584: goto -> 655
/*      */       //   587: aload #7
/*      */       //   589: getfield typarams : Lcom/sun/tools/javac/util/List;
/*      */       //   592: aload_1
/*      */       //   593: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   596: ifeq -> 624
/*      */       //   599: aload #4
/*      */       //   601: getstatic com/sun/tools/javac/code/TargetType.METHOD_TYPE_PARAMETER : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   604: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   607: aload #4
/*      */       //   609: aload #7
/*      */       //   611: getfield typarams : Lcom/sun/tools/javac/util/List;
/*      */       //   614: aload_1
/*      */       //   615: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   618: putfield parameter_index : I
/*      */       //   621: goto -> 655
/*      */       //   624: new java/lang/StringBuilder
/*      */       //   627: dup
/*      */       //   628: invokespecial <init> : ()V
/*      */       //   631: ldc 'Could not determine position of tree '
/*      */       //   633: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   636: aload_1
/*      */       //   637: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   640: ldc ' within frame '
/*      */       //   642: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   645: aload_2
/*      */       //   646: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   649: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   652: invokestatic error : (Ljava/lang/String;)V
/*      */       //   655: return
/*      */       //   656: aload_3
/*      */       //   657: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   660: astore #7
/*      */       //   662: aload_2
/*      */       //   663: checkcast com/sun/tools/javac/tree/JCTree$JCTypeApply
/*      */       //   666: getfield clazz : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   669: aload_1
/*      */       //   670: if_acmpne -> 676
/*      */       //   673: goto -> 828
/*      */       //   676: aload_2
/*      */       //   677: checkcast com/sun/tools/javac/tree/JCTree$JCTypeApply
/*      */       //   680: getfield arguments : Lcom/sun/tools/javac/util/List;
/*      */       //   683: aload_1
/*      */       //   684: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   687: ifeq -> 797
/*      */       //   690: aload_2
/*      */       //   691: checkcast com/sun/tools/javac/tree/JCTree$JCTypeApply
/*      */       //   694: astore #8
/*      */       //   696: aload #8
/*      */       //   698: getfield arguments : Lcom/sun/tools/javac/util/List;
/*      */       //   701: aload_1
/*      */       //   702: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   705: istore #9
/*      */       //   707: aload #4
/*      */       //   709: aload #4
/*      */       //   711: getfield location : Lcom/sun/tools/javac/util/List;
/*      */       //   714: new com/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntry
/*      */       //   717: dup
/*      */       //   718: getstatic com/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntryKind.TYPE_ARGUMENT : Lcom/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntryKind;
/*      */       //   721: iload #9
/*      */       //   723: invokespecial <init> : (Lcom/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntryKind;I)V
/*      */       //   726: invokevirtual prepend : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/List;
/*      */       //   729: putfield location : Lcom/sun/tools/javac/util/List;
/*      */       //   732: aload #7
/*      */       //   734: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   737: ifnull -> 779
/*      */       //   740: aload #7
/*      */       //   742: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   745: getfield head : Ljava/lang/Object;
/*      */       //   748: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   751: getstatic com/sun/tools/javac/tree/JCTree$Tag.NEWCLASS : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   754: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   757: ifeq -> 779
/*      */       //   760: aload #7
/*      */       //   762: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   765: getfield head : Ljava/lang/Object;
/*      */       //   768: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   771: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   774: astore #10
/*      */       //   776: goto -> 786
/*      */       //   779: aload #8
/*      */       //   781: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   784: astore #10
/*      */       //   786: aload_0
/*      */       //   787: aload #10
/*      */       //   789: aload #4
/*      */       //   791: invokespecial locateNestedTypes : (Lcom/sun/tools/javac/code/Type;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   794: goto -> 828
/*      */       //   797: new java/lang/StringBuilder
/*      */       //   800: dup
/*      */       //   801: invokespecial <init> : ()V
/*      */       //   804: ldc 'Could not determine type argument position of tree '
/*      */       //   806: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   809: aload_1
/*      */       //   810: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   813: ldc ' within frame '
/*      */       //   815: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   818: aload_2
/*      */       //   819: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   822: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   825: invokestatic error : (Ljava/lang/String;)V
/*      */       //   828: aload_0
/*      */       //   829: aload #7
/*      */       //   831: getfield head : Ljava/lang/Object;
/*      */       //   834: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   837: aload #7
/*      */       //   839: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   842: getfield head : Ljava/lang/Object;
/*      */       //   845: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   848: aload #7
/*      */       //   850: aload #4
/*      */       //   852: invokespecial resolveFrame : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   855: return
/*      */       //   856: aload_2
/*      */       //   857: checkcast com/sun/tools/javac/tree/JCTree$JCMemberReference
/*      */       //   860: astore #7
/*      */       //   862: aload #7
/*      */       //   864: getfield expr : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   867: aload_1
/*      */       //   868: if_acmpne -> 986
/*      */       //   871: getstatic com/sun/tools/javac/code/TypeAnnotations$3.$SwitchMap$com$sun$source$tree$MemberReferenceTree$ReferenceMode : [I
/*      */       //   874: aload #7
/*      */       //   876: getfield mode : Lcom/sun/source/tree/MemberReferenceTree$ReferenceMode;
/*      */       //   879: invokevirtual ordinal : ()I
/*      */       //   882: iaload
/*      */       //   883: lookupswitch default -> 930, 1 -> 908, 2 -> 919
/*      */       //   908: aload #4
/*      */       //   910: getstatic com/sun/tools/javac/code/TargetType.METHOD_REFERENCE : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   913: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   916: goto -> 974
/*      */       //   919: aload #4
/*      */       //   921: getstatic com/sun/tools/javac/code/TargetType.CONSTRUCTOR_REFERENCE : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   924: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   927: goto -> 974
/*      */       //   930: new java/lang/StringBuilder
/*      */       //   933: dup
/*      */       //   934: invokespecial <init> : ()V
/*      */       //   937: ldc 'Unknown method reference mode '
/*      */       //   939: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   942: aload #7
/*      */       //   944: getfield mode : Lcom/sun/source/tree/MemberReferenceTree$ReferenceMode;
/*      */       //   947: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   950: ldc ' for tree '
/*      */       //   952: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   955: aload_1
/*      */       //   956: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   959: ldc ' within frame '
/*      */       //   961: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   964: aload_2
/*      */       //   965: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   968: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   971: invokestatic error : (Ljava/lang/String;)V
/*      */       //   974: aload #4
/*      */       //   976: aload_2
/*      */       //   977: getfield pos : I
/*      */       //   980: putfield pos : I
/*      */       //   983: goto -> 1173
/*      */       //   986: aload #7
/*      */       //   988: getfield typeargs : Lcom/sun/tools/javac/util/List;
/*      */       //   991: ifnull -> 1142
/*      */       //   994: aload #7
/*      */       //   996: getfield typeargs : Lcom/sun/tools/javac/util/List;
/*      */       //   999: aload_1
/*      */       //   1000: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   1003: ifeq -> 1142
/*      */       //   1006: aload #7
/*      */       //   1008: getfield typeargs : Lcom/sun/tools/javac/util/List;
/*      */       //   1011: aload_1
/*      */       //   1012: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   1015: istore #8
/*      */       //   1017: aload #4
/*      */       //   1019: iload #8
/*      */       //   1021: putfield type_index : I
/*      */       //   1024: getstatic com/sun/tools/javac/code/TypeAnnotations$3.$SwitchMap$com$sun$source$tree$MemberReferenceTree$ReferenceMode : [I
/*      */       //   1027: aload #7
/*      */       //   1029: getfield mode : Lcom/sun/source/tree/MemberReferenceTree$ReferenceMode;
/*      */       //   1032: invokevirtual ordinal : ()I
/*      */       //   1035: iaload
/*      */       //   1036: lookupswitch default -> 1086, 1 -> 1064, 2 -> 1075
/*      */       //   1064: aload #4
/*      */       //   1066: getstatic com/sun/tools/javac/code/TargetType.METHOD_REFERENCE_TYPE_ARGUMENT : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1069: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1072: goto -> 1130
/*      */       //   1075: aload #4
/*      */       //   1077: getstatic com/sun/tools/javac/code/TargetType.CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1080: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1083: goto -> 1130
/*      */       //   1086: new java/lang/StringBuilder
/*      */       //   1089: dup
/*      */       //   1090: invokespecial <init> : ()V
/*      */       //   1093: ldc 'Unknown method reference mode '
/*      */       //   1095: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1098: aload #7
/*      */       //   1100: getfield mode : Lcom/sun/source/tree/MemberReferenceTree$ReferenceMode;
/*      */       //   1103: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1106: ldc ' for tree '
/*      */       //   1108: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1111: aload_1
/*      */       //   1112: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1115: ldc ' within frame '
/*      */       //   1117: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1120: aload_2
/*      */       //   1121: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1124: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   1127: invokestatic error : (Ljava/lang/String;)V
/*      */       //   1130: aload #4
/*      */       //   1132: aload_2
/*      */       //   1133: getfield pos : I
/*      */       //   1136: putfield pos : I
/*      */       //   1139: goto -> 1173
/*      */       //   1142: new java/lang/StringBuilder
/*      */       //   1145: dup
/*      */       //   1146: invokespecial <init> : ()V
/*      */       //   1149: ldc 'Could not determine type argument position of tree '
/*      */       //   1151: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1154: aload_1
/*      */       //   1155: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1158: ldc ' within frame '
/*      */       //   1160: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1163: aload_2
/*      */       //   1164: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1167: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   1170: invokestatic error : (Ljava/lang/String;)V
/*      */       //   1173: return
/*      */       //   1174: new com/sun/tools/javac/util/ListBuffer
/*      */       //   1177: dup
/*      */       //   1178: invokespecial <init> : ()V
/*      */       //   1181: astore #7
/*      */       //   1183: aload #7
/*      */       //   1185: getstatic com/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntry.ARRAY : Lcom/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntry;
/*      */       //   1188: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */       //   1191: astore #7
/*      */       //   1193: aload_3
/*      */       //   1194: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1197: astore #8
/*      */       //   1199: aload #8
/*      */       //   1201: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1204: getfield head : Ljava/lang/Object;
/*      */       //   1207: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1210: astore #9
/*      */       //   1212: aload #9
/*      */       //   1214: getstatic com/sun/tools/javac/tree/JCTree$Tag.TYPEARRAY : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   1217: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   1220: ifeq -> 1243
/*      */       //   1223: aload #8
/*      */       //   1225: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1228: astore #8
/*      */       //   1230: aload #7
/*      */       //   1232: getstatic com/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntry.ARRAY : Lcom/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntry;
/*      */       //   1235: invokevirtual append : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/ListBuffer;
/*      */       //   1238: astore #7
/*      */       //   1240: goto -> 1261
/*      */       //   1243: aload #9
/*      */       //   1245: getstatic com/sun/tools/javac/tree/JCTree$Tag.ANNOTATED_TYPE : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   1248: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   1251: ifeq -> 1264
/*      */       //   1254: aload #8
/*      */       //   1256: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1259: astore #8
/*      */       //   1261: goto -> 1199
/*      */       //   1264: aload #4
/*      */       //   1266: aload #4
/*      */       //   1268: getfield location : Lcom/sun/tools/javac/util/List;
/*      */       //   1271: aload #7
/*      */       //   1273: invokevirtual toList : ()Lcom/sun/tools/javac/util/List;
/*      */       //   1276: invokevirtual prependList : (Lcom/sun/tools/javac/util/List;)Lcom/sun/tools/javac/util/List;
/*      */       //   1279: putfield location : Lcom/sun/tools/javac/util/List;
/*      */       //   1282: aload_0
/*      */       //   1283: aload #8
/*      */       //   1285: getfield head : Ljava/lang/Object;
/*      */       //   1288: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1291: aload #8
/*      */       //   1293: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1296: getfield head : Ljava/lang/Object;
/*      */       //   1299: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1302: aload #8
/*      */       //   1304: aload #4
/*      */       //   1306: invokespecial resolveFrame : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   1309: return
/*      */       //   1310: aload_3
/*      */       //   1311: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1314: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1317: getfield head : Ljava/lang/Object;
/*      */       //   1320: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1323: getstatic com/sun/tools/javac/tree/JCTree$Tag.CLASSDEF : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   1326: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   1329: ifeq -> 1428
/*      */       //   1332: aload_3
/*      */       //   1333: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1336: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1339: getfield head : Ljava/lang/Object;
/*      */       //   1342: checkcast com/sun/tools/javac/tree/JCTree$JCClassDecl
/*      */       //   1345: astore #7
/*      */       //   1347: aload #4
/*      */       //   1349: getstatic com/sun/tools/javac/code/TargetType.CLASS_TYPE_PARAMETER_BOUND : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1352: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1355: aload #4
/*      */       //   1357: aload #7
/*      */       //   1359: getfield typarams : Lcom/sun/tools/javac/util/List;
/*      */       //   1362: aload_3
/*      */       //   1363: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1366: getfield head : Ljava/lang/Object;
/*      */       //   1369: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   1372: putfield parameter_index : I
/*      */       //   1375: aload #4
/*      */       //   1377: aload_2
/*      */       //   1378: checkcast com/sun/tools/javac/tree/JCTree$JCTypeParameter
/*      */       //   1381: getfield bounds : Lcom/sun/tools/javac/util/List;
/*      */       //   1384: aload_1
/*      */       //   1385: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   1388: putfield bound_index : I
/*      */       //   1391: aload_2
/*      */       //   1392: checkcast com/sun/tools/javac/tree/JCTree$JCTypeParameter
/*      */       //   1395: getfield bounds : Lcom/sun/tools/javac/util/List;
/*      */       //   1398: iconst_0
/*      */       //   1399: invokevirtual get : (I)Ljava/lang/Object;
/*      */       //   1402: checkcast com/sun/tools/javac/tree/JCTree$JCExpression
/*      */       //   1405: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   1408: invokevirtual isInterface : ()Z
/*      */       //   1411: ifeq -> 1425
/*      */       //   1414: aload #4
/*      */       //   1416: dup
/*      */       //   1417: getfield bound_index : I
/*      */       //   1420: iconst_1
/*      */       //   1421: iadd
/*      */       //   1422: putfield bound_index : I
/*      */       //   1425: goto -> 1577
/*      */       //   1428: aload_3
/*      */       //   1429: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1432: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1435: getfield head : Ljava/lang/Object;
/*      */       //   1438: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1441: getstatic com/sun/tools/javac/tree/JCTree$Tag.METHODDEF : Lcom/sun/tools/javac/tree/JCTree$Tag;
/*      */       //   1444: invokevirtual hasTag : (Lcom/sun/tools/javac/tree/JCTree$Tag;)Z
/*      */       //   1447: ifeq -> 1546
/*      */       //   1450: aload_3
/*      */       //   1451: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1454: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1457: getfield head : Ljava/lang/Object;
/*      */       //   1460: checkcast com/sun/tools/javac/tree/JCTree$JCMethodDecl
/*      */       //   1463: astore #7
/*      */       //   1465: aload #4
/*      */       //   1467: getstatic com/sun/tools/javac/code/TargetType.METHOD_TYPE_PARAMETER_BOUND : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1470: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1473: aload #4
/*      */       //   1475: aload #7
/*      */       //   1477: getfield typarams : Lcom/sun/tools/javac/util/List;
/*      */       //   1480: aload_3
/*      */       //   1481: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1484: getfield head : Ljava/lang/Object;
/*      */       //   1487: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   1490: putfield parameter_index : I
/*      */       //   1493: aload #4
/*      */       //   1495: aload_2
/*      */       //   1496: checkcast com/sun/tools/javac/tree/JCTree$JCTypeParameter
/*      */       //   1499: getfield bounds : Lcom/sun/tools/javac/util/List;
/*      */       //   1502: aload_1
/*      */       //   1503: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   1506: putfield bound_index : I
/*      */       //   1509: aload_2
/*      */       //   1510: checkcast com/sun/tools/javac/tree/JCTree$JCTypeParameter
/*      */       //   1513: getfield bounds : Lcom/sun/tools/javac/util/List;
/*      */       //   1516: iconst_0
/*      */       //   1517: invokevirtual get : (I)Ljava/lang/Object;
/*      */       //   1520: checkcast com/sun/tools/javac/tree/JCTree$JCExpression
/*      */       //   1523: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   1526: invokevirtual isInterface : ()Z
/*      */       //   1529: ifeq -> 1543
/*      */       //   1532: aload #4
/*      */       //   1534: dup
/*      */       //   1535: getfield bound_index : I
/*      */       //   1538: iconst_1
/*      */       //   1539: iadd
/*      */       //   1540: putfield bound_index : I
/*      */       //   1543: goto -> 1577
/*      */       //   1546: new java/lang/StringBuilder
/*      */       //   1549: dup
/*      */       //   1550: invokespecial <init> : ()V
/*      */       //   1553: ldc 'Could not determine position of tree '
/*      */       //   1555: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1558: aload_1
/*      */       //   1559: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1562: ldc ' within frame '
/*      */       //   1564: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1567: aload_2
/*      */       //   1568: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1571: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   1574: invokestatic error : (Ljava/lang/String;)V
/*      */       //   1577: aload #4
/*      */       //   1579: aload_2
/*      */       //   1580: getfield pos : I
/*      */       //   1583: putfield pos : I
/*      */       //   1586: return
/*      */       //   1587: aload_2
/*      */       //   1588: checkcast com/sun/tools/javac/tree/JCTree$JCVariableDecl
/*      */       //   1591: getfield sym : Lcom/sun/tools/javac/code/Symbol$VarSymbol;
/*      */       //   1594: astore #7
/*      */       //   1596: aload #4
/*      */       //   1598: aload_2
/*      */       //   1599: getfield pos : I
/*      */       //   1602: putfield pos : I
/*      */       //   1605: getstatic com/sun/tools/javac/code/TypeAnnotations$3.$SwitchMap$javax$lang$model$element$ElementKind : [I
/*      */       //   1608: aload #7
/*      */       //   1610: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   1613: invokevirtual ordinal : ()I
/*      */       //   1616: iaload
/*      */       //   1617: tableswitch default -> 1750, 1 -> 1652, 2 -> 1663, 3 -> 1674, 4 -> 1728, 5 -> 1739
/*      */       //   1652: aload #4
/*      */       //   1654: getstatic com/sun/tools/javac/code/TargetType.LOCAL_VARIABLE : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1657: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1660: goto -> 1786
/*      */       //   1663: aload #4
/*      */       //   1665: getstatic com/sun/tools/javac/code/TargetType.FIELD : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1668: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1671: goto -> 1786
/*      */       //   1674: aload #7
/*      */       //   1676: invokevirtual getQualifiedName : ()Lcom/sun/tools/javac/util/Name;
/*      */       //   1679: aload_0
/*      */       //   1680: getfield this$0 : Lcom/sun/tools/javac/code/TypeAnnotations;
/*      */       //   1683: getfield names : Lcom/sun/tools/javac/util/Names;
/*      */       //   1686: getfield _this : Lcom/sun/tools/javac/util/Name;
/*      */       //   1689: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   1692: ifeq -> 1706
/*      */       //   1695: aload #4
/*      */       //   1697: getstatic com/sun/tools/javac/code/TargetType.METHOD_RECEIVER : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1700: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1703: goto -> 1786
/*      */       //   1706: aload #4
/*      */       //   1708: getstatic com/sun/tools/javac/code/TargetType.METHOD_FORMAL_PARAMETER : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1711: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1714: aload #4
/*      */       //   1716: aload_0
/*      */       //   1717: aload_3
/*      */       //   1718: aload_2
/*      */       //   1719: invokespecial methodParamIndex : (Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/tree/JCTree;)I
/*      */       //   1722: putfield parameter_index : I
/*      */       //   1725: goto -> 1786
/*      */       //   1728: aload #4
/*      */       //   1730: getstatic com/sun/tools/javac/code/TargetType.EXCEPTION_PARAMETER : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1733: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1736: goto -> 1786
/*      */       //   1739: aload #4
/*      */       //   1741: getstatic com/sun/tools/javac/code/TargetType.RESOURCE_VARIABLE : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1744: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   1747: goto -> 1786
/*      */       //   1750: new java/lang/StringBuilder
/*      */       //   1753: dup
/*      */       //   1754: invokespecial <init> : ()V
/*      */       //   1757: ldc 'Found unexpected type annotation for variable: '
/*      */       //   1759: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1762: aload #7
/*      */       //   1764: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1767: ldc ' with kind: '
/*      */       //   1769: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1772: aload #7
/*      */       //   1774: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   1777: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   1780: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   1783: invokestatic error : (Ljava/lang/String;)V
/*      */       //   1786: aload #7
/*      */       //   1788: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   1791: getstatic javax/lang/model/element/ElementKind.FIELD : Ljavax/lang/model/element/ElementKind;
/*      */       //   1794: if_acmpeq -> 1810
/*      */       //   1797: aload #7
/*      */       //   1799: getfield owner : Lcom/sun/tools/javac/code/Symbol;
/*      */       //   1802: aload #7
/*      */       //   1804: invokevirtual getRawTypeAttributes : ()Lcom/sun/tools/javac/util/List;
/*      */       //   1807: invokevirtual appendUniqueTypeAttributes : (Lcom/sun/tools/javac/util/List;)V
/*      */       //   1810: return
/*      */       //   1811: aload_2
/*      */       //   1812: aload_1
/*      */       //   1813: if_acmpne -> 1898
/*      */       //   1816: aload_2
/*      */       //   1817: checkcast com/sun/tools/javac/tree/JCTree$JCAnnotatedType
/*      */       //   1820: astore #8
/*      */       //   1822: aload #8
/*      */       //   1824: getfield underlyingType : Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   1827: getfield type : Lcom/sun/tools/javac/code/Type;
/*      */       //   1830: astore #9
/*      */       //   1832: aload #9
/*      */       //   1834: ifnonnull -> 1838
/*      */       //   1837: return
/*      */       //   1838: aload #9
/*      */       //   1840: getfield tsym : Lcom/sun/tools/javac/code/Symbol$TypeSymbol;
/*      */       //   1843: astore #10
/*      */       //   1845: aload #10
/*      */       //   1847: invokevirtual getKind : ()Ljavax/lang/model/element/ElementKind;
/*      */       //   1850: getstatic javax/lang/model/element/ElementKind.TYPE_PARAMETER : Ljavax/lang/model/element/ElementKind;
/*      */       //   1853: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   1856: ifne -> 1898
/*      */       //   1859: aload #9
/*      */       //   1861: invokevirtual getKind : ()Ljavax/lang/model/type/TypeKind;
/*      */       //   1864: getstatic javax/lang/model/type/TypeKind.WILDCARD : Ljavax/lang/model/type/TypeKind;
/*      */       //   1867: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   1870: ifne -> 1898
/*      */       //   1873: aload #9
/*      */       //   1875: invokevirtual getKind : ()Ljavax/lang/model/type/TypeKind;
/*      */       //   1878: getstatic javax/lang/model/type/TypeKind.ARRAY : Ljavax/lang/model/type/TypeKind;
/*      */       //   1881: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   1884: ifeq -> 1890
/*      */       //   1887: goto -> 1898
/*      */       //   1890: aload_0
/*      */       //   1891: aload #9
/*      */       //   1893: aload #4
/*      */       //   1895: invokespecial locateNestedTypes : (Lcom/sun/tools/javac/code/Type;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   1898: aload_3
/*      */       //   1899: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1902: astore #8
/*      */       //   1904: aload_0
/*      */       //   1905: aload #8
/*      */       //   1907: getfield head : Ljava/lang/Object;
/*      */       //   1910: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1913: aload #8
/*      */       //   1915: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1918: getfield head : Ljava/lang/Object;
/*      */       //   1921: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1924: aload #8
/*      */       //   1926: aload #4
/*      */       //   1928: invokespecial resolveFrame : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   1931: return
/*      */       //   1932: aload_3
/*      */       //   1933: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1936: astore #8
/*      */       //   1938: aload_0
/*      */       //   1939: aload #8
/*      */       //   1941: getfield head : Ljava/lang/Object;
/*      */       //   1944: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1947: aload #8
/*      */       //   1949: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1952: getfield head : Ljava/lang/Object;
/*      */       //   1955: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   1958: aload #8
/*      */       //   1960: aload #4
/*      */       //   1962: invokespecial resolveFrame : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   1965: return
/*      */       //   1966: aload_2
/*      */       //   1967: checkcast com/sun/tools/javac/tree/JCTree$JCTypeIntersection
/*      */       //   1970: astore #8
/*      */       //   1972: aload #4
/*      */       //   1974: aload #8
/*      */       //   1976: getfield bounds : Lcom/sun/tools/javac/util/List;
/*      */       //   1979: aload_1
/*      */       //   1980: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   1983: putfield type_index : I
/*      */       //   1986: aload_3
/*      */       //   1987: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   1990: astore #9
/*      */       //   1992: aload_0
/*      */       //   1993: aload #9
/*      */       //   1995: getfield head : Ljava/lang/Object;
/*      */       //   1998: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   2001: aload #9
/*      */       //   2003: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   2006: getfield head : Ljava/lang/Object;
/*      */       //   2009: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   2012: aload #9
/*      */       //   2014: aload #4
/*      */       //   2016: invokespecial resolveFrame : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   2019: return
/*      */       //   2020: aload_2
/*      */       //   2021: checkcast com/sun/tools/javac/tree/JCTree$JCMethodInvocation
/*      */       //   2024: astore #8
/*      */       //   2026: aload #8
/*      */       //   2028: getfield typeargs : Lcom/sun/tools/javac/util/List;
/*      */       //   2031: aload_1
/*      */       //   2032: invokevirtual contains : (Ljava/lang/Object;)Z
/*      */       //   2035: ifne -> 2070
/*      */       //   2038: new java/lang/StringBuilder
/*      */       //   2041: dup
/*      */       //   2042: invokespecial <init> : ()V
/*      */       //   2045: ldc '{'
/*      */       //   2047: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   2050: aload_1
/*      */       //   2051: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   2054: ldc '} is not an argument in the invocation: '
/*      */       //   2056: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   2059: aload #8
/*      */       //   2061: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   2064: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   2067: invokestatic error : (Ljava/lang/String;)V
/*      */       //   2070: aload #8
/*      */       //   2072: invokevirtual getMethodSelect : ()Lcom/sun/tools/javac/tree/JCTree$JCExpression;
/*      */       //   2075: invokestatic symbol : (Lcom/sun/tools/javac/tree/JCTree;)Lcom/sun/tools/javac/code/Symbol;
/*      */       //   2078: checkcast com/sun/tools/javac/code/Symbol$MethodSymbol
/*      */       //   2081: astore #9
/*      */       //   2083: aload #9
/*      */       //   2085: ifnonnull -> 2119
/*      */       //   2088: new java/lang/StringBuilder
/*      */       //   2091: dup
/*      */       //   2092: invokespecial <init> : ()V
/*      */       //   2095: ldc 'could not determine symbol for {'
/*      */       //   2097: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   2100: aload #8
/*      */       //   2102: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   2105: ldc '}'
/*      */       //   2107: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   2110: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   2113: invokestatic error : (Ljava/lang/String;)V
/*      */       //   2116: goto -> 2146
/*      */       //   2119: aload #9
/*      */       //   2121: invokevirtual isConstructor : ()Z
/*      */       //   2124: ifeq -> 2138
/*      */       //   2127: aload #4
/*      */       //   2129: getstatic com/sun/tools/javac/code/TargetType.CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   2132: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   2135: goto -> 2146
/*      */       //   2138: aload #4
/*      */       //   2140: getstatic com/sun/tools/javac/code/TargetType.METHOD_INVOCATION_TYPE_ARGUMENT : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   2143: putfield type : Lcom/sun/tools/javac/code/TargetType;
/*      */       //   2146: aload #4
/*      */       //   2148: aload #8
/*      */       //   2150: getfield pos : I
/*      */       //   2153: putfield pos : I
/*      */       //   2156: aload #4
/*      */       //   2158: aload #8
/*      */       //   2160: getfield typeargs : Lcom/sun/tools/javac/util/List;
/*      */       //   2163: aload_1
/*      */       //   2164: invokevirtual indexOf : (Ljava/lang/Object;)I
/*      */       //   2167: putfield type_index : I
/*      */       //   2170: return
/*      */       //   2171: aload #4
/*      */       //   2173: aload #4
/*      */       //   2175: getfield location : Lcom/sun/tools/javac/util/List;
/*      */       //   2178: getstatic com/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntry.WILDCARD : Lcom/sun/tools/javac/code/TypeAnnotationPosition$TypePathEntry;
/*      */       //   2181: invokevirtual prepend : (Ljava/lang/Object;)Lcom/sun/tools/javac/util/List;
/*      */       //   2184: putfield location : Lcom/sun/tools/javac/util/List;
/*      */       //   2187: aload_3
/*      */       //   2188: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   2191: astore #8
/*      */       //   2193: aload_0
/*      */       //   2194: aload #8
/*      */       //   2196: getfield head : Ljava/lang/Object;
/*      */       //   2199: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   2202: aload #8
/*      */       //   2204: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   2207: getfield head : Ljava/lang/Object;
/*      */       //   2210: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   2213: aload #8
/*      */       //   2215: aload #4
/*      */       //   2217: invokespecial resolveFrame : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   2220: return
/*      */       //   2221: aload_3
/*      */       //   2222: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   2225: astore #8
/*      */       //   2227: aload_0
/*      */       //   2228: aload #8
/*      */       //   2230: getfield head : Ljava/lang/Object;
/*      */       //   2233: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   2236: aload #8
/*      */       //   2238: getfield tail : Lcom/sun/tools/javac/util/List;
/*      */       //   2241: getfield head : Ljava/lang/Object;
/*      */       //   2244: checkcast com/sun/tools/javac/tree/JCTree
/*      */       //   2247: aload #8
/*      */       //   2249: aload #4
/*      */       //   2251: invokespecial resolveFrame : (Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/tree/JCTree;Lcom/sun/tools/javac/util/List;Lcom/sun/tools/javac/code/TypeAnnotationPosition;)V
/*      */       //   2254: return
/*      */       //   2255: new java/lang/StringBuilder
/*      */       //   2258: dup
/*      */       //   2259: invokespecial <init> : ()V
/*      */       //   2262: ldc 'Unresolved frame: '
/*      */       //   2264: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   2267: aload_2
/*      */       //   2268: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   2271: ldc ' of kind: '
/*      */       //   2273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   2276: aload_2
/*      */       //   2277: invokevirtual getKind : ()Lcom/sun/source/tree/Tree$Kind;
/*      */       //   2280: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   2283: ldc '\\n    Looking for tree: '
/*      */       //   2285: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   2288: aload_1
/*      */       //   2289: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */       //   2292: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   2295: invokestatic error : (Ljava/lang/String;)V
/*      */       //   2298: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #677	-> 0
/*      */       //   #679	-> 108
/*      */       //   #680	-> 114
/*      */       //   #681	-> 122
/*      */       //   #684	-> 139
/*      */       //   #686	-> 145
/*      */       //   #687	-> 154
/*      */       //   #690	-> 155
/*      */       //   #691	-> 163
/*      */       //   #692	-> 172
/*      */       //   #695	-> 173
/*      */       //   #696	-> 179
/*      */       //   #698	-> 187
/*      */       //   #699	-> 194
/*      */       //   #700	-> 203
/*      */       //   #701	-> 211
/*      */       //   #702	-> 220
/*      */       //   #703	-> 232
/*      */       //   #704	-> 240
/*      */       //   #707	-> 257
/*      */       //   #710	-> 288
/*      */       //   #711	-> 303
/*      */       //   #712	-> 311
/*      */       //   #714	-> 328
/*      */       //   #716	-> 336
/*      */       //   #717	-> 345
/*      */       //   #720	-> 346
/*      */       //   #721	-> 354
/*      */       //   #722	-> 363
/*      */       //   #728	-> 364
/*      */       //   #729	-> 373
/*      */       //   #730	-> 384
/*      */       //   #731	-> 392
/*      */       //   #732	-> 401
/*      */       //   #733	-> 415
/*      */       //   #734	-> 423
/*      */       //   #735	-> 442
/*      */       //   #736	-> 456
/*      */       //   #737	-> 464
/*      */       //   #739	-> 483
/*      */       //   #742	-> 514
/*      */       //   #745	-> 515
/*      */       //   #746	-> 521
/*      */       //   #747	-> 530
/*      */       //   #748	-> 542
/*      */       //   #749	-> 550
/*      */       //   #750	-> 567
/*      */       //   #751	-> 576
/*      */       //   #752	-> 587
/*      */       //   #753	-> 599
/*      */       //   #754	-> 607
/*      */       //   #756	-> 624
/*      */       //   #759	-> 655
/*      */       //   #763	-> 656
/*      */       //   #765	-> 662
/*      */       //   #767	-> 676
/*      */       //   #768	-> 690
/*      */       //   #769	-> 696
/*      */       //   #770	-> 707
/*      */       //   #773	-> 732
/*      */       //   #776	-> 760
/*      */       //   #778	-> 779
/*      */       //   #781	-> 786
/*      */       //   #782	-> 794
/*      */       //   #783	-> 797
/*      */       //   #787	-> 828
/*      */       //   #788	-> 855
/*      */       //   #792	-> 856
/*      */       //   #794	-> 862
/*      */       //   #795	-> 871
/*      */       //   #797	-> 908
/*      */       //   #798	-> 916
/*      */       //   #800	-> 919
/*      */       //   #801	-> 927
/*      */       //   #803	-> 930
/*      */       //   #806	-> 974
/*      */       //   #807	-> 986
/*      */       //   #808	-> 1000
/*      */       //   #809	-> 1006
/*      */       //   #810	-> 1017
/*      */       //   #811	-> 1024
/*      */       //   #813	-> 1064
/*      */       //   #814	-> 1072
/*      */       //   #816	-> 1075
/*      */       //   #817	-> 1083
/*      */       //   #819	-> 1086
/*      */       //   #822	-> 1130
/*      */       //   #823	-> 1139
/*      */       //   #824	-> 1142
/*      */       //   #827	-> 1173
/*      */       //   #831	-> 1174
/*      */       //   #832	-> 1183
/*      */       //   #833	-> 1193
/*      */       //   #835	-> 1199
/*      */       //   #836	-> 1212
/*      */       //   #837	-> 1223
/*      */       //   #838	-> 1230
/*      */       //   #839	-> 1243
/*      */       //   #840	-> 1254
/*      */       //   #844	-> 1261
/*      */       //   #845	-> 1264
/*      */       //   #846	-> 1282
/*      */       //   #847	-> 1309
/*      */       //   #851	-> 1310
/*      */       //   #852	-> 1332
/*      */       //   #853	-> 1347
/*      */       //   #854	-> 1355
/*      */       //   #855	-> 1375
/*      */       //   #856	-> 1391
/*      */       //   #858	-> 1414
/*      */       //   #860	-> 1425
/*      */       //   #861	-> 1450
/*      */       //   #862	-> 1465
/*      */       //   #863	-> 1473
/*      */       //   #864	-> 1493
/*      */       //   #865	-> 1509
/*      */       //   #867	-> 1532
/*      */       //   #869	-> 1543
/*      */       //   #870	-> 1546
/*      */       //   #873	-> 1577
/*      */       //   #874	-> 1586
/*      */       //   #877	-> 1587
/*      */       //   #878	-> 1596
/*      */       //   #879	-> 1605
/*      */       //   #881	-> 1652
/*      */       //   #882	-> 1660
/*      */       //   #884	-> 1663
/*      */       //   #885	-> 1671
/*      */       //   #887	-> 1674
/*      */       //   #889	-> 1695
/*      */       //   #891	-> 1706
/*      */       //   #892	-> 1714
/*      */       //   #894	-> 1725
/*      */       //   #896	-> 1728
/*      */       //   #897	-> 1736
/*      */       //   #899	-> 1739
/*      */       //   #900	-> 1747
/*      */       //   #902	-> 1750
/*      */       //   #904	-> 1786
/*      */       //   #905	-> 1797
/*      */       //   #907	-> 1810
/*      */       //   #910	-> 1811
/*      */       //   #914	-> 1816
/*      */       //   #915	-> 1822
/*      */       //   #916	-> 1832
/*      */       //   #919	-> 1837
/*      */       //   #921	-> 1838
/*      */       //   #922	-> 1845
/*      */       //   #923	-> 1861
/*      */       //   #924	-> 1875
/*      */       //   #929	-> 1890
/*      */       //   #932	-> 1898
/*      */       //   #933	-> 1904
/*      */       //   #934	-> 1931
/*      */       //   #938	-> 1932
/*      */       //   #939	-> 1938
/*      */       //   #940	-> 1965
/*      */       //   #944	-> 1966
/*      */       //   #945	-> 1972
/*      */       //   #946	-> 1986
/*      */       //   #947	-> 1992
/*      */       //   #948	-> 2019
/*      */       //   #952	-> 2020
/*      */       //   #953	-> 2026
/*      */       //   #954	-> 2038
/*      */       //   #956	-> 2070
/*      */       //   #957	-> 2083
/*      */       //   #958	-> 2088
/*      */       //   #959	-> 2119
/*      */       //   #960	-> 2127
/*      */       //   #962	-> 2138
/*      */       //   #964	-> 2146
/*      */       //   #965	-> 2156
/*      */       //   #966	-> 2170
/*      */       //   #972	-> 2171
/*      */       //   #973	-> 2187
/*      */       //   #974	-> 2193
/*      */       //   #975	-> 2220
/*      */       //   #979	-> 2221
/*      */       //   #980	-> 2227
/*      */       //   #981	-> 2254
/*      */       //   #985	-> 2255
/*      */       //   #987	-> 2298 }
/* 1152 */     public void visitVarDef(JCTree.JCVariableDecl param1JCVariableDecl) { if (!param1JCVariableDecl.mods.annotations.isEmpty())
/*      */       {
/*      */
/* 1155 */         if (param1JCVariableDecl.sym == null) {
/* 1156 */           Assert.error("Visiting tree node before memberEnter");
/* 1157 */         } else if (param1JCVariableDecl.sym.getKind() != ElementKind.PARAMETER) {
/*      */
/* 1159 */           if (param1JCVariableDecl.sym.getKind() == ElementKind.FIELD) {
/* 1160 */             if (this.sigOnly) {
/* 1161 */               TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1162 */               typeAnnotationPosition.type = TargetType.FIELD;
/* 1163 */               typeAnnotationPosition.pos = param1JCVariableDecl.pos;
/* 1164 */               separateAnnotationsKinds((JCTree)param1JCVariableDecl.vartype, param1JCVariableDecl.sym.type, param1JCVariableDecl.sym, typeAnnotationPosition);
/*      */             }
/* 1166 */           } else if (param1JCVariableDecl.sym.getKind() == ElementKind.LOCAL_VARIABLE) {
/* 1167 */             TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1168 */             typeAnnotationPosition.type = TargetType.LOCAL_VARIABLE;
/* 1169 */             typeAnnotationPosition.pos = param1JCVariableDecl.pos;
/* 1170 */             typeAnnotationPosition.onLambda = this.currentLambda;
/* 1171 */             separateAnnotationsKinds((JCTree)param1JCVariableDecl.vartype, param1JCVariableDecl.sym.type, param1JCVariableDecl.sym, typeAnnotationPosition);
/* 1172 */           } else if (param1JCVariableDecl.sym.getKind() == ElementKind.EXCEPTION_PARAMETER) {
/* 1173 */             TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1174 */             typeAnnotationPosition.type = TargetType.EXCEPTION_PARAMETER;
/* 1175 */             typeAnnotationPosition.pos = param1JCVariableDecl.pos;
/* 1176 */             typeAnnotationPosition.onLambda = this.currentLambda;
/* 1177 */             separateAnnotationsKinds((JCTree)param1JCVariableDecl.vartype, param1JCVariableDecl.sym.type, param1JCVariableDecl.sym, typeAnnotationPosition);
/* 1178 */           } else if (param1JCVariableDecl.sym.getKind() == ElementKind.RESOURCE_VARIABLE) {
/* 1179 */             TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1180 */             typeAnnotationPosition.type = TargetType.RESOURCE_VARIABLE;
/* 1181 */             typeAnnotationPosition.pos = param1JCVariableDecl.pos;
/* 1182 */             typeAnnotationPosition.onLambda = this.currentLambda;
/* 1183 */             separateAnnotationsKinds((JCTree)param1JCVariableDecl.vartype, param1JCVariableDecl.sym.type, param1JCVariableDecl.sym, typeAnnotationPosition);
/* 1184 */           } else if (param1JCVariableDecl.sym.getKind() != ElementKind.ENUM_CONSTANT) {
/*      */
/*      */
/*      */
/* 1188 */             Assert.error("Unhandled variable kind: " + param1JCVariableDecl + " of kind: " + param1JCVariableDecl.sym.getKind());
/*      */           }
/*      */         }  }
/* 1191 */       push((JCTree)param1JCVariableDecl);
/*      */
/* 1193 */       scan((JCTree)param1JCVariableDecl.mods);
/* 1194 */       scan((JCTree)param1JCVariableDecl.vartype);
/* 1195 */       if (!this.sigOnly) {
/* 1196 */         scan((JCTree)param1JCVariableDecl.init);
/*      */       }
/* 1198 */       pop(); } private void locateNestedTypes(Type param1Type, TypeAnnotationPosition param1TypeAnnotationPosition) { ListBuffer listBuffer = new ListBuffer(); Type type = param1Type.getEnclosingType(); while (type != null && type.getKind() != TypeKind.NONE && type.getKind() != TypeKind.ERROR) { listBuffer = listBuffer.append(TypeAnnotationPosition.TypePathEntry.INNER_TYPE); type = type.getEnclosingType(); }  if (listBuffer.nonEmpty()) param1TypeAnnotationPosition.location = param1TypeAnnotationPosition.location.prependList(listBuffer.toList());  }
/*      */     private int methodParamIndex(List<JCTree> param1List, JCTree param1JCTree) { List<JCTree> list = param1List; while (((JCTree)list.head).getTag() != JCTree.Tag.METHODDEF && ((JCTree)list.head).getTag() != JCTree.Tag.LAMBDA)
/*      */         list = list.tail;  if (((JCTree)list.head).getTag() == JCTree.Tag.METHODDEF) { JCTree.JCMethodDecl jCMethodDecl = (JCTree.JCMethodDecl)list.head; return jCMethodDecl.params.indexOf(param1JCTree); }  if (((JCTree)list.head).getTag() == JCTree.Tag.LAMBDA) { JCTree.JCLambda jCLambda = (JCTree.JCLambda)list.head; return jCLambda.params.indexOf(param1JCTree); }  Assert.error("methodParamIndex expected to find method or lambda for param: " + param1JCTree); return -1; }
/*      */     public void visitClassDef(JCTree.JCClassDecl param1JCClassDecl) { if (this.isInClass)
/*      */         return;  this.isInClass = true; if (this.sigOnly) { scan((JCTree)param1JCClassDecl.mods); scan(param1JCClassDecl.typarams); scan((JCTree)param1JCClassDecl.extending); scan(param1JCClassDecl.implementing); }  scan(param1JCClassDecl.defs); }
/*      */     public void visitMethodDef(JCTree.JCMethodDecl param1JCMethodDecl) { if (param1JCMethodDecl.sym == null)
/*      */         Assert.error("Visiting tree node before memberEnter");  if (this.sigOnly) { if (!param1JCMethodDecl.mods.annotations.isEmpty()) { TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition(); typeAnnotationPosition.type = TargetType.METHOD_RETURN; if (param1JCMethodDecl.sym.isConstructor()) { typeAnnotationPosition.pos = param1JCMethodDecl.pos; separateAnnotationsKinds((JCTree)param1JCMethodDecl, null, param1JCMethodDecl.sym, typeAnnotationPosition); } else { typeAnnotationPosition.pos = param1JCMethodDecl.restype.pos; separateAnnotationsKinds((JCTree)param1JCMethodDecl.restype, param1JCMethodDecl.sym.type.getReturnType(), param1JCMethodDecl.sym, typeAnnotationPosition); }  }  if (param1JCMethodDecl.recvparam != null && param1JCMethodDecl.recvparam.sym != null && !param1JCMethodDecl.recvparam.mods.annotations.isEmpty()) { TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition(); typeAnnotationPosition.type = TargetType.METHOD_RECEIVER; typeAnnotationPosition.pos = param1JCMethodDecl.recvparam.vartype.pos; separateAnnotationsKinds((JCTree)param1JCMethodDecl.recvparam.vartype, param1JCMethodDecl.recvparam.sym.type, param1JCMethodDecl.recvparam.sym, typeAnnotationPosition); }  byte b = 0; for (JCTree.JCVariableDecl jCVariableDecl : param1JCMethodDecl.params) { if (!jCVariableDecl.mods.annotations.isEmpty()) { TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition(); typeAnnotationPosition.type = TargetType.METHOD_FORMAL_PARAMETER; typeAnnotationPosition.parameter_index = b; typeAnnotationPosition.pos = jCVariableDecl.vartype.pos; separateAnnotationsKinds((JCTree)jCVariableDecl.vartype, jCVariableDecl.sym.type, jCVariableDecl.sym, typeAnnotationPosition); }  b++; }  }  push((JCTree)param1JCMethodDecl); if (this.sigOnly) { scan((JCTree)param1JCMethodDecl.mods); scan((JCTree)param1JCMethodDecl.restype); scan(param1JCMethodDecl.typarams); scan((JCTree)param1JCMethodDecl.recvparam); scan(param1JCMethodDecl.params); scan(param1JCMethodDecl.thrown); } else { scan((JCTree)param1JCMethodDecl.defaultValue); scan((JCTree)param1JCMethodDecl.body); }  pop(); }
/* 1205 */     public void visitBlock(JCTree.JCBlock param1JCBlock) { if (!this.sigOnly) {
/* 1206 */         scan(param1JCBlock.stats);
/*      */       } }
/*      */
/*      */
/*      */
/*      */     public void visitAnnotatedType(JCTree.JCAnnotatedType param1JCAnnotatedType) {
/* 1212 */       push((JCTree)param1JCAnnotatedType);
/* 1213 */       findPosition((JCTree)param1JCAnnotatedType, (JCTree)param1JCAnnotatedType, param1JCAnnotatedType.annotations);
/* 1214 */       pop();
/* 1215 */       super.visitAnnotatedType(param1JCAnnotatedType);
/*      */     }
/*      */
/*      */
/*      */     public void visitTypeParameter(JCTree.JCTypeParameter param1JCTypeParameter) {
/* 1220 */       findPosition((JCTree)param1JCTypeParameter, peek2(), param1JCTypeParameter.annotations);
/* 1221 */       super.visitTypeParameter(param1JCTypeParameter);
/*      */     }
/*      */
/*      */     private void copyNewClassAnnotationsToOwner(JCTree.JCNewClass param1JCNewClass) {
/* 1225 */       Symbol.ClassSymbol classSymbol = param1JCNewClass.def.sym;
/* 1226 */       TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1227 */       ListBuffer listBuffer = new ListBuffer();
/*      */
/*      */
/* 1230 */       for (Attribute.TypeCompound typeCompound : classSymbol.getRawTypeAttributes()) {
/* 1231 */         listBuffer.append(new Attribute.TypeCompound(typeCompound.type, typeCompound.values, typeAnnotationPosition));
/*      */       }
/*      */
/*      */
/* 1235 */       typeAnnotationPosition.type = TargetType.NEW;
/* 1236 */       typeAnnotationPosition.pos = param1JCNewClass.pos;
/* 1237 */       classSymbol.owner.appendUniqueTypeAttributes(listBuffer.toList());
/*      */     }
/*      */
/*      */
/*      */     public void visitNewClass(JCTree.JCNewClass param1JCNewClass) {
/* 1242 */       if (param1JCNewClass.def != null &&
/* 1243 */         !param1JCNewClass.def.mods.annotations.isEmpty()) {
/* 1244 */         JCTree.JCClassDecl jCClassDecl = param1JCNewClass.def;
/* 1245 */         TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1246 */         typeAnnotationPosition.type = TargetType.CLASS_EXTENDS;
/* 1247 */         typeAnnotationPosition.pos = param1JCNewClass.pos;
/* 1248 */         if (jCClassDecl.extending == param1JCNewClass.clazz) {
/* 1249 */           typeAnnotationPosition.type_index = -1;
/* 1250 */         } else if (jCClassDecl.implementing.contains(param1JCNewClass.clazz)) {
/* 1251 */           typeAnnotationPosition.type_index = jCClassDecl.implementing.indexOf(param1JCNewClass.clazz);
/*      */         } else {
/*      */
/* 1254 */           Assert.error("Could not determine position of tree " + param1JCNewClass);
/*      */         }
/* 1256 */         Type type = jCClassDecl.sym.type;
/* 1257 */         separateAnnotationsKinds((JCTree)jCClassDecl, param1JCNewClass.clazz.type, jCClassDecl.sym, typeAnnotationPosition);
/* 1258 */         copyNewClassAnnotationsToOwner(param1JCNewClass);
/*      */
/*      */
/*      */
/* 1262 */         jCClassDecl.sym.type = type;
/*      */       }
/*      */
/* 1265 */       scan((JCTree)param1JCNewClass.encl);
/* 1266 */       scan(param1JCNewClass.typeargs);
/* 1267 */       scan((JCTree)param1JCNewClass.clazz);
/* 1268 */       scan(param1JCNewClass.args);
/*      */     }
/*      */
/*      */
/*      */
/*      */
/*      */
/*      */     public void visitNewArray(JCTree.JCNewArray param1JCNewArray) {
/* 1276 */       findPosition((JCTree)param1JCNewArray, (JCTree)param1JCNewArray, param1JCNewArray.annotations);
/* 1277 */       int i = param1JCNewArray.dimAnnotations.size();
/* 1278 */       ListBuffer listBuffer = new ListBuffer();
/*      */
/*      */
/* 1281 */       for (byte b = 0; b < i; b++) {
/* 1282 */         TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1283 */         typeAnnotationPosition.pos = param1JCNewArray.pos;
/* 1284 */         typeAnnotationPosition.onLambda = this.currentLambda;
/* 1285 */         typeAnnotationPosition.type = TargetType.NEW;
/* 1286 */         if (b != 0) {
/* 1287 */           listBuffer = listBuffer.append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/* 1288 */           typeAnnotationPosition.location = typeAnnotationPosition.location.appendList(listBuffer.toList());
/*      */         }
/*      */
/* 1291 */         setTypeAnnotationPos((List<JCTree.JCAnnotation>)param1JCNewArray.dimAnnotations.get(b), typeAnnotationPosition);
/*      */       }
/*      */
/*      */
/*      */
/*      */
/* 1297 */       JCTree.JCExpression jCExpression = param1JCNewArray.elemtype;
/* 1298 */       listBuffer = listBuffer.append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/* 1299 */       while (jCExpression != null) {
/* 1300 */         if (jCExpression.hasTag(JCTree.Tag.ANNOTATED_TYPE)) {
/* 1301 */           JCTree.JCAnnotatedType jCAnnotatedType = (JCTree.JCAnnotatedType)jCExpression;
/* 1302 */           TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1303 */           typeAnnotationPosition.type = TargetType.NEW;
/* 1304 */           typeAnnotationPosition.pos = param1JCNewArray.pos;
/* 1305 */           typeAnnotationPosition.onLambda = this.currentLambda;
/* 1306 */           locateNestedTypes(jCExpression.type, typeAnnotationPosition);
/* 1307 */           typeAnnotationPosition.location = typeAnnotationPosition.location.prependList(listBuffer.toList());
/* 1308 */           setTypeAnnotationPos(jCAnnotatedType.annotations, typeAnnotationPosition);
/* 1309 */           jCExpression = jCAnnotatedType.underlyingType; continue;
/* 1310 */         }  if (jCExpression.hasTag(JCTree.Tag.TYPEARRAY)) {
/* 1311 */           listBuffer = listBuffer.append(TypeAnnotationPosition.TypePathEntry.ARRAY);
/* 1312 */           jCExpression = ((JCTree.JCArrayTypeTree)jCExpression).elemtype; continue;
/* 1313 */         }  if (jCExpression.hasTag(JCTree.Tag.SELECT)) {
/* 1314 */           jCExpression = ((JCTree.JCFieldAccess)jCExpression).selected;
/*      */         }
/*      */       }
/*      */
/*      */
/* 1319 */       scan(param1JCNewArray.elems);
/*      */     }
/*      */
/*      */     private void findPosition(JCTree param1JCTree1, JCTree param1JCTree2, List<JCTree.JCAnnotation> param1List) {
/* 1323 */       if (!param1List.isEmpty()) {
/*      */
/*      */
/*      */
/*      */
/*      */
/* 1329 */         TypeAnnotationPosition typeAnnotationPosition = new TypeAnnotationPosition();
/* 1330 */         typeAnnotationPosition.onLambda = this.currentLambda;
/* 1331 */         resolveFrame(param1JCTree1, param1JCTree2, this.frames.toList(), typeAnnotationPosition);
/* 1332 */         setTypeAnnotationPos(param1List, typeAnnotationPosition);
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     private void setTypeAnnotationPos(List<JCTree.JCAnnotation> param1List, TypeAnnotationPosition param1TypeAnnotationPosition) {
/* 1338 */       for (JCTree.JCAnnotation jCAnnotation : param1List) {
/*      */
/*      */
/* 1341 */         if (jCAnnotation.attribute != null) {
/* 1342 */           ((Attribute.TypeCompound)jCAnnotation.attribute).position = param1TypeAnnotationPosition;
/*      */         }
/*      */       }
/*      */     }
/*      */
/*      */
/*      */     public String toString() {
/* 1349 */       return super.toString() + ": sigOnly: " + this.sigOnly;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\code\TypeAnnotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
