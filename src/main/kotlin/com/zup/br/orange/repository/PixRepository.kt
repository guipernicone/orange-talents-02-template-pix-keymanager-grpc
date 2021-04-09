package com.zup.br.orange.repository

import com.zup.br.orange.AccountType
import com.zup.br.orange.entity.pix.Pix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface PixRepository: CrudRepository<Pix,Long > {
    fun existsByPixValue(pixValue: String?) : Boolean
}