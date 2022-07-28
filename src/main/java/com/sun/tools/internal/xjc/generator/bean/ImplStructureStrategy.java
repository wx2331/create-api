/*     */ package com.sun.tools.internal.xjc.generator.bean;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassContainer;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JPackage;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.annotation.spec.XmlAccessorTypeWriter;
/*     */ import com.sun.tools.internal.xjc.model.CClassInfo;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.internal.xjc.outline.Outline;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlEnum;
/*     */ import javax.xml.bind.annotation.XmlEnumValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlEnum(Boolean.class)
/*     */ public enum ImplStructureStrategy
/*     */ {
/*  62 */   BEAN_ONLY
/*     */   {
/*     */     protected Result createClasses(Outline outline, CClassInfo bean) {
/*  65 */       JClassContainer parent = outline.getContainer(bean.parent(), Aspect.EXPOSED);
/*     */       
/*  67 */       JDefinedClass impl = outline.getClassFactory().createClass(parent, 0x1 | (
/*     */           
/*  69 */           parent.isPackage() ? 0 : 16) | (bean.isAbstract() ? 32 : 0), bean.shortName, bean
/*  70 */           .getLocator());
/*  71 */       ((XmlAccessorTypeWriter)impl.annotate2(XmlAccessorTypeWriter.class)).value(XmlAccessType.FIELD);
/*     */       
/*  73 */       return new Result(impl, impl);
/*     */     }
/*     */     
/*     */     protected JPackage getPackage(JPackage pkg, Aspect a) {
/*  77 */       return pkg;
/*     */     }
/*     */     
/*     */     protected MethodWriter createMethodWriter(final ClassOutlineImpl target) {
/*  81 */       assert target.ref == target.implClass;
/*     */       
/*  83 */       return new MethodWriter(target) {
/*  84 */           private final JDefinedClass impl = target.implClass;
/*     */           
/*     */           private JMethod implMethod;
/*     */           
/*     */           public JVar addParameter(JType type, String name) {
/*  89 */             return this.implMethod.param(type, name);
/*     */           }
/*     */           
/*     */           public JMethod declareMethod(JType returnType, String methodName) {
/*  93 */             this.implMethod = this.impl.method(1, returnType, methodName);
/*  94 */             return this.implMethod;
/*     */           }
/*     */           
/*     */           public JDocComment javadoc() {
/*  98 */             return this.implMethod.javadoc();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     protected void _extends(ClassOutlineImpl derived, ClassOutlineImpl base) {
/* 104 */       derived.implClass._extends(base.implRef);
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 114 */   INTF_AND_IMPL
/*     */   {
/*     */     protected Result createClasses(Outline outline, CClassInfo bean) {
/* 117 */       JClassContainer parent = outline.getContainer(bean.parent(), Aspect.EXPOSED);
/*     */       
/* 119 */       JDefinedClass intf = outline.getClassFactory().createInterface(parent, bean.shortName, bean
/* 120 */           .getLocator());
/*     */       
/* 122 */       parent = outline.getContainer(bean.parent(), Aspect.IMPLEMENTATION);
/* 123 */       JDefinedClass impl = outline.getClassFactory().createClass(parent, 0x1 | (
/*     */           
/* 125 */           parent.isPackage() ? 0 : 16) | (bean.isAbstract() ? 32 : 0), bean.shortName + "Impl", bean
/* 126 */           .getLocator());
/* 127 */       ((XmlAccessorTypeWriter)impl.annotate2(XmlAccessorTypeWriter.class)).value(XmlAccessType.FIELD);
/*     */       
/* 129 */       impl._implements((JClass)intf);
/*     */       
/* 131 */       return new Result(intf, impl);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected JPackage getPackage(JPackage pkg, Aspect a) {
/*     */       // Byte code:
/*     */       //   0: getstatic com/sun/tools/internal/xjc/generator/bean/ImplStructureStrategy$3.$SwitchMap$com$sun$tools$internal$xjc$outline$Aspect : [I
/*     */       //   3: aload_2
/*     */       //   4: invokevirtual ordinal : ()I
/*     */       //   7: iaload
/*     */       //   8: lookupswitch default -> 45, 1 -> 36, 2 -> 38
/*     */       //   36: aload_1
/*     */       //   37: areturn
/*     */       //   38: aload_1
/*     */       //   39: ldc 'impl'
/*     */       //   41: invokevirtual subPackage : (Ljava/lang/String;)Lcom/sun/codemodel/internal/JPackage;
/*     */       //   44: areturn
/*     */       //   45: getstatic com/sun/tools/internal/xjc/generator/bean/ImplStructureStrategy$2.$assertionsDisabled : Z
/*     */       //   48: ifne -> 59
/*     */       //   51: new java/lang/AssertionError
/*     */       //   54: dup
/*     */       //   55: invokespecial <init> : ()V
/*     */       //   58: athrow
/*     */       //   59: new java/lang/IllegalStateException
/*     */       //   62: dup
/*     */       //   63: invokespecial <init> : ()V
/*     */       //   66: athrow
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #135	-> 0
/*     */       //   #137	-> 36
/*     */       //   #139	-> 38
/*     */       //   #141	-> 45
/*     */       //   #142	-> 59
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	67	0	this	Lcom/sun/tools/internal/xjc/generator/bean/ImplStructureStrategy$2;
/*     */       //   0	67	1	pkg	Lcom/sun/codemodel/internal/JPackage;
/*     */       //   0	67	2	a	Lcom/sun/tools/internal/xjc/outline/Aspect;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected MethodWriter createMethodWriter(final ClassOutlineImpl target) {
/* 147 */       return new MethodWriter(target) {
/* 148 */           private final JDefinedClass intf = target.ref;
/* 149 */           private final JDefinedClass impl = target.implClass;
/*     */           
/*     */           private JMethod intfMethod;
/*     */           
/*     */           private JMethod implMethod;
/*     */           
/*     */           public JVar addParameter(JType type, String name) {
/* 156 */             if (this.intf != null)
/* 157 */               this.intfMethod.param(type, name); 
/* 158 */             return this.implMethod.param(type, name);
/*     */           }
/*     */           
/*     */           public JMethod declareMethod(JType returnType, String methodName) {
/* 162 */             if (this.intf != null)
/* 163 */               this.intfMethod = this.intf.method(0, returnType, methodName); 
/* 164 */             this.implMethod = this.impl.method(1, returnType, methodName);
/* 165 */             return this.implMethod;
/*     */           }
/*     */           
/*     */           public JDocComment javadoc() {
/* 169 */             if (this.intf != null) {
/* 170 */               return this.intfMethod.javadoc();
/*     */             }
/* 172 */             return this.implMethod.javadoc();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     protected void _extends(ClassOutlineImpl derived, ClassOutlineImpl base) {
/* 178 */       derived.implClass._extends(base.implRef);
/* 179 */       derived.ref._implements((JClass)base.ref);
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Result createClasses(Outline paramOutline, CClassInfo paramCClassInfo);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract JPackage getPackage(JPackage paramJPackage, Aspect paramAspect);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract MethodWriter createMethodWriter(ClassOutlineImpl paramClassOutlineImpl);
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void _extends(ClassOutlineImpl paramClassOutlineImpl1, ClassOutlineImpl paramClassOutlineImpl2);
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Result
/*     */   {
/*     */     public final JDefinedClass exposed;
/*     */ 
/*     */     
/*     */     public final JDefinedClass implementation;
/*     */ 
/*     */ 
/*     */     
/*     */     public Result(JDefinedClass exposed, JDefinedClass implementation) {
/* 212 */       this.exposed = exposed;
/* 213 */       this.implementation = implementation;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\ImplStructureStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */