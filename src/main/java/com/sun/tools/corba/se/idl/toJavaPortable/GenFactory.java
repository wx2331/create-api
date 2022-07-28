/*     */ package com.sun.tools.corba.se.idl.toJavaPortable;
/*     */ 
/*     */ import com.sun.tools.corba.se.idl.AttributeGen;
/*     */ import com.sun.tools.corba.se.idl.ConstGen;
/*     */ import com.sun.tools.corba.se.idl.EnumGen;
/*     */ import com.sun.tools.corba.se.idl.ExceptionGen;
/*     */ import com.sun.tools.corba.se.idl.ForwardGen;
/*     */ import com.sun.tools.corba.se.idl.ForwardValueGen;
/*     */ import com.sun.tools.corba.se.idl.GenFactory;
/*     */ import com.sun.tools.corba.se.idl.IncludeGen;
/*     */ import com.sun.tools.corba.se.idl.InterfaceGen;
/*     */ import com.sun.tools.corba.se.idl.MethodGen;
/*     */ import com.sun.tools.corba.se.idl.ModuleGen;
/*     */ import com.sun.tools.corba.se.idl.NativeGen;
/*     */ import com.sun.tools.corba.se.idl.ParameterGen;
/*     */ import com.sun.tools.corba.se.idl.PragmaGen;
/*     */ import com.sun.tools.corba.se.idl.PrimitiveGen;
/*     */ import com.sun.tools.corba.se.idl.SequenceGen;
/*     */ import com.sun.tools.corba.se.idl.StringGen;
/*     */ import com.sun.tools.corba.se.idl.StructGen;
/*     */ import com.sun.tools.corba.se.idl.TypedefGen;
/*     */ import com.sun.tools.corba.se.idl.UnionGen;
/*     */ import com.sun.tools.corba.se.idl.ValueBoxGen;
/*     */ import com.sun.tools.corba.se.idl.ValueGen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenFactory
/*     */   implements GenFactory
/*     */ {
/*     */   public AttributeGen createAttributeGen() {
/*  48 */     if (Util.corbaLevel(2.4F, 99.0F)) {
/*  49 */       return new AttributeGen24();
/*     */     }
/*  51 */     return new AttributeGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConstGen createConstGen() {
/*  56 */     return new ConstGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeGen createNativeGen() {
/*  61 */     return new NativeGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumGen createEnumGen() {
/*  66 */     return new EnumGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public ExceptionGen createExceptionGen() {
/*  71 */     return new ExceptionGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public ForwardGen createForwardGen() {
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ForwardValueGen createForwardValueGen() {
/*  81 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public IncludeGen createIncludeGen() {
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public InterfaceGen createInterfaceGen() {
/*  91 */     return new InterfaceGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueGen createValueGen() {
/*  96 */     if (Util.corbaLevel(2.4F, 99.0F)) {
/*  97 */       return new ValueGen24();
/*     */     }
/*  99 */     return new ValueGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public ValueBoxGen createValueBoxGen() {
/* 104 */     if (Util.corbaLevel(2.4F, 99.0F)) {
/* 105 */       return new ValueBoxGen24();
/*     */     }
/* 107 */     return new ValueBoxGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public MethodGen createMethodGen() {
/* 112 */     if (Util.corbaLevel(2.4F, 99.0F)) {
/* 113 */       return new MethodGen24();
/*     */     }
/* 115 */     return new MethodGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public ModuleGen createModuleGen() {
/* 120 */     return new ModuleGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public ParameterGen createParameterGen() {
/* 125 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PragmaGen createPragmaGen() {
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PrimitiveGen createPrimitiveGen() {
/* 135 */     return new PrimitiveGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public SequenceGen createSequenceGen() {
/* 140 */     return new SequenceGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public StringGen createStringGen() {
/* 145 */     return new StringGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public StructGen createStructGen() {
/* 150 */     return new StructGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedefGen createTypedefGen() {
/* 155 */     return new TypedefGen();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnionGen createUnionGen() {
/* 160 */     return new UnionGen();
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\corba\se\idl\toJavaPortable\GenFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */