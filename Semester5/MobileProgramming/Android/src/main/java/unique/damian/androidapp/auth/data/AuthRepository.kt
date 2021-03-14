package unique.damian.androidapp.auth.data

import androidx.navigation.fragment.findNavController
import unique.damian.androidapp.Others.Api
import unique.damian.androidapp.auth.data.remote.AuthApi
import unique.damian.androidapp.Others.Result
import unique.damian.androidapp.Others.WebSocketNotifications
import unique.damian.androidapp.R
import androidx.navigation.fragment.findNavController

object AuthRepository {
    var user:User?=null
        private set
    val isLoggedIn:Boolean
        get() =user!=null

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = AuthApi.login(user)
        if (result is Result.Success<TokenHolder>) {
            setLoggedInUser(user, result.data)
        }
        return result
    }
    fun logout(){
        Api.tokenInterceptor.token = null;
        this.user = null
    }

    private fun setLoggedInUser(user: User, tokenHolder: TokenHolder) {
        this.user = user
        Api.tokenInterceptor.token = tokenHolder.token

    }

}