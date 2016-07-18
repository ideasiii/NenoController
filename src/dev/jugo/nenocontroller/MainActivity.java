package dev.jugo.nenocontroller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity
{
    private boolean m_bIsLevel2Show = true;
    private boolean m_bIsLevel3Show = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	initMenu();
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
