package foo.bar.multi.test;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import jakarta.ejb.EJB;
import foo.bar.multi.TestLocal;

/**
 * This integration test is used to validate the project created from the archetype.
 */
@RunWith(Arquillian.class)
public class ArchetypeIT extends SampleIT{

  @EJB
  private TestLocal testLocal;

  
  /**
   * A sample test...
   * 
   */
  @Test
  public void test() {
    // This line will be written on the server console.
    System.out.println("=================================================");
    System.out.println("Test is invoked...");
    System.out.println("=================================================");
    
    this.testLocal.doTest ("test");
  }
}
