$(function() {
	
	$('.js-config-mos').on('click', function(e) {
		e.preventDefault();

		$('.progress').removeClass('hide');

		// obtem CA, para configurar o MOS
		ajaxConfigurarMOS().done(function(ca) {

			$('.progress').addClass('hide');

			if (ca)
				callDesktopMOS(ca);
			else
				console.log('CA não foi criado, ao configurar MOS');

		}).fail(function(jqXHR, textStatus) {
			console.log('Falha ao criar CA para configuração do MOS. ' + textStatus);
		});
	});

	$('.js-cons-dfe').on('click', function(e) {

		$('.progress').removeClass('hide');

		// realiza requisição ajax, para gerar a CA e os eventos
		// de requisição
		// para a aplicação desktop
		ajaxConsultaNfeDFe().done(function(ca) {

			$('.progress').addClass('hide');

			// se o CA foi retornado, então
			// execute a aplicação desktop, para
			// processar os eventos de
			// requisição
			if (ca) {

				// invoca MOS para processar os eventos
				callDesktopMOS(ca);

				// realiza requisição ajax, para
				// verificar se encontrou os
				// eventos retornados por CA
				ajaxChecarEventosRetornados(ca).done(function(data) {

					if (data === 'true') {
						window.location.href = $('.js-cons-dfe').attr('href') + ca;
					} else {
						console.log('Não encontrou eventos retornados!!');
					}

				}).fail(function(jqXHR, textStatus) {
					console.log('Falha ao realizar requisição para verificar se encontrou os eventos retornados por ca [' + textStatus + ']');
				});

			} else {
				console.log('ca ' + ca + ' não encontrado!!!');
			}

		}).fail(function(jqXHR, textStatus) {
			$('.progress').addClass('hide');
			console.log('Falha ao requisitar a consulta de nfe/dfe [' + textStatus + ']');
		});

		return false;

	});

	function callDesktopMOS(ca) {
		var w = (window.parent) ? window.parent : window;
		w.location.assign('testJava:ca=' + ca);
	}

	function ajaxConfigurarMOS() {

		return $.ajax({
			method : 'POST',
			url : '/mos/configurar',
			dataType : 'text'
		});
	}

	function ajaxConsultaNfeDFe() {

		return $.ajax({
			method : 'POST',
			url : '/acesso/nfe/consulta/dfe',
			dataType : 'text'
		});
	}

	function ajaxChecarEventosRetornados(ca) {

		return $.ajax({
			method : 'GET',
			url : '/nfe/checar/dfe/' + ca,
			dataType : 'text'
		});
	}

});