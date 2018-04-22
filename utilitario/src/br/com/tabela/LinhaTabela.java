package br.com.tabela;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

public class LinhaTabela extends Linha {

	public LinhaTabela(Tabela tabela) {
		super(tabela);
	}

	public void desenhar(int[] xs, Graphics g) {
		desenhar(g);
		tabela.desenhar(g);
	}

	@Override
	public void configL(FontMetrics fm) {
		tabela.configL(fm);
		largura = tabela.largura + Constantes.MARGEM_DIREITA_LINHA_TABELA;
	}

	@Override
	public void configA() {
		tabela.configA();
		altura = tabela.altura + Constantes.MARGEM_SUPERIOR_LINHA_TABELA;
	}

	@Override
	public void configY(AtomicInteger acumulador) {
		y = acumulador.getAndAdd(altura);
		tabela.configY(new AtomicInteger(y + Constantes.MARGEM_SUPERIOR_LINHA_TABELA / 2));
	}

	@Override
	public void configX(int tab) {
		tabela.configX(tab + Constantes.MARGEM_DIREITA_LINHA_TABELA / 2);
		x = tab;
	}

	@Override
	public int getL(int i, FontMetrics fm) {
		return 0;
	}
}