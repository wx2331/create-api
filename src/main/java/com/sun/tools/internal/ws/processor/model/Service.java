/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaInterface;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class Service
/*     */   extends ModelObject
/*     */ {
/*     */   private QName name;
/*     */   private List<Port> ports;
/*     */   private Map<QName, Port> portsByName;
/*     */   private JavaInterface javaInterface;
/*     */   
/*     */   public Service(Entity entity) {
/*  41 */     super(entity);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     this.ports = new ArrayList<>();
/* 118 */     this.portsByName = new HashMap<>(); } public Service(QName name, JavaInterface javaInterface, Entity entity) { super(entity); this.ports = new ArrayList<>(); this.portsByName = new HashMap<>();
/*     */     this.name = name;
/*     */     this.javaInterface = javaInterface; }
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
/*     */   public void addPort(Port port) {
/*     */     if (this.portsByName.containsKey(port.getName()))
/*     */       throw new ModelException("model.uniqueness", new Object[0]); 
/*     */     this.ports.add(port);
/*     */     this.portsByName.put(port.getName(), port);
/*     */   }
/*     */   
/*     */   public Port getPortByName(QName n) {
/*     */     if (this.portsByName.size() != this.ports.size())
/*     */       initializePortsByName(); 
/*     */     return this.portsByName.get(n);
/*     */   }
/*     */   
/*     */   public List<Port> getPorts() {
/*     */     return this.ports;
/*     */   }
/*     */   
/*     */   public void setPorts(List<Port> m) {
/*     */     this.ports = m;
/*     */   }
/*     */   
/*     */   private void initializePortsByName() {
/*     */     this.portsByName = new HashMap<>();
/*     */     if (this.ports != null)
/*     */       for (Iterator<Port> iter = this.ports.iterator(); iter.hasNext(); ) {
/*     */         Port port = iter.next();
/*     */         if (port.getName() != null && this.portsByName.containsKey(port.getName()))
/*     */           throw new ModelException("model.uniqueness", new Object[0]); 
/*     */         this.portsByName.put(port.getName(), port);
/*     */       }  
/*     */   }
/*     */   
/*     */   public JavaInterface getJavaIntf() {
/*     */     return getJavaInterface();
/*     */   }
/*     */   
/*     */   public JavaInterface getJavaInterface() {
/*     */     return this.javaInterface;
/*     */   }
/*     */   
/*     */   public void setJavaInterface(JavaInterface i) {
/*     */     this.javaInterface = i;
/*     */   }
/*     */   
/*     */   public void accept(ModelVisitor visitor) throws Exception {
/*     */     visitor.visit(this);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */