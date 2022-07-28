/*     */ package com.sun.tools.internal.ws.processor.model;
/*     */ 
/*     */ import com.sun.tools.internal.ws.processor.model.java.JavaParameter;
/*     */ import com.sun.tools.internal.ws.wsdl.document.Message;
/*     */ import com.sun.tools.internal.ws.wsdl.framework.Entity;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.jws.WebParam;
/*     */ 
/*     */ public class Parameter extends ModelObject {
/*     */   private final String entityName;
/*     */   private String name;
/*     */   private JavaParameter javaParameter;
/*     */   private AbstractType type;
/*     */   private Block block;
/*     */   private Parameter link;
/*     */   private boolean embedded;
/*     */   private String typeName;
/*     */   private String customName;
/*     */   private WebParam.Mode mode;
/*     */   private int parameterOrderPosition;
/*     */   private List<String> annotations;
/*     */   
/*     */   public String getEntityName() {
/*     */     return this.entityName;
/*     */   }
/*     */   
/*     */   public String getName() {
/*     */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String s) {
/*     */     this.name = s;
/*     */   }
/*     */   
/*     */   public JavaParameter getJavaParameter() {
/*     */     return this.javaParameter;
/*     */   }
/*     */   
/*     */   public void setJavaParameter(JavaParameter p) {
/*     */     this.javaParameter = p;
/*     */   }
/*     */   
/*  44 */   public Parameter(String name, Entity entity) { super(entity);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     this.annotations = new ArrayList<>(); this.name = name; if (entity instanceof Message) { this.entityName = ((Message)entity).getName(); } else if (entity instanceof MessagePart) { this.entityName = ((MessagePart)entity).getName(); } else { this.entityName = name; }
/*     */      } public AbstractType getType() { return this.type; }
/*     */   public void setType(AbstractType t) { this.type = t; }
/*     */   public String getTypeName() { return this.typeName; }
/*     */   public void setTypeName(String t) { this.typeName = t; }
/*     */   public Block getBlock() { return this.block; }
/* 164 */   public List<String> getAnnotations() { return this.annotations; }
/*     */   public void setBlock(Block d) { this.block = d; }
/*     */   public Parameter getLinkedParameter() { return this.link; }
/*     */   public void setLinkedParameter(Parameter p) { this.link = p; }
/*     */   public boolean isEmbedded() {
/*     */     return this.embedded;
/*     */   } public void setEmbedded(boolean b) {
/*     */     this.embedded = b;
/* 172 */   } public void setAnnotations(List<String> annotations) { this.annotations = annotations; }
/*     */   public void accept(ModelVisitor visitor) throws Exception { visitor.visit(this); }
/*     */   public int getParameterIndex() { return this.parameterOrderPosition; }
/*     */   public void setParameterIndex(int parameterOrderPosition) { this.parameterOrderPosition = parameterOrderPosition; }
/* 176 */   public boolean isReturn() { return (this.parameterOrderPosition == -1); } public String getCustomName() { return this.customName; } public void setCustomName(String customName) { this.customName = customName; } public void setMode(WebParam.Mode mode) { this.mode = mode; }
/*     */ 
/*     */   
/*     */   public boolean isIN() {
/* 180 */     return (this.mode == WebParam.Mode.IN);
/*     */   }
/*     */   
/*     */   public boolean isOUT() {
/* 184 */     return (this.mode == WebParam.Mode.OUT);
/*     */   }
/*     */   
/*     */   public boolean isINOUT() {
/* 188 */     return (this.mode == WebParam.Mode.INOUT);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\Parameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */