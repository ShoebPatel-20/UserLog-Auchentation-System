package com.demo.userlog;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainMenu = new JFrame("Welcome");
            mainMenu.setSize(300, 150);
            mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainMenu.setLayout(new GridLayout(1, 2));

            JButton registerBtn = new JButton("Register");
            JButton loginBtn = new JButton("Login");

            registerBtn.addActionListener(e -> {
                new RegisterForm();
                mainMenu.dispose();
            });

            loginBtn.addActionListener(e -> {
                new LoginForm();
                mainMenu.dispose();
            });

            mainMenu.add(registerBtn);
            mainMenu.add(loginBtn);
            mainMenu.setVisible(true);
        });
    }
}
       // launch();

