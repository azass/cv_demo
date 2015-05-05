package demo.apps;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
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
public class ScauterApp extends CvApp {
	
	FaceDetector faceDetector;
	String originalPath;
	String resultPath;
	VideoTranslator videoTranslator;
	
	static final Logger log = LoggerFactory.getLogger(ScauterApp.class);

	public ScauterApp(String originalPath, String resultPath, String classifierFilePath) {
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
		Mat source = Mat.createFrom(filter(image));
		faceDetector.detectFaces(source, FaceTranslator::scouter);
		return source.getBufferedImage();
	}
	
    private BufferedImage filter(BufferedImage image) {
    	float[] f = {0.5f,2.0f,0.5f}; // 係数
        float[] d = {10f,0f,0f}; // オフセット
        
        int w1 = image.getWidth(),h1 = image.getHeight();
        BufferedImage im2 = new BufferedImage(w1,h1,image.getType());
        
        RescaleOp op = new RescaleOp(f,d,null);
        op.filter(image,im2);
        return im2;
    }
    
    @Override
    public void cancel() {
    	videoTranslator.interrupt();
    }
}
