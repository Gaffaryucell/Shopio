package com.example.ecommerceapp.model

class UserModel {
    var userId: String? = null
    var username: String? = null
    var email: String? = null
    var image : String? = null

    constructor()

    constructor(userId: String? = null, username: String? = null, email: String? = null,image : String? = null) : this() {
        this.userId = userId
        this.username = username
        this.email = email
        this.image = image
    }
}