/*     */ package com.sun.tools.internal.xjc.generator.bean.field;
/*     */ 
/*     */ import com.sun.codemodel.internal.JAssignmentTarget;
/*     */ import com.sun.codemodel.internal.JBlock;
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JExpr;
/*     */ import com.sun.codemodel.internal.JExpression;
/*     */ import com.sun.codemodel.internal.JMethod;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.codemodel.internal.JVar;
/*     */ import com.sun.tools.internal.xjc.generator.bean.ClassOutlineImpl;
/*     */ import com.sun.tools.internal.xjc.generator.bean.MethodWriter;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UntypedListField
/*     */   extends AbstractListField
/*     */ {
/*     */   private final JClass coreList;
/*     */   private JMethod $get;
/*     */   
/*     */   protected UntypedListField(ClassOutlineImpl context, CPropertyInfo prop, JClass coreList) {
/*  95 */     super(context, prop, !coreList.fullName().equals("java.util.ArrayList"));
/*  96 */     this.coreList = coreList.narrow(this.exposedType.boxify());
/*  97 */     generate();
/*     */   }
/*     */   
/*     */   protected final JClass getCoreListType() {
/* 101 */     return this.coreList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/* 106 */     MethodWriter writer = this.outline.createMethodWriter();
/* 107 */     Accessor acc = create(JExpr._this());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     this.$get = writer.declareMethod((JType)this.listT, "get" + this.prop.getName(true));
/* 114 */     writer.javadoc().append(this.prop.javadoc);
/* 115 */     JBlock block = this.$get.body();
/* 116 */     fixNullRef(block);
/* 117 */     block._return(acc.ref(true));
/*     */     
/* 119 */     String pname = NameConverter.standard.toVariableName(this.prop.getName(true));
/* 120 */     writer.javadoc().append("Gets the value of the " + pname + " property.\n\n<p>\nThis accessor method returns a reference to the live list,\nnot a snapshot. Therefore any modification you make to the\nreturned list will be present inside the JAXB object.\nThis is why there is not a <CODE>set</CODE> method for the " + pname + " property.\n\n<p>\nFor example, to add a new item, do as follows:\n<pre>\n   get" + this.prop
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 131 */         .getName(true) + "().add(newItem);\n</pre>\n\n\n");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     writer.javadoc().append("<p>\nObjects of the following type(s) are allowed in the list\n")
/*     */ 
/*     */       
/* 139 */       .append(listPossibleTypes(this.prop));
/*     */   }
/*     */   
/*     */   public Accessor create(JExpression targetObject) {
/* 143 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   class Accessor extends AbstractListField.Accessor {
/*     */     protected Accessor(JExpression $target) {
/* 148 */       super($target);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 155 */       block.assign((JAssignmentTarget)$var, (JExpression)JExpr._new(UntypedListField.this.codeModel.ref(ArrayList.class).narrow(UntypedListField.this.exposedType.boxify())).arg((JExpression)this.$target
/* 156 */             .invoke(UntypedListField.this.$get)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 163 */       JVar $list = block.decl((JType)UntypedListField.this.listT, uniqueName + 'l', (JExpression)this.$target.invoke(UntypedListField.this.$get));
/* 164 */       block.invoke((JExpression)$list, "addAll").arg($var);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\UntypedListField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */