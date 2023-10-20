package com.example.coursehub

import com.example.coursehub.network.UserService
import com.example.coursehub.network.sendCreateUserData
import com.example.coursehub.users.User
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun sendCreateUserDataSuccessfulResponse() = runBlocking {

        val email = "test1@example.com"
        val fullName = "Test User"
        val username = "testuser1"
        val password = "password123"
        val user = User(username, email, fullName, password)

        val userService = Mockito.mock(UserService::class.java)


        val successfulResponse = User(username,email,fullName,null)
        Mockito.`when`(userService.createUser(user)).thenReturn(successfulResponse)


        val result = sendCreateUserData(email, fullName, username, password)

        assertTrue(result.isSuccess)
    }
}