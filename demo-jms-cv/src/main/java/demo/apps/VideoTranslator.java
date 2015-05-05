package demo.apps;

import static org.bytedeco.javacpp.avcodec.AV_CODEC_ID_NONE;
import static org.bytedeco.javacpp.avutil.AV_PIX_FMT_YUV420P;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.CvApp;

public class VideoTranslator extends Thread {
	static final Logger log = LoggerFactory.getLogger(VideoTranslator.class);

	CvApp cvApp;

	String filePath; 
	String resultPath;
	
	boolean isFinished = false;
	int progress = 0;
	String resultStatus;
	
	public VideoTranslator(CvApp cvApp, String filePath, String resultPath) {
		this.cvApp = cvApp;
		this.filePath = filePath;
		this.resultPath = resultPath;
	}
	
	@Override
	public void run() {
		log.info("VideoTranslator START...");

		FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(resultPath, 1920, 1080);
		recorder.setVideoCodec(AV_CODEC_ID_NONE);
		recorder.setFrameRate(10);
		recorder.setPixelFormat(AV_PIX_FMT_YUV420P);
		FrameGrabber grabber = new FFmpegFrameGrabber(filePath);

		long start = System.currentTimeMillis();
		int cnt = 0;
		try {
			recorder.start();
			grabber.start();
			
			int total = grabber.getLengthInFrames();
			Frame frame = grabber.grabFrame();
			while (frame != null) {
				if (isInterrupted()) {
					resultStatus = "CANCEL";
					return;
				}
				if (frame.image != null) {
					if (cnt % 15 == 0) {
						BufferedImage im = frame.image.getBufferedImage();
						recorder.record(IplImage.createFrom(cvApp.process(im)));
//						ImageIO.write(writeImage, "jpeg", new File("sample.jpeg"));
						im.flush();
					}
					++cnt;
					progress = 100 * cnt / total;
					cvApp.setProgress(progress);
				}
//				frame.release();
				frame = grabber.grabFrame();
			}
			cvApp.setProgress(100);
			resultStatus = "OK";
			
		} catch (Exception e) {
			resultStatus = "NG";
			e.printStackTrace();
		} finally {
			try {
				grabber.stop();
				grabber.release();
				recorder.stop();
				recorder.release();
			} catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
				e.printStackTrace();
			} catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
				e.printStackTrace();
			}
			isFinished = true;
			cvApp.finished();
			log.info("VideoTranslator END Time: " + (System.currentTimeMillis() - start) + "  " + cnt);
		}
	}

	public boolean isFinished() {
		return isFinished;
	}
	
	public int getProgress() {
		return progress;
	}
}
