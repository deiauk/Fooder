package fooder.deividas.com.fooder.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fooder.deividas.com.fooder.MyApplication;
import fooder.deividas.com.fooder.R;
import fooder.deividas.com.fooder.activities.DetailCameraAdditiveActivity;
import fooder.deividas.com.fooder.database.models.FoodAdditive;
import fooder.deividas.com.fooder.levensitein.CustomExtractedResult;
import fooder.deividas.com.fooder.levensitein.CustomFuzzySearch;
import fooder.deividas.com.fooder.levensitein.Levenshtein;
import io.fotoapparat.Fotoapparat;
import io.fotoapparat.FotoapparatSwitcher;
import io.fotoapparat.error.CameraErrorCallback;
import io.fotoapparat.hardware.CameraException;
import io.fotoapparat.hardware.provider.CameraProviders;
import io.fotoapparat.parameter.LensPosition;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.parameter.Size;
import io.fotoapparat.parameter.update.UpdateRequest;
import io.fotoapparat.photo.BitmapPhoto;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.preview.FrameProcessor;
import io.fotoapparat.result.PendingResult;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.view.CameraView;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

import static io.fotoapparat.log.Loggers.fileLogger;
import static io.fotoapparat.log.Loggers.logcat;
import static io.fotoapparat.log.Loggers.loggers;
import static io.fotoapparat.parameter.selector.AspectRatioSelectors.standardRatio;
import static io.fotoapparat.parameter.selector.FlashSelectors.autoFlash;
import static io.fotoapparat.parameter.selector.FlashSelectors.autoRedEye;
import static io.fotoapparat.parameter.selector.FlashSelectors.off;
import static io.fotoapparat.parameter.selector.FlashSelectors.torch;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.autoFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.continuousFocus;
import static io.fotoapparat.parameter.selector.FocusModeSelectors.fixed;
import static io.fotoapparat.parameter.selector.LensPositionSelectors.lensPosition;
import static io.fotoapparat.parameter.selector.PreviewFpsRangeSelectors.rangeWithHighestFps;
import static io.fotoapparat.parameter.selector.Selectors.firstAvailable;
import static io.fotoapparat.parameter.selector.SensorSensitivitySelectors.highestSensorSensitivity;
import static io.fotoapparat.parameter.selector.SizeSelectors.biggestSize;

public class CameraFragment extends Fragment {

    private CameraView cameraView;
    private FotoapparatSwitcher fotoapparatSwitcher;
    private Fotoapparat backFotoapparat;
    private TextRecognizer textRecognizer;

    private List<FoodAdditive> foodAdditives;

    private boolean isFlashTurnOn;

    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_fragment, container, false);
        cameraView = view.findViewById(R.id.camera_view);

        backFotoapparat = createFotoapparat(LensPosition.BACK);
        fotoapparatSwitcher = FotoapparatSwitcher.withDefault(backFotoapparat);

        textRecognizer = new TextRecognizer.Builder(getContext()).build();

        FloatingActionButton takePicture = view.findViewById(R.id.takePicture);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        FloatingActionButton flash = view.findViewById(R.id.flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlashTurnOn = !isFlashTurnOn;
                fotoapparatSwitcher
                        .getCurrentFotoapparat()
                        .updateParameters(UpdateRequest.builder().flash(isFlashTurnOn ? torch() : off()).build()
                        );
            }
        });

        List<FoodAdditive> dbFoodAdditives = ((MyApplication) getActivity().getApplicationContext()).getDataList();
        foodAdditives = new ArrayList<>();
        for (FoodAdditive foodAdditive : dbFoodAdditives) {
            String newStr = StringUtils.stripAccents(foodAdditive.getName());
            FoodAdditive newFoodAdditive = new FoodAdditive(foodAdditive, newStr);
            foodAdditives.add(newFoodAdditive);
        }

        return view;
    }

    private Fotoapparat createFotoapparat(LensPosition position) {
        return Fotoapparat
                .with(getContext())
                .cameraProvider(CameraProviders.v1()) // change this to v2 to test Camera2 API
                .into(cameraView)
               // .previewSize(getPreviewSize())
                .previewScaleType(ScaleType.CENTER_CROP)
                .photoSize(standardRatio(biggestSize()))
                .lensPosition(lensPosition(position))
                .focusMode(
                        firstAvailable(
                        continuousFocus(),
                        autoFocus(),
                        fixed())
                )
                .flash(firstAvailable(
                        autoFlash(),
                        torch(),
                        off()
                ))
                .previewFpsRange(rangeWithHighestFps())
                .sensorSensitivity(highestSensorSensitivity())
                .frameProcessor(new FrameProcessor() {
                    @Override
                    public void processFrame(Frame frame) {

                    }
                })
                .logger(loggers(
                        logcat(),
                        fileLogger(getContext())
                ))
                .cameraErrorCallback(new CameraErrorCallback() {
                    @Override
                    public void onError(CameraException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
    }

    private void takePicture() {
        PhotoResult photoResult = fotoapparatSwitcher.getCurrentFotoapparat().takePicture();

        photoResult.saveToFile(new File(
                getContext().getExternalFilesDir("photos"),
                "photo.jpg"
        ));

        photoResult.toBitmap().whenAvailable(new PendingResult.Callback<BitmapPhoto>() {
            @Override
            public void onResult(BitmapPhoto result) {
                String plainText = getTextFromImage(result);
                analyzeImage(plainText, result.bitmap);
            }
        });
    }

    public static Bitmap rotateBitmapZoom(Bitmap bmOrg, float degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        float newHeight = bmOrg.getHeight();
        float newWidth  = bmOrg.getWidth() / 100 * (100.0f / bmOrg.getHeight() * newHeight);

        return Bitmap.createBitmap(bmOrg, 0, 0, (int)newWidth, (int)newHeight, matrix, true);
    }

    private void analyzeImage(String plainText, Bitmap bitmap) {
        Log.d("PLAINTEXTIS", plainText +" asaas " + plainText.length());
        String[] cameraWordsArray = getWordsFromString(plainText);
        ArrayList<Integer> ids = new ArrayList<>();
        final int BARRIER = 4;
        for (int i = 0; i < cameraWordsArray.length; i++) {
            String ocrWord = StringUtils.stripAccents(cameraWordsArray[i]);
            for (int j = 0; j < foodAdditives.size(); j++) {
                String db = foodAdditives.get(j).getName();
                String dbList[] = getWordsFromString(db);
                //int firstWordRatio = FuzzySearch.ratio(dbList[0], ocrWord);
                int firstWordRatio = Levenshtein.distance(dbList[0], ocrWord);
                Log.d("TIKRINADABAR", "primas " + dbList[0] + " " + ocrWord + " " + firstWordRatio);
                if (firstWordRatio >= BARRIER) {
                    Log.d("PEREJADGAISD", "primas " + dbList[0] + " " + ocrWord + " " + firstWordRatio);
                    int ocrIndex = i + 1;
                    int max = 0;
                    int id = dbList.length == 1 ? foodAdditives.get(j).getId() : -1;
                    Log.d("PEREJADGAISD", "id = " + id);
                    for (int k = 1; k < dbList.length; k++) {
                        String nextWord = StringUtils.stripAccents(cameraWordsArray[ocrIndex]);
                        int secondPhraseRatio = Levenshtein.distance(dbList[0], ocrWord); //FuzzySearch.ratio(dbList[k], nextWord);
                        ocrIndex++;
                        if (secondPhraseRatio >= BARRIER) {
                            if (max < secondPhraseRatio) {
                                max = secondPhraseRatio;
                            }
                            id = foodAdditives.get(j).getId();
                        } else {
                            id = -1;
                        }
                    }

                    if (id >= 0) {
                        ids.add(id);
                    }
                }
            }
        }

        DetailCameraAdditiveActivity.setImage(bitmap);
        Intent intent = new Intent(getContext(), DetailCameraAdditiveActivity.class);
        intent.putIntegerArrayListExtra(DetailCameraAdditiveActivity.IDS, ids);
        startActivity(intent);
    }

    private String getTextFromImage(BitmapPhoto bitmapPhoto) {
        Bitmap bitmap = bitmapPhoto.bitmap;
        if (bitmapPhoto.rotationDegrees != 0) {
            bitmap = rotateBitmapZoom(bitmapPhoto.bitmap, 360 - bitmapPhoto.rotationDegrees);
        }
        com.google.android.gms.vision.Frame frame = new com.google.android.gms.vision.Frame.Builder().setBitmap(bitmap).build();
        SparseArray<TextBlock> origTextBlocks = textRecognizer.detect(frame);
        List<TextBlock> textBlocks = new ArrayList<>();
        for (int i = 0; i < origTextBlocks.size(); i++) {
            TextBlock textBlock = origTextBlocks.valueAt(i);
            textBlocks.add(textBlock);
        }
        Collections.sort(textBlocks, new Comparator<TextBlock>() {
            @Override
            public int compare(TextBlock o1, TextBlock o2) {
                int diffOfTops = o1.getBoundingBox().top - o2.getBoundingBox().top;
                int diffOfLefts = o1.getBoundingBox().left - o2.getBoundingBox().left;
                if (diffOfTops != 0) {
                    return diffOfTops;
                }
                return diffOfLefts;
            }
        });

        StringBuilder detectedText = new StringBuilder();
        for (TextBlock textBlock : textBlocks) {
            if (textBlock != null && textBlock.getValue() != null) {
                detectedText.append(textBlock.getValue());
            }
        }
        Log.d("SDYADASDSDASD", detectedText.toString() + "");
        return detectedText.toString();
    }

    @Override
    public void onStart() {
        super.onStart();

        fotoapparatSwitcher.start();
    }

    @Override
    public void onStop() {
        super.onStop();

        fotoapparatSwitcher.stop();
    }

    private boolean isLengthValid(String word) {
        return word.length() > 4;
    }

    private String[] getWordsFromString(String str) {
        return str.replaceAll("[,.]", "").toLowerCase().split("\\s+");
    }

    /*
    int a = FuzzySearch.ratio("pieno", "prenorigsts");
    int b = FuzzySearch.partialRatio("pieno", "prenorigsts");
    int c = FuzzySearch.tokenSortPartialRatio("pieno", "prenorigsts");
    int d = FuzzySearch.tokenSortRatio("pieno", "prenorigsts");
    int e = FuzzySearch.tokenSetRatio("pieno", "prenorigsts");
    int f = FuzzySearch.tokenSetPartialRatio("pieno", "prenorigsts");
    int g = FuzzySearch.weightedRatio("pieno", "prenorigsts");


    int aa = FuzzySearch.ratio("prenorigsts", "pieno");
    int bb = FuzzySearch.partialRatio("prenorigsts", "pieno");
    int cc = FuzzySearch.tokenSortPartialRatio("prenorigsts", "pieno");
    int dd = FuzzySearch.tokenSortRatio("prenorigsts", "pieno");
    int ee = FuzzySearch.tokenSetRatio("prenorigsts", "pieno");
    int ff = FuzzySearch.tokenSetPartialRatio("prenorigsts", "pieno");
    int gg = FuzzySearch.weightedRatio("prenorigsts", "pieno");

        Log.d("sfdgysdfsdfdsfd", "a " + a);
        Log.d("sfdgysdfsdfdsfd", "b " + b);
        Log.d("sfdgysdfsdfdsfd", "c " + c);
        Log.d("sfdgysdfsdfdsfd", "d " + d);
        Log.d("sfdgysdfsdfdsfd", "e " + e);
        Log.d("sfdgysdfsdfdsfd", "f " + f);
        Log.d("sfdgysdfsdfdsfd", "g " + g);
        Log.d("sfdgysdfsdfdsfd", "****************************** ");

        Log.d("sfdgysdfsdfdsfd", "a " + aa);
        Log.d("sfdgysdfsdfdsfd", "b " + bb);
        Log.d("sfdgysdfsdfdsfd", "c " + cc);
        Log.d("sfdgysdfsdfdsfd", "d " + dd);
        Log.d("sfdgysdfsdfdsfd", "e " + ee);
        Log.d("sfdgysdfsdfdsfd", "f " + ff);
        Log.d("sfdgysdfsdfdsfd", "g " + gg);

        */
}
