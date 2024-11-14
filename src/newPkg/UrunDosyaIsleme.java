package newPkg;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UrunDosyaIsleme {
	// Bu bölümde text dosyasındaki ürünler okuyup bir listeye ekleyeceğiz
	
	String filePath = "urunler.txt";  // Dosya proje dizininde

	
	
	public static List<UrunSinifi> urunleriDosyadanYukle(String filePath) {//urunleri text dosyasından okuyup listeye ekleyecek metod
		//filePath dosya yolunu parametre alır ve içinde ürünlerin saklandığı bir list<urunSinifi> döndürür
		
		List<UrunSinifi> urunler = new ArrayList<>();//ürünleri saklamak için bir array list olşuturuyoruz
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {//dosyadan satır satır veri okuyabilmek i.in bufferedreader ve filereader kullanıyoruz
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				int id = Integer.parseInt(data[0]);
				String name = data[1];
				String category = data[2];
				int stock = Integer.parseInt(data[3]);
				double price = Double.parseDouble(data[4]);
				int satilanAdet=(data.length==6) ? Integer.parseInt(data[5]):0;

				UrunSinifi urun = new UrunSinifi(id, name, category, stock, price);
				urun.setSatilanMiktar(satilanAdet);
				urunler.add(urun);
			}
		} catch (IOException e) {
			System.out.println("Dosyadan okuma hatası: " + e.getMessage());
		}
		return urunler;
	}
	
	public static void urunleriDosyayaKaydet(String filePath,List<UrunSinifi> urunler) {//bu metod ürün listesini List<UrunSinifi> text dosyasına yazar ve dosya yolu parametre olarak alınır
		try(BufferedWriter bw=new BufferedWriter(new FileWriter(filePath))){
			for(UrunSinifi urun:urunler) {
				bw.write(urun.toString());// ürün listesindeki her bir ürünmü dosyaya yazıyoruz,bunu ayparken kendi yazdığımız toString metodunu kulllanıyoruz
				bw.newLine(); //her ürünü yeni bir satıra yazıyoruz
			}
		}catch(IOException e) {
			System.out.println("Dosya yazma hatası: "+e.getMessage());
		}
	}
	public static void toplamKazanciKaydet(double toplamSatisKazanc) {
		try(BufferedWriter writer=new BufferedWriter(new FileWriter("toplam_kazanc.txt"))){
			writer.write(String.valueOf(toplamSatisKazanc));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public static double toplamKazanciYukle() {
		try(BufferedReader reader=new BufferedReader(new FileReader("toplam_kazanc.txt"))){
			String line=reader.readLine();
			return line!=null ? Double.parseDouble(line):0.0;
		}catch (IOException | NumberFormatException e) {
			e.printStackTrace();
			return 0.0;
		}
				
				
	}

}
