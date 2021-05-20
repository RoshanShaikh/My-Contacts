package com.RoshanShaikh.mycontacts.model

class Contact() {
    var id = 0
    var name: String = ""
    var phoneNumber: String = ""

    /** constructor for Contact Object
     * @param name - name of the contact
     * @param phoneNumber - contactNumber
     */
    constructor(name: String, phoneNumber: String) : this() {
        this.name = name
        this.phoneNumber = phoneNumber
    }

    /** constructor for Contact Object
     * @param id - id in database
     * @param name - name of the contact
     * @param phoneNumber - contactNumber
     */
    constructor(id: Int, name: String, phoneNumber: String) : this() {
        this.id = id
        this.name = name
        this.phoneNumber = phoneNumber
    }

}