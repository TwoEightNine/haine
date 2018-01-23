package global.msnthrp.messenger.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import global.msnthrp.messenger.App
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseActivity
import global.msnthrp.messenger.dialogs.Message
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.profile.User
import global.msnthrp.messenger.utils.getTime
import global.msnthrp.messenger.utils.showToast
import javax.inject.Inject

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatActivity : BaseActivity(), ChatView {

    @Inject
    lateinit var api: ApiService

    private val toolbar: Toolbar by view(R.id.toolbar)
    private val recyclerView: RecyclerView by view(R.id.recyclerView)
    private val etInput: EditText by view(R.id.etInput)
    private val ivSend: ImageView by view(R.id.ivSend)
    private val progressBar: ProgressBar by view(R.id.progressBar)

    private lateinit var user: User
    private val presenter: ChatPresenter by lazy { ChatPresenter(this, api, user) }
    private val adapter: ChatAdapter by lazy { ChatAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        App.appComponent.inject(this)
        obtainArgs()
        initToolbar(toolbar) {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = user.name
            it.subtitle = getTime(user.lastSeen, true)
        }
        ivSend.setOnClickListener {
            presenter.sendMessage(etInput.text.toString())
            etInput.setText("")
        }
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter
        presenter.loadDialogs()
    }

    private fun obtainArgs() {
        if (intent.extras != null) {
            user = intent.extras.getSerializable(USER) as User
        } else {
            showToast(this, "User not found")
            onBackPressed()
        }
    }

    private fun scrollToBottom() {
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    override fun onShowLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onHideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun onShowError(error: String) {
        showToast(this, error)
    }

    override fun onMessagesLoaded(messages: ArrayList<Message>) {
        var added = 0
        messages.forEach {
            if (it !in adapter.items) {
                adapter.add(it)
                added++
            }
        }
        if (added > 0) {
            scrollToBottom()
        }
    }

    override fun onMessageSent(message: Message) {
        if (message.id == 0) {
            etInput.setText(message.body)
        } else {
            adapter.add(message)
            scrollToBottom()
        }
    }

    companion object {
        val USER = "user"

        fun launch(context: Context, user: User) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(USER, user)
            context.startActivity(intent)
        }
    }
}