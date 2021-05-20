package com.RoshanShaikh.mycontacts.model

class Contact() {
    var id = 0
    var name: String = ""
    var phoneNumber: String = ""

    constructor(name: String, phoneNumber: String) : this() {
        this.name = name
        this.phoneNumber = phoneNumber
    }

    constructor(id: Int, name: String, phoneNumber: String) : this() {
        this.id = id
        this.name = name
        this.phoneNumber = phoneNumber
    }

}