package dev.jugo.view;

import java.io.InputStream;
import java.lang.reflect.Field;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import dev.jugo.nenocontroller.R;

public class PowerImageView extends ImageView
{

    private Movie mMovie;
    private long  mMovieStart;
    private int	  mImageWidth;
    private int	  mImageHeight;

    public PowerImageView(Context context)
    {
	super(context);
    }

    public PowerImageView(Context context, AttributeSet attrs)
    {
	this(context, attrs, 0);
    }

    public PowerImageView(Context context, AttributeSet attrs, int defStyleAttr)
    {
	super(context, attrs, defStyleAttr);
	TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PowerImageView);
	int resourceId = getResourceId(a, context, attrs);
	if (resourceId != 0)
	{
	    InputStream is = getResources().openRawResource(resourceId);
	    mMovie = Movie.decodeStream(is);
	    if (mMovie != null)
	    {

		Bitmap bitmap = BitmapFactory.decodeStream(is);
		mImageWidth = bitmap.getWidth();
		mImageHeight = bitmap.getHeight();
		bitmap.recycle();
	    }
	}
    }

    private int getResourceId(TypedArray a, Context context, AttributeSet attrs)
    {
	try
	{
	    Field field = TypedArray.class.getDeclaredField("mValue");
	    field.setAccessible(true);
	    TypedValue typedValueObject = (TypedValue) field.get(a);
	    return typedValueObject.resourceId;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	finally
	{
	    if (a != null)
	    {
		a.recycle();
	    }
	}
	return 0;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
	if (mMovie == null)
	{
	    super.onDraw(canvas);
	}
	else
	{
	    playMovie(canvas);
	    invalidate();
	}
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	if (mMovie != null)
	{
	    setMeasuredDimension(mImageWidth, mImageHeight);
	}
    }

    private boolean playMovie(Canvas canvas)
    {
	long now = SystemClock.uptimeMillis();
	if (mMovieStart == 0)
	{
	    mMovieStart = now;
	}
	int duration = mMovie.duration();
	if (duration == 0)
	{
	    duration = 1000;
	}
	int relTime = (int) ((now - mMovieStart) % duration);
	mMovie.setTime(relTime);
	mMovie.draw(canvas, 0, 0);
	if ((now - mMovieStart) >= duration)
	{
	    mMovieStart = 0;
	    return true;
	}
	return false;
    }

}
