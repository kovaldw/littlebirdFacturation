package com.ecommerce.microcommerce.extensions

const val AUTH_LOGIN_URL: String = "/api/authenticate"

const val JWT_SECRET = "TOKENSECRETDOITETRESUPERIEURA512BITSOHQUECESTLONGDEPLUSILNECOMPTEPASLESESPACESOHLALALALALA"
const val JWT_EXPIRE_IN = 604800000

// JWT token defaults
const val TOKEN_HEADER = "Authorization"
const val TOKEN_PREFIX = "Bearer "
const val TOKEN_TYPE = "JWT"
const val TOKEN_ISSUER = "secure-api"
const val TOKEN_AUDIENCE = "secure-app"