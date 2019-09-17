package it.algos.vaadwam.daemons;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Schedulatore che esegue periodicamente singoli schedule in appositi thread sul server.
 * Implementa il pattern Singleton.
 * Uso:
 * Per avviare il daemon e schedulare i schedule, invocare il metodo start().
 * Per fermare il daemon invocare il metodo stop().
 * Nel metodo execute() della classe WamTask, eseguire i schedule.
 *
 */
//@SpringComponent
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class WamScheduler extends Scheduler {

	/**
	 * Inietta da Spring come 'singleton'
	 */
	@Autowired
	private DemoTask demoTask;

	private final static Logger logger = Logger.getLogger(WamScheduler.class.getName());
	public static final String DAEMON_NAME = "wam_daemon";


	private WamScheduler() {
		super();
	}// end of basic constructor

	class TaskDemo extends Task {
		@Override
		public void execute(TaskExecutionContext context) throws RuntimeException {
			logger.log(Level.INFO, "Start WAM demo tasks");
			demoTask.run();
			logger.log(Level.INFO, "End WAM demo tasks");
		}// end of inner method
	}// end of inner class


	class TaskCompany extends Task {
		@Override
		public void execute(TaskExecutionContext context) throws RuntimeException {
			logger.log(Level.INFO, "Start WAM daemon tasks");
//			Runnable checker = new CompanyTask();
//			checker.run();
			logger.log(Level.INFO, "End WAM daemon tasks");
		}// end of inner method
	}// end of inner class



	class TaskMigration2017 extends Task {
		@Override
		public void execute(TaskExecutionContext context) throws RuntimeException {
			logger.log(Level.INFO, "Start WAM migration tasks for 2017 only");
//			Runnable migration2017 = new Migration2017Task();
//			migration2017.run();
			logger.log(Level.INFO, "End WAM migration tasks 2017");
		}// end of inner method
	}// end of inner class


	class TaskMigrationAll extends Task {
		@Override
		public void execute(TaskExecutionContext context) throws RuntimeException {
			logger.log(Level.INFO, "Start WAM migration tasks for all years");
//			Runnable migrationAll = new MigrationAllTask();
//			migrationAll.run();
			logger.log(Level.INFO, "End WAM migration tasks all");
		}// end of inner method
	}// end of inner class


	@Override
	public void start() throws IllegalStateException {
		if (!isStarted()) {
			super.start();


			// save daemon status flag into servlet context
//			ServletContext svc = ABootStrap.getServletContext();
//			svc.setAttribute(DAEMON_NAME, true);
//			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(DAEMON_NAME, true);


			// Schedule schedule.
			//--ogni ora (ai quindici)
			schedule("15 * * * *", new TaskDemo());


			// Schedule schedule.
//			schedule("0 * * * *", new TaskCompany());
//			logger.log(Level.INFO, "WAM daemon attivato (eseguirà all'inizio di ogni ora).");

			// Schedule schedule.
			//--ogni ora (alla mezz'ora)
			schedule("30 * * * *", new TaskMigration2017());

			// Schedule schedule.
			//--ogni giorno (alle quattro del mattino)
			schedule("0 4 * * *", new TaskMigrationAll());

		}// end of method
	}// end of method

	@Override
	public void stop() throws IllegalStateException {
		if (isStarted()) {
			super.stop();

			// save daemon status flag into servlet context
//			ServletContext svc = ABootStrap.getServletContext();
//			svc.setAttribute(DAEMON_NAME, false);
//			VaadinService.getCurrentRequest().getWrappedSession().setAttribute(DAEMON_NAME, false);

			logger.log(Level.INFO, "WAM daemon disattivato.");

		}// end of if cycle
	}// end of method


}// end of scheduler class
