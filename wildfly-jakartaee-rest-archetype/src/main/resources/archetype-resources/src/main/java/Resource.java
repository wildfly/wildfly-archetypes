package ${package};

import java.util.Date;
import java.util.Objects;

public class Resource {
    private Date creationDate;
    private Date now;
    private String message;


    public Resource() {
    }

    public Resource(Date creationDate, Date now, String message) {
        this.creationDate = creationDate;
        this.now = now;
        this.message = message;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getNow() {
        return this.now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Resource creationDate(Date creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Resource now(Date now) {
        this.now = now;
        return this;
    }

    public Resource message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Resource)) {
            return false;
        }
        Resource resource = (Resource) o;
        return Objects.equals(creationDate, resource.creationDate) && Objects.equals(now, resource.now) && Objects.equals(message, resource.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creationDate, now, message);
    }

    @Override
    public String toString() {
        return "{" +
            " creationDate='" + getCreationDate() + "'" +
            ", now='" + getNow() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }

}