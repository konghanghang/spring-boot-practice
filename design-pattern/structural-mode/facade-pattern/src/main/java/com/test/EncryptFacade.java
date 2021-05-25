package com.test;

public class EncryptFacade {

    private FileReader fileReader;
    private CipherMachine cipherMachine;
    private FileWriter fileWriter;

    public EncryptFacade(FileReader fileReader, CipherMachine cipherMachine, FileWriter fileWriter) {
        this.fileReader = fileReader;
        this.cipherMachine = cipherMachine;
        this.fileWriter = fileWriter;
    }

    public void FileEncrypt(){
        System.out.println("操作开始....");
        fileReader.read();
        cipherMachine.encrpt();
        fileWriter.write();
        System.out.println("操作结束....");
    }
}
