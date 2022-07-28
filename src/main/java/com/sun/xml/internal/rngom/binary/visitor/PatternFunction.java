package com.sun.xml.internal.rngom.binary.visitor;

import com.sun.xml.internal.rngom.binary.AfterPattern;
import com.sun.xml.internal.rngom.binary.AttributePattern;
import com.sun.xml.internal.rngom.binary.ChoicePattern;
import com.sun.xml.internal.rngom.binary.DataExceptPattern;
import com.sun.xml.internal.rngom.binary.DataPattern;
import com.sun.xml.internal.rngom.binary.ElementPattern;
import com.sun.xml.internal.rngom.binary.EmptyPattern;
import com.sun.xml.internal.rngom.binary.ErrorPattern;
import com.sun.xml.internal.rngom.binary.GroupPattern;
import com.sun.xml.internal.rngom.binary.InterleavePattern;
import com.sun.xml.internal.rngom.binary.ListPattern;
import com.sun.xml.internal.rngom.binary.NotAllowedPattern;
import com.sun.xml.internal.rngom.binary.OneOrMorePattern;
import com.sun.xml.internal.rngom.binary.RefPattern;
import com.sun.xml.internal.rngom.binary.TextPattern;
import com.sun.xml.internal.rngom.binary.ValuePattern;

public interface PatternFunction {
  Object caseEmpty(EmptyPattern paramEmptyPattern);
  
  Object caseNotAllowed(NotAllowedPattern paramNotAllowedPattern);
  
  Object caseError(ErrorPattern paramErrorPattern);
  
  Object caseGroup(GroupPattern paramGroupPattern);
  
  Object caseInterleave(InterleavePattern paramInterleavePattern);
  
  Object caseChoice(ChoicePattern paramChoicePattern);
  
  Object caseOneOrMore(OneOrMorePattern paramOneOrMorePattern);
  
  Object caseElement(ElementPattern paramElementPattern);
  
  Object caseAttribute(AttributePattern paramAttributePattern);
  
  Object caseData(DataPattern paramDataPattern);
  
  Object caseDataExcept(DataExceptPattern paramDataExceptPattern);
  
  Object caseValue(ValuePattern paramValuePattern);
  
  Object caseText(TextPattern paramTextPattern);
  
  Object caseList(ListPattern paramListPattern);
  
  Object caseRef(RefPattern paramRefPattern);
  
  Object caseAfter(AfterPattern paramAfterPattern);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\xml\internal\rngom\binary\visitor\PatternFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */