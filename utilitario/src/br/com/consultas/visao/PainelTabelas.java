package br.com.consultas.visao;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.tree.TreePath;

import br.com.consultas.Referencia;
import br.com.consultas.Tabela;
import br.com.consultas.util.Icones;
import br.com.consultas.util.Persistencia;
import br.com.consultas.util.SQL;
import br.com.consultas.util.TreeCellRenderer;
import br.com.consultas.util.Util;
import br.com.consultas.visao.comp.Arvore;
import br.com.consultas.visao.comp.Button;
import br.com.consultas.visao.comp.CheckBox;
import br.com.consultas.visao.comp.PanelBorderLayout;
import br.com.consultas.visao.comp.PanelLeft;
import br.com.consultas.visao.comp.ScrollPane;
import br.com.consultas.visao.comp.SplitPane;
import br.com.consultas.visao.dialog.CampoDialog;
import br.com.consultas.visao.dialog.DadosDialog;
import br.com.consultas.visao.modelo.ModeloArvore;
import br.com.tabela.Painel;

public class PainelTabelas extends PanelBorderLayout {
	private static final long serialVersionUID = 1L;

	private final CheckBox chkRaizVisivel = new CheckBox("label.raiz_visivel", "tabelas.raiz_visivel");
	private final CheckBox chkLinhaRaiz = new CheckBox("label.raiz_linha", "tabelas.raiz_linha");
	private final Button buttonAtualizar = new Button("label.atualizar", Icones.ATUALIZAR);
	protected final SplitPane splitPane = new SplitPane(SplitPane.HORIZONTAL_SPLIT);
	public static final byte PAINEL_COM_REGISTROS = 0;
	public static final byte PAINEL_SEM_REGISTROS = 1;
	public static final byte PAINEL_DESTAQUES = 2;
	public static final byte PAINEL_TABELAS = 3;
	private final Popup popup = new Popup();
	private Painel painel = new Painel();
	private final Formulario formulario;
	private Referencia selecionado;
	private final Arvore arvore;
	private final byte tipo;

	public PainelTabelas(Formulario formulario, byte tipo) {
		this.formulario = formulario;
		this.tipo = tipo;

		List<Referencia> referencias = Util.criarReferencias(formulario.getTabelas().getTabelas());
		aplicarFiltros(referencias);

		arvore = new Arvore(new ModeloArvore(referencias, Util.getString("label.tabelas")));
		arvore.setCellRenderer(new TreeCellRenderer());
		arvore.addMouseListener(new OuvinteArvore());

		splitPane.setLeftComponent(new ScrollPane(arvore));
		splitPane.setRightComponent(new ScrollPane(painel));

		PanelLeft panelNorte = new PanelLeft();

		if (Util.getBooleanConfig("config_arvore")) {
			panelNorte.adicionar(chkRaizVisivel, chkLinhaRaiz);
		}

		panelNorte.adicionar(buttonAtualizar);

		add(BorderLayout.NORTH, panelNorte);
		add(BorderLayout.CENTER, splitPane);

		cfg();
	}

	private void aplicarFiltros(List<Referencia> referencias) {
		if (PAINEL_DESTAQUES == tipo) {
			Util.somenteDestaques(referencias, formulario.getTabelas());

		} else if (PAINEL_COM_REGISTROS == tipo) {
			Util.somenteComRegistros(referencias);

		} else if (PAINEL_SEM_REGISTROS == tipo) {
			Util.somenteSemRegistros(referencias);
		}

		Util.ordenar(referencias);
	}

	private void itemRegistrosDialogoLimpo(Referencia selecionado) {
		Tabela tabela = Util.limparID(selecionado, formulario);
		formulario.atualizarCampoIDForm(tabela);
		registros(selecionado, true);
	}

	private void itemRegistrosMemoriaLimpo() {
		Tabela tabela = Util.limparID(selecionado, formulario);
		formulario.atualizarCampoIDForm(tabela);
		registros(selecionado, false);
	}

	private void cfg() {
		popup.dialogoMeuSQL();
		popup.addSeparator();
		popup.memoriaMeuSQL();
		popup.addSeparator();
		popup.campos();
		popup.addSeparator();
		popup.dml();

		popup.itemRegistrosDialogoLimpo.addActionListener(e -> itemRegistrosDialogoLimpo(selecionado));

		popup.itemRegistrosDialogo.addActionListener(e -> registros(selecionado, true));

		popup.itemRegistrosMemoriaLimpo.addActionListener(e -> itemRegistrosMemoriaLimpo());

		popup.itemRegistrosMemoria.addActionListener(e -> registros(selecionado, false));

		popup.itemPesquisaSelecionados
				.addActionListener(e -> Util.pesquisaSelecionadosMemoria(selecionado, formulario.getTabelas()));

		popup.itemLimparCampos.addActionListener(e -> {
			Tabela tabela = selecionado.getTabela(formulario.getTabelas());
			tabela.limparCampos();
			formulario.atualizarCampoIDForm(tabela);
		});

		popup.itemLimparId.addActionListener(e -> {
			Tabela tabela = selecionado.getTabela(formulario.getTabelas());
			tabela.limparID();
			formulario.atualizarCampoIDForm(tabela);
		});

		popup.itemCampos
				.addActionListener(e -> new CampoDialog(formulario, selecionado.getTabela(formulario.getTabelas())));

		popup.itemUpdate
				.addActionListener(e -> formulario.textArea.setText(selecionado.gerarUpdate(formulario.getTabelas())));

		popup.itemDelete
				.addActionListener(e -> formulario.textArea.setText(selecionado.gerarDelete(formulario.getTabelas())));

		chkRaizVisivel.addActionListener(e -> arvore.setRootVisible(chkRaizVisivel.isSelected()));

		chkLinhaRaiz.addActionListener(e -> arvore.setShowsRootHandles(chkLinhaRaiz.isSelected()));

		splitPane.addPropertyChangeListener(evt -> {
			SplitPane splitPane = (SplitPane) evt.getSource();
			String propertyName = evt.getPropertyName();

			if (SplitPane.DIVIDER_LOCATION_PROPERTY.equals(propertyName)) {
				formulario.divisao(splitPane.getDividerLocation());
			}
		});

		buttonAtualizar.addActionListener(e -> {
			List<Referencia> referencias = Util.criarReferencias(formulario.getTabelas().getTabelas());
			aplicarFiltros(referencias);
			arvore.setModel(new ModeloArvore(referencias, Util.getString("label.tabelas")));
		});
	}

	private void registros(Referencia selecionado, boolean abrirDialogo) {
		pesquisa(selecionado, abrirDialogo, null);
	}

	private void pesquisa(Referencia selecionado, boolean abrirDialogo, String aliasTemp) {
		SQL sql = Util.criarSQL(selecionado, formulario.getTabelas(), aliasTemp);

		Tabela tabela = selecionado.getTabela(formulario.getTabelas());
		formulario.textArea.setText(sql.dados);
		Util.setContentTransfered(sql.dados);

		if (abrirDialogo) {
			try {
				new DadosDialog(formulario, selecionado, tabela, false, null, aliasTemp);
			} catch (Exception e) {
				String msg = Util.getStackTrace(getClass().getName() + ".texto()", e);
				Util.mensagem(this, msg);
			}
		}
	}

	void windowOpened() {
		arvore.setShowsRootHandles(chkLinhaRaiz.isSelected());
		arvore.setRootVisible(chkRaizVisivel.isSelected());
		splitPane.setDividerLocation(Util.DIVISAO2);
		// table.ajustar(getGraphics());
	}

	private class OuvinteArvore extends MouseAdapter {
		Referencia ultimoSelecionado;

		@Override
		public void mouseClicked(MouseEvent e) {
			TreePath path = arvore.getSelectionPath();

			if (path == null) {
				return;
			}

			if (path.getLastPathComponent() instanceof Referencia) {
				selecionado = (Referencia) path.getLastPathComponent();
				if (ultimoSelecionado != selecionado) {
					ultimoSelecionado = selecionado;
					// table.setModel(new ModeloOrdenacao(new
					// ModeloCampo(selecionado.getTabela(formulario.getTabelas()))));
					// table.ajustar(getGraphics(), Util.LARGURA_ICONE_ORDENAR);
					try {
						br.com.tabela.Tabela tabela = Persistencia.criarTabela(selecionado, formulario.getTabelas());
						painel.setTabela(tabela);
						painel.configurar();
						repaint();
					} catch (Exception ex) {
						String msg = Util.getStackTrace(getClass().getName() + ".texto()", ex);
						Util.mensagem(PainelTabelas.this, msg);
					}
				}
			}

			if (e.getClickCount() >= Util.DOIS && ultimoSelecionado != null) {
				itemRegistrosDialogoLimpo(ultimoSelecionado);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			processar(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			processar(e);
		}

		private void processar(MouseEvent e) {
			if (!e.isPopupTrigger()) {
				return;
			}

			TreePath path = arvore.getSelectionPath();
			if (path == null) {
				return;
			}

			if (path.getLastPathComponent() instanceof Referencia) {
				selecionado = (Referencia) path.getLastPathComponent();
				popup.show(arvore, e.getX(), e.getY());
			}
		}
	}
}