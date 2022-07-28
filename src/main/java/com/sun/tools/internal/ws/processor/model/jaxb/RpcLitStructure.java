/*    */ package com.sun.tools.internal.ws.processor.model.jaxb;
/*    */ 
/*    */ import com.sun.tools.internal.ws.processor.model.AbstractType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RpcLitStructure
/*    */   extends AbstractType
/*    */ {
/*    */   private List<RpcLitMember> members;
/*    */   private JAXBModel jaxbModel;
/*    */   
/*    */   public RpcLitStructure() {}
/*    */   
/*    */   public RpcLitStructure(QName name, JAXBModel jaxbModel) {
/* 51 */     setName(name);
/* 52 */     this.jaxbModel = jaxbModel;
/* 53 */     this.members = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public RpcLitStructure(QName name, JAXBModel jaxbModel, List<RpcLitMember> members) {
/* 57 */     setName(name);
/* 58 */     this.members = members;
/*    */   }
/*    */   
/*    */   public void accept(JAXBTypeVisitor visitor) throws Exception {
/* 62 */     visitor.visit(this);
/*    */   }
/*    */   
/*    */   public List<RpcLitMember> getRpcLitMembers() {
/* 66 */     return this.members;
/*    */   }
/*    */   
/*    */   public List<RpcLitMember> setRpcLitMembers(List<RpcLitMember> members) {
/* 70 */     return this.members = members;
/*    */   }
/*    */   
/*    */   public void addRpcLitMember(RpcLitMember member) {
/* 74 */     this.members.add(member);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JAXBModel getJaxbModel() {
/* 80 */     return this.jaxbModel;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setJaxbModel(JAXBModel jaxbModel) {
/* 86 */     this.jaxbModel = jaxbModel;
/*    */   }
/*    */   
/*    */   public boolean isLiteralType() {
/* 90 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\ws\processor\model\jaxb\RpcLitStructure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */