package com.king.reading.common.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.reading.model.EvaluateResult;
import com.king.reading.model.LineResult;
import com.unisound.edu.oraleval.sdk.sep15.IOralEvalSDK;
import com.unisound.edu.oraleval.sdk.sep15.IOralEvalSDK.OfflineSDKError;
import com.unisound.edu.oraleval.sdk.sep15.OralEvalSDKFactory;
import com.unisound.edu.oraleval.sdk.sep15.SDKError;

import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VoiceEvaluate {
	private static final String TAG = "VoiceEvaluate";
	static final boolean USE_OFFLINE_SDK_IF_FAIL_TO_SERVER = false;
	private Context context;
	private static VoiceEvaluate instance;
	IOralEvalSDK _oe;
	private FileOutputStream audioFileOut;

	private IOralEvalSDK.ICallback callback = null;
	
	private float _scoreAdjuest = 1.8f;
    private String serviceType = "A";

	public void closeFileOut() {
		if (audioFileOut != null) {
			try {
				audioFileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			audioFileOut = null;
		}
	}

	public VoiceEvaluate(Context context) {
		this.context = context;
		initialize();
	}

	private void initialize() {
		if (USE_OFFLINE_SDK_IF_FAIL_TO_SERVER) {
			Log.i(TAG, "start init offline sdk");
			OfflineSDKError err = OralEvalSDKFactory.initOfflineSDK(context, null);
			Log.i(TAG, "end init offline sdk");
			if (err != OfflineSDKError.NOERROR) {
				Log.i(TAG, "init sdk failed:" + err);
			}
		}
	}

	public void setCallback(IOralEvalSDK.ICallback callback) {
		this.callback = callback;
	}

	public void stopIOralEvalSDK() {
		if (_oe != null) {
			_oe.stop();
			_oe = null;
		}
	}
	
	public IOralEvalSDK getIOralEvalSDK() {
		return _oe;
	}

	private OralEvalSDKFactory.StartConfig getCfg(String txt){
        OralEvalSDKFactory.StartConfig cfg=null;
        cfg = new OralEvalSDKFactory.StartConfig(txt);
//        cfg.setUid(SharedPreferencesUtil.GetUserID());
        cfg.setVadEnable(false);
        cfg.setVadAfterMs(15000);
        cfg.setVadBeforeMs(15000);
        

        if(USE_OFFLINE_SDK_IF_FAIL_TO_SERVER) {
            cfg.set_useOfflineWhenFailedToConnectToServer(true);
        }
        cfg.setMp3Audio(true);//use mp3 in onAudioData() callback, or pcm output
        cfg.setScoreAdjuest(_scoreAdjuest);
        cfg.setServiceType(serviceType);
        return cfg;
    }

	public void evaluate(String str) {
		if (_oe == null) {
			if (str == null) {
				Toast.makeText(context, "评测文本为空！", Toast.LENGTH_SHORT).show();
				return;
			}
			OralEvalSDKFactory.StartConfig cfg = getCfg(str);
			if(cfg==null){
				return;
			}
			_oe = OralEvalSDKFactory.start(this.context, cfg, callback);
		} else {
			stopIOralEvalSDK();
		}
	}

	public EvaluateResult parseJsonResult(String result) {
		EvaluateResult evaluateResult = new EvaluateResult();
		try {
			JSONObject jsonObject = new JSONObject(result);
			evaluateResult.setVersion(jsonObject.getString("version"));
			Gson gson = new Gson();
			List<LineResult> lines = new ArrayList<LineResult>();
			lines = gson.fromJson(jsonObject.getString("lines"),
					new TypeToken<List<LineResult>>() {
					}.getType());
			evaluateResult.setLines(lines);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return evaluateResult;

	}
	
	public static void onlineSDKError(Context context,SDKError error){
		switch (SDKError.Category.valueOf(error.category.toString())) {
		case Device:
			Toast.makeText(context, "录音设备错误,请开启录音权限", Toast.LENGTH_SHORT).show();
			break;
		case Network:
			Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
			break;
		case Server:
			Toast.makeText(context, "服务器错误。 遇到此错误联系云知声", Toast.LENGTH_SHORT).show();
			break;
		case Unknown_word:
			Toast.makeText(context, "非法评测内容", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
