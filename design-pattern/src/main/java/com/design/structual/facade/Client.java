package com.design.structual.facade;

public class Client {

    public static void main(String[] args) {
        EncryptFacade encryptFacade = new EncryptFacade(new FileReader(),new CipherMachine(),new FileWriter());
        encryptFacade.FileEncrypt();
    }

}
