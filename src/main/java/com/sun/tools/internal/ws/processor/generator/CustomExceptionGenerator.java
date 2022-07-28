/*     */ package com.sun.tools.internal.ws.processor.generator;
/*     */ 
/*     */ import com.sun.codemodel.internal.ClassType;
/*     */ import com.sun.codemodel.internal.JAnnotatable;
/*     */ import com.sun.codemodel.internal.JAnnotationUse;
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JClassAlreadyExistsException;
/*     */ import com.sun.codemodel.internal.JDefinedClass;
/*     */ import com.sun.codemodel.internal.JDocComment;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JFieldRef;
/*     */ import com.sun.codemodel.internal.JFieldVar;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.ws.processor.model.Fault;
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.wscompile.ErrorReceiver;
/*     */ import com.sun.tools.internal.ws.wscompile.WsimportOptions;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.ws.WebFault;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CustomExceptionGenerator
/*     */   extends GeneratorBase
/*     */ {
/*  56 */   private Map<String, JClass> faults = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   public static void generate(Model model, WsimportOptions options, ErrorReceiver receiver) {
/*  61 */     CustomExceptionGenerator exceptionGen = new CustomExceptionGenerator();
/*  62 */     exceptionGen.init(model, options, receiver);
/*  63 */     exceptionGen.doGeneration();
/*     */   }
/*     */   
/*     */   public GeneratorBase getGenerator(Model model, WsimportOptions options, ErrorReceiver receiver) {
/*  67 */     GeneratorBase g = new CustomExceptionGenerator();
/*  68 */     g.init(model, options, receiver);
/*  69 */     return g;
/*     */   }
/*     */ 
/*     */   
/*     */   public void visit(Fault fault) throws Exception {
/*  74 */     if (isRegistered(fault))
/*     */       return; 
/*  76 */     registerFault(fault);
/*     */   }
/*     */   
/*     */   private boolean isRegistered(Fault fault) {
/*  80 */     if (this.faults.keySet().contains(fault.getJavaException().getName())) {
/*  81 */       fault.setExceptionClass(this.faults.get(fault.getJavaException().getName()));
/*  82 */       return true;
/*     */     } 
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   private void registerFault(Fault fault) {
/*     */     try {
/*  89 */       write(fault);
/*  90 */       this.faults.put(fault.getJavaException().getName(), fault.getExceptionClass());
/*  91 */     } catch (JClassAlreadyExistsException e) {
/*  92 */       throw new GeneratorException("generator.nestedGeneratorError", new Object[] { e });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void write(Fault fault) throws JClassAlreadyExistsException {
/*  97 */     String className = Names.customExceptionClassName(fault);
/*     */     
/*  99 */     JDefinedClass cls = this.cm._class(className, ClassType.CLASS);
/* 100 */     JDocComment comment = cls.javadoc();
/* 101 */     if (fault.getJavaDoc() != null) {
/* 102 */       comment.add(fault.getJavaDoc());
/* 103 */       comment.add("\n\n");
/*     */     } 
/*     */     
/* 106 */     for (String doc : getJAXWSClassComment()) {
/* 107 */       comment.add(doc);
/*     */     }
/*     */     
/* 110 */     cls._extends(Exception.class);
/*     */ 
/*     */     
/* 113 */     JAnnotationUse faultAnn = cls.annotate(WebFault.class);
/* 114 */     faultAnn.param("name", fault.getBlock().getName().getLocalPart());
/* 115 */     faultAnn.param("targetNamespace", fault.getBlock().getName().getNamespaceURI());
/*     */     
/* 117 */     JType faultBean = fault.getBlock().getType().getJavaType().getType().getType();
/*     */ 
/*     */     
/* 120 */     JFieldVar fi = cls.field(4, faultBean, "faultInfo");
/*     */ 
/*     */     
/* 123 */     fault.getBlock().getType().getJavaType().getType().annotate((JAnnotatable)fi);
/*     */     
/* 125 */     fi.javadoc().add("Java type that goes as soapenv:Fault detail element.");
/* 126 */     JFieldRef fr = JExpr.ref(JExpr._this(), (JVar)fi);
/*     */ 
/*     */     
/* 129 */     JMethod constrc1 = cls.constructor(1);
/* 130 */     JVar var1 = constrc1.param(String.class, "message");
/* 131 */     JVar var2 = constrc1.param(faultBean, "faultInfo");
/* 132 */     constrc1.javadoc().addParam(var1);
/* 133 */     constrc1.javadoc().addParam(var2);
/* 134 */     JBlock cb1 = constrc1.body();
/* 135 */     cb1.invoke("super").arg((JExpression)var1);
/*     */     
/* 137 */     cb1.assign((JAssignmentTarget)fr, (JExpression)var2);
/*     */ 
/*     */     
/* 140 */     JMethod constrc2 = cls.constructor(1);
/* 141 */     var1 = constrc2.param(String.class, "message");
/* 142 */     var2 = constrc2.param(faultBean, "faultInfo");
/* 143 */     JVar var3 = constrc2.param(Throwable.class, "cause");
/* 144 */     constrc2.javadoc().addParam(var1);
/* 145 */     constrc2.javadoc().addParam(var2);
/* 146 */     constrc2.javadoc().addParam(var3);
/* 147 */     JBlock cb2 = constrc2.body();
/* 148 */     cb2.invoke("super").arg((JExpression)var1).arg((JExpression)var3);
/* 149 */     cb2.assign((JAssignmentTarget)fr, (JExpression)var2);
/*     */ 
/*     */ 
/*     */     
/* 153 */     JMethod fim = cls.method(1, faultBean, "getFaultInfo");
/* 154 */     fim.javadoc().addReturn().add("returns fault bean: " + faultBean.fullName());
/* 155 */     JBlock fib = fim.body();
/* 156 */     fib._return((JExpression)fi);
/* 157 */     fault.setExceptionClass((JClass)cls);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\generator\CustomExceptionGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */