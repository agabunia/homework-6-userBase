package com.example.homework_6_userbase

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.example.homework_6_userbase.databinding.ActivityMainBinding
import java.util.Objects

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var activeUsersNumber: Int = 0
    var deletedUsersNumber: Int = 0
    var usersList: MutableList<User> = mutableListOf()

    data class User(var firstName: String, var lastName: String, var age: Int, var email: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }

    fun setUp(){
        binding.btnAddUser.setOnClickListener {
            val firstName: String = binding.etFirstName.text.toString()
            val lastName: String = binding.etLastName.text.toString()
            val age: Int = binding.etAge.text.toString().toIntOrNull() ?: 0
            val email: String = binding.etEmail.text.toString()

            if(inputsFilled(firstName, lastName, age, email)) {
                if(!userExists(User(firstName, lastName, age, email))) {
                    usersList.add(User(firstName, lastName, age, email))
                    activeUsersNumber++
                    binding.tvActiveUsers.text = "Active Users: $activeUsersNumber"
                    showSuccessMessage("User added successfully")
                } else {
                    showErrorMessage("User already exists")
                }
                clearInputs()
            } else {
                showErrorMessage("All fields must be filled")
            }
        }
        binding.btnRemoveUser.setOnClickListener {
            val firstName: String = binding.etFirstName.text.toString()
            val lastName: String = binding.etLastName.text.toString()
            val age: Int = binding.etAge.text.toString().toIntOrNull() ?: 0
            val email: String = binding.etEmail.text.toString()

            if(inputsFilled(firstName, lastName, age, email)) {
                if(userExists(User(firstName, lastName, age, email))) {
                    usersList.remove(User(firstName, lastName, age, email))
                    activeUsersNumber--
                    deletedUsersNumber++
                    binding.tvActiveUsers.text = "Active Users: $activeUsersNumber"
                    binding.tvDeletedUsers.text = "Deleted Users: $deletedUsersNumber"
                    showSuccessMessage("User deleted successfully")
                } else {
                    showErrorMessage("User does not exists")
                }
                clearInputs()
            } else {
                showErrorMessage("All fields must be filled")
            }
        }
        binding.btnUpdateUser.setOnClickListener {
            val firstName: String = binding.etFirstName.text.toString()
            val lastName: String = binding.etLastName.text.toString()
            val age: Int = binding.etAge.text.toString().toIntOrNull() ?: 0
            val email: String = binding.etEmail.text.toString()

            if(inputsFilled(firstName, lastName, age, email)) {
                val userIndex: Int = findUser(firstName, lastName, email)
                if(userIndex != -1) {
                    usersList[userIndex].apply {
                        this.firstName = firstName
                        this.lastName = lastName
                        this.age = age
                        this.email = email
                    }
                    showSuccessMessage("User updated successfully")
                    clearInputs()
                } else {
                    showErrorMessage("User was not found")
                }
            } else {
                showErrorMessage("All fields must be filled")
            }
        }
    }

    fun inputsFilled(firstName: String, lastName: String, age: Int, email: String): Boolean {
        if(firstName.isNullOrEmpty() || lastName.isNullOrEmpty() || age == 0 || email.isNullOrEmpty()) {
            return false
        }
        return age > 0 && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    fun userExists(user: User): Boolean {
        return usersList.any {
            it.firstName == user.firstName && it.lastName == user.lastName &&
            it.age == user.age && it.email == user.email
        }
    }
    fun showSuccessMessage(message: String) {
        binding.tvSuccessError.setTextColor(Color.parseColor("#4CAF50"))
        binding.tvSuccessError.text = message
    }
    fun showErrorMessage(message: String) {
        binding.tvSuccessError.setTextColor(Color.parseColor("#E91E63"))
        binding.tvSuccessError.text = message
    }
    fun clearInputs() {
        binding.etFirstName.setText("")
        binding.etLastName.setText("")
        binding.etAge.setText("")
        binding.etEmail.setText("")
    }
    fun findUser(firstName: String, lastName: String, email: String): Int {
        return if (usersList.indexOfFirst{ it.firstName == firstName && it.lastName == lastName } != -1 ) {
            usersList.indexOfFirst{
                it.firstName == firstName && it.lastName == lastName
            }
        } else if(usersList.indexOfFirst { it.email == email } != -1) {
            usersList.indexOfFirst {
                it.email == email
            }
        } else {
            -1
        }
    }
}