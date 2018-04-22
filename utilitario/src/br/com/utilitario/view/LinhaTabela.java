package br.com.utilitario.view;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.utilitario.util.Constantes;

public class LinhaTabela extends Linha {

	public LinhaTabela(Tabela tabela) {
		super(tabela);
	}

	public void desenhar(int[] xs, Graphics g) {
		desenhar(g);
		tabela.desenhar(g);
	}

	@Override
	public void calcularLargura(FontMetrics fm) {
		tabela.calcularLargura(fm);
		largura = tabela.largura;
		largura += Constantes.MARGEM_DIREITA_LINHA_TABELA;
	}

	@Override
	public void calcularAltura() {
		tabela.calcularAltura();
		altura = tabela.altura;
		altura += Constantes.MARGEM_SUPERIOR_LINHA_TABELA;
	}

	@Override
	public void calcularY(AtomicInteger acumulador) {
		y = acumulador.getAndAdd(altura);
		tabela.calcularY(new AtomicInteger(y + Constantes.MARGEM_SUPERIOR_LINHA_TABELA / 2));
	}

	@Override
	public void calcularX(int tab) {
		tabela.calcularX(tab + Constantes.MARGEM_DIREITA_LINHA_TABELA / 2);
		x = tab;
	}

	@Override
	public int getLargura(int i, FontMetrics fm) {
		return 0;
	}
}