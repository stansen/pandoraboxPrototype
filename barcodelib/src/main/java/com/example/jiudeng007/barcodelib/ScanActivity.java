package com.example.jiudeng007.barcodelib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiudeng007.barcodelib.zxing.ViewFinderView;
import com.example.jiudeng007.barcodelib.zxing.ZXingScannerView;
import com.google.zxing.Result;


public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    FrameLayout mScannerContent;
    TextView mResultText;
    Button mChooseButton;
    private ZXingScannerView mZXingScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mScannerContent = (FrameLayout)findViewById(R.id.scanner_content);
        mResultText = (TextView)findViewById(R.id.result_text);
        mZXingScannerView = new ZXingScannerView(this);
        mScannerContent.addView(mZXingScannerView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mZXingScannerView.setResultHandler(this);
        mZXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mZXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mZXingScannerView.resumeCameraPreview(ScanActivity.this);
            }
        }, 2000);
        mResultText.setText("Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString());
    }


//    protected Result scanningImage(String path) {
//        if (TextUtils.isEmpty(path)) {
//
//            return null;
//
//        }
//        // DecodeHintType 和EncodeHintType
//        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
//        hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true; // 先获取原大小
//        scanBitmap = BitmapFactory.decodeFile(path, options);
//        options.inJustDecodeBounds = false; // 获取新的大小
//
//        int sampleSize = (int) (options.outHeight / (float) 200);
//
//        if (sampleSize <= 0)
//            sampleSize = 1;
//        options.inSampleSize = sampleSize;
//        scanBitmap = BitmapFactory.decodeFile(path, options);
//
//
//
//        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
//        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
//        QRCodeReader reader = new QRCodeReader();
//        try {
//
//            return reader.decode(bitmap1, hints);
//
//        } catch (NotFoundException e) {
//
//            e.printStackTrace();
//
//        } catch (ChecksumException e) {
//
//            e.printStackTrace();
//
//        } catch (FormatException e) {
//
//            e.printStackTrace();
//
//        }
//
//        return null;
//
//    }

    private static class CustomViewFinderView extends ViewFinderView {
        public static final String TRADE_MARK_TEXT = "ZXing";
        public static final int TRADE_MARK_TEXT_SIZE_SP = 40;
        public final Paint PAINT = new Paint();

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            PAINT.setColor(Color.WHITE);
            PAINT.setAntiAlias(true);
            float textPixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                    TRADE_MARK_TEXT_SIZE_SP, getResources().getDisplayMetrics());
            PAINT.setTextSize(textPixelSize);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawTradeMark(canvas);
        }

        private void drawTradeMark(Canvas canvas) {
            Rect framingRect = getFramingRect();
            float tradeMarkTop;
            float tradeMarkLeft;
            if (framingRect != null) {
                tradeMarkTop = framingRect.bottom + PAINT.getTextSize() + 10;
                tradeMarkLeft = framingRect.left;
            } else {
                tradeMarkTop = 10;
                tradeMarkLeft = canvas.getHeight() - PAINT.getTextSize() - 10;
            }
            canvas.drawText(TRADE_MARK_TEXT, tradeMarkLeft, tradeMarkTop, PAINT);
        }
    }
}
