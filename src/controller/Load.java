package controller;

import java.io.*;
import java.util.*;

public class Load {
	public static ArrayList<String> main (String[] args) throws IOException{
		ArrayList<String> resultado = new ArrayList<String>();
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader("partida_salva.txt")));
			while (s.hasNext()) {
				// System.out.println(s.next());
				resultado.add(s.next());
			}
		}
		finally {
			if (s != null) {
				s.close();
			}
			else {
				resultado = null;
			}
		}
		return resultado;
	}
}