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
/*     */ import com.sun.tools.internal.xjc.outline.Aspect;
/*     */ import com.sun.tools.internal.xjc.outline.FieldAccessor;
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import java.io.Serializable;
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
/*     */ public class ContentListField
/*     */   extends AbstractListField
/*     */ {
/*     */   private final JClass coreList;
/*     */   private JMethod $get;
/*     */   
/*     */   protected ContentListField(ClassOutlineImpl context, CPropertyInfo prop, JClass coreList) {
/*  97 */     super(context, prop, false);
/*  98 */     this.coreList = coreList;
/*  99 */     generate();
/*     */   }
/*     */   
/*     */   protected final JClass getCoreListType() {
/* 103 */     return this.coreList;
/*     */   }
/*     */ 
/*     */   
/*     */   public void generateAccessors() {
/* 108 */     MethodWriter writer = this.outline.createMethodWriter();
/* 109 */     Accessor acc = create(JExpr._this());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     this.$get = writer.declareMethod((JType)this.listT, "get" + this.prop.getName(true));
/* 116 */     writer.javadoc().append(this.prop.javadoc);
/* 117 */     JBlock block = this.$get.body();
/* 118 */     fixNullRef(block);
/* 119 */     block._return(acc.ref(true));
/*     */     
/* 121 */     String pname = NameConverter.standard.toVariableName(this.prop.getName(true));
/* 122 */     writer.javadoc().append("Gets the value of the " + pname + " property.\n\n<p>\nThis accessor method returns a reference to the live list,\nnot a snapshot. Therefore any modification you make to the\nreturned list will be present inside the JAXB object.\nThis is why there is not a <CODE>set</CODE> method for the " + pname + " property.\n\n<p>\nFor example, to add a new item, do as follows:\n<pre>\n   get" + this.prop
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 133 */         .getName(true) + "().add(newItem);\n</pre>\n\n\n");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 138 */     writer.javadoc().append("<p>\nObjects of the following type(s) are allowed in the list\n")
/*     */ 
/*     */       
/* 141 */       .append(listPossibleTypes(this.prop));
/*     */   }
/*     */   
/*     */   public Accessor create(JExpression targetObject) {
/* 145 */     return new Accessor(targetObject);
/*     */   }
/*     */   
/*     */   class Accessor extends AbstractListField.Accessor {
/*     */     protected Accessor(JExpression $target) {
/* 150 */       super($target);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void toRawValue(JBlock block, JVar $var) {
/* 157 */       block.assign((JAssignmentTarget)$var, (JExpression)JExpr._new(ContentListField.this.codeModel.ref(ArrayList.class).narrow(ContentListField.this.exposedType.boxify())).arg((JExpression)this.$target
/* 158 */             .invoke(ContentListField.this.$get)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void fromRawValue(JBlock block, String uniqueName, JExpression $var) {
/* 165 */       JVar $list = block.decl((JType)ContentListField.this.listT, uniqueName + 'l', (JExpression)this.$target.invoke(ContentListField.this.$get));
/* 166 */       block.invoke((JExpression)$list, "addAll").arg($var);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected JType getType(Aspect aspect) {
/* 172 */     if (Aspect.IMPLEMENTATION.equals(aspect)) {
/* 173 */       return super.getType(aspect);
/*     */     }
/* 175 */     return (JType)this.codeModel.ref(Serializable.class);
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\generator\bean\field\ContentListField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */