package foo.bar.multi;

import jakarta.ejb.Stateless;

/**
 * This bean is invoked by the Arquillian unit test.
 * 
 */
@Stateless()
public class TestBean implements TestRemote, TestLocal
{
  /**
   * Constructor.
   */
  public TestBean()
  {
  }

  /**
   * This method is invoked by the Arquillian unit test.
   * 
   * @param test Some test string
   * @return value of parameter "test"
   */
  public String doTest(String test)
  {
    System.out.println("=================================================");
	System.out.println("doTest is invoked with parameter 'test' = " + test);
    System.out.println("=================================================");
	
    return test;
  }
}
