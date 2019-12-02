package ${package};
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/Sample")
public class SampleEndPoint {

    @GET
	@Path("/helloworld")
	public String helloworld() {
        return "Hello World!\n " + "now is: " + new Date();
	}
	
}