package com.zup.br.orange.client.itau.response

data class ConsultAccountResponse(
    var tipo: String,
    var instituicao: ConsultAccountInstituicaoResponse,
    var agencia: String,
    var numero: String,
    var titular: ConsultAccountTitularResponse
)