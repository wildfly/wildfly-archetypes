package ${package};

import java.util.Date;


import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AppScopedResource {

    private final Date creationDate = new Date();


    public AppScopedResource() {
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    

}