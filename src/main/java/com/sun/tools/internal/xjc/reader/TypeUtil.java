/*     */ package com.sun.tools.internal.xjc.reader;
/*     */ 
/*     */ import com.sun.codemodel.internal.JClass;
/*     */ import com.sun.codemodel.internal.JCodeModel;
/*     */ import com.sun.codemodel.internal.JType;
/*     */ import com.sun.tools.internal.xjc.ErrorReceiver;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeUtil
/*     */ {
/*     */   public static JType getCommonBaseType(JCodeModel codeModel, Collection<? extends JType> types) {
/*  61 */     return getCommonBaseType(codeModel, types.<JType>toArray(new JType[types.size()]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JType getCommonBaseType(JCodeModel codeModel, JType... t) {
/*  76 */     Set<JType> uniqueTypes = new TreeSet<>(typeComparator);
/*  77 */     for (JType type : t) {
/*  78 */       uniqueTypes.add(type);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  83 */     if (uniqueTypes.size() == 1) {
/*  84 */       return uniqueTypes.iterator().next();
/*     */     }
/*     */     
/*  87 */     assert !uniqueTypes.isEmpty();
/*     */ 
/*     */     
/*  90 */     uniqueTypes.remove(codeModel.NULL);
/*     */ 
/*     */     
/*  93 */     Set<JClass> s = null;
/*     */     
/*  95 */     for (JType type : uniqueTypes) {
/*  96 */       JClass cls = type.boxify();
/*     */       
/*  98 */       if (s == null) {
/*  99 */         s = getAssignableTypes(cls); continue;
/*     */       } 
/* 101 */       s.retainAll(getAssignableTypes(cls));
/*     */     } 
/*     */ 
/*     */     
/* 105 */     s.add(codeModel.ref(Object.class));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     JClass[] raw = s.<JClass>toArray(new JClass[s.size()]);
/* 112 */     s.clear();
/*     */     
/* 114 */     for (int i = 0; i < raw.length; i++) {
/*     */       int k;
/* 116 */       for (k = 0; k < raw.length; k++) {
/* 117 */         if (i != k)
/*     */         {
/*     */           
/* 120 */           if (raw[i].isAssignableFrom(raw[k]))
/*     */             break; 
/*     */         }
/*     */       } 
/* 124 */       if (k == raw.length)
/*     */       {
/* 126 */         s.add(raw[i]);
/*     */       }
/*     */     } 
/* 129 */     assert !s.isEmpty();
/*     */ 
/*     */     
/* 132 */     JClass result = pickOne(s);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (result.isParameterized()) {
/* 141 */       return (JType)result;
/*     */     }
/*     */     
/* 144 */     List<List<JClass>> parameters = new ArrayList<>(uniqueTypes.size());
/* 145 */     int paramLen = -1;
/*     */     
/* 147 */     for (JType type : uniqueTypes) {
/* 148 */       JClass cls = type.boxify();
/* 149 */       JClass bp = cls.getBaseClass(result);
/*     */ 
/*     */ 
/*     */       
/* 153 */       if (bp.equals(result)) {
/* 154 */         return (JType)result;
/*     */       }
/* 156 */       assert bp.isParameterized();
/* 157 */       List<JClass> tp = bp.getTypeParameters();
/* 158 */       parameters.add(tp);
/*     */       
/* 160 */       assert paramLen == -1 || paramLen == tp.size();
/*     */ 
/*     */       
/* 163 */       paramLen = tp.size();
/*     */     } 
/*     */     
/* 166 */     List<JClass> paramResult = new ArrayList<>();
/* 167 */     List<JClass> argList = new ArrayList<>(parameters.size());
/*     */     
/* 169 */     for (int j = 0; j < paramLen; j++) {
/* 170 */       argList.clear();
/* 171 */       for (List<JClass> list : parameters) {
/* 172 */         argList.add(list.get(j));
/*     */       }
/*     */       
/* 175 */       JClass bound = (JClass)getCommonBaseType(codeModel, (Collection)argList);
/* 176 */       boolean allSame = true;
/* 177 */       for (JClass a : argList)
/* 178 */         allSame &= a.equals(bound); 
/* 179 */       if (!allSame) {
/* 180 */         bound = bound.wildcard();
/*     */       }
/* 182 */       paramResult.add(bound);
/*     */     } 
/*     */     
/* 185 */     return (JType)result.narrow(paramResult);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static JClass pickOne(Set<JClass> s) {
/* 194 */     for (JClass c : s) {
/* 195 */       if (c instanceof com.sun.codemodel.internal.JDefinedClass) {
/* 196 */         return c;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 201 */     return s.iterator().next();
/*     */   }
/*     */   
/*     */   private static Set<JClass> getAssignableTypes(JClass t) {
/* 205 */     Set<JClass> r = new TreeSet(typeComparator);
/* 206 */     getAssignableTypes(t, r);
/* 207 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void getAssignableTypes(JClass t, Set<JClass> s) {
/* 219 */     if (!s.add(t)) {
/*     */       return;
/*     */     }
/*     */     
/* 223 */     s.add(t.erasure());
/*     */ 
/*     */ 
/*     */     
/* 227 */     JClass _super = t._extends();
/* 228 */     if (_super != null) {
/* 229 */       getAssignableTypes(_super, s);
/*     */     }
/*     */     
/* 232 */     Iterator<JClass> itr = t._implements();
/* 233 */     while (itr.hasNext()) {
/* 234 */       getAssignableTypes(itr.next(), s);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JType getType(JCodeModel codeModel, String typeName, ErrorReceiver errorHandler, Locator errorSource) {
/*     */     try {
/* 245 */       return codeModel.parseType(typeName);
/* 246 */     } catch (ClassNotFoundException ee) {
/*     */ 
/*     */       
/* 249 */       errorHandler.warning(new SAXParseException(Messages.ERR_CLASS_NOT_FOUND
/* 250 */             .format(new Object[] { typeName }, ), errorSource));
/*     */ 
/*     */ 
/*     */       
/* 254 */       return (JType)codeModel.directClass(typeName);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 261 */   private static final Comparator<JType> typeComparator = new Comparator<JType>() {
/*     */       public int compare(JType t1, JType t2) {
/* 263 */         return t1.fullName().compareTo(t2.fullName());
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\internal\xjc\reader\TypeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */