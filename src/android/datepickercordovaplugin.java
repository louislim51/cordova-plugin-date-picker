package com.louis.datepicker;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.app.Activity;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Typeface;

import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.nexgo.oaf.apiv3.device.printer.BarcodeFormatEnum;
import com.nexgo.oaf.apiv3.device.printer.DotMatrixFontEnum;
import com.nexgo.oaf.apiv3.device.printer.FontEntity;
import com.nexgo.oaf.apiv3.device.printer.GrayLevelEnum;
import com.nexgo.oaf.apiv3.device.printer.OnPrintListener;
import com.nexgo.oaf.apiv3.device.printer.Printer;

/**
 * This class echoes a string called from JavaScript.
 */
public class datepickercordovaplugin extends CordovaPlugin {

    public DeviceEngine deviceEngine;
    private Printer printer;
    private Context context;
    private Activity activity;

    private final int FONT_SIZE_SMALL = 20;
    private final int FONT_SIZE_NORMAL = 24;
    private final int FONT_SIZE_BIG = 24;
    private FontEntity fontSmall = new FontEntity(DotMatrixFontEnum.CH_SONG_20X20, DotMatrixFontEnum.ASC_SONG_8X16);
    private FontEntity fontNormal = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24, DotMatrixFontEnum.ASC_SONG_12X24);
    private FontEntity fontBold = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24,
            DotMatrixFontEnum.ASC_SONG_BOLD_16X24);
    private FontEntity fontBig = new FontEntity(DotMatrixFontEnum.CH_SONG_24X24, DotMatrixFontEnum.ASC_SONG_12X24,
            false, true);

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        context = this.cordova.getActivity().getBaseContext();
        activity = this.cordova.getActivity();
        deviceEngine = APIProxy.getDeviceEngine(context);
        printer = deviceEngine.getPrinter();
        printer.setTypeface(Typeface.DEFAULT);

    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            Log.d("myTag", "This is my message");
            Log.d("myTag", message);

            this.coolMethod(message, callbackContext);
            return true;
        } else if (action.equals("printReceipt")) {
            Log.d("myTag", "This is print receipt");

            this.printReceipt(args.getJSONArray(0), callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void printReceipt(JSONArray args, CallbackContext callbackContext) {
        printer.initPrinter(); // init printer
        printer.setTypeface(Typeface.DEFAULT); // change print type
        printer.setLetterSpacing(5); // change the line space between each line
        printer.setGray(GrayLevelEnum.LEVEL_2); // change print gray

        try {
            for (int i = 0; i < args.length(); i++) {
                String valueString = args.getString(i);
                printer.appendPrnStr(valueString, FONT_SIZE_NORMAL, AlignEnum.CENTER, false);

            }
        } catch (JSONException e) {
            Log.i("CordovaLog", e.getLocalizedMessage());
        }

        printer.startPrint(false, new OnPrintListener() { // roll paper or not
            @Override
            public void onPrintResult(final int retCode) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, retCode + "", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
