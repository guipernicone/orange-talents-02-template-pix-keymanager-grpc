package com.zup.br.orange

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("com.zup.br.orange")
		.start()
}

