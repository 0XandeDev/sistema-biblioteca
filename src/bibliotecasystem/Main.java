package bibliotecasystem;

import bibliotecasystem.telas.TelaLogin;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Iniciando Sistema Biblioteca...");
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Configurar aparência do sistema
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    try {
                        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    } catch (Exception ex) {
                        // Ignora erro de look and feel
                    }
                }
                
                // Criar e exibir a tela de login
                TelaLogin login = new TelaLogin();
                login.setVisible(true);
                
                System.out.println("✅ Sistema iniciado com sucesso!");
            }
        });
    }
}