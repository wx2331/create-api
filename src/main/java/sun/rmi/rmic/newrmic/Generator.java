package sun.rmi.rmic.newrmic;

import com.sun.javadoc.ClassDoc;
import java.io.File;
import java.util.Set;

public interface Generator {
  boolean parseArgs(String[] paramArrayOfString, Main paramMain);
  
  Class<? extends BatchEnvironment> envClass();
  
  Set<String> bootstrapClassNames();
  
  void generate(BatchEnvironment paramBatchEnvironment, ClassDoc paramClassDoc, File paramFile);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\sun\rmi\rmic\newrmic\Generator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */