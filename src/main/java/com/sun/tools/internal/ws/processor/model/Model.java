/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.jaxb.JAXBModel;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
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
/*     */ public class Model
/*     */   extends ModelObject
/*     */ {
/*     */   private QName name;
/*     */   private String targetNamespace;
/*     */   private List<Service> services;
/*     */   private Map<QName, Service> servicesByName;
/*     */   private Set<AbstractType> extraTypes;
/*     */   private String source;
/*     */   private JAXBModel jaxBModel;
/*     */   
/*     */   public Model(Entity entity) {
/*  43 */     super(entity);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 153 */     this.services = new ArrayList<>();
/* 154 */     this.servicesByName = new HashMap<>();
/* 155 */     this.extraTypes = new HashSet<>();
/*     */     
/* 157 */     this.jaxBModel = null; } public Model(QName name, Entity entity) { super(entity); this.services = new ArrayList<>(); this.servicesByName = new HashMap<>(); this.extraTypes = new HashSet<>(); this.jaxBModel = null;
/*     */     this.name = name; }
/*     */ 
/*     */   
/*     */   public QName getName() {
/*     */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(QName n) {
/*     */     this.name = n;
/*     */   }
/*     */   
/*     */   public String getTargetNamespaceURI() {
/*     */     return this.targetNamespace;
/*     */   }
/*     */   
/*     */   public void setTargetNamespaceURI(String s) {
/*     */     this.targetNamespace = s;
/*     */   }
/*     */   
/*     */   public void addService(Service service) {
/*     */     if (this.servicesByName.containsKey(service.getName()))
/*     */       throw new ModelException("model.uniqueness", new Object[0]); 
/*     */     this.services.add(service);
/*     */     this.servicesByName.put(service.getName(), service);
/*     */   }
/*     */   
/*     */   public Service getServiceByName(QName name) {
/*     */     if (this.servicesByName.size() != this.services.size())
/*     */       initializeServicesByName(); 
/*     */     return this.servicesByName.get(name);
/*     */   }
/*     */   
/*     */   public List<Service> getServices() {
/*     */     return this.services;
/*     */   }
/*     */   
/*     */   public void setServices(List<Service> l) {
/*     */     this.services = l;
/*     */   }
/*     */   
/*     */   private void initializeServicesByName() {
/*     */     this.servicesByName = new HashMap<>();
/*     */     if (this.services != null)
/*     */       for (Service service : this.services) {
/*     */         if (service.getName() != null && this.servicesByName.containsKey(service.getName()))
/*     */           throw new ModelException("model.uniqueness", new Object[0]); 
/*     */         this.servicesByName.put(service.getName(), service);
/*     */       }  
/*     */   }
/*     */   
/*     */   public void addExtraType(AbstractType type) {
/*     */     this.extraTypes.add(type);
/*     */   }
/*     */   
/*     */   public Iterator getExtraTypes() {
/*     */     return this.extraTypes.iterator();
/*     */   }
/*     */   
/*     */   public Set<AbstractType> getExtraTypesSet() {
/*     */     return this.extraTypes;
/*     */   }
/*     */   
/*     */   public void setExtraTypesSet(Set<AbstractType> s) {
/*     */     this.extraTypes = s;
/*     */   }
/*     */   
/*     */   public void accept(ModelVisitor visitor) throws Exception {
/*     */     visitor.visit(this);
/*     */   }
/*     */   
/*     */   public String getSource() {
/*     */     return this.source;
/*     */   }
/*     */   
/*     */   public void setSource(String string) {
/*     */     this.source = string;
/*     */   }
/*     */   
/*     */   public void setJAXBModel(JAXBModel jaxBModel) {
/*     */     this.jaxBModel = jaxBModel;
/*     */   }
/*     */   
/*     */   public JAXBModel getJAXBModel() {
/*     */     return this.jaxBModel;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Model.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */