package com.zup.br.orange.repository

import com.zup.br.orange.AccountType
import com.zup.br.orange.entity.pix.Pix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository
import java.util.*

@Repository
interface PixRepository: CrudRepository<Pix,Long > {

    fun findByIdAndClientId(id: Long?, clientId: String) : Optional<Pix>

    fun findByPixValue(pixValue: String?) : Optional<Pix>

    fun existsByPixValue(pixValue: String?) : Boolean
}