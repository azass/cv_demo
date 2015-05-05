package demo;

import java.io.File;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import demo.apps.PrivacyApp;
import demo.apps.ScauterApp;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AppController extends Thread {
//	@Value("${classifierFile:classpath:/haarcascade_frontalface_default.xml}")
	File classifierFile;

	@Autowired
	TaskDAO taskDao;

	@Autowired
	AppInfoDAO appInfoDao;

	Receiver receiver;
	Task task;
	
	static final Logger log = LoggerFactory.getLogger(AppController.class);
	
	public void execute(Task task, Receiver receiver) {
		log.info(this + " start ");
		this.task = task;
		log.info(this.task.toString());
		this.receiver = receiver;
		this.start();
	}

    @Override
    public void run() {
    	CvApp app = createCvApp();
    	task.setStatus(CvApp.PROCESSING);
    	task.setStarttime(new Date());
    	taskDao.save(task);
    	app.execute();
		while(!app.isFinished()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Task _task = taskDao.find(task.getId());
			if (_task.getStatus() == CvApp.CANCEL) {
				app.cancel();
			} else {
				_task.setProgress(app.getProgress());
				taskDao.save(_task);
			}
			task.setStatus(_task.getStatus());
			task.setProgress(_task.getProgress());
		}
		task.setStatus(task.getStatus() + 1);
		task.setEndtime(new Date());
		taskDao.save(task);
		receiver.notifyComplete(this.task);
    }

    private CvApp createCvApp() {
    	AppInfo appInfo = appInfoDao.find(task.getAppKey());
    	if (appInfo.getAppKey().equals("privacy")) {
    		return new PrivacyApp(task.getOriginalPath(), task.getResultPath(), appInfo.getOption());
    	}
    	else if (appInfo.getAppKey().equals("scauter")) {
    		return new ScauterApp(task.getOriginalPath(), task.getResultPath(), appInfo.getOption());
    	}
    	return new ScauterApp(task.getOriginalPath(), task.getResultPath(), appInfo.getOption());
    }
}
