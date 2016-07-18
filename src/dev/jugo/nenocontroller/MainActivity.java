package dev.jugo.nenocontroller;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import sdk.ideas.common.Logs;
import sdk.ideas.common.OnCallbackResult;
import sdk.ideas.common.ResponseCode;
import sdk.ideas.tool.speech.SpeechRecognizerHandler;

public class MainActivity extends Activity
{
    private TextView		    mTextView		     = null;
    private boolean		    m_bIsLevel2Show	     = true;
    private boolean		    m_bIsLevel3Show	     = true;
    private SpeechRecognizerHandler mSpeechRecognizerHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	this.getActionBar().hide();
	mTextView = (TextView) this.findViewById(R.id.textViewMessage);
	initMenu();
	initSpeech();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
	if (null != mSpeechRecognizerHandler)
	{
	    mSpeechRecognizerHandler.onActivityResult(requestCode, resultCode, data);
	}
	super.onActivityResult(requestCode, resultCode, data);
    }

    private void initSpeech()
    {
	mSpeechRecognizerHandler = new SpeechRecognizerHandler(this);
	mSpeechRecognizerHandler.setOnCallbackResultListener(new OnCallbackResult()
	{
	    @Override
	    public void onCallbackResult(int result, int what, int from, final HashMap<String, String> message)
	    {
		Logs.showTrace("Result: " + String.valueOf(result) + " What: " + String.valueOf(what) + " From: "
			+ String.valueOf(from) + "Message: " + message);
		if (result == ResponseCode.ERR_SUCCESS
			&& from == ResponseCode.METHOD_RETURN_TEXT_SPEECH_RECOGNIZER_SIMPLE)
		{
		    runOnUiThread(new Runnable()
		    {
			@Override
			public void run()
			{
			    mTextView.setText(message.get("message"));
			    Logs.showTrace(message.get("message"));
			    // get text than send to controller, and run google
			    // speech again
			    mSpeechRecognizerHandler.startVoiceRecognition();
			}

		    });
		}
	    }
	});
    }

    private void initMenu()
    {
	final RelativeLayout rllevel2 = (RelativeLayout) this.findViewById(R.id.level2);
	final RelativeLayout rllevel3 = (RelativeLayout) this.findViewById(R.id.level3);

	this.findViewById(R.id.micphone).setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		if (!m_bIsLevel2Show)
		{
		    MyAnimation.startAnimationIN(rllevel2, 500);
		}
		else
		{
		    if (m_bIsLevel3Show)
		    {
			MyAnimation.startAnimationOUT(rllevel3, 500, 0);
			MyAnimation.startAnimationOUT(rllevel2, 500, 500);
			m_bIsLevel3Show = !m_bIsLevel3Show;
		    }
		    else
		    {
			MyAnimation.startAnimationOUT(rllevel2, 500, 0);
		    }
		    mSpeechRecognizerHandler.startVoiceRecognition();
		}
		m_bIsLevel2Show = !m_bIsLevel2Show;
	    }
	});

	this.findViewById(R.id.menu).setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		if (m_bIsLevel3Show)
		{
		    MyAnimation.startAnimationOUT(rllevel3, 500, 0);
		}
		else
		{
		    MyAnimation.startAnimationIN(rllevel3, 500);
		}

		m_bIsLevel3Show = !m_bIsLevel3Show;
	    }
	});
    }

}
