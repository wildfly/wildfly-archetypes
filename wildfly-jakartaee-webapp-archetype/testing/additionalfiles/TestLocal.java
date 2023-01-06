package foo.bar.multi;
import jakarta.ejb.Local;

/**
 * Local interface of the Arquillian unit test bean.
 * 
 */
@Local
public interface TestLocal {

  /**
   * This method is invoked by the Arquillian unit test.
   * 
   * @param test Some test string
   * @return value of parameter "test"
   */
  public String doTest(String test);
}
