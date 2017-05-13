$(function() {

	// Manifesta as notas fiscais selecionadas
	$('.js-manifestar').on('click', function(e) {
		e.preventDefault();
		e.stopPropagation();

		// obtem todas as nfe selecionadas
		var listaNFe = $('.m-tb-nfe tbody').find('input:checkbox:checked').map(function() {
			return this.id;
		}).get();

		if (listaNFe.length <= 0) {
			console.log('Nenhuma nota fiscal selecionada para manifestação');
			return;
		}

		// obtem acesso, e cria xml para manifestacao do destinatario, com base nas nfe selecionadas
		ajaxAcessoManifestoDestinatario(listaNFe).done(function(ca) {

			// se o CA foi retornado corretamente
			if (ca) {

				// invoca MOS para realizar o processamento dos eventos
				callDesktopMOS(ca);

				ajaxManifestoDestinatario(ca).done(function(listaChNFe) {
					console.log(listaChNFe)
					atualizarTabelaDistDFe(listaChNFe);
				}).fail(function(jqXHR, textStatus) {
					console.log('Falha ao manifestar as notas fiscais: ' + textStatus);
				});

			} else {
				console.log("CA não foi criado para a manifestação do destinatário");
			}

		}).fail(function(jqXHR, textStatus) {
			console.log('Falha ao obter acesso a manifestação do destinatário: ' + textStatus);
		});

	});

	$('.js-selecionar-todos').on('click', function(e) {

		var checkMarcarTodos = $(e.target);

		if (checkMarcarTodos.prop('checked')) {
			$(e.target).parents('table').find('tbody').find('input:checkbox').prop('checked', 'checked');
		} else {
			$(e.target).parents('table').find('tbody').find('input:checkbox').prop('checked', '');
		}

	});

	$('.js-selecionar-linha').on('click', function(e) {

		var linhas = $(e.target).parents('table').find('tbody');
		var checkbox = $(e.target).parent().children().find("input:checkbox");

		if (checkbox.prop('checked'))
			checkbox.prop('checked', '');
		else
			checkbox.prop('checked', 'checked');

		// pega quantidade total de linhas
		var quantidadeLinhas = linhas.find('input:checkbox');

		// pega quantidade de linhas marcadas
		var quantidadeLinhasMarcadas = linhas.find('input:checkbox:checked');

		// TODO: para melhorar desempenho, criar variavel referenciado o elemento 'selecionar todos'
		// se o número de linhas selecionadas = quantidade de linhas, então será necessário marcar a 'linha geral'
		if (quantidadeLinhasMarcadas.length >= quantidadeLinhas.length)
			$('.js-selecionar-todos').prop('checked', 'checked');
		else
			$('.js-selecionar-todos').prop('checked', '');

	});

	function ajaxAcessoManifestoDestinatario(listaNFe) {

		return $.ajax({
			method : 'POST',
			url : '/acesso/nfe/manifesto-dest/' + listaNFe,
			dataType : 'text'
		});
	}

	function ajaxManifestoDestinatario(ca) {

		return $.ajax({
			method : 'GET',
			url : '/nfe/manifesto-dest/' + ca,
			dataType : 'json'
		});
	}

	function atualizarTabelaDistDFe(listaChNFe) {

		$.each(listaChNFe, function(id, ret) {
			console.log(ret);

			var checkbox = $('#' + ret.chNFe);
			var trNFe = checkbox.parents('tr');

			checkbox.remove();

			if (ret.tipo == '1') {
				trNFe.addClass('tr-success');
				trNFe.find('td:first-child').append('<i class="material-icons left">check</i>');
			} else {
				trNFe.addClass('tr-error');
				trNFe.find('td:first-child').append('<i class="material-icons left">clear</i>');
			}
		});

	}

});
