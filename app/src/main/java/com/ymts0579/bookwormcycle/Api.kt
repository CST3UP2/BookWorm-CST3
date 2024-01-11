package com.ymts0579.bookwormcycle


import com.ymts0579.bookwormcycle.model.BooksResponse
import com.ymts0579.bookwormcycle.model.ExchangeResponse
import com.ymts0579.bookwormcycle.model.adbookresponse
import com.ymts0579.fooddonationapp.model.Userresponse
import com.ymts0579.model.model.DefaultResponse
import com.ymts0579.model.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @FormUrlEncoded
    @POST("users.php")
    fun register(
        @Field("name") name:String,
        @Field("mobile")mobile:String,
        @Field("email")email :String,
        @Field("city") city:String,
        @Field("password") password:String,
        @Field("address")address :String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun login(@Field("email") email:String, @Field("password") password:String,
              @Field("condition") condition:String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("users.php")
    fun usersemail(@Field("email") email:String,@Field("condition") condition:String): Call<Userresponse>

    @FormUrlEncoded
    @POST("users.php")
    fun updateusers(
        @Field("name") name:String, @Field("mobile")moblie:String, @Field("password") password:String,
        @Field("address")address :String, @Field("city") city:String,
        @Field("id")id:Int, @Field("condition")condition:String): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Books.php")
    fun Addbooks(
        @Field("name") name:String,
        @Field("author") author:String,
        @Field("description") description :String,
        @Field("email") email:String,
        @Field("status") status:String,
        @Field("city")    city:String,
        @Field("path") path:String,
        @Field("Bookid") Bookid:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("Books.php")
    fun usersbooks( @Field("email") email:String,@Field("condition") condition:String): Call<BooksResponse>

    @FormUrlEncoded
    @POST("Books.php")
    fun searchbooks( @Field("city")  city:String,@Field("condition") condition:String): Call<BooksResponse>

    @FormUrlEncoded
    @POST("Books.php")
    fun searchbookname(  @Field("name") name:String,@Field("city")  city:String,@Field("condition") condition:String): Call<BooksResponse>

    @FormUrlEncoded
    @POST("Books.php")
    fun usersbookselect(  @Field("Bookid") Bookid:String,@Field("condition") condition:String): Call<BooksResponse>


    @FormUrlEncoded
    @POST("Books.php")
    fun deletebooks(

        @Field("id") id: Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("exchange.php")
    fun exchangebooks(
        @Field("name") name:String,
        @Field("author") author:String,
        @Field("bookid") bookid :String,
        @Field("oemail") oemail:String,
        @Field("nemail") nemail:String,
        @Field("path")    path:String,
        @Field("Status") Status:String,
        @Field("date") date:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("exchange.php")
    fun viewexchangebooks(
        @Field("nemail") nemail:String,
        @Field("condition") condition:String,
    ): Call<ExchangeResponse>

    @FormUrlEncoded
    @POST("exchange.php")
    fun viewexchangebooksrequest(
        @Field("oemail") oemail:String,
        @Field("condition") condition:String,
    ): Call<ExchangeResponse>


    @FormUrlEncoded
    @POST("exchange.php")
    fun updateexchangebooks(
        @Field("id")id: Int,
        @Field("Status") Status:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("exchange.php")
    fun updateexchange(
        @Field("id")id: Int,

        @Field("Status") Status:String,
        @Field("condition") condition:String,
        @Field("bookid") bookid :String,
    ): Call<DefaultResponse>


    @GET("getuser.php")
    fun getuser():Call<Userresponse>


    @FormUrlEncoded
    @POST("Adminbooks.php")
    fun Addadbooks(
        @Field("name") name:String,
        @Field("author") author:String,
        @Field("description") description :String,
        @Field("path") path:String,
        @Field("link") link:String,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>


    @GET("getadminbook.php")
    fun getadminbook():Call<adbookresponse>

    @FormUrlEncoded
    @POST("Adminbooks.php")
    fun deleteadbooks(
        @Field("id") id: Int,
        @Field("condition") condition:String,
    ): Call<DefaultResponse>


}