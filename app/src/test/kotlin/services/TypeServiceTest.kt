package net.unotec.app.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeServiceTest {


    private val typeService = TypeService()

    @Test
    fun should_transfer_to_big_int() {
        //Arrange on simule une valeur de la taille d'un BigInt avec le code correspondant au BigInt
        val bigIntString = "9223372036854775807"
        val bigIntCode = -5

        //Act : on fait la conversion
        val result = typeService.convert(bigIntString, bigIntCode)

        //Assert : on doit retrouver la même valeur
        assertEquals(9223372036854775807, result)
    }

    @Test
    fun should_return_null_when_impossible_conversion() {
        //Assert : on simule une valeur impossible à convertir en Int avec le code du type Int
        val impossibleInt = "bonjour"
        val intCode = 4

        //Act : on tente la conversion
        val result = typeService.convert(impossibleInt, intCode)

        //Assert : la méthode doit retourner un null
        assertEquals(null, result)
    }
}