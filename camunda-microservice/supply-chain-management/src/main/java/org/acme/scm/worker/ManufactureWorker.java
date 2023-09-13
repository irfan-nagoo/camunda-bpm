package org.acme.scm.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.quarkiverse.zeebe.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.acme.scm.constants.StatusType;

import java.util.Map;

import static org.acme.scm.constants.SCMConstants.INVENTORY_STATUS;
import static org.acme.scm.constants.SCMConstants.MANUFACTURING_STATUS;

/**
 * @author irfan.nagoo
 */

@Slf4j
public class ManufactureWorker {

    /**
     * This mock worker actually manufactures the ordered product batch
     * using the inventory.
     *
     * @param jobClient - Camunda job client
     * @param job - Current job
     * @return - Map of response
     */
    @JobWorker(type = "manufacture", fetchAllVariables = true)
    public Map<String, Object> executeManufactureTask(final JobClient jobClient, final ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        log.info("Received Manufacturing request for {} [{}] with quantity [{}] and Due Date[{}]", variables.get("product_type"), variables.get("model"),
                variables.get("batch"), variables.get("due_date"));
        if (variables.get(INVENTORY_STATUS).toString().equalsIgnoreCase(StatusType.AVAILABLE.toString())) {
            log.info("Mocking inventory received from Depot");
            log.info("Manufacture in PROGRESS");
            log.info("Manufacture COMPLETED");
            return Map.of(MANUFACTURING_STATUS, StatusType.COMPLETED);
        } else {
            log.error("Manufacturing cannot be performed since required inventory is not available");
            jobClient.newFailCommand(job.getKey())
                    .retries(0)
                    .send();
            return Map.of(MANUFACTURING_STATUS, StatusType.CANCELLED);
        }
    }
}
