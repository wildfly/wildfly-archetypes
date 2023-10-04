#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ${defaultClassPrefix}Service {

    public String hello(String name) {
        return String.format("Hello '%s'.", name);
    }
}