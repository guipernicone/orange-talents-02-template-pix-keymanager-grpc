package com.zup.br.orange.entity.pix

import com.zup.br.orange.AccountType
import com.zup.br.orange.KeyType
import java.util.*
import javax.persistence.*

@Entity
class Pix(
    var clientId: String,
    @Enumerated(EnumType.STRING) var accountType: AccountType,
    @Enumerated(EnumType.STRING) var pixType: KeyType,
    var pixValue: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var pix: Long? = null
}