package org.acme.scm.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.quarkiverse.zeebe.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.acme.scm.constants.StatusType;

import java.util.Map;

import static org.acme.scm.constants.SCMConstants.INVENTORY_STATUS;

/**
 * @author irfan.nagoo
 */

@Slf4j
public class InventoryWorker {

    /**
     * This mock worker checks the availability of inventory items to be used
     * for manufacturing the ordered product batch.
     *
     * @param jobClient - Camunda job client
     * @param job - Current job
     * @return - Map of response
     */
    @JobWorker(type = "inventory", fetchAllVariables = true)
    public Map<String, Object> executeInventoryTask(final JobClient jobClient, final ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        log.info("Received Inventory request for {} [{}] with quantity [{}] and Due Date[{}]", variables.get("product_type"), variables.get("model"),
                variables.get("batch"), variables.get("due_date"));
        log.info("Mocking Inventory available");
        return Map.of(INVENTORY_STATUS, StatusType.AVAILABLE);
    }


}
