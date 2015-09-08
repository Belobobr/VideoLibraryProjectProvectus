package com.miiskin.videolibraryproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

/**
 * LiveLongAndProsperIntentService is a base class for {@link android.app.Service}s that handle
 * asynchronous * requests (expressed as {@link android.content.Intent}s) on demand.  Clients send
 * requests through {@link android.content.Context#startService(android.content.Intent)} calls; the
 * service is started as needed, handles each Intent in turn using a worker
 * thread, and <b>doesn't stop</b> itself when it runs out of work.
 * <p/>
 * <p>This "work queue processor" pattern is commonly used to offload tasks
 * from an application's main thread.  The LiveLongAndProsperIntentService class exists to
 * simplify this pattern and take care of the mechanics.  To use it, extend
 * LiveLongAndProsperIntentService and implement {@link #onHandleIntent(android.content.Intent)}.
 * LiveLongAndProsperIntentService will receive the Intents, and launch a worker thread.
 * <p/>
 * <p>All requests are handled on a single worker thread -- they may take as
 * long as necessary (and will not block the application's main loop), but
 * only one request will be processed at a time.
 * <p/>
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For a detailed discussion about how to create services, read the
 * <a href="{@docRoot}guide/topics/fundamentals/services.html">Services</a> developer guide.</p>
 * </div>
 *
 * @see android.os.AsyncTask
 * <p/>
 * Created by ustimov.d on 30.04.2015.
 */
public abstract class LiveLongAndProsperIntentService extends Service {

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private final String mName;

	/**
	 * Creates an LiveLongAndProsperIntentService. Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public LiveLongAndProsperIntentService(final String name) {
		super();
		mName = name;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		final HandlerThread thread = new HandlerThread("LiveLongAndProsperIntentService [" + mName + "]");
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper, this);
	}

	@SuppressWarnings("deprecated")
	@Override
	public final void onStart(final Intent intent, final int startId) {
		final Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		msg.obj = intent;
		mServiceHandler.sendMessage(msg);
	}

	/**
	 * You should not override this method for your LiveLongAndProsperIntentService. Instead,
	 * override {@link #onHandleIntent}, which the system calls when the
	 * LiveLongAndProsperIntentService receives a start request.
	 *
	 * @see android.app.Service#onStartCommand
	 */
	@Override
	public final int onStartCommand(final Intent intent, final int flags, final int startId) {
		onStart(intent, startId);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		mServiceLooper.quit();
	}

	/**
	 * Unless you provide binding for your service, you don't need to implement this
	 * method, because the default implementation returns null.
	 *
	 * @see android.app.Service#onBind
	 */
	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	/**
	 * This method is invoked on the worker thread with a request to process.
	 * Only one Intent is processed at a time, but the processing happens on a
	 * worker thread that runs independently from other application logic.
	 * So, if this code takes a long time, it will hold up other requests to
	 * the same LiveLongAndProsperIntentService, but it will not hold up anything else.
	 * When all requests have been handled, the LiveLongAndProsperIntentService
	 * will not stop itself, so you can use it to observer data changes.
	 *
	 * @param intent The value passed to {@link
	 *               android.content.Context#startService(android.content.Intent)}.
	 */
	protected abstract void onHandleIntent(@NonNull final Intent intent);

	private static class ServiceHandler extends Handler {

		private LiveLongAndProsperIntentService mService;

		public ServiceHandler(final Looper looper, final LiveLongAndProsperIntentService service) {
			super(looper);
			mService = service;
		}

		@Override
		public void handleMessage(final Message msg) {
			final Intent intent = (Intent) msg.obj;
			if (intent != null) {
				mService.onHandleIntent(intent);
			}
		}
	}

}
