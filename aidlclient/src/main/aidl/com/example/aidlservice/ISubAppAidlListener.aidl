// ISubAppAidlListener.aidl
package com.example.aidlservice;

// Declare any non-default types here with import statements

interface ISubAppAidlListener {
    void onNext(String jsonNextData);
    void onMethodFinished(String jsonResponseData);
}
