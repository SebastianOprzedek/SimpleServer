package pl.polsl.student.sebastianoprzedek.simpleserver.service;

import java.io.File;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

/**
 * Created by Sebastian Oprzędek on 18.12.2017.
 */

public class FrameService {

    VideoCapture videoCapture;
    String prefix;

    public FrameService(String file, String prefix) throws Exception{
        System.load(System.getProperty("user.dir") + "/lib/x64/" + Core.NATIVE_LIBRARY_NAME + ".dll");
        this.videoCapture = new VideoCapture(file);
        this.prefix = prefix;
    }

    public void closeService() throws IOException{
        videoCapture.release();
    }

    public void saveFrames(){
        new File(prefix).mkdirs();
        try {
            int i = 0;
            while (videoCapture.isOpened()) {
                Mat image = new Mat();
                videoCapture.read(image);
                if(image.empty()) break;
                Imgproc.putText(image, "" + i, new Point(10, 450), 0, 1, new Scalar(0, 0, 255));
                String file = prefix + "/" + i + ".jpg";
                Imgcodecs.imwrite(file, image);
                i++;
            }
        }
        catch(Exception e){}
    }
}
