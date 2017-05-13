package br.com.bf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.bf.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
	
	@Query("from Empresa a where a.cnpj = :cnpj")
	public Empresa buscarPorCNPJ(@Param("cnpj") String cnpj);
}
