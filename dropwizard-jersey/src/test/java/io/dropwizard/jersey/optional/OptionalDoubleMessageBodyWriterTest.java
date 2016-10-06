package io.dropwizard.jersey.optional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.OptionalDouble;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.codahale.metrics.MetricRegistry;

import io.dropwizard.jersey.DropwizardResourceConfig;
import io.dropwizard.logging.BootstrapLogging;

public class OptionalDoubleMessageBodyWriterTest extends JerseyTest {
    static {
        BootstrapLogging.bootstrap();
    }

    @Override
    protected Application configure() {
        forceSet(TestProperties.CONTAINER_PORT, "0");
        return DropwizardResourceConfig.forTesting(new MetricRegistry())
                .register(OptionalDoubleReturnResource.class);
    }

    @Test
    public void presentOptionalsReturnTheirValue() throws Exception {
        assertThat(target("optional-return")
                .queryParam("id", "1").request()
                .get(Double.class))
                .isEqualTo(1);
    }

    @Test
    public void presentOptionalsReturnTheirValueWithResponse() throws Exception {
        assertThat(target("optional-return/response-wrapped")
                .queryParam("id", "1").request()
                .get(Double.class))
                .isEqualTo(1);
    }

    @Test
    public void absentOptionalsReturnANoContent() throws Exception {
        Response response = target("optional-return").request().get();
        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Path("optional-return")
    @Produces(MediaType.TEXT_PLAIN)
    public static class OptionalDoubleReturnResource {
        @GET
        public OptionalDouble showWithQueryParam(@QueryParam("id") OptionalDouble id) {
            return id;
        }

        @POST
        public OptionalDouble showWithFormParam(@FormParam("id") OptionalDouble id) {
            return id;
        }

        @Path("response-wrapped")
        @GET
        public Response showWithQueryParamResponse(@QueryParam("id") OptionalDouble id) {
            return Response.ok(id).build();
        }
    }
}
