package com.apcpdcl.departmentapp.activities;

import android.content.Context;
import android.net.http.SslCertificate;

import com.apcpdcl.departmentapp.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;


public class Certificates {
    private static final int[] CERTIFICATES = {
            R.raw.www_apspdcl_in,   // you can put several certificates
    };

    public ArrayList<SslCertificate>  loadSSLCertificates(Context context) {
        ArrayList<SslCertificate> certificates = new ArrayList<>();
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            for (int rawId : CERTIFICATES) {
                InputStream inputStream = context.getResources().openRawResource(rawId);
                InputStream certificateInput = new BufferedInputStream(inputStream);
                try {
                    Certificate certificate = certificateFactory.generateCertificate(certificateInput);
                    if (certificate instanceof X509Certificate) {
                        X509Certificate x509Certificate = (X509Certificate) certificate;
                        SslCertificate sslCertificate = new SslCertificate(x509Certificate);
                        certificates.add(sslCertificate);
                    } else {
                        //Log.w(TAG, "Wrong Certificat e format: " + rawId);
                    }
                } catch (CertificateException exception) {
                   // Log.w(TAG, "Cannot read certificate: " + rawId);
                } finally {
                    try {
                        certificateInput.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return certificates;
    }
}
