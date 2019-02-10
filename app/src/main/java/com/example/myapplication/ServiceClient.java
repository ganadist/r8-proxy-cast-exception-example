package com.example.myapplication;

public interface ServiceClient extends Client {
    void blockContact(int reqSeq, String id);
}
