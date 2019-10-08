package com.example.travcesadminpanelapp.data.remote.travces.model.data

import java.io.Serializable

class GetDriverData : Serializable {
  var id: String = ""
  var fname: String = ""
  val lname: String = ""
  var email: String = ""
  var phone: String = ""
  var address: String = ""
  var cnic: String = ""
  var is_online: String = ""
}

