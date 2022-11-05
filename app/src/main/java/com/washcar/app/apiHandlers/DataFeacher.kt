package com.washcar.app.apiHandlers

import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson
import com.washcar.app.RootApplication
import com.washcar.app.classes.Constants
import com.washcar.app.classes.UtilityApp
import com.washcar.app.models.*


class DataFeacher(callBack: DataFetcherCallBack?) {
    var dataFetcherCallBack: DataFetcherCallBack? = callBack
//    var activity: Activity? = Activity()

    var fireStoreDB = RootApplication.fireStoreDB


    val TAG: String? = "Log"

    //    var accessToken: String?
//    var lang: String?
    var headerMap: MutableMap<String, Any?> = HashMap()

    /*********************************** POST Fetcher  **********************************/
    fun loginHandle(memberModel: RegisterUserModel) {

        Log.i(TAG, "Log loginHandle")
        Log.i(TAG, "Log email " + memberModel.email)
        Log.i(TAG, "Log password " + memberModel.password)
//        Log.i(TAG, "Log mobileWithCountry " + memberModel?.mobileWithCountry)

//        val phoneNumber = memberModel?.mobileWithCountry
        fireStoreDB?.collection(ApiUrl.Users.name)?.document(memberModel.email)?.get()
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val document = it.result
                    if (document.exists()) {
                        dataFetcherCallBack?.Result(document, Constants.SUCCESS, true)
                    } else {
                        dataFetcherCallBack?.Result(null, Constants.USER_NOT_EXIST, false)

                    }
                } else {
                    dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, false)
                }
            }?.addOnSuccessListener { document ->
                if (document.exists()) {
                    dataFetcherCallBack?.Result(document, Constants.SUCCESS, true)

                } else {
                    dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, false)

                }

            }


    }

    fun registerHandle(memberModel: MemberModel) {

        Log.i(TAG, "Log email ${memberModel.email}")

        val email = memberModel.email.toString()
//        this.activity = activity

        fireStoreDB!!.collection(ApiUrl.Users.name).document(email).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    dataFetcherCallBack?.Result(null, Constants.USER_EXIST, false)

                } else {
                    fireStoreDB!!.collection(ApiUrl.Users.name).document(email).set(memberModel)
                        .addOnSuccessListener {
                            dataFetcherCallBack?.Result(memberModel, Constants.SUCCESS, true)
                        }.addOnFailureListener {
                            dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, true)
                        }

                }

            }

    }

    fun confirmAccount(mobile: String) {

        Log.i(TAG, "Log confirmAccount")
        Log.i(TAG, "Log headerMap $headerMap")
        Log.i(TAG, "Log mobile $mobile")

        RootApplication.fireStoreDB?.collection(ApiUrl.Users.name)?.document(mobile)
            ?.update("isVerified", true)?.addOnSuccessListener {

                dataFetcherCallBack?.Result("", Constants.SUCCESS, true)

            }?.addOnFailureListener { e ->
                dataFetcherCallBack?.Result("", Constants.FAIL_DATA, true)
            }

    }

    fun getMyAccount(email: String?) {

        Log.i(TAG, "Log getMyAccount")
        Log.i(TAG, "Log headerMap $headerMap")
        Log.i(TAG, "Log email $email")

        if (email == null) {
            dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, false)
            return
        }
        RootApplication.fireStoreDB?.collection(ApiUrl.Users.name)?.document(email)?.get()
            ?.addOnSuccessListener {

                val user = it.toObject(MemberModel::class.java)
                dataFetcherCallBack?.Result(user, Constants.SUCCESS, true)

            }?.addOnFailureListener { e ->
                dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, false)
            }


    }

    fun forgetPassword(mobile: String) {
        Log.i(TAG, "Log forgetPassword")
        Log.i(TAG, "Log mobile $mobile")

        fireStoreDB?.collection(ApiUrl.Users.name)?.document(mobile)?.get()
            ?.addOnSuccessListener { document ->
                if (document.exists()) {
                    dataFetcherCallBack?.Result(document, Constants.SUCCESS, true)

                } else {
                    dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, true)

                }
            }?.addOnFailureListener {
                dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, true)

            }


    }

    fun resetPassword(mobile: String, newPassword: String) {

        Log.i(TAG, "Log resetPassword")
        Log.i(TAG, "Log mobile $mobile")
        Log.i(TAG, "Log new_password $newPassword")
        Log.i(TAG, "Log confirm_password $newPassword")

        RootApplication.fireStoreDB?.collection(ApiUrl.Users.name)?.document(mobile)
            ?.update("password", newPassword, "password_confirm", newPassword)
            ?.addOnSuccessListener {

                dataFetcherCallBack?.Result("", Constants.SUCCESS, true)

            }?.addOnFailureListener { e ->
                dataFetcherCallBack?.Result("", Constants.FAIL_DATA, true)
            }


    }

    fun changePassword(mobile: String?, oldPassword: String, newPassword: String) {

        Log.i(TAG, "Log changePassword")
        Log.i(TAG, "Log mobile changePassword $mobile")
        Log.i(TAG, "Log oldPassword $oldPassword")
        Log.i(TAG, "Log newPassword $newPassword")

        fireStoreDB?.collection(ApiUrl.Users.name)?.document(mobile!!)?.get()
            ?.addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(MemberModel::class.java)
                    val password = user?.password
                    if (oldPassword == password) {
                        RootApplication.fireStoreDB?.collection(ApiUrl.Users.name)?.document(mobile)
                            ?.update("password", newPassword, "password_confirm", newPassword)
                            ?.addOnSuccessListener {

                                dataFetcherCallBack?.Result("", Constants.SUCCESS, true)

                            }?.addOnFailureListener { e ->
                                dataFetcherCallBack?.Result("", Constants.FAIL_DATA, true)
                            }
                    } else {
                        dataFetcherCallBack?.Result("", Constants.PASSWORD_WRONG, true)

                    }

                } else {
                    dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, false)

                }

            }


    }

    fun getUsers() {
        Log.i(TAG, "Log getUsers")

        fireStoreDB?.collection(ApiUrl.Users.name)?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {
                val query = it.result

                val list = mutableListOf<MemberModel>()
                for (document in query!!) {
                    val objectModel = document?.toObject(MemberModel::class.java)
                    list.add(objectModel!!)
                }

                dataFetcherCallBack?.Result(list, Constants.SUCCESS, true)
            } else {
                it.exception?.printStackTrace()
            }

        }

    }

    fun deleteDriver(driverModel: DriverModel) {

//        val phoneNumber = map["mobileWithCountry"] as String
////        val mobile = map["countryCode"]

        Log.i(TAG, "Log deleteDriver")
        Log.i(TAG, "Log phoneNumber ${driverModel.mobileWithCountry}")

//        val phoneNumber = memberModel.mobileWithCountry.toString()
        fireStoreDB!!.collection(ApiUrl.Users.name).document(driverModel.mobileWithCountry!!)
            .delete().addOnSuccessListener {
                dataFetcherCallBack?.Result("", Constants.SUCCESS, true)

            }.addOnFailureListener {
                dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, false)
            }

    }

    fun getCurrentRequests(driver_id: String?) {
        Log.i(TAG, "Log getCurrentRequests")

        fireStoreDB?.collection(ApiUrl.Orders.name)?.whereEqualTo("requestStatus", 0)
            ?.whereEqualTo("driver_id", driver_id)?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val query = it.result

                    val requestList = mutableListOf<RequestModel>()
                    for (document in query!!) {
                        val requestModel = document?.toObject(RequestModel::class.java)
                        requestList.add(requestModel!!)
                    }

                    val json = Gson().toJson(requestList)
                    dataFetcherCallBack?.Result(requestList, Constants.SUCCESS, true)
                } else {
                    it.exception?.printStackTrace()
                }

            }

    }

    fun getProviderAllRequests(providerId: String, status: String) {
        Log.i(TAG, "Log getProviderAllRequests")
        Log.i(TAG, "Log providerId  $providerId")
        Log.i(TAG, "Log status  $status")

        fireStoreDB?.collection(ApiUrl.Orders.name)?.whereEqualTo("providerId", providerId)
            ?.whereEqualTo("status", status)?.orderBy("createdAt", Query.Direction.DESCENDING)
            ?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val query = it.result

                    val requestList = mutableListOf<RequestModel?>()
                    for (document in query!!) {
                        val requestModel = document?.toObject(RequestModel::class.java)
                        requestList.add(requestModel)
                    }

                    dataFetcherCallBack?.Result(requestList, Constants.SUCCESS, true)
                } else {
                    it.exception?.printStackTrace()
                }

            }

    }

    fun getProviderCategories(providerEmail: String) {
        Log.i(TAG, "Log getProviderCategories")

        fireStoreDB?.collection(ApiUrl.Users.name)?.document(providerEmail)
            ?.collection(ApiUrl.Categories.name)?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val query = it.result

                    val categoriesList = mutableListOf<CategoryModel>()
                    for (document in query!!) {
                        val categoryModel = document?.toObject(CategoryModel::class.java)
                        categoriesList.add(categoryModel!!)
                    }

                    dataFetcherCallBack?.Result(categoriesList, Constants.SUCCESS, true)
                } else {
                    it.exception?.printStackTrace()
                }

            }

    }

    fun editProviderCategory(providerEmail: String, categoryModel: CategoryModel) {

//        val categoryId = fireStoreDB?.collection(ApiUrl.Users.name)?.document()?.id

        Log.i(TAG, "Log editProviderCategory")
        Log.i(TAG, "Log category  $categoryModel")

        fireStoreDB?.collection(ApiUrl.Users.name)?.document(providerEmail)
            ?.collection(ApiUrl.Categories.name)?.document(categoryModel.id ?: "")?.set(
                categoryModel, SetOptions.merge()
            )?.addOnCompleteListener {
                if (it.isSuccessful) {
                    dataFetcherCallBack?.Result("", Constants.SUCCESS, true)
                } else {

                    dataFetcherCallBack?.Result(it.exception?.message, Constants.FAIL_DATA, true)
                }
            }
    }

    fun setProviderAnnounced(providerEmail: String, announced: Boolean) {

        Log.i(TAG, "Log setProviderAnnounced")
        Log.i(TAG, "Log announced  $announced")

        val dataMap = mutableMapOf<String, Any>().apply {
            this["announced"] = announced
        }

        fireStoreDB?.collection(ApiUrl.Users.name)?.document(providerEmail)
            ?.set(dataMap, SetOptions.merge())?.addOnCompleteListener {
                if (it.isSuccessful) {
                    dataFetcherCallBack?.Result("", Constants.SUCCESS, true)
                } else {

                    dataFetcherCallBack?.Result(it.exception?.message, Constants.FAIL_DATA, true)
                }
            }
    }


    fun getAllClientRequests(customerId: String?, status: String?) {
        Log.i(TAG, "Log getAllRequests")
        Log.i(TAG, "Log customerId  $customerId")

        fireStoreDB?.collection(ApiUrl.Orders.name)?.whereEqualTo("customerId", customerId)
            ?.whereEqualTo("status", status)?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val query = it.result

                    val requestList = mutableListOf<RequestModel>()
                    for (document in query!!) {
                        val requestModel = document?.toObject(RequestModel::class.java)
                        requestList.add(requestModel ?: RequestModel())
                    }

                    dataFetcherCallBack?.Result(requestList, Constants.SUCCESS, true)
                } else {
                    it.exception?.printStackTrace()
                }

            }

    }

    fun addCategory(categoryName: String?) {

        val categoryId = fireStoreDB?.collection(ApiUrl.Categories.name)?.document()?.id

        Log.i(TAG, "Log addCategory")
        Log.i(TAG, "Log categoryId  $categoryId")
        Log.i(TAG, "Log categoryName $categoryName")

        val categoryModel = CategoryModel().apply {
            this.id = categoryId
            this.name = categoryName
        }

        fireStoreDB?.collection(ApiUrl.Categories.name)?.document(categoryId!!)?.set(
            categoryModel, SetOptions.merge()
        )?.addOnCompleteListener {
            if (it.isSuccessful) {
                dataFetcherCallBack?.Result("", Constants.SUCCESS, true)
            } else {

                dataFetcherCallBack?.Result(it.exception?.message, Constants.FAIL_DATA, true)
            }
        }
    }

    fun getCategories() {
        Log.i(TAG, "Log getCategories")

        fireStoreDB?.collection(ApiUrl.Categories.name)?.get()?.addOnCompleteListener {
            if (it.isSuccessful) {
                val query = it.result

                val categoriesList = mutableListOf<CategoryModel>()
                for (document in query!!) {
                    val categoryModel = document?.toObject(CategoryModel::class.java)
                    categoriesList.add(categoryModel!!)
                }

                val json = Gson().toJson(categoriesList)
                UtilityApp.setToShPref(Constants.DB_Categories, json)
                dataFetcherCallBack?.Result(categoriesList, Constants.SUCCESS, true)
            } else {
                it.exception?.printStackTrace()
            }

        }

    }

    fun sendOrderHandle(requestModel: RequestModel) {

        Log.i(TAG, "Log orderHandler")

        val orderId: String = fireStoreDB!!.collection(ApiUrl.Orders.name).document().id
        requestModel.requestId = orderId

        fireStoreDB!!.collection(ApiUrl.Orders.name).document(orderId).set(requestModel)
            .addOnSuccessListener {
                dataFetcherCallBack?.Result(requestModel, Constants.SUCCESS, true)
            }.addOnFailureListener {
                dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, true)
            }


    }


    fun sendReviewHandle(reviewModel: ReviewModel) {

        Log.i(TAG, "Log sendReviewHandle")

        val reviewId: String = fireStoreDB!!.collection(ApiUrl.Reviews.name).document().id
        reviewModel.id = reviewId

        fireStoreDB!!.collection(ApiUrl.Reviews.name).document(reviewId).set(reviewModel)
            .addOnSuccessListener {
                dataFetcherCallBack?.Result(reviewId, Constants.SUCCESS, true)
            }.addOnFailureListener {
                dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, true)
            }


    }

    fun getReviews(providerId: String?) {
        Log.i(TAG, "Log getReviews")
        Log.i(TAG, "Log providerId  $providerId")

        fireStoreDB?.collection(ApiUrl.Reviews.name)?.whereEqualTo("providerId", providerId)
            ?.get()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    val query = it.result

                    val list = mutableListOf<ReviewModel?>()
                    for (document in query) {
                        val reviewModel = document?.toObject(ReviewModel::class.java)
                        list.add(reviewModel)
                    }

                    dataFetcherCallBack?.Result(list, Constants.SUCCESS, true)
                } else {
                    it.exception?.printStackTrace()
                }

            }

    }

    fun finishRequestByProvider(requestId: String) {

        Log.i(TAG, "Log finishRequestByProvider")
        Log.i(TAG, "Log requestId $requestId")

        val dataMap = mutableMapOf<String, Any>().apply {
            this["status"] = RequestModel.STATUS_FINISHED
        }

        fireStoreDB!!.collection(ApiUrl.Orders.name).document(requestId)
            .set(dataMap, SetOptions.merge())
            .addOnSuccessListener {
                dataFetcherCallBack?.Result("", Constants.SUCCESS, true)
            }.addOnFailureListener {
                dataFetcherCallBack?.Result(null, Constants.FAIL_DATA, true)
            }


    }

}