package com.sun.xml.internal.rngom.parse;

import com.sun.xml.internal.rngom.ast.builder.BuildException;
import com.sun.xml.internal.rngom.ast.builder.IncludedGrammar;
import com.sun.xml.internal.rngom.ast.builder.SchemaBuilder;
import com.sun.xml.internal.rngom.ast.builder.Scope;

public interface Parseable {
  <P extends com.sun.xml.internal.rngom.ast.om.ParsedPattern> P parse(SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder) throws BuildException, IllegalSchemaException;
  
  <P extends com.sun.xml.internal.rngom.ast.om.ParsedPattern> P parseInclude(String paramString1, SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder, IncludedGrammar<P, ?, ?, ?, ?> paramIncludedGrammar, String paramString2) throws BuildException, IllegalSchemaException;
  
  <P extends com.sun.xml.internal.rngom.ast.om.ParsedPattern> P parseExternal(String paramString1, SchemaBuilder<?, P, ?, ?, ?, ?> paramSchemaBuilder, Scope paramScope, String paramString2) throws BuildException, IllegalSchemaException;
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\parse\Parseable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */