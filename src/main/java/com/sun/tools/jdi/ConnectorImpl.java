/*     */ package com.sun.tools.jdi;
/*     */
/*     */ import com.sun.jdi.InternalException;
/*     */ import com.sun.jdi.connect.Connector;
/*     */ import com.sun.jdi.connect.IllegalConnectorArgumentsException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ abstract class ConnectorImpl
/*     */   implements Connector
/*     */ {
/*  42 */   Map<String, Argument> defaultArguments = new LinkedHashMap<>();
/*     */
/*     */
/*  45 */   static String trueString = null;
/*     */   static String falseString;
/*     */
/*     */   public Map<String, Argument> defaultArguments() {
/*  49 */     LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
/*  50 */     Collection<Argument> collection = this.defaultArguments.values();
/*     */
/*  52 */     Iterator<Argument> iterator = collection.iterator();
/*  53 */     while (iterator.hasNext()) {
/*  54 */       ArgumentImpl argumentImpl = (ArgumentImpl)iterator.next();
/*  55 */       linkedHashMap.put(argumentImpl.name(), argumentImpl.clone());
/*     */     }
/*  57 */     return (Map)linkedHashMap;
/*     */   }
/*     */
/*     */
/*     */   void addStringArgument(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) {
/*  62 */     this.defaultArguments.put(paramString1, new StringArgumentImpl(paramString1, paramString2, paramString3, paramString4, paramBoolean));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   void addBooleanArgument(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) {
/*  71 */     this.defaultArguments.put(paramString1, new BooleanArgumentImpl(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   void addIntegerArgument(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, int paramInt1, int paramInt2) {
/*  81 */     this.defaultArguments.put(paramString1, new IntegerArgumentImpl(paramString1, paramString2, paramString3, paramString4, paramBoolean, paramInt1, paramInt2));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   void addSelectedArgument(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean, List<String> paramList) {
/*  92 */     this.defaultArguments.put(paramString1, new SelectedArgumentImpl(paramString1, paramString2, paramString3, paramString4, paramBoolean, paramList));
/*     */   }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */   ArgumentImpl argument(String paramString, Map<String, ? extends Argument> paramMap) throws IllegalConnectorArgumentsException {
/* 102 */     ArgumentImpl argumentImpl = (ArgumentImpl)paramMap.get(paramString);
/* 103 */     if (argumentImpl == null) {
/* 104 */       throw new IllegalConnectorArgumentsException("Argument missing", paramString);
/*     */     }
/*     */
/* 107 */     String str = argumentImpl.value();
/* 108 */     if (str == null || str.length() == 0) {
/* 109 */       if (argumentImpl.mustSpecify()) {
/* 110 */         throw new IllegalConnectorArgumentsException("Argument unspecified", paramString);
/*     */       }
/*     */     }
/* 113 */     else if (!argumentImpl.isValid(str)) {
/* 114 */       throw new IllegalConnectorArgumentsException("Argument invalid", paramString);
/*     */     }
/*     */
/*     */
/* 118 */     return argumentImpl;
/*     */   }
/*     */
/*     */
/* 122 */   private ResourceBundle messages = null;
/*     */
/*     */   String getString(String paramString) {
/* 125 */     if (this.messages == null) {
/* 126 */       this.messages = ResourceBundle.getBundle("com.sun.tools.jdi.resources.jdi");
/*     */     }
/* 128 */     return this.messages.getString(paramString);
/*     */   }
/*     */
/*     */   public String toString() {
/* 132 */     String str = name() + " (defaults: ";
/* 133 */     Iterator<ArgumentImpl> iterator = defaultArguments().values().iterator();
/* 134 */     boolean bool = true;
/* 135 */     while (iterator.hasNext()) {
/* 136 */       ArgumentImpl argumentImpl = iterator.next();
/* 137 */       if (!bool) {
/* 138 */         str = str + ", ";
/*     */       }
/* 140 */       str = str + argumentImpl.toString();
/* 141 */       bool = false;
/*     */     }
/* 143 */     str = str + ")";
/* 144 */     return str;
/*     */   }
/*     */
/*     */   abstract class ArgumentImpl
/*     */     implements Argument, Cloneable, Serializable
/*     */   {
/*     */     private String name;
/*     */     private String label;
/*     */     private String description;
/*     */     private String value;
/*     */     private boolean mustSpecify;
/*     */
/*     */     ArgumentImpl(String param1String1, String param1String2, String param1String3, String param1String4, boolean param1Boolean) {
/* 157 */       this.name = param1String1;
/* 158 */       this.label = param1String2;
/* 159 */       this.description = param1String3;
/* 160 */       this.value = param1String4;
/* 161 */       this.mustSpecify = param1Boolean;
/*     */     }
/*     */
/*     */     public abstract boolean isValid(String param1String);
/*     */
/*     */     public String name() {
/* 167 */       return this.name;
/*     */     }
/*     */
/*     */     public String label() {
/* 171 */       return this.label;
/*     */     }
/*     */
/*     */     public String description() {
/* 175 */       return this.description;
/*     */     }
/*     */
/*     */     public String value() {
/* 179 */       return this.value;
/*     */     }
/*     */
/*     */     public void setValue(String param1String) {
/* 183 */       if (param1String == null) {
/* 184 */         throw new NullPointerException("Can't set null value");
/*     */       }
/* 186 */       this.value = param1String;
/*     */     }
/*     */
/*     */     public boolean mustSpecify() {
/* 190 */       return this.mustSpecify;
/*     */     }
/*     */
/*     */     public boolean equals(Object param1Object) {
/* 194 */       if (param1Object != null && param1Object instanceof Argument) {
/* 195 */         Argument argument = (Argument)param1Object;
/* 196 */         return (name().equals(argument.name()) &&
/* 197 */           description().equals(argument.description()) &&
/* 198 */           mustSpecify() == argument.mustSpecify() &&
/* 199 */           value().equals(argument.value()));
/*     */       }
/* 201 */       return false;
/*     */     }
/*     */
/*     */
/*     */     public int hashCode() {
/* 206 */       return description().hashCode();
/*     */     }
/*     */
/*     */     public Object clone() {
/*     */       try {
/* 211 */         return super.clone();
/* 212 */       } catch (CloneNotSupportedException cloneNotSupportedException) {
/*     */
/* 214 */         throw new InternalException();
/*     */       }
/*     */     }
/*     */
/*     */     public String toString() {
/* 219 */       return name() + "=" + value();
/*     */     }
/*     */   }
/*     */
/*     */   class BooleanArgumentImpl
/*     */     extends ArgumentImpl
/*     */     implements BooleanArgument {
/*     */     private static final long serialVersionUID = 1624542968639361316L;
/*     */
/*     */     BooleanArgumentImpl(String param1String1, String param1String2, String param1String3, boolean param1Boolean1, boolean param1Boolean2) {
/* 229 */       super(param1String1, param1String2, param1String3, null, param1Boolean2);
/* 230 */       if (ConnectorImpl.trueString == null) {
/* 231 */         ConnectorImpl.trueString = ConnectorImpl.this.getString("true");
/* 232 */         ConnectorImpl.falseString = ConnectorImpl.this.getString("false");
/*     */       }
/* 234 */       setValue(param1Boolean1);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */     public void setValue(boolean param1Boolean) {
/* 241 */       setValue(stringValueOf(param1Boolean));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isValid(String param1String) {
/* 251 */       return (param1String.equals(ConnectorImpl.trueString) || param1String.equals(ConnectorImpl.falseString));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String stringValueOf(boolean param1Boolean) {
/* 262 */       return param1Boolean ? ConnectorImpl.trueString : ConnectorImpl.falseString;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean booleanValue() {
/* 274 */       return value().equals(ConnectorImpl.trueString);
/*     */     }
/*     */   }
/*     */
/*     */   class IntegerArgumentImpl
/*     */     extends ArgumentImpl
/*     */     implements IntegerArgument
/*     */   {
/*     */     private static final long serialVersionUID = 763286081923797770L;
/*     */     private final int min;
/*     */     private final int max;
/*     */
/*     */     IntegerArgumentImpl(String param1String1, String param1String2, String param1String3, String param1String4, boolean param1Boolean, int param1Int1, int param1Int2) {
/* 287 */       super(param1String1, param1String2, param1String3, param1String4, param1Boolean);
/* 288 */       this.min = param1Int1;
/* 289 */       this.max = param1Int2;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public void setValue(int param1Int) {
/* 300 */       setValue(stringValueOf(param1Int));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isValid(String param1String) {
/* 309 */       if (param1String == null) {
/* 310 */         return false;
/*     */       }
/*     */       try {
/* 313 */         return isValid(Integer.decode(param1String).intValue());
/* 314 */       } catch (NumberFormatException numberFormatException) {
/* 315 */         return false;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isValid(int param1Int) {
/* 325 */       return (this.min <= param1Int && param1Int <= this.max);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public String stringValueOf(int param1Int) {
/* 340 */       return "" + param1Int;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public int intValue() {
/* 352 */       if (value() == null) {
/* 353 */         return 0;
/*     */       }
/*     */       try {
/* 356 */         return Integer.decode(value()).intValue();
/* 357 */       } catch (NumberFormatException numberFormatException) {
/* 358 */         return 0;
/*     */       }
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public int max() {
/* 367 */       return this.max;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public int min() {
/* 375 */       return this.min;
/*     */     }
/*     */   }
/*     */
/*     */   class StringArgumentImpl
/*     */     extends ArgumentImpl
/*     */     implements StringArgument {
/*     */     private static final long serialVersionUID = 7500484902692107464L;
/*     */
/*     */     StringArgumentImpl(String param1String1, String param1String2, String param1String3, String param1String4, boolean param1Boolean) {
/* 385 */       super(param1String1, param1String2, param1String3, param1String4, param1Boolean);
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isValid(String param1String) {
/* 393 */       return true;
/*     */     }
/*     */   }
/*     */
/*     */   class SelectedArgumentImpl
/*     */     extends ArgumentImpl
/*     */     implements SelectedArgument
/*     */   {
/*     */     private static final long serialVersionUID = -5689584530908382517L;
/*     */     private final List<String> choices;
/*     */
/*     */     SelectedArgumentImpl(String param1String1, String param1String2, String param1String3, String param1String4, boolean param1Boolean, List<String> param1List) {
/* 405 */       super(param1String1, param1String2, param1String3, param1String4, param1Boolean);
/* 406 */       this.choices = Collections.unmodifiableList(new ArrayList<>(param1List));
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public List<String> choices() {
/* 414 */       return this.choices;
/*     */     }
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */     public boolean isValid(String param1String) {
/* 422 */       return this.choices.contains(param1String);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdi\ConnectorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */
