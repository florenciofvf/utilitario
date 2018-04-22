package br.com.utilitario.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.utilitario.util.Constantes;
import br.com.utilitario.util.Util;

public class Cabecalho extends View {
	private final String string;

	public Cabecalho(String string) {
		this.string = string;
	}

	@Override
	public void desenhar(Graphics g) {
		g.setColor(Constantes.COR_CABECALHO);
		g.fillRect(x, y, largura, altura);

		g.setColor(Constantes.COR_FONTE);
		g.drawString(string, xFonte, yFonte);
	}

	@Override
	public void configL(FontMetrics fm) {
		largura = Util.getLargura(string, fm);
	}

	@Override
	public void configA() {
		altura = Constantes.ALTURA_CABECALHO;
	}

	@Override
	public void configY(AtomicInteger acumulador) {
		y = acumulador.getAndAdd(altura);
		yFonte = y + altura + Constantes.DELTA_Y_FONTE;
	}

	@Override
	public void configX(int tab) {
		x = tab;
		xFonte = x + Constantes.DELTA_X_FONTE;
	}
}