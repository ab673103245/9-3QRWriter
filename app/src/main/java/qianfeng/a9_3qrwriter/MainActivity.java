package qianfeng.a9_3qrwriter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText et;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = ((EditText) findViewById(R.id.et));
        iv = ((ImageView) findViewById(R.id.iv));

    }

    public void QRCode(View view) {

        Bitmap qrCodeBitmap = getQRCode(et.getText().toString(), 400, 400);

        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Bitmap bitmap = addBitmap(qrCodeBitmap, logoBitmap);

        iv.setImageBitmap(bitmap);

    }

    private Bitmap addBitmap(Bitmap qrCodeBitmap, Bitmap logoBitmap) {

        int width = qrCodeBitmap.getWidth();

        int height = qrCodeBitmap.getHeight();

        //如果有需要，先对logoBitmap进行二次采样,减少logo图形的大小。
        int logoBitmapWidth = logoBitmap.getWidth();

        int logoBitmapHeight = logoBitmap.getHeight();

        Bitmap blankBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // 这里Canvas接收的blankBitmap是可变的bitmap (可变数组)
        // 而Bitmap.createBitmap生成的Bitmap是 不可变的bitmap, 所以不能直接在二维码的bitmap上绘制。
        Canvas canvas = new Canvas(blankBitmap);

        canvas.drawBitmap(qrCodeBitmap, 0, 0, null);

        // 第二个和第三个参数都是这个bitmap的偏移位置，注意最先被绘制的bitmap会被后来绘制的bitmap给覆盖的。
        canvas.drawBitmap(logoBitmap, (width - logoBitmapWidth) / 2, (height - logoBitmapHeight) / 2, null);

        return blankBitmap;
    }

    private Bitmap getQRCode(String content, int width, int height) {


        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置容错级别
        // L: 7%
        // M: 15%
        // Q: 25%
        // H: 30%
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        try {
            //1.要生成二维码的文本
            //2.二维码的格式
            //3.水平方向上像素点的个数
            //4.竖直方向上像素点的个数
            //5.其他参数
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            // 遍历二维数组，用一个一位数组记录里面的值，如果是true，就是黑色，如果是false，就是白色。
            int[] colors = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    // 如果该点有数据，下面这个if判断就是true
                    if (bitMatrix.get(j, i)) {
                        colors[i * width + j] = Color.BLACK;
                    } else {
                        colors[i * width + j] = Color.WHITE;
                    }
                }
            }

            // 第二个参数表示colors数组是从第几位开始，就填0

            //1.生成Bitmap的颜色数组
            //2.数组偏移值
            //3.水平方向上像素点的个数
            //4.Bitmap宽度
            //5.Bitmap高度
            //6.Bitmap色彩模式
            Bitmap bitmap = Bitmap.createBitmap(colors, 0, width, width, height, Bitmap.Config.RGB_565);

            return bitmap;


        } catch (WriterException e) {
            e.printStackTrace();
        }


        return null;

    }


}
