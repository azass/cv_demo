package demo.apps;

import java.util.function.BiConsumer;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FaceDetector {
    CascadeClassifier classifier;

    static final Logger log = LoggerFactory.getLogger(FaceDetector.class);

    public FaceDetector(CascadeClassifier classifier) {
    	this.classifier = classifier;
    }
    
    public synchronized void detectFaces(Mat source, BiConsumer<Mat, Rect> detectAction) {
        // 顔認識結果
        Rect faceDetections = new Rect();
        // 顔認識実行
        classifier.detectMultiScale(source, faceDetections);
        // 認識した顔の数
        int numOfFaces = faceDetections.limit();
//        log.info("{} faces are detected!", numOfFaces);
        for (int i = 0; i < numOfFaces; i++) {
            // i番目の認識結果
            Rect r = faceDetections.position(i);
            // 認識結果を変換処理にかける
            detectAction.accept(source, r);
        }
    }
    
    public synchronized Rect detectFaces(Mat source) {
    	// 顔認識結果
        Rect faceDetections = new Rect();
        // 顔認識実行
        classifier.detectMultiScale(source, faceDetections);
        
        return faceDetections;
    }
}

