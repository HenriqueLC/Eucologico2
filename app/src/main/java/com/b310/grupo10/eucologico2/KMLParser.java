package com.b310.grupo10.eucologico2;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.google.android.maps.GeoPoint;

//Essa classe parseia uma parte do arquivo KML
//preocupando-se apenas com os elementos <coordinate>
//do arquivo.
public class KMLParser extends DefaultHandler {
	private List<Place> coords = new ArrayList<Place>();
	private boolean cs = false;
	private boolean index = false;
	private boolean description = false;
	private boolean id = false;
	private Place instance;

	public List<Place> getData() {
		return coords;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);

		if (localName.equals("local")) {
			if (instance.GetDescription() == null)
				Log.d("Descr", "NULL");
			if (instance.GetIndex() == null)
				Log.d("Nome", "NULL");
			if (instance.GetGeoPoint() == null)
				Log.d("GeoPoint", "NULL");
			coords.add(instance);
		}

		if (localName.equals("tipo")) {
			description = false;
		}

		if (localName.equals("nome")) {
			index = false;
		}

		if (localName.equals("coord")) {
			cs = false;
		}
		
		if (localName.equals("id")) {
			id = false;
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);

		if (localName.equals("local"))
			instance = new Place();

		if (localName.equals("tipo")) {
			description = true;
		}

		if (localName.equals("nome")) {
			index = true;
		}

		if (localName.equals("coord")) {
			cs = true;
		}

		if (localName.equals("id")) {
			id = true;
		}
	}

	private String ToText(String types) {
		String t[] = types.split(",");
		String text = "";
		int rec[] = new int[t.length];
		int i = 0;

		for (String st : t) {
			rec[i] = Integer.valueOf(st);
			i++;
			
			switch (Integer.valueOf(st)) {
			// [0] Lixo reciclavel comum
			case 0:
				text += "Descarte de lixo reciclavel\n";
				break;
			// [1] Roupas
			case 1:
				text += "Doacao de roupas\n";
				break;
			// [2] Equipamentos eletronicos
			case 2:
				text += "Descarte de equipamentos eletronicos\n";
				break;
			// [3] Cartuchos de tinta e tonners
			case 3:
				text += "Descarte de cartuchos de tinta/tonners\n";
				break;
			// [4] Pilhas e baterias
			case 4:
				text += "Descarte de pilhas e baterias\n";
				break;
			// [5] Material escolar
			case 5:
				text += "Doacao de material escolar\n";
				break;
			// [6] Produtos químicos
			case 6:
				text += "Descarte de produtos químicos\n";
				break;
			// [7] Lixo hospitalar / infectante
			case 7:
				text += "Descarte de lixo hospitalar\n";
				break;
			// [8] L�mpadas
			case 8:
				text += "Descarte de lampadas\n";
				break;
			// [9] Lixo toxico
			case 9:
				text += "Descarte de lixo tóxico\n";
				break;
			case 10:
				text += "Descarte de lixo biológico\n";
				break;
			case 11:
				text += "Descarte de lixo radioativo\n";
				break;
			case 12:
				text += "Descarte de resíduos de construção civil\n";
				break;
			}
		}
		text.trim();
		instance.SetRecycleList(rec);
		return text;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);

		Pattern pattern;
		Matcher matcher;

		if (index == true) {
			instance.SetIndex((new String(ch, start, length)));
		}
		
		if (id == true) {
			instance.SetID(Integer.valueOf((new String(ch, start, length))));
		}

		if (description == true) {
			instance.SetDescription(ToText(new String(ch, start, length)));
		}

		if (cs) {
			String str = new String(ch, start, length);
			str = str.trim();

			// regex feio para validar, visto que o parser do java
			// mostrou alguns bugs estranhos...
			pattern = Pattern.compile("(-?[0-9]{1,}.?[0-9]{1,},?){2}");
			matcher = pattern.matcher(str);

			// Validar o que esta tentando parsear
			try {
				double lat = 0;
				double lon = 0;
				while (matcher.find()) {
					String[] cds = matcher.group().split(",");
					lat = Double.valueOf(cds[0]);
					lon = Double.valueOf(cds[1]);

					LatLng point = new LatLng((double) (lat),
							(double) (lon));
					instance.SetGeoPoint(point);
				}
			} catch (IllegalStateException e) {
				Log.e("Regex Error", e.getMessage());
			} catch (NumberFormatException e) {
				Log.e("DoubleParse Error", e.getMessage());
			}
		}

	}

}
