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
/*     */ import com.sun.tools.internal.xjc.model.CElement;
/*     */ import com.sun.tools.internal.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.internal.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NoExtendedContentField
/*     */   extends AbstractListField
/*     */ {
/*     */   private final JClass coreList;
/*     */   private JMethod $get;
/*     */   
/*     */   protected NoExtendedContentField(ClassOutlineImpl context, CPropertyInfo prop, JClass coreList) {
/* 100 */     super(context, prop, false);
/* 101 */     this.coreList = coreList;
/* 102 */     generate();
/*     */   }
/*     */   
/*     */   protected final JClass getCoreListType() {
/* 106 */     return this.coreList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/* 111 */     MethodWriter writer = this.outline.createMethodWriter();
/* 112 */     Accessor acc = create(JExpr._this());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 118 */     this.$get = writer.declareMethod((JType)this.listT, "get" + this.prop.getName(true));
/* 119 */     writer.javadoc().append(this.prop.javadoc);
/* 120 */     JBlock block = this.$get.body();
/* 121 */     fixNullRef(block);
/* 122 */     block._return(acc.ref(true));
/*     */     
/* 124 */     String pname = NameConverter.standard.toVariableName(this.prop.getName(true));
/* 125 */     writer.javadoc().append("Gets the value of the " + pname + " property.\n\n<p>\nThis accessor method returns a reference to the live list,\nnot a snapshot. Therefore any modification you make to the\nreturned list will be present inside the JAXB object.\nThis is why there is not a <CODE>set</CODE> method for the " + pname + " property.\n\n<p>\nFor example, to add a new item, do as follows:\n<pre>\n   get" + this.prop
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 136 */         .getName(true) + "().add(newItem);\n</pre>\n\n\n");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     writer.javadoc().append("<p>\nObjects of the following type(s) are allowed in the list\n")
/*     */ 
/*     */       
/* 144 */       .append(listPossibleTypes(this.prop));
/*     */   }
/*     */   
/*     */   public Accessor create(JExpression targetObject) {
/* 148 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   class Accessor extends AbstractListField.Accessor {
/*     */     protected Accessor(JExpression $target) {
/* 153 */       super($target);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 160 */       block.assign((JAssignmentTarget)$var, (JExpression)JExpr._new(NoExtendedContentField.this.codeModel.ref(ArrayList.class).narrow(NoExtendedContentField.this.getType(Aspect.EXPOSED).boxify())).arg((JExpression)this.$target
/* 161 */             .invoke(NoExtendedContentField.this.$get)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 168 */       JVar $list = block.decl((JType)NoExtendedContentField.this.listT, uniqueName + 'l', (JExpression)this.$target.invoke(NoExtendedContentField.this.$get));
/* 169 */       block.invoke((JExpression)$list, "addAll").arg($var);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected JType getType(Aspect aspect) {
/* 175 */     if (Aspect.IMPLEMENTATION.equals(aspect)) {
/* 176 */       return super.getType(aspect);
/*     */     }
/*     */     
/* 179 */     if (this.prop instanceof CReferencePropertyInfo) {
/* 180 */       Set<CElement> elements = ((CReferencePropertyInfo)this.prop).getElements();
/* 181 */       if (elements != null && elements.size() > 0) {
/* 182 */         return (JType)this.codeModel.ref(Serializable.class);
/*     */       }
/*     */     } 
/*     */     
/* 186 */     return (JType)this.codeModel.ref(String.class);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\NoExtendedContentField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */