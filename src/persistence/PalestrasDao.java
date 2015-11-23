package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Palestra;

public class PalestrasDao {

	private Connection c;
	
	public PalestrasDao() throws SQLException{
		GenericoDao gDao = new GenericoDao();
		c = gDao.getConnection();
	}
	
	public List<Palestra> consultaPalestraPorAluno(String ra) throws SQLException{
		List<Palestra> lista = new ArrayList<Palestra>();
		StringBuffer sql = new StringBuffer();
		sql.append("select al.ra, pal.TITULO, pal.ID   ");
		sql.append("from alunos al   ");
		sql.append("inner join participacao p  ");
		sql.append("on p.ra_aluno = al.ra  ");
		sql.append("inner join palestras pal  ");
		sql.append("on p.id_palestra = pal.ID  ");
		sql.append("where RA = ?  ");
		sql.append("order by TITULO ");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ps.setString(1, ra);
		ResultSet rs = ps.executeQuery();
		while (rs.next()){
			Palestra p = new Palestra();
			p.setRa(rs.getString("ra"));
			p.setTitulo(rs.getString("titulo"));
			p.setCodTitulo(rs.getInt("id"));
			lista.add(p);
		}
		rs.close();
		ps.close();
		return lista;
	}
	
	public String consultaTipoPalestra(int titulo) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("select TIPO ");
		sql.append("from palestras ");
		sql.append("where ID = ? ");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ps.setInt(1, titulo);
		ResultSet rs = ps.executeQuery();
		String tipo = "";
		if (rs.next()){
			tipo = rs.getString("TIPO");
		}
		rs.close();
		ps.close();
		return tipo;
	}
	
	public int consultaAnoPalestra(int titulo) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("select YEAR(DATA) as ano ");
		sql.append("from palestras$ ");
		sql.append("where TITULO = ? ");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ps.setInt(1, titulo);
		ResultSet rs = ps.executeQuery();
		int ano = 0;
		if (rs.next()){
			ano = rs.getInt("ano");
		}
		rs.close();
		ps.close();
		return ano;
	}
	
	public String consultaCursoPalestra(int titulo) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("select CURSO ");
		sql.append("from palestras ");
		sql.append("where ID = ? ");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ps.setInt(1, titulo);
		ResultSet rs = ps.executeQuery();
		String curso = "";
		if (rs.next()){
			curso = rs.getString("curso");
		}
		rs.close();
		ps.close();
		return curso;
	}
	
	public String consultaTituloPalestra(int titulo) throws SQLException{
		StringBuffer sql = new StringBuffer();
		sql.append("select titulo ");
		sql.append("from titulo ");
		sql.append("where codigo = ? ");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ps.setInt(1, titulo);
		ResultSet rs = ps.executeQuery();
		String tit = "";
		if (rs.next()){
			tit = rs.getString("titulo");
		}
		rs.close();
		ps.close();
		return tit;
	}
	
}
