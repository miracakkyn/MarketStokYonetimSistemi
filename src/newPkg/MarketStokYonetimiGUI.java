package newPkg;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class MarketStokYonetimiGUI {

	// ana pencereyi(frame), tabloyu (JTable) ve veri giriş alanlarını (JTextField)
	// tanımlıyoruz
	private JFrame frame;
	private JTable urunTablo;
	private JTextField idField, nameField, categoryField, stockField, priceField, searchField;
	private UrunTabloModel tabloModel;
	private List<UrunSinifi> urunler;
	private JComboBox<String> sortOptions;
	private double toplamSatisKazanc = 0.0; // toplam satışdan elde edilen geliri saklayacak

	public MarketStokYonetimiGUI(List<UrunSinifi> urunler) { // urunleri liste olarak alır ve guı başlatır
		this.urunler = urunler;
		this.toplamSatisKazanc=UrunDosyaIsleme.toplamKazanciYukle();//program açıldığında toplam kazancı yükler
		initialize(urunler); // GUI bileşenlerini başlatmak için initialize metodunu çağırır
	}

	private void initialize(List<UrunSinifi> urunler) {
		// ana pencere (JFrame) oluşturuyoruz
		frame = new JFrame("Market Stok Yönetim Sistemi"); // Pencere Başlığı
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // çıkış butonu ile kapatma
		frame.setSize(800, 500); // pencere boyutu
		frame.setLayout(new BorderLayout()); // BorderLayout kullanarak bileşenleri yerleştirme

		// Urunleri göstermek için tabloyu oluşturuyoruz
		tabloModel = new UrunTabloModel(urunler); // ürünleri tabloya aktarmak için tablo modeli
		urunTablo = new JTable(tabloModel); // JTable'e modeli ekliyoruz
		JScrollPane scrollPane = new JScrollPane(urunTablo); // tabloyu kaydırabilir hale getiriyoruz
		frame.add(scrollPane, BorderLayout.CENTER); // tabloyu pencerenin ortasına ekliyoruz

		// Tabloya tıklama olayını dinleyen MouseAdapter
		urunTablo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selectedRow = urunTablo.getSelectedRow(); // Seçili satırı al
				if (selectedRow >= 0) {
					UrunSinifi urun = urunler.get(selectedRow); // Seçili ürünü al
					// Form alanlarını seçili ürün bilgileriyle doldur
					idField.setText(String.valueOf(urun.getId()));
					nameField.setText(urun.getName());
					categoryField.setText(urun.getCategory());
					stockField.setText(String.valueOf(urun.getStock()));
					priceField.setText(String.valueOf(urun.getPrice()));
				}
			}
		});
		
		// Pencere kapanırken toplam kazancı kaydet
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                UrunDosyaIsleme.toplamKazanciKaydet(toplamSatisKazanc); // Program kapanırken toplam kazancı kaydeder
            }
        });

		// Ürünleri ekleme/güncelleme formu için panel oluşturuyoruz
		JPanel formPanel = new JPanel(new GridLayout(8, 2)); // 7 satır ve 2 sütundan oluşan bir GridLayout
																// oluşturuyoruz

		// ID alanı
		formPanel.add(new JLabel("ID:")); // "ID:" etiketi ekliyoruz
		idField = new JTextField(); // ID girişi için metin alanı
		formPanel.add(idField);

		// Ürün adı alanı
		formPanel.add(new JLabel("Ürün Adı:")); // ürün adı etiketi
		nameField = new JTextField(); // ürün adı girişi için metin alanı
		formPanel.add(nameField);

		// kategori alanı
		formPanel.add(new JLabel("Kategori:")); // "Kategori:" etiketi
		categoryField = new JTextField(); // Kategori girişi için metin alanı
		formPanel.add(categoryField);

		// Stok alanı
		formPanel.add(new JLabel("Stok:")); // "Stok:" etiketi
		stockField = new JTextField(); // Stok girişi için metin alanı
		formPanel.add(stockField);

		// Fiyat alanı
		formPanel.add(new JLabel("Fiyat:")); // "Fiyat:" etiketi
		priceField = new JTextField(); // Fiyat girişi için metin alanı
		formPanel.add(priceField);

		// ürün ekleme ve güncelleme butonları ekleniyor
		JButton addButton = new JButton("Ürün Ekle"); // ürün ekle butonu
		formPanel.add(addButton);
		JButton updateButton = new JButton("Ürünü Güncelle");
		formPanel.add(updateButton);

		// Satış yap ve estok ekleme butonları
		JButton satisYapButton = new JButton("Satış Yap");
		formPanel.add(satisYapButton);
		JButton stokEkleButton = new JButton("Stok Ekle");
		formPanel.add(stokEkleButton);

		// Toplam satışları göster butonu
		JButton toplamSatisButton = new JButton("Toplam Satış");
		formPanel.add(toplamSatisButton);

		// Ürün Silme Butonu
		JButton deleteButton = new JButton("Ürünü Sil"); // Ürün silme butonu
		formPanel.add(deleteButton);

		// form panelini pencerenin alt kısmına ekliyoruz
		frame.add(formPanel, BorderLayout.SOUTH);
		frame.setVisible(true);

		// Satış Yap Butonu İşlevi
		satisYapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = urunTablo.getSelectedRow();
				if (selectedRow >= 0) {
					UrunSinifi urun = urunler.get(selectedRow);
					if (urun.getStock() > 0) { // Stokta en az 1 ürün varsa satış yapılabilir
						urun.setStock(urun.getStock() - 1);
						urun.setSatilanMiktar(urun.getSatilanMiktar()+1);
						toplamSatisKazanc += urun.getPrice(); // Satış yapıldıkça toplam satışa ekle
						tabloModel.fireTableDataChanged();
						UrunDosyaIsleme.urunleriDosyayaKaydet("urunler.txt", urunler);
					} else {
						JOptionPane.showMessageDialog(frame, "Stokta yeterli ürün yok!", "Uyarı",
								JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Satış yapmak için bir ürün seçin!", "Hata",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// Stok Ekle Butonu İşlevi
		stokEkleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = urunTablo.getSelectedRow();
				if (selectedRow >= 0) {
					UrunSinifi urun = urunler.get(selectedRow);
					urun.setStock(urun.getStock() + 1); // Stok miktarını 1 artır
					tabloModel.fireTableDataChanged();
					UrunDosyaIsleme.urunleriDosyayaKaydet("urunler.txt", urunler);
				} else {
					JOptionPane.showMessageDialog(frame, "Stok eklemek için bir ürün seçin!", "Hata",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// Toplam Satışları Göster Butonu İşlevi
		toplamSatisButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Toplam Satış Miktarı: " + toplamSatisKazanc, "Toplam Satış",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Sıralama Seçenekleri ve Butonu
		JPanel sortPanel = new JPanel();
		sortPanel.add(new JLabel("Sırala:"));
		sortOptions = new JComboBox<>(new String[] { "ID", "Stok", "Fiyat", "Kategori" });
		sortPanel.add(sortOptions);
		JButton sortButton = new JButton("Sırala");
		sortPanel.add(sortButton);
		frame.add(sortPanel, BorderLayout.NORTH);

		// Arama Alanı ve Butonu
		JPanel searchPanel = new JPanel();
		searchPanel.add(new JLabel("Ara:"));
		searchField = new JTextField(10);
		searchPanel.add(searchField);
		JButton searchButton = new JButton("Ara");
		searchPanel.add(searchButton);
		frame.add(searchPanel, BorderLayout.EAST);

		// Tümünü Göster Butonunu Ekliyoruz
		JButton showAllButton = new JButton("Tümünü Göster");
		searchPanel.add(showAllButton);
		frame.add(searchPanel, BorderLayout.EAST);

		// Pencereyi görünür hale getiriyoruz
		frame.setVisible(true);

		// "Ürün Ekle" Butonuna İşlev Ekle
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// Form alanlarından veri al
					int id = Integer.parseInt(idField.getText());
					String name = nameField.getText();
					String category = categoryField.getText();
					int stock = Integer.parseInt(stockField.getText());
					double price = Double.parseDouble(priceField.getText());

					// Yeni ürünü oluştur ve listeye ekle
					UrunSinifi urun = new UrunSinifi(id, name, category, stock, price);
					urunler.add(urun);

					// Tabloyu güncelle ve dosyaya kaydet
					tabloModel.fireTableDataChanged();
					UrunDosyaIsleme.urunleriDosyayaKaydet("urunler.txt", urunler);
					clearFormFields();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(frame, "Geçerli bir sayı girin!", "Hata", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		// "Ürünü Güncelle" Butonuna İşlev Ekle
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = urunTablo.getSelectedRow(); // Seçili satırı al
				if (selectedRow >= 0) {
					try {
						// Form alanlarından yeni verileri al
						int id = Integer.parseInt(idField.getText());
						String name = nameField.getText();
						String category = categoryField.getText();
						int stock = Integer.parseInt(stockField.getText());
						double price = Double.parseDouble(priceField.getText());

						// Mevcut ürünü güncelle
						UrunSinifi urun = urunler.get(selectedRow);
						urun.setId(id);
						urun.setName(name);
						urun.setCategory(category);
						urun.setStock(stock);
						urun.setPrice(price);

						// Tabloyu güncelle ve dosyaya kaydet
						tabloModel.fireTableDataChanged();
						UrunDosyaIsleme.urunleriDosyayaKaydet("urunler.txt", urunler);
						clearFormFields();
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(frame, "Geçerli bir sayı girin!", "Hata",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Lütfen bir ürün seçin!", "Hata", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// "Ürünü Sil" Butonuna İşlev Ekle
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = urunTablo.getSelectedRow(); // Seçili satırı al
				if (selectedRow >= 0) {
					urunler.remove(selectedRow); // Listeden seçili ürünü sil
					tabloModel.fireTableDataChanged(); // Tabloyu güncelle
					UrunDosyaIsleme.urunleriDosyayaKaydet("urunler.txt", urunler); // Güncellenen listeyi dosyaya kaydet
				} else {
					JOptionPane.showMessageDialog(frame, "Lütfen silinecek bir ürün seçin!", "Hata",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// "Sırala" Butonuna İşlev Ekle
		sortButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedOption = (String) sortOptions.getSelectedItem();
				if (selectedOption.equals("ID")) {
					urunler.sort(Comparator.comparingInt(UrunSinifi::getId));
				} else if (selectedOption.equals("Stok")) {
					urunler.sort(Comparator.comparingInt(UrunSinifi::getStock));
				} else if (selectedOption.equals("Fiyat")) {
					urunler.sort(Comparator.comparingDouble(UrunSinifi::getPrice));
				} else if (selectedOption.equals("Kategori")) {
					urunler.sort(Comparator.comparing(UrunSinifi::getCategory));
				}
				tabloModel.fireTableDataChanged();
			}
		});

		// "Ara" Butonuna İşlev Ekle
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = searchField.getText().toLowerCase();
				List<UrunSinifi> filteredUrunler = urunler.stream().filter(
						u -> u.getName().toLowerCase().contains(query) || u.getCategory().toLowerCase().contains(query))
						.collect(Collectors.toList());

				tabloModel = new UrunTabloModel(filteredUrunler);
				urunTablo.setModel(tabloModel);
			}
		});
		// Tümünü Göster Butonuna İşlev Ekliyoruz
		showAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Tüm ürünleri gösteren tablo modelini yeniden ayarlıyoruz
				tabloModel = new UrunTabloModel(urunler);
				urunTablo.setModel(tabloModel);
			}
		});
	}

	private boolean validateFormFields() {
		// ID alanı boş veya sayısal değilse hata verir
		try {
			Integer.parseInt(idField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Geçerli bir ID girin!", "Hata", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Ürün adı boş bırakılırsa hata verir
		if (nameField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Ürün adı boş bırakılamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Kategori boş bırakılırsa hata verir
		if (categoryField.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Kategori boş bırakılamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Stok alanı boş veya sayısal değilse hata verir
		try {
			Integer.parseInt(stockField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Geçerli bir stok değeri girin!", "Hata", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// Fiyat alanı boş veya sayısal değilse hata verir
		try {
			Double.parseDouble(priceField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(frame, "Geçerli bir fiyat değeri girin!", "Hata", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true; // Tüm alanlar geçerliyse true döner
	}

	// Form alanlarını temizleyen metod
	private void clearFormFields() {
		idField.setText("");
		nameField.setText("");
		categoryField.setText("");
		stockField.setText("");
		priceField.setText("");
	}

	// Programı başlatan ana metod
	public static void main(String[] args) {
		// ürünleri dosyadan okuyoruz
		List<UrunSinifi> urunler = UrunDosyaIsleme.urunleriDosyadanYukle("urunler.txt");

		// GUI'yi başlatıyoruz
		new MarketStokYonetimiGUI(urunler);
	}
}
