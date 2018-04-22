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
		g.drawString(string, x + Constantes.DELTA_FONTE, yFonte);
	}

	@Override
	public void calcularLargura(FontMetrics fm) {
		largura = Util.getLargura(string, fm);
	}

	@Override
	public void calcularAltura() {
		altura = Constantes.ALTURA_CABECALHO;
	}

	@Override
	public void calcularY(AtomicInteger acumulador) {
		y = acumulador.getAndAdd(altura);
		yFonte = y + altura;
	}

	@Override
	public void calcularX(int tab) {
		x = tab;
	}
}