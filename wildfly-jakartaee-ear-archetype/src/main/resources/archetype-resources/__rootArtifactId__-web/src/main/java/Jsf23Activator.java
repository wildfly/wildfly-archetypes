package ${package};

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;

/**This bean is required to activate JSF 2.3.
 * See https://github.com/eclipse-ee4j/mojarra#user-content-activating-cdi-in-jakarta-faces-30
 * 
 * Remove this class if you don't need JSF.
 */
@ApplicationScoped
@FacesConfig(version = FacesConfig.Version.JSF_2_3)
public class Jsf23Activator {
}