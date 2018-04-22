package br.com.utilitario.util;

import java.awt.FontMetrics;

public class Util {

	private Util() {
	}

	public static int getLargura(String string, FontMetrics fm) {
		return fm.stringWidth(string) + Constantes.DELTA_LARGURA_FONTE;
	}
}