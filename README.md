# camunda-bpm

![image](https://github.com/raptor-23/camunda-bpm/assets/142492599/b13df2e4-8228-4f71-92b8-5a4efcc0999b)



This project covers few of the important use cases of Camunda 8 BPM platform. The Camunda 8 BPM is a Business Process Management and Business Process Automation platform used in various capacities within an organization. Inorder to understand the various terminologies used in Camunda 8 BPM, I recommend going through the [Camunda 8 docs](https://docs.camunda.io/docs/guides/) for better understanding. The Camunda 8 BPM is generally used to model the following aspects:

  1. Human task orchestration.
  2. Microservice orchestration.
  3. API Endpoint orchestration.

This project mainly covers human task orchestration and Microservice orchestration. API orchestration was also explored as part of this project however, the REST Connector provided by Camunda were not compatible with the local Camunda deployment however, worked smoothly with Camunda 8 cloud. There are some other alternatives to Camunda also like jPBM, RHPAM, Activiti etc. which are out of scope for this project.

## Components of Camunda 8 Platform

Following Camunda 8 components were mainly used for modeling workflows, administration, deployment and execution:

  1. **Camunda 8 BPM Modeler** - Both desktop and Cloud version were used as part of this project. The Modeler is used to model the business workflows using various Camunda 8 entities like:
      - Human Task
      - Service Task
      - REST Connector
      - Gateway
      - Camunda Forms etc.       
  2.  **Zeebe**    - This component is mainly used for workflow deployment and administration purpose.
  3.  **Operate**  - This component is used for workflow/process/task management purpose like task retry, cancel, progress etc.
  4.  **Tasklist** - This component lists the Human/User tasks as per assignment and displays the forms as task (Camunda native or external source)  

### Microservice Endpoint

This project is packaged with a sample Quarkus Camunda 8 Microservice (camunda-microservice) to explore the Microservice Orchestration use case. This Microservice can connect with local Camunda 8 platform (default) as well as Camunda 8 cloud (need to provide the credentials in the properties file). This Microservice also exposes a Swagger UI Endpoint and a sample REST API to deploy a Workflow and also to start it. Any external application can trigger this API with user specific request parameters and start a user specific process/workflow. This is just to give an indication on how Camunda could be integrated with existing/new systems. The Quarkus Zeebee library also exposes more APIs to retry a task, cancel a task etc.

## Human Task Orchestration: Employee-Hiring

The Employee-Hiring workflow represents a real world use case where the Camunda 8 BPM could be used. The Workflow is packaged with Camunda 8 Forms which actually demonstrate the execution of the full workflow from end to end perspective (this example is for single candidate only). The Camundad 8 forms are pretty simple in terms of UX however, more advanced HTML/CSS etc. forms could also be used as per requirement. This workflow starts with the Recruiter posting a job opening and ends with either the candidate getting eliminated in the process or getting hired.  

The workflow could be deployed and started either through the attached Microservice REST API or Modeler UI. The user (Recuiter/Candidate) tasks could be accessed directly through Tasklist UI component. 

## Microservice Orchestration: Supply-Chain-Management

The Supply-Chain-Management represents a use case which could be more realistic if we only keep the Human Tasks in the workflow. Microservices were added to explore the Microservice Orchestration aspect of Camunda 8 and does not represent actual use case. The Quarkus Microservice has four Workers which get attached to the Camunda 8 instance (local/cloud) at the startup and are orchestrated as per the configuration in the workflow. These workers have access to all input process variables from previous tasks/workers, they contain the business logic and return a response which gets added to the process variable and could be accessed by other tasks/workers in horizon. This workflow starts within a Planning department configuring a Batch of products to be manufactured and ends with either the rejection of the order or the shipment of those products to the configurated destination.

## API Endpoint Orchestration: Supply-Chain-Management

The Supply-Chain-Management use case could be easily converted to API Endpoint Orchestration if we replace Camunda 8 "Service Tasks" with "REST Connector" entitles. The external application which exposes REST APIs does not need to connect with any Camunda instance. The Camunda 8 "REST Connector" task would invoke the REST API of the service with the required input variables and stores the response as process variables. These process variables are available to other API Endpoint calls/tasks next inline.

