package br.com.bf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.bf.entity.MosTmpEvento;
import br.com.bf.wrapper.MosReqEventoWrapper;

public interface MOSTmpEventoRepository extends JpaRepository<MosTmpEvento, Long> {

	@Query("select new br.com.bf.wrapper.MosReqEventoWrapper(a.id, a.xmlRequisicao, a.evento) from MosTmpEvento a where a.mosAcesso.ca = :ca and a.cnpj = :cnpj")
	public List<MosReqEventoWrapper> getEventosRequisicao(@Param("ca") String ca, @Param("cnpj") String cnpj);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("update MosTmpEvento set xmlRetorno = :xml_retorno, concluido = true where (mosAcesso.ca = :ca) and (cnpj = :cnpj) and (id = :evento_id)")
	public void atualizarEvento(@Param("ca") String ca, @Param("cnpj") String cnpj, @Param("evento_id") Long eventoId,
			@Param("xml_retorno") String xmlRetorno);

	@Query("from MosTmpEvento a where a.mosAcesso.ca = :ca and a.cnpj = :cnpj")
	public List<MosTmpEvento> getEventosRetorno(@Param("ca") String ca, @Param("cnpj") String cnpj);

	@Query("from MosTmpEvento a where a.mosAcesso.ca = :ca and a.concluido = true")
	public List<MosTmpEvento> getEventosRetornadosByCA(@Param("ca") String ca);
	
	@Query("select count(*) from MosTmpEvento a where a.mosAcesso.ca = :ca and a.concluido = true")
	public Long countEventosRetornadosPorCA(@Param("ca") String ca);
}
