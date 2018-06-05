package in.juspay.samplehyperapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import in.juspay.android_lib.core.JuspayLogger;
import in.juspay.godel.ui.PaymentFragment;
import in.juspay.hypersdk.core.JuspayCallback;
import in.juspay.hypersdk.data.JuspayConstants;

public class MainActivity extends AppCompatActivity {
    PaymentFragment fragment;
    JuspayCallback juspayCallback = new JuspayCallback() {
        @Override
        public void onResult(int requestCode, int resultCode, @NonNull Intent intent) {
            JuspayLogger.d("MainActivity", "onResult: " + requestCode + " resultCode: " + resultCode + " result: " + intent.getStringExtra("payload"));
            removeFragment(fragment);
            fragment = null;
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PaymentFragment.canOpenPaymentPage(this, new Handler.Callback() {
            @Override
            public boolean handleMessage(final Message message) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Can Open Payment Page:" + message.obj, Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
        });

        fragment = new PaymentFragment();
        fragment.setArguments(getBundle());
        fragment.setWebViewClient(new SampleWebViewClient(this, fragment.getWebView(), fragment));
        showFragment(fragment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private  Bundle getBundle() {
        Bundle args = new Bundle();

        args.putString("merchantId", "juspay");
        args.putString("customerId", "juspay_android");

        args.putString("amount", "1.00");
        args.putString("orderId", "veera");
        args.putString("transactionId", "veera_txn");

        args.putString("url", "https://reload.in");
        //args.putString("postData", "msisdn=9739534710&amount=10.0&orderId=ES28311888171052&segment=PREPAID&rechargeProdID=prod8240744");

        args.putString("service", "in.juspay.godel"); // To start the godel SDK

        args.putParcelable(JuspayConstants.JUSPAY_CALLBACK, juspayCallback);
        return args;
    }

    @Override
    public void onBackPressed() {
        if(fragment != null) {
            fragment.backPressHandler(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(fragment != null) {
            outState.putBundle("params", getIntent().getExtras());
            getSupportFragmentManager().putFragment(outState, "CURRENT_FRAGMENT", fragment);
            super.onSaveInstanceState(outState);
        }
    }

    public void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(fragment)
                .commitAllowingStateLoss();
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.container, fragment)
                .commit();
    }
}
