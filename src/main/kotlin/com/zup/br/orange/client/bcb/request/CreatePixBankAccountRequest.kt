package com.zup.br.orange.client.bcb.request

data class CreatePixBankAccountRequest (
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: String
)