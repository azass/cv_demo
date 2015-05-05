package demo.apps;

import java.awt.image.BufferedImage;
import java.io.File;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import demo.CvApp;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrivacyApp extends CvApp {
	FaceDetector faceDetector;
	String originalPath;
	String resultPath;
	VideoTranslator videoTranslator;
	
	static final Logger log = LoggerFactory.getLogger(PrivacyApp.class);

	public PrivacyApp(String originalPath, String resultPath, String classifierFilePath) {
		this.originalPath = originalPath;
		this.resultPath = resultPath;
		File classifierFile = new File(classifierFilePath);
		if (!classifierFile.exists()) {
			throw new RuntimeException();
		}
		this.faceDetector = new FaceDetector(new CascadeClassifier(classifierFile.toPath().toString()));
	}

	@Override
	public void execute() {
		log.info(this + " start , FaceDetector=" + faceDetector);
		videoTranslator = new VideoTranslator(this, originalPath, resultPath);
		videoTranslator.start();
	}
	
	@Override
	public BufferedImage process(BufferedImage image) {
		Mat source = Mat.createFrom(image);
		faceDetector.detectFaces(source, FaceTranslator::privacy);
		return source.getBufferedImage();
	}
	    
    @Override
    public void cancel() {
    	videoTranslator.interrupt();
    }

}
