/*     */ package com.sun.tools.internal.ws.processor.util;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.AbstractType;
/*     */ import com.sun.tools.internal.ws.processor.model.Block;
/*     */ import com.sun.tools.internal.ws.processor.model.ExtendedModelVisitor;
/*     */ import com.sun.tools.internal.ws.processor.model.Fault;
/*     */ import com.sun.tools.internal.ws.processor.model.Model;
/*     */ import com.sun.tools.internal.ws.processor.model.Parameter;
/*     */ import com.sun.tools.internal.ws.processor.model.Port;
/*     */ import com.sun.tools.internal.ws.processor.model.Service;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBType;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBTypeVisitor;
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.RpcLitStructure;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClassNameCollector
/*     */   extends ExtendedModelVisitor
/*     */   implements JAXBTypeVisitor
/*     */ {
/*     */   private Set<String> _seiClassNames;
/*     */   private Set<String> _jaxbGeneratedClassNames;
/*     */   private Set<String> _exceptionClassNames;
/*     */   
/*     */   public void process(Model model) {
/*     */     try {
/*  52 */       this._allClassNames = new HashSet();
/*  53 */       this._exceptions = new HashSet();
/*  54 */       this._wsdlBindingNames = new HashSet();
/*  55 */       this._conflictingClassNames = new HashSet();
/*  56 */       this._seiClassNames = new HashSet<>();
/*  57 */       this._jaxbGeneratedClassNames = new HashSet<>();
/*  58 */       this._exceptionClassNames = new HashSet<>();
/*  59 */       this._portTypeNames = new HashSet<>();
/*  60 */       visit(model);
/*  61 */     } catch (Exception e) {
/*  62 */       e.printStackTrace();
/*     */     } finally {
/*     */       
/*  65 */       this._allClassNames = null;
/*  66 */       this._exceptions = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set getConflictingClassNames() {
/*  71 */     return this._conflictingClassNames;
/*     */   }
/*     */   
/*     */   protected void postVisit(Model model) throws Exception {
/*  75 */     for (Iterator<AbstractType> iter = model.getExtraTypes(); iter.hasNext();) {
/*  76 */       visitType(iter.next());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void preVisit(Service service) throws Exception {
/*  81 */     registerClassName(service
/*  82 */         .getJavaInterface().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processPort11x(Port port) {
/*  90 */     QName wsdlBindingName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLBindingName");
/*     */     
/*  92 */     if (!this._wsdlBindingNames.contains(wsdlBindingName))
/*     */     {
/*     */       
/*  95 */       registerClassName(port.getJavaInterface().getName());
/*     */     }
/*  97 */     registerClassName((String)port.getProperty("com.sun.xml.internal.ws.processor.model.StubClassName"));
/*     */     
/*  99 */     registerClassName((String)port.getProperty("com.sun.xml.internal.ws.processor.model.TieClassName"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void preVisit(Port port) throws Exception {
/* 104 */     QName portTypeName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName");
/* 105 */     if (this._portTypeNames.contains(portTypeName)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 111 */     addSEIClassName(port.getJavaInterface().getName());
/*     */   }
/*     */   
/*     */   private void addSEIClassName(String s) {
/* 115 */     this._seiClassNames.add(s);
/* 116 */     registerClassName(s);
/*     */   }
/*     */   
/*     */   protected void postVisit(Port port) throws Exception {
/* 120 */     QName wsdlBindingName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLBindingName");
/*     */     
/* 122 */     if (!this._wsdlBindingNames.contains(wsdlBindingName)) {
/* 123 */       this._wsdlBindingNames.add(wsdlBindingName);
/*     */     }
/*     */     
/* 126 */     QName portTypeName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLPortTypeName");
/* 127 */     if (!this._portTypeNames.contains(portTypeName)) {
/* 128 */       this._portTypeNames.add(portTypeName);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean shouldVisit(Port port) {
/* 133 */     QName wsdlBindingName = (QName)port.getProperty("com.sun.xml.internal.ws.processor.model.WSDLBindingName");
/*     */     
/* 135 */     return !this._wsdlBindingNames.contains(wsdlBindingName);
/*     */   }
/*     */   
/*     */   protected void preVisit(Fault fault) throws Exception {
/* 139 */     if (!this._exceptions.contains(fault.getJavaException())) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 144 */       this._exceptions.add(fault.getJavaException());
/* 145 */       addExceptionClassName(fault.getJavaException().getName());
/*     */       
/* 147 */       Iterator<Fault> iter = fault.getSubfaults();
/* 148 */       while (iter != null && iter.hasNext()) {
/*     */         
/* 150 */         Fault subfault = iter.next();
/* 151 */         preVisit(subfault);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addExceptionClassName(String name) {
/* 157 */     if (this._allClassNames.contains(name))
/* 158 */       this._exceptionClassNames.add(name); 
/* 159 */     registerClassName(name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitBodyBlock(Block block) throws Exception {
/* 164 */     visitBlock(block);
/*     */   }
/*     */   
/*     */   protected void visitHeaderBlock(Block block) throws Exception {
/* 168 */     visitBlock(block);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void visitFaultBlock(Block block) throws Exception {}
/*     */   
/*     */   protected void visitBlock(Block block) throws Exception {
/* 175 */     visitType(block.getType());
/*     */   }
/*     */   
/*     */   protected void visit(Parameter parameter) throws Exception {
/* 179 */     visitType(parameter.getType());
/*     */   }
/*     */   
/*     */   private void visitType(AbstractType type) throws Exception {
/* 183 */     if (type != null) {
/* 184 */       if (type instanceof JAXBType) {
/* 185 */         visitType((JAXBType)type);
/* 186 */       } else if (type instanceof RpcLitStructure) {
/* 187 */         visitType((RpcLitStructure)type);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void visitType(JAXBType type) throws Exception {
/* 193 */     type.accept(this);
/*     */   }
/*     */   
/*     */   private void visitType(RpcLitStructure type) throws Exception {
/* 197 */     type.accept(this);
/*     */   }
/*     */   private void registerClassName(String name) {
/* 200 */     if (name == null || name.equals("")) {
/*     */       return;
/*     */     }
/* 203 */     if (this._allClassNames.contains(name)) {
/* 204 */       this._conflictingClassNames.add(name);
/*     */     } else {
/* 206 */       this._allClassNames.add(name);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<String> getSeiClassNames() {
/* 211 */     return this._seiClassNames;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getJaxbGeneratedClassNames() {
/* 217 */     return this._jaxbGeneratedClassNames;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getExceptionClassNames() {
/* 224 */     return this._exceptionClassNames;
/*     */   }
/*     */   boolean doneVisitingJAXBModel = false; private Set _allClassNames;
/*     */   private Set _exceptions;
/*     */   
/*     */   public void visit(JAXBType type) throws Exception {
/* 230 */     if (!this.doneVisitingJAXBModel && type.getJaxbModel() != null) {
/* 231 */       Set<String> classNames = type.getJaxbModel().getGeneratedClassNames();
/* 232 */       for (String className : classNames) {
/* 233 */         addJAXBGeneratedClassName(className);
/*     */       }
/* 235 */       this.doneVisitingJAXBModel = true;
/*     */     } 
/*     */   }
/*     */   private Set _wsdlBindingNames; private Set _conflictingClassNames; private Set<QName> _portTypeNames;
/*     */   public void visit(RpcLitStructure type) throws Exception {
/* 240 */     if (!this.doneVisitingJAXBModel) {
/* 241 */       Set<String> classNames = type.getJaxbModel().getGeneratedClassNames();
/* 242 */       for (String className : classNames) {
/* 243 */         addJAXBGeneratedClassName(className);
/*     */       }
/* 245 */       this.doneVisitingJAXBModel = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addJAXBGeneratedClassName(String name) {
/* 251 */     this._jaxbGeneratedClassNames.add(name);
/* 252 */     registerClassName(name);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processo\\util\ClassNameCollector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */