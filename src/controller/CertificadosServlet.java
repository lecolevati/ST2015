package controller;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.GenericoDao;
import persistence.PalestrasDao;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.util.JRLoader;

@WebServlet("/certificados")
public class CertificadosServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CertificadosServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String erro = "";
		String ra = request.getParameter("ra").trim();
		String titulo = request.getParameter("titulo").trim();
		int codTitulo = Integer.parseInt(titulo);
		byte[] bytes = null;
		ServletContext context = getServletContext();

		String fundo = context.getRealPath("/WEB-INF/images/fundo.png");
		String assinatura = "";
		try {
			PalestrasDao pDao = new PalestrasDao();
			String tipo = pDao.consultaTipoPalestra(codTitulo);
			String curso = pDao.consultaCursoPalestra(codTitulo);

			if (curso.contains("COM")) {
				assinatura = context.getRealPath("/WEB-INF/images/asshil.jpg");
			} else {
				if (curso.contains("POL")) {
					assinatura = context
							.getRealPath("/WEB-INF/images/assluc.jpg");
				} else {
					if (curso.contains("LOG")) {
						assinatura = context
								.getRealPath("/WEB-INF/images/assrob.jpg");
					} else {
						if (curso.contains("GES")) {
							assinatura = context
									.getRealPath("/WEB-INF/images/assabel.jpg");
						} else {
							if (curso.contains("AN")) {
								assinatura = context
										.getRealPath("/WEB-INF/images/assand.jpg");
							}
						}
					}
				}
			}

			HashMap<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ra", ra);
			parametros.put("fundo", fundo);
			parametros.put("titulo", codTitulo);
			parametros.put("assinatura", assinatura);

			StringBuffer pathJasper = new StringBuffer();
			pathJasper.append("/WEB-INF/reports/");
			if (tipo.contains("Palestra")) {
				pathJasper.append("certificadopalestrast.jasper");
			} else {
				if (tipo.contains("Exp")) {
					pathJasper.append("certificadoexposicao.jasper");
				} else {
					if (tipo.contains("Mini")) {
						pathJasper.append("certificadominicurso.jasper");
					} else {
						if (tipo.contains("Exi")) {
							pathJasper.append("certificadoexibicao.jasper");
						} else {
							if (tipo.contains("Mesa")) {
								pathJasper.append("certificadomesa.jasper");
							} else {
								throw new JRException(
										"Erro no tipo de certificado");
							}
						}
					}
				}
			}
			JasperReport relatorioJasper = (JasperReport) JRLoader
					.loadObjectFromFile(context.getRealPath(pathJasper
							.toString()));
			bytes = JasperRunManager.runReportToPdf(relatorioJasper,
					parametros, new GenericoDao().getConnection());
		} catch (SQLException | JRException e) {
			erro = e.getMessage();
		} finally {
			if (bytes != null) {
				response.setContentType("application/pdf");
				response.setContentLength(bytes.length);
				ServletOutputStream ouputStream = response.getOutputStream();
				ouputStream.write(bytes, 0, bytes.length);
				ouputStream.flush();
				ouputStream.close();
			} else {
				PrintStream tela = new PrintStream(response.getOutputStream());
				tela.println("<HTML><BODY>");
				tela.println("<script>alert('Sem certificados para ==> RA = '"
						+ ra + "\n" + erro + ");history.back();</script>");
				tela.println("<BR><P>");
				tela.println("</HTML></BODY>");
			}
		}
	}
}
