package demo.apps;

import static org.bytedeco.javacpp.opencv_core.CV_AA;
import static org.bytedeco.javacpp.opencv_core.CV_FONT_HERSHEY_SIMPLEX;
import static org.bytedeco.javacpp.opencv_core.circle;
import static org.bytedeco.javacpp.opencv_core.fillConvexPoly;
import static org.bytedeco.javacpp.opencv_core.line;
import static org.bytedeco.javacpp.opencv_core.putText;
import static org.bytedeco.javacpp.opencv_core.rectangle;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;

class FaceTranslator {
    public static void privacy(Mat source, Rect r) {
        int x = r.x(), y = r.y(), h = r.height(), w = r.width();
        // 上半分の黒四角
        rectangle(source, new Point(x + w / 8, y + h * 7 / 24), new Point(x + w * 7 / 8, y + h * 6 / 13),
                new Scalar(0, 0, 0, 0), -1, CV_AA, 0);
    }
    
    public static void scouter(Mat source, Rect rect) {
    	int x = rect.x(), y = rect.y(), h = rect.height(), w = rect.width(), r = (w + h) * 2 / 7, t = r / 3;
    	Scalar color = new Scalar(0, 255, 255, 0);
    	if (r < 12) return;
    	circle(source, new Point(x + h / 2, y + h / 2), r, color, r/12, CV_AA, 0);
//    	fillConvexPoly(source, new Mat(new Point((int)(x + h / 2 - 0.707 * r - r / 5))), new Scalar(0, 255, 255, 0));
    	Point hatPoints = new Point(3);
    	hatPoints.position(0).x((int)(x + h / 2 - 0.707 * r * 13 / 12)).y((int)(y + h / 2 - 0.707 * r * 13 / 12));
        hatPoints.position(1).x((int)(x + h / 2 - 0.707 * r * 13 / 12 - t)).y((int)(y + h / 2 - 0.707 * r * 13 / 12));
        hatPoints.position(2).x((int)(x + h / 2 - 0.707 * r * 13 / 12)).y((int)(y + h / 2 - 0.707 * r * 13 / 12 - t));
        fillConvexPoly(source, hatPoints.position(0), 3, color, CV_AA, 0);
        
    	hatPoints.position(0).x((int)(x + h / 2 - 0.707 * r * 13 / 12)).y((int)(y + h / 2 + 0.707 * r * 13 / 12));
        hatPoints.position(1).x((int)(x + h / 2 - 0.707 * r * 13 / 12 - t)).y((int)(y + h / 2 + 0.707 * r * 13 / 12));
        hatPoints.position(2).x((int)(x + h / 2 - 0.707 * r * 13 / 12)).y((int)(y + h / 2 + 0.707 * r * 13 / 12 + t));
        fillConvexPoly(source, hatPoints.position(0), 3, color, CV_AA, 0);

    	hatPoints.position(0).x((int)(x + h / 2 + 0.707 * r * 13 / 12)).y((int)(y + h / 2 + 0.707 * r * 13 / 12));
        hatPoints.position(1).x((int)(x + h / 2 + 0.707 * r * 13 / 12 + t)).y((int)(y + h / 2 + 0.707 * r * 13 / 12));
        hatPoints.position(2).x((int)(x + h / 2 + 0.707 * r * 13 / 12)).y((int)(y + h / 2 + 0.707 * r * 13 / 12 + t));
        fillConvexPoly(source, hatPoints.position(0), 3, color, CV_AA, 0);

        line(source, new Point((int)(x + h / 2 + 0.707 * r * 13 / 12), (int)(y + h / 2 - 0.707 * r * 13 / 12)), 
        		new Point((int)(x + h / 2 + 0.707 * r * 13 / 12 + t / 2), (int)(y + h / 2 - 0.707 * r * 13 / 12) - t / 2),
        		color, r/36, CV_AA, 0);
        line(source, new Point((int)(x + h / 2 + 0.707 * r * 13 / 12 + t / 2), (int)(y + h / 2 - 0.707 * r * 13 / 12) - t / 2), 
        		new Point((int)(x + h / 2 + 0.707 * r * 13 / 12 + t / 2 + r), (int)(y + h / 2 - 0.707 * r * 13 / 12) - t / 2),
        		color, r/36, CV_AA, 0);
        putText(source, new BytePointer("999"), 
        		new Point((int)(x + h / 2 + 0.707 * r * 13 / 12 + t / 2), (int)(y + h / 2 - 0.707 * r * 13 / 12) - t * 3/4), 
        		CV_FONT_HERSHEY_SIMPLEX, 1.8, color, 3, CV_AA, false);
    }
}