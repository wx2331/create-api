/*     */ package com.sun.tools.javac.processing;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.annotation.processing.AbstractProcessor;
/*     */ import javax.annotation.processing.RoundEnvironment;
/*     */ import javax.annotation.processing.SupportedAnnotationTypes;
/*     */ import javax.annotation.processing.SupportedSourceVersion;
/*     */ import javax.lang.model.SourceVersion;
/*     */ import javax.lang.model.element.AnnotationMirror;
/*     */ import javax.lang.model.element.Element;
/*     */ import javax.lang.model.element.ElementKind;
/*     */ import javax.lang.model.element.ExecutableElement;
/*     */ import javax.lang.model.element.NestingKind;
/*     */ import javax.lang.model.element.PackageElement;
/*     */ import javax.lang.model.element.Parameterizable;
/*     */ import javax.lang.model.element.TypeElement;
/*     */ import javax.lang.model.element.TypeParameterElement;
/*     */ import javax.lang.model.element.VariableElement;
/*     */ import javax.lang.model.type.ArrayType;
/*     */ import javax.lang.model.type.TypeKind;
/*     */ import javax.lang.model.type.TypeMirror;
/*     */ import javax.lang.model.util.Elements;
/*     */ import javax.lang.model.util.SimpleElementVisitor7;
/*     */ import javax.lang.model.util.SimpleElementVisitor8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @SupportedAnnotationTypes({"*"})
/*     */ @SupportedSourceVersion(SourceVersion.RELEASE_8)
/*     */ public class PrintingProcessor
/*     */   extends AbstractProcessor
/*     */ {
/*  58 */   PrintWriter writer = new PrintWriter(System.out);
/*     */ 
/*     */   
/*     */   public void setWriter(Writer paramWriter) {
/*  62 */     this.writer = new PrintWriter(paramWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment) {
/*  69 */     for (Element element : paramRoundEnvironment.getRootElements()) {
/*  70 */       print(element);
/*     */     }
/*     */ 
/*     */     
/*  74 */     return true;
/*     */   }
/*     */   
/*     */   void print(Element paramElement) {
/*  78 */     (new PrintingElementVisitor(this.writer, this.processingEnv.getElementUtils()))
/*  79 */       .visit(paramElement).flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class PrintingElementVisitor
/*     */     extends SimpleElementVisitor8<PrintingElementVisitor, Boolean>
/*     */   {
/*     */     int indentation;
/*     */     
/*     */     final PrintWriter writer;
/*     */     
/*     */     final Elements elementUtils;
/*     */     
/*     */     public PrintingElementVisitor(Writer param1Writer, Elements param1Elements) {
/*  93 */       this.writer = new PrintWriter(param1Writer);
/*  94 */       this.elementUtils = param1Elements;
/*  95 */       this.indentation = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     protected PrintingElementVisitor defaultAction(Element param1Element, Boolean param1Boolean) {
/* 100 */       if (param1Boolean != null && param1Boolean.booleanValue())
/* 101 */         this.writer.println(); 
/* 102 */       printDocComment(param1Element);
/* 103 */       printModifiers(param1Element);
/* 104 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PrintingElementVisitor visitExecutable(ExecutableElement param1ExecutableElement, Boolean param1Boolean) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: invokeinterface getKind : ()Ljavax/lang/model/element/ElementKind;
/*     */       //   6: astore_3
/*     */       //   7: aload_3
/*     */       //   8: getstatic javax/lang/model/element/ElementKind.STATIC_INIT : Ljavax/lang/model/element/ElementKind;
/*     */       //   11: if_acmpeq -> 253
/*     */       //   14: aload_3
/*     */       //   15: getstatic javax/lang/model/element/ElementKind.INSTANCE_INIT : Ljavax/lang/model/element/ElementKind;
/*     */       //   18: if_acmpeq -> 253
/*     */       //   21: aload_1
/*     */       //   22: invokeinterface getEnclosingElement : ()Ljavax/lang/model/element/Element;
/*     */       //   27: astore #4
/*     */       //   29: aload_3
/*     */       //   30: getstatic javax/lang/model/element/ElementKind.CONSTRUCTOR : Ljavax/lang/model/element/ElementKind;
/*     */       //   33: if_acmpne -> 62
/*     */       //   36: aload #4
/*     */       //   38: ifnull -> 62
/*     */       //   41: getstatic javax/lang/model/element/NestingKind.ANONYMOUS : Ljavax/lang/model/element/NestingKind;
/*     */       //   44: new com/sun/tools/javac/processing/PrintingProcessor$PrintingElementVisitor$1
/*     */       //   47: dup
/*     */       //   48: aload_0
/*     */       //   49: invokespecial <init> : (Lcom/sun/tools/javac/processing/PrintingProcessor$PrintingElementVisitor;)V
/*     */       //   52: aload #4
/*     */       //   54: invokevirtual visit : (Ljavax/lang/model/element/Element;)Ljava/lang/Object;
/*     */       //   57: if_acmpne -> 62
/*     */       //   60: aload_0
/*     */       //   61: areturn
/*     */       //   62: aload_0
/*     */       //   63: aload_1
/*     */       //   64: iconst_1
/*     */       //   65: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */       //   68: invokevirtual defaultAction : (Ljavax/lang/model/element/Element;Ljava/lang/Boolean;)Lcom/sun/tools/javac/processing/PrintingProcessor$PrintingElementVisitor;
/*     */       //   71: pop
/*     */       //   72: aload_0
/*     */       //   73: aload_1
/*     */       //   74: iconst_1
/*     */       //   75: invokespecial printFormalTypeParameters : (Ljavax/lang/model/element/Parameterizable;Z)V
/*     */       //   78: getstatic com/sun/tools/javac/processing/PrintingProcessor$1.$SwitchMap$javax$lang$model$element$ElementKind : [I
/*     */       //   81: aload_3
/*     */       //   82: invokevirtual ordinal : ()I
/*     */       //   85: iaload
/*     */       //   86: lookupswitch default -> 176, 1 -> 112, 2 -> 133
/*     */       //   112: aload_0
/*     */       //   113: getfield writer : Ljava/io/PrintWriter;
/*     */       //   116: aload_1
/*     */       //   117: invokeinterface getEnclosingElement : ()Ljavax/lang/model/element/Element;
/*     */       //   122: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
/*     */       //   127: invokevirtual print : (Ljava/lang/Object;)V
/*     */       //   130: goto -> 176
/*     */       //   133: aload_0
/*     */       //   134: getfield writer : Ljava/io/PrintWriter;
/*     */       //   137: aload_1
/*     */       //   138: invokeinterface getReturnType : ()Ljavax/lang/model/type/TypeMirror;
/*     */       //   143: invokeinterface toString : ()Ljava/lang/String;
/*     */       //   148: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   151: aload_0
/*     */       //   152: getfield writer : Ljava/io/PrintWriter;
/*     */       //   155: ldc ' '
/*     */       //   157: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   160: aload_0
/*     */       //   161: getfield writer : Ljava/io/PrintWriter;
/*     */       //   164: aload_1
/*     */       //   165: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
/*     */       //   170: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   173: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   176: aload_0
/*     */       //   177: getfield writer : Ljava/io/PrintWriter;
/*     */       //   180: ldc '('
/*     */       //   182: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   185: aload_0
/*     */       //   186: aload_1
/*     */       //   187: invokespecial printParameters : (Ljavax/lang/model/element/ExecutableElement;)V
/*     */       //   190: aload_0
/*     */       //   191: getfield writer : Ljava/io/PrintWriter;
/*     */       //   194: ldc ')'
/*     */       //   196: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   199: aload_1
/*     */       //   200: invokeinterface getDefaultValue : ()Ljavax/lang/model/element/AnnotationValue;
/*     */       //   205: astore #5
/*     */       //   207: aload #5
/*     */       //   209: ifnull -> 239
/*     */       //   212: aload_0
/*     */       //   213: getfield writer : Ljava/io/PrintWriter;
/*     */       //   216: new java/lang/StringBuilder
/*     */       //   219: dup
/*     */       //   220: invokespecial <init> : ()V
/*     */       //   223: ldc ' default '
/*     */       //   225: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   228: aload #5
/*     */       //   230: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   233: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   236: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   239: aload_0
/*     */       //   240: aload_1
/*     */       //   241: invokespecial printThrows : (Ljavax/lang/model/element/ExecutableElement;)V
/*     */       //   244: aload_0
/*     */       //   245: getfield writer : Ljava/io/PrintWriter;
/*     */       //   248: ldc ';'
/*     */       //   250: invokevirtual println : (Ljava/lang/String;)V
/*     */       //   253: aload_0
/*     */       //   254: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #109	-> 0
/*     */       //   #111	-> 7
/*     */       //   #113	-> 21
/*     */       //   #116	-> 29
/*     */       //   #125	-> 54
/*     */       //   #126	-> 60
/*     */       //   #128	-> 62
/*     */       //   #129	-> 72
/*     */       //   #131	-> 78
/*     */       //   #134	-> 112
/*     */       //   #135	-> 130
/*     */       //   #138	-> 133
/*     */       //   #139	-> 151
/*     */       //   #140	-> 160
/*     */       //   #144	-> 176
/*     */       //   #145	-> 185
/*     */       //   #146	-> 190
/*     */       //   #147	-> 199
/*     */       //   #148	-> 207
/*     */       //   #149	-> 212
/*     */       //   #151	-> 239
/*     */       //   #152	-> 244
/*     */       //   #154	-> 253
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PrintingElementVisitor visitType(TypeElement param1TypeElement, Boolean param1Boolean) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: invokeinterface getKind : ()Ljavax/lang/model/element/ElementKind;
/*     */       //   6: astore_3
/*     */       //   7: aload_1
/*     */       //   8: invokeinterface getNestingKind : ()Ljavax/lang/model/element/NestingKind;
/*     */       //   13: astore #4
/*     */       //   15: getstatic javax/lang/model/element/NestingKind.ANONYMOUS : Ljavax/lang/model/element/NestingKind;
/*     */       //   18: aload #4
/*     */       //   20: if_acmpne -> 148
/*     */       //   23: aload_0
/*     */       //   24: getfield writer : Ljava/io/PrintWriter;
/*     */       //   27: ldc 'new '
/*     */       //   29: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   32: aload_1
/*     */       //   33: invokeinterface getInterfaces : ()Ljava/util/List;
/*     */       //   38: astore #5
/*     */       //   40: aload #5
/*     */       //   42: invokeinterface isEmpty : ()Z
/*     */       //   47: ifne -> 68
/*     */       //   50: aload_0
/*     */       //   51: getfield writer : Ljava/io/PrintWriter;
/*     */       //   54: aload #5
/*     */       //   56: iconst_0
/*     */       //   57: invokeinterface get : (I)Ljava/lang/Object;
/*     */       //   62: invokevirtual print : (Ljava/lang/Object;)V
/*     */       //   65: goto -> 81
/*     */       //   68: aload_0
/*     */       //   69: getfield writer : Ljava/io/PrintWriter;
/*     */       //   72: aload_1
/*     */       //   73: invokeinterface getSuperclass : ()Ljavax/lang/model/type/TypeMirror;
/*     */       //   78: invokevirtual print : (Ljava/lang/Object;)V
/*     */       //   81: aload_0
/*     */       //   82: getfield writer : Ljava/io/PrintWriter;
/*     */       //   85: ldc '('
/*     */       //   87: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   90: aload #5
/*     */       //   92: invokeinterface isEmpty : ()Z
/*     */       //   97: ifeq -> 136
/*     */       //   100: aload_1
/*     */       //   101: invokeinterface getEnclosedElements : ()Ljava/util/List;
/*     */       //   106: invokestatic constructorsIn : (Ljava/lang/Iterable;)Ljava/util/List;
/*     */       //   109: astore #6
/*     */       //   111: aload #6
/*     */       //   113: invokeinterface isEmpty : ()Z
/*     */       //   118: ifne -> 136
/*     */       //   121: aload_0
/*     */       //   122: aload #6
/*     */       //   124: iconst_0
/*     */       //   125: invokeinterface get : (I)Ljava/lang/Object;
/*     */       //   130: checkcast javax/lang/model/element/ExecutableElement
/*     */       //   133: invokespecial printParameters : (Ljavax/lang/model/element/ExecutableElement;)V
/*     */       //   136: aload_0
/*     */       //   137: getfield writer : Ljava/io/PrintWriter;
/*     */       //   140: ldc ')'
/*     */       //   142: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   145: goto -> 399
/*     */       //   148: aload #4
/*     */       //   150: getstatic javax/lang/model/element/NestingKind.TOP_LEVEL : Ljavax/lang/model/element/NestingKind;
/*     */       //   153: if_acmpne -> 215
/*     */       //   156: aload_0
/*     */       //   157: getfield elementUtils : Ljavax/lang/model/util/Elements;
/*     */       //   160: aload_1
/*     */       //   161: invokeinterface getPackageOf : (Ljavax/lang/model/element/Element;)Ljavax/lang/model/element/PackageElement;
/*     */       //   166: astore #5
/*     */       //   168: aload #5
/*     */       //   170: invokeinterface isUnnamed : ()Z
/*     */       //   175: ifne -> 215
/*     */       //   178: aload_0
/*     */       //   179: getfield writer : Ljava/io/PrintWriter;
/*     */       //   182: new java/lang/StringBuilder
/*     */       //   185: dup
/*     */       //   186: invokespecial <init> : ()V
/*     */       //   189: ldc 'package '
/*     */       //   191: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   194: aload #5
/*     */       //   196: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
/*     */       //   201: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   204: ldc ';\\n'
/*     */       //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   209: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   212: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   215: aload_0
/*     */       //   216: aload_1
/*     */       //   217: iconst_1
/*     */       //   218: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */       //   221: invokevirtual defaultAction : (Ljavax/lang/model/element/Element;Ljava/lang/Boolean;)Lcom/sun/tools/javac/processing/PrintingProcessor$PrintingElementVisitor;
/*     */       //   224: pop
/*     */       //   225: getstatic com/sun/tools/javac/processing/PrintingProcessor$1.$SwitchMap$javax$lang$model$element$ElementKind : [I
/*     */       //   228: aload_3
/*     */       //   229: invokevirtual ordinal : ()I
/*     */       //   232: iaload
/*     */       //   233: lookupswitch default -> 264, 3 -> 252
/*     */       //   252: aload_0
/*     */       //   253: getfield writer : Ljava/io/PrintWriter;
/*     */       //   256: ldc '@interface'
/*     */       //   258: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   261: goto -> 278
/*     */       //   264: aload_0
/*     */       //   265: getfield writer : Ljava/io/PrintWriter;
/*     */       //   268: aload_3
/*     */       //   269: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   272: invokestatic toLowerCase : (Ljava/lang/String;)Ljava/lang/String;
/*     */       //   275: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   278: aload_0
/*     */       //   279: getfield writer : Ljava/io/PrintWriter;
/*     */       //   282: ldc ' '
/*     */       //   284: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   287: aload_0
/*     */       //   288: getfield writer : Ljava/io/PrintWriter;
/*     */       //   291: aload_1
/*     */       //   292: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
/*     */       //   297: invokevirtual print : (Ljava/lang/Object;)V
/*     */       //   300: aload_0
/*     */       //   301: aload_1
/*     */       //   302: iconst_0
/*     */       //   303: invokespecial printFormalTypeParameters : (Ljavax/lang/model/element/Parameterizable;Z)V
/*     */       //   306: aload_3
/*     */       //   307: getstatic javax/lang/model/element/ElementKind.CLASS : Ljavax/lang/model/element/ElementKind;
/*     */       //   310: if_acmpne -> 394
/*     */       //   313: aload_1
/*     */       //   314: invokeinterface getSuperclass : ()Ljavax/lang/model/type/TypeMirror;
/*     */       //   319: astore #5
/*     */       //   321: aload #5
/*     */       //   323: invokeinterface getKind : ()Ljavax/lang/model/type/TypeKind;
/*     */       //   328: getstatic javax/lang/model/type/TypeKind.NONE : Ljavax/lang/model/type/TypeKind;
/*     */       //   331: if_acmpeq -> 394
/*     */       //   334: aload #5
/*     */       //   336: checkcast javax/lang/model/type/DeclaredType
/*     */       //   339: invokeinterface asElement : ()Ljavax/lang/model/element/Element;
/*     */       //   344: checkcast javax/lang/model/element/TypeElement
/*     */       //   347: astore #6
/*     */       //   349: aload #6
/*     */       //   351: invokeinterface getSuperclass : ()Ljavax/lang/model/type/TypeMirror;
/*     */       //   356: invokeinterface getKind : ()Ljavax/lang/model/type/TypeKind;
/*     */       //   361: getstatic javax/lang/model/type/TypeKind.NONE : Ljavax/lang/model/type/TypeKind;
/*     */       //   364: if_acmpeq -> 394
/*     */       //   367: aload_0
/*     */       //   368: getfield writer : Ljava/io/PrintWriter;
/*     */       //   371: new java/lang/StringBuilder
/*     */       //   374: dup
/*     */       //   375: invokespecial <init> : ()V
/*     */       //   378: ldc ' extends '
/*     */       //   380: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   383: aload #5
/*     */       //   385: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */       //   388: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   391: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   394: aload_0
/*     */       //   395: aload_1
/*     */       //   396: invokespecial printInterfaces : (Ljavax/lang/model/element/TypeElement;)V
/*     */       //   399: aload_0
/*     */       //   400: getfield writer : Ljava/io/PrintWriter;
/*     */       //   403: ldc ' {'
/*     */       //   405: invokevirtual println : (Ljava/lang/String;)V
/*     */       //   408: aload_0
/*     */       //   409: dup
/*     */       //   410: getfield indentation : I
/*     */       //   413: iconst_1
/*     */       //   414: iadd
/*     */       //   415: putfield indentation : I
/*     */       //   418: aload_3
/*     */       //   419: getstatic javax/lang/model/element/ElementKind.ENUM : Ljavax/lang/model/element/ElementKind;
/*     */       //   422: if_acmpne -> 653
/*     */       //   425: new java/util/ArrayList
/*     */       //   428: dup
/*     */       //   429: aload_1
/*     */       //   430: invokeinterface getEnclosedElements : ()Ljava/util/List;
/*     */       //   435: invokespecial <init> : (Ljava/util/Collection;)V
/*     */       //   438: astore #5
/*     */       //   440: new java/util/ArrayList
/*     */       //   443: dup
/*     */       //   444: invokespecial <init> : ()V
/*     */       //   447: astore #6
/*     */       //   449: aload #5
/*     */       //   451: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   456: astore #7
/*     */       //   458: aload #7
/*     */       //   460: invokeinterface hasNext : ()Z
/*     */       //   465: ifeq -> 506
/*     */       //   468: aload #7
/*     */       //   470: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   475: checkcast javax/lang/model/element/Element
/*     */       //   478: astore #8
/*     */       //   480: aload #8
/*     */       //   482: invokeinterface getKind : ()Ljavax/lang/model/element/ElementKind;
/*     */       //   487: getstatic javax/lang/model/element/ElementKind.ENUM_CONSTANT : Ljavax/lang/model/element/ElementKind;
/*     */       //   490: if_acmpne -> 503
/*     */       //   493: aload #6
/*     */       //   495: aload #8
/*     */       //   497: invokeinterface add : (Ljava/lang/Object;)Z
/*     */       //   502: pop
/*     */       //   503: goto -> 458
/*     */       //   506: aload #6
/*     */       //   508: invokeinterface isEmpty : ()Z
/*     */       //   513: ifne -> 609
/*     */       //   516: iconst_0
/*     */       //   517: istore #7
/*     */       //   519: iload #7
/*     */       //   521: aload #6
/*     */       //   523: invokeinterface size : ()I
/*     */       //   528: iconst_1
/*     */       //   529: isub
/*     */       //   530: if_icmpge -> 569
/*     */       //   533: aload_0
/*     */       //   534: aload #6
/*     */       //   536: iload #7
/*     */       //   538: invokeinterface get : (I)Ljava/lang/Object;
/*     */       //   543: checkcast javax/lang/model/element/Element
/*     */       //   546: iconst_1
/*     */       //   547: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */       //   550: invokevirtual visit : (Ljavax/lang/model/element/Element;Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   553: pop
/*     */       //   554: aload_0
/*     */       //   555: getfield writer : Ljava/io/PrintWriter;
/*     */       //   558: ldc ','
/*     */       //   560: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   563: iinc #7, 1
/*     */       //   566: goto -> 519
/*     */       //   569: aload_0
/*     */       //   570: aload #6
/*     */       //   572: iload #7
/*     */       //   574: invokeinterface get : (I)Ljava/lang/Object;
/*     */       //   579: checkcast javax/lang/model/element/Element
/*     */       //   582: iconst_1
/*     */       //   583: invokestatic valueOf : (Z)Ljava/lang/Boolean;
/*     */       //   586: invokevirtual visit : (Ljavax/lang/model/element/Element;Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   589: pop
/*     */       //   590: aload_0
/*     */       //   591: getfield writer : Ljava/io/PrintWriter;
/*     */       //   594: ldc ';\\n'
/*     */       //   596: invokevirtual println : (Ljava/lang/String;)V
/*     */       //   599: aload #5
/*     */       //   601: aload #6
/*     */       //   603: invokeinterface removeAll : (Ljava/util/Collection;)Z
/*     */       //   608: pop
/*     */       //   609: aload #5
/*     */       //   611: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   616: astore #7
/*     */       //   618: aload #7
/*     */       //   620: invokeinterface hasNext : ()Z
/*     */       //   625: ifeq -> 650
/*     */       //   628: aload #7
/*     */       //   630: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   635: checkcast javax/lang/model/element/Element
/*     */       //   638: astore #8
/*     */       //   640: aload_0
/*     */       //   641: aload #8
/*     */       //   643: invokevirtual visit : (Ljavax/lang/model/element/Element;)Ljava/lang/Object;
/*     */       //   646: pop
/*     */       //   647: goto -> 618
/*     */       //   650: goto -> 698
/*     */       //   653: aload_1
/*     */       //   654: invokeinterface getEnclosedElements : ()Ljava/util/List;
/*     */       //   659: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   664: astore #5
/*     */       //   666: aload #5
/*     */       //   668: invokeinterface hasNext : ()Z
/*     */       //   673: ifeq -> 698
/*     */       //   676: aload #5
/*     */       //   678: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   683: checkcast javax/lang/model/element/Element
/*     */       //   686: astore #6
/*     */       //   688: aload_0
/*     */       //   689: aload #6
/*     */       //   691: invokevirtual visit : (Ljavax/lang/model/element/Element;)Ljava/lang/Object;
/*     */       //   694: pop
/*     */       //   695: goto -> 666
/*     */       //   698: aload_0
/*     */       //   699: dup
/*     */       //   700: getfield indentation : I
/*     */       //   703: iconst_1
/*     */       //   704: isub
/*     */       //   705: putfield indentation : I
/*     */       //   708: aload_0
/*     */       //   709: invokespecial indent : ()V
/*     */       //   712: aload_0
/*     */       //   713: getfield writer : Ljava/io/PrintWriter;
/*     */       //   716: ldc '}'
/*     */       //   718: invokevirtual println : (Ljava/lang/String;)V
/*     */       //   721: aload_0
/*     */       //   722: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #160	-> 0
/*     */       //   #161	-> 7
/*     */       //   #163	-> 15
/*     */       //   #167	-> 23
/*     */       //   #171	-> 32
/*     */       //   #172	-> 40
/*     */       //   #173	-> 50
/*     */       //   #175	-> 68
/*     */       //   #177	-> 81
/*     */       //   #180	-> 90
/*     */       //   #185	-> 100
/*     */       //   #186	-> 101
/*     */       //   #188	-> 111
/*     */       //   #189	-> 121
/*     */       //   #191	-> 136
/*     */       //   #192	-> 145
/*     */       //   #193	-> 148
/*     */       //   #194	-> 156
/*     */       //   #195	-> 168
/*     */       //   #196	-> 178
/*     */       //   #199	-> 215
/*     */       //   #201	-> 225
/*     */       //   #203	-> 252
/*     */       //   #204	-> 261
/*     */       //   #206	-> 264
/*     */       //   #208	-> 278
/*     */       //   #209	-> 287
/*     */       //   #211	-> 300
/*     */       //   #214	-> 306
/*     */       //   #215	-> 313
/*     */       //   #216	-> 321
/*     */       //   #217	-> 334
/*     */       //   #218	-> 339
/*     */       //   #219	-> 349
/*     */       //   #220	-> 367
/*     */       //   #224	-> 394
/*     */       //   #226	-> 399
/*     */       //   #227	-> 408
/*     */       //   #229	-> 418
/*     */       //   #230	-> 425
/*     */       //   #231	-> 430
/*     */       //   #233	-> 440
/*     */       //   #234	-> 449
/*     */       //   #235	-> 480
/*     */       //   #236	-> 493
/*     */       //   #237	-> 503
/*     */       //   #238	-> 506
/*     */       //   #240	-> 516
/*     */       //   #241	-> 533
/*     */       //   #242	-> 554
/*     */       //   #240	-> 563
/*     */       //   #244	-> 569
/*     */       //   #245	-> 590
/*     */       //   #247	-> 599
/*     */       //   #250	-> 609
/*     */       //   #251	-> 640
/*     */       //   #252	-> 650
/*     */       //   #253	-> 653
/*     */       //   #254	-> 688
/*     */       //   #257	-> 698
/*     */       //   #258	-> 708
/*     */       //   #259	-> 712
/*     */       //   #260	-> 721
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PrintingElementVisitor visitVariable(VariableElement param1VariableElement, Boolean param1Boolean) {
/* 265 */       ElementKind elementKind = param1VariableElement.getKind();
/* 266 */       defaultAction(param1VariableElement, param1Boolean);
/*     */       
/* 268 */       if (elementKind == ElementKind.ENUM_CONSTANT) {
/* 269 */         this.writer.print(param1VariableElement.getSimpleName());
/*     */       } else {
/* 271 */         this.writer.print(param1VariableElement.asType().toString() + " " + param1VariableElement.getSimpleName());
/* 272 */         Object object = param1VariableElement.getConstantValue();
/* 273 */         if (object != null) {
/* 274 */           this.writer.print(" = ");
/* 275 */           this.writer.print(this.elementUtils.getConstantExpression(object));
/*     */         } 
/* 277 */         this.writer.println(";");
/*     */       } 
/* 279 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public PrintingElementVisitor visitTypeParameter(TypeParameterElement param1TypeParameterElement, Boolean param1Boolean) {
/* 284 */       this.writer.print(param1TypeParameterElement.getSimpleName());
/* 285 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PrintingElementVisitor visitPackage(PackageElement param1PackageElement, Boolean param1Boolean) {
/* 291 */       defaultAction(param1PackageElement, Boolean.valueOf(false));
/* 292 */       if (!param1PackageElement.isUnnamed()) {
/* 293 */         this.writer.println("package " + param1PackageElement.getQualifiedName() + ";");
/*     */       } else {
/* 295 */         this.writer.println("// Unnamed package");
/* 296 */       }  return this;
/*     */     }
/*     */     
/*     */     public void flush() {
/* 300 */       this.writer.flush();
/*     */     }
/*     */     
/*     */     private void printDocComment(Element param1Element) {
/* 304 */       String str = this.elementUtils.getDocComment(param1Element);
/*     */       
/* 306 */       if (str != null) {
/*     */         
/* 308 */         StringTokenizer stringTokenizer = new StringTokenizer(str, "\n\r");
/*     */         
/* 310 */         indent();
/* 311 */         this.writer.println("/**");
/*     */         
/* 313 */         while (stringTokenizer.hasMoreTokens()) {
/* 314 */           indent();
/* 315 */           this.writer.print(" *");
/* 316 */           this.writer.println(stringTokenizer.nextToken());
/*     */         } 
/*     */         
/* 319 */         indent();
/* 320 */         this.writer.println(" */");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void printModifiers(Element param1Element) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: invokeinterface getKind : ()Ljavax/lang/model/element/ElementKind;
/*     */       //   6: astore_2
/*     */       //   7: aload_2
/*     */       //   8: getstatic javax/lang/model/element/ElementKind.PARAMETER : Ljavax/lang/model/element/ElementKind;
/*     */       //   11: if_acmpne -> 22
/*     */       //   14: aload_0
/*     */       //   15: aload_1
/*     */       //   16: invokespecial printAnnotationsInline : (Ljavax/lang/model/element/Element;)V
/*     */       //   19: goto -> 31
/*     */       //   22: aload_0
/*     */       //   23: aload_1
/*     */       //   24: invokespecial printAnnotations : (Ljavax/lang/model/element/Element;)V
/*     */       //   27: aload_0
/*     */       //   28: invokespecial indent : ()V
/*     */       //   31: aload_2
/*     */       //   32: getstatic javax/lang/model/element/ElementKind.ENUM_CONSTANT : Ljavax/lang/model/element/ElementKind;
/*     */       //   35: if_acmpne -> 39
/*     */       //   38: return
/*     */       //   39: new java/util/LinkedHashSet
/*     */       //   42: dup
/*     */       //   43: invokespecial <init> : ()V
/*     */       //   46: astore_3
/*     */       //   47: aload_3
/*     */       //   48: aload_1
/*     */       //   49: invokeinterface getModifiers : ()Ljava/util/Set;
/*     */       //   54: invokeinterface addAll : (Ljava/util/Collection;)Z
/*     */       //   59: pop
/*     */       //   60: getstatic com/sun/tools/javac/processing/PrintingProcessor$1.$SwitchMap$javax$lang$model$element$ElementKind : [I
/*     */       //   63: aload_2
/*     */       //   64: invokevirtual ordinal : ()I
/*     */       //   67: iaload
/*     */       //   68: tableswitch default -> 206, 2 -> 140, 3 -> 104, 4 -> 104, 5 -> 117, 6 -> 140
/*     */       //   104: aload_3
/*     */       //   105: getstatic javax/lang/model/element/Modifier.ABSTRACT : Ljavax/lang/model/element/Modifier;
/*     */       //   108: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */       //   113: pop
/*     */       //   114: goto -> 206
/*     */       //   117: aload_3
/*     */       //   118: getstatic javax/lang/model/element/Modifier.FINAL : Ljavax/lang/model/element/Modifier;
/*     */       //   121: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */       //   126: pop
/*     */       //   127: aload_3
/*     */       //   128: getstatic javax/lang/model/element/Modifier.ABSTRACT : Ljavax/lang/model/element/Modifier;
/*     */       //   131: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */       //   136: pop
/*     */       //   137: goto -> 206
/*     */       //   140: aload_1
/*     */       //   141: invokeinterface getEnclosingElement : ()Ljavax/lang/model/element/Element;
/*     */       //   146: astore #4
/*     */       //   148: aload #4
/*     */       //   150: ifnull -> 206
/*     */       //   153: aload #4
/*     */       //   155: invokeinterface getKind : ()Ljavax/lang/model/element/ElementKind;
/*     */       //   160: invokevirtual isInterface : ()Z
/*     */       //   163: ifeq -> 206
/*     */       //   166: aload_3
/*     */       //   167: getstatic javax/lang/model/element/Modifier.PUBLIC : Ljavax/lang/model/element/Modifier;
/*     */       //   170: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */       //   175: pop
/*     */       //   176: aload_3
/*     */       //   177: getstatic javax/lang/model/element/Modifier.ABSTRACT : Ljavax/lang/model/element/Modifier;
/*     */       //   180: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */       //   185: pop
/*     */       //   186: aload_3
/*     */       //   187: getstatic javax/lang/model/element/Modifier.STATIC : Ljavax/lang/model/element/Modifier;
/*     */       //   190: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */       //   195: pop
/*     */       //   196: aload_3
/*     */       //   197: getstatic javax/lang/model/element/Modifier.FINAL : Ljavax/lang/model/element/Modifier;
/*     */       //   200: invokeinterface remove : (Ljava/lang/Object;)Z
/*     */       //   205: pop
/*     */       //   206: aload_3
/*     */       //   207: invokeinterface iterator : ()Ljava/util/Iterator;
/*     */       //   212: astore #4
/*     */       //   214: aload #4
/*     */       //   216: invokeinterface hasNext : ()Z
/*     */       //   221: ifeq -> 269
/*     */       //   224: aload #4
/*     */       //   226: invokeinterface next : ()Ljava/lang/Object;
/*     */       //   231: checkcast javax/lang/model/element/Modifier
/*     */       //   234: astore #5
/*     */       //   236: aload_0
/*     */       //   237: getfield writer : Ljava/io/PrintWriter;
/*     */       //   240: new java/lang/StringBuilder
/*     */       //   243: dup
/*     */       //   244: invokespecial <init> : ()V
/*     */       //   247: aload #5
/*     */       //   249: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   252: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   255: ldc ' '
/*     */       //   257: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */       //   260: invokevirtual toString : ()Ljava/lang/String;
/*     */       //   263: invokevirtual print : (Ljava/lang/String;)V
/*     */       //   266: goto -> 214
/*     */       //   269: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #325	-> 0
/*     */       //   #326	-> 7
/*     */       //   #327	-> 14
/*     */       //   #329	-> 22
/*     */       //   #330	-> 27
/*     */       //   #333	-> 31
/*     */       //   #334	-> 38
/*     */       //   #336	-> 39
/*     */       //   #337	-> 47
/*     */       //   #339	-> 60
/*     */       //   #342	-> 104
/*     */       //   #343	-> 114
/*     */       //   #346	-> 117
/*     */       //   #347	-> 127
/*     */       //   #348	-> 137
/*     */       //   #352	-> 140
/*     */       //   #353	-> 148
/*     */       //   #354	-> 155
/*     */       //   #355	-> 166
/*     */       //   #356	-> 176
/*     */       //   #357	-> 186
/*     */       //   #358	-> 196
/*     */       //   #364	-> 206
/*     */       //   #365	-> 236
/*     */       //   #366	-> 266
/*     */       //   #367	-> 269
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void printFormalTypeParameters(Parameterizable param1Parameterizable, boolean param1Boolean) {
/* 371 */       List<? extends TypeParameterElement> list = param1Parameterizable.getTypeParameters();
/* 372 */       if (list.size() > 0) {
/* 373 */         this.writer.print("<");
/*     */         
/* 375 */         boolean bool = true;
/* 376 */         for (TypeParameterElement typeParameterElement : list) {
/* 377 */           if (!bool)
/* 378 */             this.writer.print(", "); 
/* 379 */           printAnnotationsInline(typeParameterElement);
/* 380 */           this.writer.print(typeParameterElement.toString());
/* 381 */           bool = false;
/*     */         } 
/*     */         
/* 384 */         this.writer.print(">");
/* 385 */         if (param1Boolean)
/* 386 */           this.writer.print(" "); 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void printAnnotationsInline(Element param1Element) {
/* 391 */       List<? extends AnnotationMirror> list = param1Element.getAnnotationMirrors();
/* 392 */       for (AnnotationMirror annotationMirror : list) {
/* 393 */         this.writer.print(annotationMirror);
/* 394 */         this.writer.print(" ");
/*     */       } 
/*     */     }
/*     */     
/*     */     private void printAnnotations(Element param1Element) {
/* 399 */       List<? extends AnnotationMirror> list = param1Element.getAnnotationMirrors();
/* 400 */       for (AnnotationMirror annotationMirror : list) {
/* 401 */         indent();
/* 402 */         this.writer.println(annotationMirror);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void printParameters(ExecutableElement param1ExecutableElement) {
/* 408 */       List<? extends VariableElement> list = param1ExecutableElement.getParameters();
/* 409 */       int i = list.size();
/*     */       
/* 411 */       switch (i) {
/*     */         case 0:
/*     */           return;
/*     */         
/*     */         case 1:
/* 416 */           for (VariableElement variableElement : list) {
/* 417 */             printModifiers(variableElement);
/*     */             
/* 419 */             if (param1ExecutableElement.isVarArgs()) {
/* 420 */               TypeMirror typeMirror = variableElement.asType();
/* 421 */               if (typeMirror.getKind() != TypeKind.ARRAY)
/* 422 */                 throw new AssertionError("Var-args parameter is not an array type: " + typeMirror); 
/* 423 */               this.writer.print(((ArrayType)ArrayType.class.cast(typeMirror)).getComponentType());
/* 424 */               this.writer.print("...");
/*     */             } else {
/* 426 */               this.writer.print(variableElement.asType());
/* 427 */             }  this.writer.print(" " + variableElement.getSimpleName());
/*     */           } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 433 */       byte b = 1;
/* 434 */       for (VariableElement variableElement : list) {
/* 435 */         if (b == 2) {
/* 436 */           this.indentation++;
/*     */         }
/* 438 */         if (b > 1) {
/* 439 */           indent();
/*     */         }
/* 441 */         printModifiers(variableElement);
/*     */         
/* 443 */         if (b == i && param1ExecutableElement.isVarArgs()) {
/* 444 */           TypeMirror typeMirror = variableElement.asType();
/* 445 */           if (typeMirror.getKind() != TypeKind.ARRAY)
/* 446 */             throw new AssertionError("Var-args parameter is not an array type: " + typeMirror); 
/* 447 */           this.writer.print(((ArrayType)ArrayType.class.cast(typeMirror)).getComponentType());
/*     */           
/* 449 */           this.writer.print("...");
/*     */         } else {
/* 451 */           this.writer.print(variableElement.asType());
/* 452 */         }  this.writer.print(" " + variableElement.getSimpleName());
/*     */         
/* 454 */         if (b < i) {
/* 455 */           this.writer.println(",");
/*     */         }
/* 457 */         b++;
/*     */       } 
/*     */       
/* 460 */       if (list.size() >= 2) {
/* 461 */         this.indentation--;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void printInterfaces(TypeElement param1TypeElement) {
/* 468 */       ElementKind elementKind = param1TypeElement.getKind();
/*     */       
/* 470 */       if (elementKind != ElementKind.ANNOTATION_TYPE) {
/* 471 */         List<? extends TypeMirror> list = param1TypeElement.getInterfaces();
/* 472 */         if (list.size() > 0) {
/* 473 */           this.writer.print(elementKind.isClass() ? " implements" : " extends");
/*     */           
/* 475 */           boolean bool = true;
/* 476 */           for (TypeMirror typeMirror : list) {
/* 477 */             if (!bool)
/* 478 */               this.writer.print(","); 
/* 479 */             this.writer.print(" ");
/* 480 */             this.writer.print(typeMirror.toString());
/* 481 */             bool = false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void printThrows(ExecutableElement param1ExecutableElement) {
/* 488 */       List<? extends TypeMirror> list = param1ExecutableElement.getThrownTypes();
/* 489 */       int i = list.size();
/* 490 */       if (i != 0) {
/* 491 */         this.writer.print(" throws");
/*     */         
/* 493 */         byte b = 1;
/* 494 */         for (TypeMirror typeMirror : list) {
/* 495 */           if (b == 1) {
/* 496 */             this.writer.print(" ");
/*     */           }
/* 498 */           if (b == 2) {
/* 499 */             this.indentation++;
/*     */           }
/* 501 */           if (b >= 2) {
/* 502 */             indent();
/*     */           }
/* 504 */           this.writer.print(typeMirror);
/*     */           
/* 506 */           if (b != i) {
/* 507 */             this.writer.println(", ");
/*     */           }
/* 509 */           b++;
/*     */         } 
/*     */         
/* 512 */         if (i >= 2)
/* 513 */           this.indentation--; 
/*     */       } 
/*     */     }
/*     */     
/* 517 */     private static final String[] spaces = new String[] { "", "  ", "    ", "      ", "        ", "          ", "            ", "              ", "                ", "                  ", "                    " };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void indent() {
/* 532 */       int i = this.indentation;
/* 533 */       if (i < 0)
/*     */         return; 
/* 535 */       int j = spaces.length - 1;
/*     */       
/* 537 */       while (i > j) {
/* 538 */         this.writer.print(spaces[j]);
/* 539 */         i -= j;
/*     */       } 
/* 541 */       this.writer.print(spaces[i]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\processing\PrintingProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */