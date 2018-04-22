package br.com.consultas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;
import java.util.Vector;

import br.com.consultas.Campo;
import br.com.consultas.Referencia;
import br.com.consultas.Tabela;
import br.com.consultas.Tabelas;
import br.com.consultas.visao.dialog.ProgressoDialog;
import br.com.consultas.visao.modelo.ModeloRSMD;
import br.com.tabela.Cabecalho;
import br.com.tabela.Linha;
import br.com.tabela.LinhaCelula;
import br.com.tabela.LinhaColuna;

public class Persistencia {
	private static Connection conn;

	public static Connection getConnection() throws Exception {
		if (conn == null || conn.isClosed()) {
			Class.forName(Util.getStringConfig("driver"));

			String url = Util.getStringConfig("url");
			String usr = Util.getStringConfig("login");
			String psw = Util.getStringConfig("senha");

			conn = DriverManager.getConnection(url, usr, psw);
		}

		return conn;
	}

	public static void close() throws Exception {
		if (conn != null && !conn.isClosed()) {
			conn.close();
		}
	}

	public static int executeUpdate(String string) throws Exception {
		Connection conn = getConnection();

		PreparedStatement psmt = conn.prepareStatement(string);

		int i = psmt.executeUpdate();

		psmt.close();

		return i;
	}

	public static void atualizarTotalRegistros(List<Tabela> tabelas, ProgressoDialog progresso) throws Exception {
		Connection conn = getConnection();

		int i = 0;

		for (Tabela tab : tabelas) {
			PreparedStatement psmt = conn.prepareStatement(tab.getConsultaCount());
			ResultSet rs = psmt.executeQuery();
			rs.next();

			tab.setTotalRegistros(rs.getInt("total"));

			progresso.atualizar(++i);

			rs.close();
			psmt.close();
		}
	}

	public static Vector<Object[]> getRegistrosAgrupados(Referencia ref, Tabelas tabelas, Campo campo)
			throws Exception {
		Vector<Object[]> vector = new Vector<Object[]>();
		Connection conn = getConnection();

		String consulta = campo == null ? ref.getConsultaGroupByCount(tabelas)
				: ref.getConsultaAgregada(tabelas, campo);

		if (Util.getBooleanConfig("consultas.area_transferencia")) {
			Util.setContentTransfered(consulta);
		}

		PreparedStatement psmt = conn.prepareStatement(consulta);
		ResultSet rs = psmt.executeQuery();

		while (rs.next()) {
			Object[] array = { rs.getString(1), rs.getString(2) };
			vector.add(array);
		}

		rs.close();
		psmt.close();

		return vector;
	}

	public static Vector<Object[]> getRegistrosAgrupados(Referencia ref, Referencia pai, Tabelas tabelas, Campo campo)
			throws Exception {
		Vector<Object[]> vector = new Vector<Object[]>();
		Connection conn = getConnection();

		String consulta = ref.getConsultaAgregada(pai, tabelas, campo);

		if (Util.getBooleanConfig("consultas.area_transferencia")) {
			Util.setContentTransfered(consulta);
		}

		PreparedStatement psmt = conn.prepareStatement(consulta);
		ResultSet rs = psmt.executeQuery();

		while (rs.next()) {
			Object[] array = { rs.getString(1), rs.getString(2) };
			vector.add(array);
		}

		rs.close();
		psmt.close();

		return vector;
	}

	public static Vector<Object[]> getRegistrosAgrupadosCOUNT(Referencia ref, Referencia pai, Tabelas tabelas,
			Campo campo) throws Exception {
		Vector<Object[]> vector = new Vector<Object[]>();
		Connection conn = getConnection();

		String consulta = ref.getConsultaGroupByCount(pai, tabelas, campo);

		if (Util.getBooleanConfig("consultas.area_transferencia")) {
			Util.setContentTransfered(consulta);
		}

		PreparedStatement psmt = conn.prepareStatement(consulta);
		ResultSet rs = psmt.executeQuery();

		while (rs.next()) {
			Object[] array = { rs.getString(1), rs.getString(2) };
			vector.add(array);
		}

		rs.close();
		psmt.close();

		return vector;
	}

	public static Vector<Vector<String>> getDados(ResultSet rs, int qtdColunas) throws Exception {
		Vector<Vector<String>> dados = new Vector<>();

		while (rs.next()) {
			Vector<String> registro = new Vector<>();

			for (int i = 0; i < qtdColunas; i++) {
				registro.add("" + rs.getString(i + 1));
			}

			dados.add(registro);
		}

		return dados;
	}

	public static br.com.tabela.Tabela criarTabela(Referencia selecionado, Tabelas tabelas) throws Exception {
		SQL sql = Util.criarSQL(selecionado, tabelas, null);

		Connection conn = Persistencia.getConnection();
		PreparedStatement psmt = conn.prepareStatement(Util.getSQL(sql.dados));
		ResultSet rs = psmt.executeQuery();

		br.com.tabela.Tabela tabela = coletar(conn, rs, selecionado.getTabela(tabelas).getNome());

		rs.close();

		return tabela;
	}

	private static br.com.tabela.Tabela coletar(Connection conn, ResultSet rs, String nomeTabela) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();

		ModeloRSMD modeloRSMD = new ModeloRSMD(rsmd);
		Vector<String> nomeColunas = modeloRSMD.getNomeColunas();
		String[] colunas = new String[nomeColunas.size()];
		nomeColunas.copyInto(colunas);

		Vector<Vector<String>> dados = Persistencia.getDados(rs, rsmd.getColumnCount());
		Linha[] linhas = new Linha[dados.size()];

		int indice = 0;

		for (Vector<String> vector : dados) {
			String[] registro = new String[vector.size()];
			vector.copyInto(registro);
			linhas[indice] = new LinhaCelula(registro);
			indice++;
		}

		return new br.com.tabela.Tabela(new Cabecalho(nomeTabela + " - " + dados.size()), new LinhaColuna(colunas), linhas);
	}
}