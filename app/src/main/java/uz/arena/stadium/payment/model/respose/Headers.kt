package uz.arena.stadium.payment.model.respose

import retrofit2.http.Field

data class Headers(
    val accept: String,
    @Field("accept-encoding")
    val accept_encoding: String,
//    val cdn-loop: String,
//    val cf-connecting-ip: String,
//    val cf-ipcountry: String,
//    val cf-ray: String,
//    val cf-visitor: String,
    val connection: String,
//    val content-length: String,
//    val content-type: String,
    val host: String,
//    val postman-token: String,
//    val user-agent: String,
//    val x-forwarded-for: String,
//    val x-forwarded-host: String,
//    val x-forwarded-port: String,
//    val x-forwarded-proto: String,
//    val x-real-ip: String
)