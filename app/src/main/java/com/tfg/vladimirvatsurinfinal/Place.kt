package com.tfg.vladimirvatsurinfinal
class Place{
    var name: String? = null
    var url: String? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    // required empty constructor - not needed to add, but needed to read
    constructor() {
    }

    // 1st overload w/all properties passed in
    constructor(name: String, url: String, lat: Double, long: Double) {
        this.name = name
        this.url = url
        this.latitude = lat
        this.longitude = long
    }
}