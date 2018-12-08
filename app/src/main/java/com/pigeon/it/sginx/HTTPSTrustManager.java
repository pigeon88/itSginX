package com.pigeon.it.sginx;

import android.annotation.SuppressLint;
import android.util.Log;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HTTPSTrustManager implements X509TrustManager {
	
	private static final String TAG = HTTPSTrustManager.class.getSimpleName();

	private static TrustManager[] trustManagers;
	private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
			throws java.security.cert.CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
			throws java.security.cert.CertificateException {
	}

	public boolean isClientTrusted(X509Certificate[] chain) {
		return true;
	}

	public boolean isServerTrusted(X509Certificate[] chain) {
		return true;
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return _AcceptedIssuers;
	}

	@SuppressLint("TrulyRandom")
	public static void allowAllSSL() {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}

		});

		SSLContext context = null;
		if (trustManagers == null) {
			trustManagers = new TrustManager[] { new HTTPSTrustManager() };
		}

		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustManagers, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "", e);
		} catch (KeyManagementException e) {
			Log.e(TAG, "", e);
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}

}
