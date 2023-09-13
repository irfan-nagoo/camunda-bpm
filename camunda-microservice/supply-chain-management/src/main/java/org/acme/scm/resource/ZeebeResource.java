package org.acme.scm.resource;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author irfan.nagoo
 */

@ApplicationScoped
@Path("/camunda")
@RequiredArgsConstructor
@Slf4j
public class ZeebeResource {

    private final ZeebeClient zeebeClient;

    /**
     * This API deploys the workflow to the local Camunda (Zeebe deployment) instance
     * and also starts the process
     *
     * @param processId - Process Id to be started
     * @return - Response with status of execution
     */
    @GET
    @Path("/{processId}/invoke")
    public Response invoke(@PathParam("processId") String processId) {
        log.info("Invoking Process [{}]", processId);

        // deploy the workflow
        zeebeClient.newDeployResourceCommand()
                .addResourceStream(this.getClass().getResourceAsStream("/bpmn/supply-chain-management.bpmn"),
                        "supply-chain-management.bpmn")
                .send()
                .join();
        log.info("Workflow Deployed");

        // start the process
        ProcessInstanceEvent processInstanceEvent = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(processId)
                .latestVersion()
                .variables(Map.of("request", "json"))
                .send()
                .join();
        if (processInstanceEvent != null) {
            log.info("Process Successfully Invoked");
            return Response.ok(String.format("Process [%s] Successfully invoked", processId))
                    .build();
        } else {
            log.info("Workflow Invocation Failed");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(String.format("Process [%s] Not Found", processId)).build();
        }
    }
}
