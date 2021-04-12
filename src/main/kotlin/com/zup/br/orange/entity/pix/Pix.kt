package com.zup.br.orange.entity.pix

import com.zup.br.orange.AccountType
import com.zup.br.orange.KeyType
import com.zup.br.orange.entity.pix.enum.PixType
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Pix(
    val clientId: String,
    @Enumerated(EnumType.STRING) val accountType: AccountType,
    @Enumerated(EnumType.STRING) val pixType: PixType,
    val pixValue: String,
    val createAt: LocalDateTime
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
}