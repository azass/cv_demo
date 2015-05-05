package demo;

import java.awt.image.BufferedImage;

public abstract class CvApp {

	public static final int REGISTER = 0;
	public static final int WAIT = 1;
	public static final int PROCESSING = 2;
	public static final int COMPLETE = 3;
	public static final int CANCEL = 4;
	
	int progress = 0;
	
	boolean isFinished = false;

	public abstract void execute();
	
	public abstract BufferedImage process(BufferedImage image);
	
	public abstract void cancel();

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public boolean isFinished() {
		return isFinished;
	}
	
	public void finished() {
		isFinished = true;
	}
}
