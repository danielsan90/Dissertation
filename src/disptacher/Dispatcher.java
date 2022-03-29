package disptacher;

import java.util.List;

import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;

import javax.faces.bean.*;

/*
 * author: Daniele Colantonio
 */
@ManagedBean(name = "dispatcher")
public class Dispatcher {

	private static ProcessEngine processEngine;
	private static RepositoryService repositoryService;
	private static RuntimeService runtimeService;
	private static TaskService taskService;
	private static Task currentTask;
	private static Task nextTask;
	private static String currentUser;
	private static ProcessInstance processInstance;

	/*
	 * START:inizializzazione activitiEngine e database
	 */
	public String start(String nome, String pass) {

		processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
				.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
				.setJdbcUrl("jdbc:mysql://localhost:3306/activitidb?autoReconnect=true")
				.setJdbcDriver("com.mysql.jdbc.Driver").setJdbcUsername("root").setJdbcPassword("root")
				.setJobExecutorActivate(true).buildProcessEngine();
		ProcessEngines.registerProcessEngine(processEngine);

		String pagJSF = login(nome, pass);

		return pagJSF;

	}

	/*
	 * LOGIN: controlla se c è una sessione per l'utente loggato
	 * 
	 */
	public String login(String username, String processName) {
		currentUser = username;
		// Get Activiti services
		repositoryService = processEngine.getRepositoryService();
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();

		/*
		 * controllo che ci sia una sessione attiva per l 'utente loggato: deve
		 * avere key==processName e processInstance con nome==username
		 * 
		 * salvo in idProcessInstance il nome della chiave del processo(che ha
		 * il nome dell pag da visualizzare)
		 */

		String idProcessInstance = searchActiveSession(username, processName);

		if (idProcessInstance != null) {

			// l ho trovata; esco dal login
			return idProcessInstance;
		} else {
			// creo una nuova istanza di processo con nome=username
			return createSession(username, processName);
		}
	}

	/*
	 * COMPLETE TASK()
	 */

	public String completeTask() {
		// recupero il task corrente in base all id dell istanza di processo
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

		// aggiorno currentTask
		currentTask = tasks.get(0);
		System.out.println("current task .getId vale:" + currentTask.getId());

		taskService.complete(currentTask.getId());
		System.out.println(" task completato:" + currentTask.getId());

		// recupero prossimo task da visualizzare
		taskService = processEngine.getTaskService();

		List<Task> task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

		if (!task.isEmpty()) {
			System.out.println("######################## task disponibili" + task.toString());
			nextTask = task.get(0);
			System.out.println("nextTask vale:" + nextTask.getId());

			System.out.println("nextTask vale:" + nextTask.getTaskDefinitionKey());
			return nextTask.getTaskDefinitionKey();
		} else {
			return "index";
		}

	}

	/*
	 * COMPLETE TASK(DIREZIONE): richiamato quando devo scegliere la direzione
	 */

	public String completeTask(String direction) {
		String ciao;
		ciao="hello";
		ciao="no";
		// recupero il task corrente in base all id dell istanza di processo
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();

		currentTask = tasks.get(0);

		System.out.println("current task .getId vale:" + currentTask.getId());

		// *******codice per dichiarare una variabile************
		// recupero l idExecution
		String executionId = processInstance.getId();
		// do un nome alla variabile
		String navigation = "navigation";
		// dichiaro la variabile di processo con idProcesso, nomeVariabile,
		// parametro dato in input
		// per specificare la direzione del gateway
		// 1-remove
		// 2- insert

		runtimeService.setVariable(executionId, "navigation", direction);

		// *************

		taskService.complete(currentTask.getId());
		System.out.println(" task completato:" + currentTask.getId());

		// recupero prossimo task da visualizzare;
		// questo task sarà quello attivo in base al valore dato in input
		taskService = processEngine.getTaskService();
		List<Task> task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		// task e' empty quando sono all endEvent
		if (!task.isEmpty()) {
			nextTask = task.get(0);
			System.out.println("nextTask vale:" + nextTask.getId());
			return nextTask.getTaskDefinitionKey();
		} else {
			return "index";
		}

	}

	/*
	 * SEARCH ACTIVE SESSION:controllo che ci sia una sessione attiva per l
	 * 'utente loggato: deve avere key==processName e processInstance con
	 * nome==username
	 */

	public String searchActiveSession(String username, String processName) {
		List<ProcessInstance> inst_P = processEngine.getRuntimeService().createProcessInstanceQuery().active().list();
		for (int i = 0; i < inst_P.size(); i++) {
			if (inst_P.get(i).getName().equals(username)
					&& inst_P.get(i).getProcessDefinitionKey().equals(processName)) {
				processInstance = inst_P.get(i);
				// ritorno il nome della pagina della sessione gia attiva
				return processInstance.getActivityId();
			}
		}
		return null;
	}

	/*
	 * CREATE SESSION: creo una nuova sessione con nome = utenteLoggato
	 */

	public String createSession(String username, String processName) {

		// Deploy the process definition
		repositoryService.createDeployment().addClasspathResource("diagrams/" + processName + ".bpmn20.xml").deploy();

		// Start a process instance

		processInstance = runtimeService.startProcessInstanceByKey(processName);

		runtimeService.setProcessInstanceName(processInstance.getId(), username);

		// PRENDO L ID DEL TASK CHE HA COME PROC_INST_ID L'ID DEL PROCESSO CON
		// NOME ASSEGNATO

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
		// se non cè parallelismo è per forza il get(0) perche è la prima pagina
		// da eseguire

		currentTask = tasks.get(0);

		System.out.println("_____---_-_-__-__-_---_" + currentTask.toString());
		System.out.println("**éé*é*é*é*é*é*é*é*é*é*é*" + currentTask.getTaskDefinitionKey());
		System.out.println("+++++++++++++++++++++++" + processInstance.getId());

		// ritorna il nome della pagina da visualizzare
		return currentTask.getTaskDefinitionKey();

	}

	/*
	 * controllo che ci sia un task assegnato per l utente loggato
	 */
	public String searchActiveTaskForUser(String username, String processName) {

		taskService = processEngine.getTaskService();
		List<Task> task = taskService.createTaskQuery().processDefinitionKey(processName).list();
		System.out.println("*****************metodo searchAtciveTask" + task.toString());

		if (!task.isEmpty()) {
			for (int i = 0; i < task.size(); i++) {
				if (task.get(i).getAssignee().equals(username)) {
					;
				}
			}
		}

		return null;
	}

}
