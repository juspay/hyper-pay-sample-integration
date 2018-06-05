package in.juspay.samplehyperapp;

import android.graphics.Bitmap;
import android.webkit.WebView;

import in.juspay.godel.core.GodelTracker;
import in.juspay.godel.ui.JuspayWebView;
import in.juspay.godel.ui.JuspayWebViewClient;
import in.juspay.godel.ui.PaymentFragment;

public class SampleWebViewClient extends JuspayWebViewClient {

    private final MainActivity activity;
    PaymentFragment fragment;

    public SampleWebViewClient(MainActivity activity, JuspayWebView juspayWebView, PaymentFragment browserFragment) {
        super(juspayWebView, browserFragment);
        fragment = browserFragment;
        this.activity = activity;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(url.contains("https://www.reload.in/recharge")) {
            activity.removeFragment(fragment);
            activity.fragment = null;
            GodelTracker.getInstance().trackPaymentStatus("veera_txn", GodelTracker.SUCCESS);
            activity.finish();
        }
    }
}
