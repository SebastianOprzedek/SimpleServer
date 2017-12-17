package pl.polsl.student.sebastianoprzedek.common.helper;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sebas on 13.12.2017.
 */

public class ByteHelper {
    public static List<Byte> byteArrayToList(byte[] bytes){
        List<Byte> byteList = new ArrayList<>();
        for(int i=0; i<bytes.length; i++){
            byteList.add(bytes[i]);
        }
        return byteList;
    }

    public static Boolean equal(byte[] bytes1, byte[] bytes2){
        if(bytes1.length != bytes2.length) return false;
        for(int i=0; i< bytes1.length; i++)
            if(bytes1[i] != bytes2[i]) return false;
        return true;
    }

    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }

    public static BufferedImage byteArrayToBufferedImage(byte[] bytes) throws IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator<?> readers = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis;
        ImageInputStream iis = ImageIO.createImageInputStream(source);
        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
        Image image = reader.read(0, param);
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        return bufferedImage;
    }

    public static byte[][] splitToBatches(byte[] bytes, int batchSize) throws Exception{
        int numberOfBatches = (int) Math.ceil(bytes.length / (double) batchSize);
        if(numberOfBatches < 1) throw new Exception("number of batches smaller than 1");
        byte[][] batchedBytes = new byte[numberOfBatches][];
        for(int i = 0; i < numberOfBatches-1; i++){
            batchedBytes[i] = new byte[batchSize];
            for(int j=0; j<batchSize; j++)
                batchedBytes[i][j] = bytes[i*batchSize+j];
        }
        int lastBatchSize = bytes.length - (numberOfBatches-1) * batchSize;
        batchedBytes[numberOfBatches-1] = new byte[lastBatchSize];
        for(int j=0; j<lastBatchSize; j++)
            batchedBytes[numberOfBatches-1][j] = bytes[(numberOfBatches-1)*batchSize+j];
        return batchedBytes;
    }

    public static byte[] mergeBatches(byte[][] batchedBytes){
        int totalSize = 0;
        for (byte[] batchedByte1 : batchedBytes) totalSize += batchedByte1.length;
        List<Byte> bytes = new ArrayList<>();
        for (byte[] batchedByte : batchedBytes)
            for (byte aBatchedByte : batchedByte) bytes.add(aBatchedByte);
        byte[] ret = new byte[bytes.size()];
        int i = 0;
        for (Byte e : bytes)
            ret[i++] = e.byteValue();
        return ret;
    }
}
