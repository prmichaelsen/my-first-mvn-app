package example.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.container.servlet.ServletContainer;

public class MinimalServerRest {

	public static void main(String[] args) throws Exception {
		ServletHolder sh = new ServletHolder(ServletContainer.class);    
		sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
		sh.setInitParameter("com.sun.jersey.config.property.packages", "rest");//Set the package where the services reside
		sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

		Server server = new Server(8080);
		ServletContextHandler context = new ServletContextHandler(server, "/jetty", ServletContextHandler.SESSIONS);
		context.addServlet(sh, "/*");
		server.start();
		server.join();      
	}
}
