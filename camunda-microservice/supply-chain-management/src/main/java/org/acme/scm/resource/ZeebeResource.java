package org.acme.scm.resource;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.scm.request.BaseRequest;
import org.acme.scm.response.BaseResponse;
import org.jboss.resteasy.reactive.RestPath;

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
     * This API deploys the workflow to the Local/Cloud Camunda 8 instance (Zeebe deployment)
     * and also starts the process.
     *
     * @param workflowName - Full name of workflow e.g. supply-chain-management.bpmn
     * @param processId    - Process Id to be started e.g. supply-chain-management
     * @return - Response with status of execution
     */
    @POST
    @Path("/workflow/{workflowName}/process/{processId}/invoke")
    public Uni<BaseResponse> invoke(@RestPath String workflowName,
                                    @RestPath String processId,
                                    BaseRequest request) {
        log.info("Invoking Process[{}] of Workflow[{}] ", processId, workflowName);

        // deploy the workflow
        zeebeClient.newDeployResourceCommand()
                .addResourceStream(this.getClass().getResourceAsStream("/bpmn/" + workflowName),
                        workflowName)
                .send()
                .join();
        log.info("Workflow Deployed");

        // start the process
        ProcessInstanceEvent processInstanceEvent = zeebeClient.newCreateInstanceCommand()
                .bpmnProcessId(processId)
                .latestVersion()
                .variables(Map.of("request", request))
                .send()
                .join();

        log.info("Process Successfully Invoked");
        return Uni.createFrom().item(new BaseResponse(HttpResponseStatus.OK.reasonPhrase(),
                String.format("Process with Instance Key [%s] Successfully Invoked",
                        processInstanceEvent.getProcessInstanceKey())));
    }

}
