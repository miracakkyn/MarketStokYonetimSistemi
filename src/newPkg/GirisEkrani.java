package newPkg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GirisEkrani {

    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public GirisEkrani(List<UrunSinifi> urunler) {
        initialize(urunler);
    }

    private void initialize(List<UrunSinifi> urunler) {
        frame = new JFrame("Giriş Ekranı");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());
        
        // arkaplan resmi olarak kullanabileceğimiz bir ImageIcon oluşturuyoruz
        ImageIcon backgroundImage=new ImageIcon("resources/market_background.jpg");
        
        if (backgroundImage.getIconWidth() == -1) {
            System.out.println("Resim yüklenemedi. Lütfen dosya yolunu kontrol edin.");
        } else {
            System.out.println("Resim başarıyla yüklendi.");
        }
        
        
        
        JLabel backgroundLabel=new JLabel(backgroundImage);
        backgroundLabel.setLayout(new GridBagLayout());//ortalamak için gridBaglayout kullanıyoruz
        
        // Kullanıcı Adı ve Şifre Alanları İçin Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3,2,10,10));
        loginPanel.setOpaque(false);//paneli saydam yapıyoruz böylece arkaplan resmi görünür
        
        
        // Kullanıcı Adı Alanı
        loginPanel.add(new JLabel("Kullanıcı Adı:",JLabel.RIGHT));
        usernameField = new JTextField(20);
        loginPanel.add(usernameField);

        // Şifre Alanı
        loginPanel.add(new JLabel("Şifre:",JLabel.RIGHT));
        passwordField = new JPasswordField(20);
        loginPanel.add(passwordField);

        // Giriş Butonu
        JButton loginButton = new JButton("Giriş Yap");
        loginPanel.add(new JLabel());//boşluk bırakmak için eklenen boş etiket
        loginPanel.add(loginButton);
        
        //giriş panelini arkaplan üzerine ortalamaka için GridBagConstraints kullanıyoruz
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor=GridBagConstraints.CENTER;
        backgroundLabel.add(loginPanel,gbc);
        
        //arkaplan etiketini çerçevenin merkezine ekliyoruz
        frame.add(backgroundLabel,BorderLayout.CENTER);

        // Giriş Butonuna İşlev Ekleme
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Kullanıcı adı ve şifre doğrulama
                if (username.equals("admin") && password.equals("veriyapilari")) {
                    // Giriş başarılı, ana ekrana geçiş yap
                    frame.dispose(); // Giriş ekranını kapat
                    new MarketStokYonetimiGUI(urunler); // Ana uygulama ekranını aç
                } else {
                    // Hatalı giriş mesajı
                    JOptionPane.showMessageDialog(frame, "Geçersiz kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Pencereyi görünür hale getirme
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Uygulama açıldığında ürünleri dosyadan okuyalım
        List<UrunSinifi> urunler = UrunDosyaIsleme.urunleriDosyadanYukle("urunler.txt");

        // Giriş ekranını başlatıyoruz
        new GirisEkrani(urunler);
    }
}
