/*     */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*     */ 
/*     */ import com.sun.tools.internal.xjc.api.J2SJAXBModel;
/*     */ import com.sun.tools.internal.xjc.api.Mapping;
/*     */ import com.sun.tools.internal.xjc.api.S2JJAXBModel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAXBModel
/*     */ {
/*     */   private List<JAXBMapping> mappings;
/*  51 */   private final Map<QName, JAXBMapping> byQName = new HashMap<>();
/*  52 */   private final Map<String, JAXBMapping> byClassName = new HashMap<>();
/*     */   private com.sun.tools.internal.xjc.api.JAXBModel rawJAXBModel;
/*     */   private Set<String> generatedClassNames;
/*     */   
/*     */   public com.sun.tools.internal.xjc.api.JAXBModel getRawJAXBModel() {
/*  57 */     return this.rawJAXBModel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public S2JJAXBModel getS2JJAXBModel() {
/*  64 */     if (this.rawJAXBModel instanceof S2JJAXBModel)
/*  65 */       return (S2JJAXBModel)this.rawJAXBModel; 
/*  66 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public J2SJAXBModel getJ2SJAXBModel() {
/*  73 */     if (this.rawJAXBModel instanceof J2SJAXBModel)
/*  74 */       return (J2SJAXBModel)this.rawJAXBModel; 
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBModel() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBModel(com.sun.tools.internal.xjc.api.JAXBModel rawModel) {
/*  88 */     this.rawJAXBModel = rawModel;
/*  89 */     if (rawModel instanceof S2JJAXBModel) {
/*  90 */       S2JJAXBModel model = (S2JJAXBModel)rawModel;
/*  91 */       List<JAXBMapping> ms = new ArrayList<>(model.getMappings().size());
/*  92 */       for (Mapping m : model.getMappings())
/*  93 */         ms.add(new JAXBMapping(m)); 
/*  94 */       setMappings(ms);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JAXBMapping> getMappings() {
/* 101 */     return this.mappings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMappings(List<JAXBMapping> mappings) {
/* 106 */     this.mappings = mappings;
/* 107 */     this.byQName.clear();
/* 108 */     this.byClassName.clear();
/* 109 */     for (JAXBMapping m : mappings) {
/* 110 */       this.byQName.put(m.getElementName(), m);
/* 111 */       this.byClassName.put(m.getType().getName(), m);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBMapping get(QName elementName) {
/* 118 */     return this.byQName.get(elementName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBMapping get(String className) {
/* 124 */     return this.byClassName.get(className);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getGeneratedClassNames() {
/* 133 */     return this.generatedClassNames;
/*     */   }
/*     */   
/*     */   public void setGeneratedClassNames(Set<String> generatedClassNames) {
/* 137 */     this.generatedClassNames = generatedClassNames;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\jaxb\JAXBModel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */