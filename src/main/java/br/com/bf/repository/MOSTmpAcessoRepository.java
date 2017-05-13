package br.com.bf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.bf.entity.Empresa;
import br.com.bf.entity.MOSTmpAcesso;
import br.com.bf.wrapper.MosConfiguracaoWrapper;

public interface MOSTmpAcessoRepository extends JpaRepository<MOSTmpAcesso, Long> {
	
	@Query("select new br.com.bf.wrapper.MosConfiguracaoWrapper(b.id, a.empresa.cnpj, STR(a.empresa.endereco.cidade.estado.codigoIbge), b.certificado, b.setup, a.ambiente) from MOSTmpAcesso a join a.empresa.mosConfiguracoes b where a.ca = :ca")
	public MosConfiguracaoWrapper buscarConfiguracao(@Param("ca") String ca);
	
	@Query("select b from MOSTmpAcesso a join a.empresa b where a.ca = :ca and b.cnpj = :cnpj")
	public Empresa buscarPorCA(@Param("ca") String ca, @Param("cnpj") String cnpj);
	
	@Query("select count(*) > 0 from MOSTmpAcesso a where a.ca = :ca and a.empresa.cnpj = :cnpj")
	public boolean buscarPorCAeCNPJ(@Param("ca") String ca, @Param("cnpj") String cnpj);
}
