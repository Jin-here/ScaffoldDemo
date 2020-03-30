package com.vgaw.scaffolddemo.model

data class AuthInfo(var token: String?)

data class UserInfo(var id: String? = null,
                    var token: String? = null,
                    var name: String? = null,
                    var avatar: String? = null)
