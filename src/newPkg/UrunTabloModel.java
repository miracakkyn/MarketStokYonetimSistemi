package newPkg;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class UrunTabloModel extends AbstractTableModel {

	// tablo başlıklarını tutuan bir dizi
	private final String[] columnNames = { "ID", "Ürün Adı", "Kategori", "Stok", "Fiyat" , "Satılan Adet" };

	// tabloya eklenecek ürünleri saklayan bir liste
	private List<UrunSinifi> urunler;

	// constructor: ürün listesini alır ve tablo modelini başlatır
	public UrunTabloModel(List<UrunSinifi> urunler) {
		this.urunler = urunler;
	}

	// Tablonun kaç satır olacağını belirler(Ürün sayısı kadar)
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return urunler.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		UrunSinifi urun = urunler.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return urun.getId();
		case 1:
			return urun.getName();
		case 2:
			return urun.getCategory();
		case 3:
			return urun.getStock();
		case 4:
			return urun.getPrice();
		case 5:
			return urun.getSatilanMiktar();
		default:
			return null;
		}
	}

// sütun isimlerini döndürür
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

}
