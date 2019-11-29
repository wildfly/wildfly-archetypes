package ${package};
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/Sample")
public class SampleEndPoint {

	@Inject
	private AppScopedResource appScopedRes;

    @GET
	@Path("/helloworld")
	public String helloworld() {
        return "Hello World!\n App created:" + appScopedRes.getCreationDate() + "now is: " + new Date();
	}
	
	@GET
	@Path("/helloJSON")
	@Produces("application/json")
	public Resource helloworldJSON() {
        return new Resource(appScopedRes.getCreationDate(), new Date(), "hello JSON");
	}
	

	@GET
	@Path("printmessage/{param}")
	public Response printMessage(@PathParam("param") String msg) {

		String result = "Restful example : " + msg;

		return Response.status(200).entity(result).build();

	}

}