package fr.uniform;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Crypt : 1");
        System.out.println("Decrypt : 2");

        int response = scanner.nextInt();
        scanner = new Scanner(System.in);
        if (response == 1) {

            System.out.println("Path : ");

            String responseStr = scanner.nextLine();

            Path path = Paths.get(responseStr);
            if (path.toFile().exists()) {

                crypt(path.toString());
            } else {
                System.out.println("File not found");
            }


        } else if (response == 2) {
            System.out.println("Path : ");
            String responseStr = scanner.nextLine();
            Path path = Paths.get(responseStr);
            if (path.toFile().exists()) {
                try {
                    decrypt(path.toString());
                } catch (Exception e) {
                    System.out.println("Wrong password");
                    throw new RuntimeException(e);
                }


            } else {
                System.out.println("File not found");
            }
        }

    }

    private static void crypt(String path) throws Exception {

        System.out.println("Password to crypt :");
        String password = "";
        Scanner scanner = new Scanner(System.in);
        String responsePass = scanner.next();

        if (!Objects.equals(responsePass, "") && responsePass != null) {
            password = responsePass;

        }

        assert !password.isEmpty();
        fr.uniform.EncryptorAesGcmPasswordFile.encryptFile(path, path, password);

    }

    private static void decrypt(String path) throws Exception {

        Scanner scanner = new Scanner(System.in);
        String password = "";
        System.out.println("Password to decrypt :");
        String responsePass = scanner.next();

        if (!Objects.equals(responsePass, "") && responsePass != null) {
            password = responsePass;

        }

        assert !password.isEmpty();
        fr.uniform.EncryptorAesGcmPasswordFile.decryptFile(path, path, password);


    }

}