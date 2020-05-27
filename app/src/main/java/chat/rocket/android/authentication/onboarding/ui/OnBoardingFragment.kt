package chat.rocket.android.authentication.onboarding.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import chat.rocket.android.analytics.AnalyticsManager
import chat.rocket.android.analytics.event.ScreenViewEvent
import chat.rocket.android.app.RocketChatApplication
import chat.rocket.android.authentication.onboarding.presentation.OnBoardingPresenter
import chat.rocket.android.authentication.onboarding.presentation.OnBoardingView
import chat.rocket.android.authentication.ui.AuthenticationActivity
import chat.rocket.android.util.extensions.*
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.fragment_authentication_on_boarding.*
import kr.co.bumin.chat.android.R
import okhttp3.*
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import kotlin.concurrent.thread


fun newInstance() = OnBoardingFragment()

class OnBoardingFragment : Fragment(), OnBoardingView {
    @Inject
    lateinit var presenter: OnBoardingPresenter
    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_authentication_on_boarding)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupOnClickListener()
        analyticsManager.logScreenView(ScreenViewEvent.OnBoarding)

        toNext()
    }

    private fun toNext() {
        showLoading()
        thread(start=true) {
            Thread.sleep(1000)
//            loadServerList()
            goSignInServer()
        }
    }

    private fun loadServerList() {
        try {
            val serverListUrl = "https://bumin.co.kr/serverlist.txt"
            val client = OkHttpClient()
            val request = Request.Builder()
                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .url(serverListUrl)
                    .build()
            client.newCall(request).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("보딩", "${e.toString()}")
                    goSignInServer()
                }
                override fun onResponse(call: Call, response: Response) {
                    val content = response.body?.string()
                    activity?.application?.let {
                        val app = it as RocketChatApplication
                        app.serverList = mutableListOf()
                        val items = content?.split("\n")?: arrayListOf()
                        for(item in items){
                            var attr = item.split(" ")
                            if(attr.size > 2 && attr[2].sanitize().equals("D")) // disabled
                                continue
                            try {
                                Log.d("보딩", "${attr.size} ${attr[0]} ${attr[1]} ${attr[2]}")
                                val server = RocketChatApplication.Server(attr[0], attr[1], "A")
                                app.serverList.add(server)
                                Log.d("보딩", "server=$server")
                            }catch(e:Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    goSignInServer()
                }
            })
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun goSignInServer() {
        ui {
            signInToYourServer()
            hideLoading()
        }
    }

    private fun setupToolbar() {
        with(activity as AuthenticationActivity) {
            view?.let { this.setLightStatusBar(it) }
            toolbar.isVisible = false
        }
    }

    private fun setupOnClickListener() {
        connect_with_a_server_container.setOnClickListener { signInToYourServer() }
        join_community_container.setOnClickListener { joinInTheCommunity() }
        create_server_container.setOnClickListener { createANewServer() }
    }

    override fun showLoading() {
        ui {
            view_loading.isVisible = true
        }
    }

    override fun hideLoading() {
        ui {
            view_loading.isVisible = false
        }
    }

    override fun showMessage(resId: Int) {
        ui {
            showToast(resId)
        }
    }

    override fun showMessage(message: String) {
        ui {
            showToast(message)
        }
    }

    override fun showGenericErrorMessage() = showMessage(getString(R.string.msg_generic_error))

    private fun signInToYourServer() = ui {
        presenter.toSignInToYourServer()
    }

    private fun joinInTheCommunity() = ui {
        presenter.connectToCommunityServer(
            getString(R.string.default_protocol) + getString(R.string.community_server_url)
        )
    }

    private fun createANewServer() = ui {
        presenter.toCreateANewServer(
            getString(R.string.default_protocol) + getString(R.string.create_server_url)
        )
    }
}
