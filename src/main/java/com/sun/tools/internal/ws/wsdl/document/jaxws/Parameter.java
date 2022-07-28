/*     */ package com.sun.tools.internal.ws.wsdl.document.jaxws;
/*     */ 
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
/*     */ public class Parameter
/*     */ {
/*     */   private String part;
/*     */   private QName element;
/*     */   private String name;
/*     */   private String messageName;
/*     */   
/*     */   public Parameter(String msgName, String part, QName element, String name) {
/*  48 */     this.part = part;
/*  49 */     this.element = element;
/*  50 */     this.name = name;
/*  51 */     this.messageName = msgName;
/*     */   }
/*     */   
/*     */   public String getMessageName() {
/*  55 */     return this.messageName;
/*     */   }
/*     */   
/*     */   public void setMessageName(String messageName) {
/*  59 */     this.messageName = messageName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getElement() {
/*  66 */     return this.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElement(QName element) {
/*  73 */     this.element = element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  80 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  87 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPart() {
/*  94 */     return this.part;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPart(String part) {
/* 101 */     this.part = part;
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\wsdl\document\jaxws\Parameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */