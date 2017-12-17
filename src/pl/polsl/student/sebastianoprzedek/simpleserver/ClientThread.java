package pl.polsl.student.sebastianoprzedek.simpleserver;

import pl.polsl.student.sebastianoprzedek.common.helper.ByteHelper;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sebastian Oprzędek on 14.12.2017.
 */
public class ClientThread extends Thread {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final String FILE_FORMAT = "JPEG";
    protected Socket socket;
    protected String name;
    protected BufferedInputStream in;
    protected DataOutputStream out;

    public ClientThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        try {
            in = new BufferedInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            confirm();
        } catch (IOException e) {
            return;
        }
        while (true) {
            try {
                byte[] input = readMessage();
                if(ByteHelper.equal(input, Dictionary.JPEG_HEADER))
                    readFrame();
                else if(ByteHelper.equal(input, Dictionary.NAME))
                    readName();
                else if(ByteHelper.equal(input, Dictionary.STOP)){
                    confirm();
                    socket.close();
                    log("closing socket on STOP message");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void readName() throws IOException {
        int length = readInt();
        byte[] bytes = new byte[length];
        in.read(bytes, 0, length);
        confirm();
        name = new String(bytes, "UTF-8");
        log("new name: " + name);
    }

    private int readInt() throws IOException {
        byte[] lengthBytes = new byte[4];
        in.read(lengthBytes, 0, 4);
        confirm();
        return ByteBuffer.wrap(lengthBytes).getInt();
    }

    private void readFrame() throws Exception {
        int numberOfBatches = readInt();
        byte[][] batchedBytes = new byte[numberOfBatches][];
        for(int i =0; i< numberOfBatches; i++){
            int length = readInt();
            batchedBytes[i] = new byte[length];
            in.read(batchedBytes[i], 0, length);
            confirm();
        }
        byte[] frameBytes = ByteHelper.mergeBatches(batchedBytes);
        File dir = new File(name);
        if(!dir.exists())
            if(!new File(name).mkdirs())
                throw new Exception("file during creating file");
        BufferedImage bufferedImage = ByteHelper.byteArrayToBufferedImage(frameBytes);
        ImageIO.write(bufferedImage, "jpg", new File(name + "/" + DATE_FORMAT.format(new Date()) + "." + FILE_FORMAT));
        log("new frame has been read and saved");
    }

    private void writeMessage(byte[] message) throws IOException {
        out.write(message, 0, Dictionary.MESSAGE_LENGTH);
    }

    private void confirm() throws IOException {
        writeMessage(Dictionary.CONFIRM);
    }

    private byte[] readMessage() throws IOException {
        byte[] bytes = new byte[4];
        in.read(bytes, 0, 4);
        confirm();
        return bytes;
    }

    private void log(String s) {
        System.out.println("Socket: " + socket.getPort() + " " + s);
    }
}
