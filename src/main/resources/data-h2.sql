insert into Estado(id, uf, nome, codigo_ibge) values (1, 'MG', 'Minas Gerais', 31);

insert into Cidade(id, municipio, codigo_ibge, estado_id) values (1, 'Monte Carmelo', 3422, 1);

insert into Endereco(id, logradouro, numero, bairro, cep, cidade_id) values (1, 'Av dos Mundins', 328, 'Centro', '38500-000', 1);

insert into Empresa(id, cnpj, senha, razao_social, nome_fantasia, fone, endereco_id) values (1, '15107846000100', 'teste', 'HP LOGISTICA LTDA ME', 'HP LOGISTICA', '', 1);

insert into mos_configuracoes(id, certificado, empresa_id, setup) values (1, '', 1, false);

-- insert into mos_tmp_acesso(ca, empresa_id, data) values ('34BC248631F79C06AD0D1AFD59A25D47', 1, now());