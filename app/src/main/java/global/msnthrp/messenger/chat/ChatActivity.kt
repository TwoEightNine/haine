package global.msnthrp.messenger.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import global.msnthrp.messenger.App
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseActivity
import global.msnthrp.messenger.chat.stickers.StickersFragment
import global.msnthrp.messenger.db.DbHelper
import global.msnthrp.messenger.extensions.isAtEnd
import global.msnthrp.messenger.model.Message
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.model.User
import global.msnthrp.messenger.utils.*
import global.msnthrp.messenger.view.FingerPrintAlertDialog
import javax.inject.Inject

/**
 * Created by msnthrp on 22/01/18.
 */
class ChatActivity : BaseActivity(), ChatView {

    @Inject
    lateinit var api: ApiService
    @Inject
    lateinit var apiUtils: ApiUtils
    @Inject
    lateinit var dbHelper: DbHelper

    private val toolbar: Toolbar by view(R.id.toolbar)
    private val recyclerView: RecyclerView by view(R.id.recyclerView)
    private val etInput: EditText by view(R.id.etInput)
    private val ivSend: ImageView by view(R.id.ivSend)
    private val progressBar: ProgressBar by view(R.id.progressBar)
    private val rlBottom: RelativeLayout by view(R.id.rlBottom)
    private val rlHideBottom: RelativeLayout by view(R.id.rlHideBottom)
    private val ivAttach: ImageView by view(R.id.ivAttach)
    private val rlExchange: RelativeLayout by view(R.id.rlExchangeHint)

    private lateinit var user: User
    private val presenter by lazy { ChatPresenter(this, api, apiUtils, dbHelper, user) }
    private val adapter by lazy { ChatAdapter(this, apiUtils, ::onAttachmentClick) }
    private val bottomSheet by lazy { BottomSheetController(rlBottom, rlHideBottom) }
    private var menu: Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_chat)
        App.appComponent.inject(this)
        obtainArgs()
        initToolbar(toolbar) {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = user.name
            it.subtitle = getTime(user.lastSeen, true)
        }
        initViews()
        initStickers()
        presenter.loadDialogs()
    }

    private fun obtainArgs() {
        if (intent.extras != null) {
            user = intent.extras.getSerializable(USER) as User
        } else {
            showToast(this, R.string.user_not_found)
            onBackPressed()
        }
    }

    private fun initViews() {
        ivSend.setOnClickListener {
            presenter.sendMessage(etInput.text.toString())
            etInput.setText("")
        }
        ivAttach.setOnClickListener {
            if (hasPermissions(this)) {
                chooseFile()
            } else {
                requestPermissions(this, PERMISSIONS_REQUEST_CODE)
            }
        }
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter
    }

    private fun initStickers() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.flContainer, StickersFragment())
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_chat, menu)
        this.menu = menu
        menu?.setVisible(R.id.menu_fingerprint, false)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?)
        = when (item?.itemId) {
            R.id.menu_fingerprint, R.id.menu_fingerprint_unchecked -> {
                val fingerprint = presenter.getFingerPrint()
                if (fingerprint == null) {
                    showToast(this, getString(R.string.no_shared))
                } else {
                    FingerPrintAlertDialog(this, fingerprint).show()
                    presenter.isFingerprintChecked = true
                }
                true
            }
            R.id.menu_exchange -> {
                showConfirm(this, getString(R.string.really_wanna_exchange)) {
                    if (it) presenter.createNewExchange()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onFingerPrintUpdated(checked: Boolean) {
        menu?.setVisible(R.id.menu_fingerprint, checked)
        menu?.setVisible(R.id.menu_fingerprint_unchecked, !checked)
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
        adapter.addAll(messages)
    }

    override fun onMessageSent(message: Message) {
        if (message.id == 0) {
            etInput.setText(message.text)
        }
    }

    override fun onSendingAllowed() {
        rlExchange.visibility = View.GONE
    }

    override fun onMessagesAdded(messages: List<Message>) {
        val atEnd = recyclerView.isAtEnd(adapter.itemCount)
        val lastMessageId = if (adapter.items.isNotEmpty()) {
            adapter.items.last().id
        } else {
            0
        }
        adapter.addAll(messages.filter { it.id > lastMessageId })
        if (atEnd) scrollToBottom()
    }

    override fun onFileAvailable(path: String) {
        showToast(this, getString(R.string.file_downloaded, getNameFromUrl(path)))
        if (ExtensionHelper.isPic(path)) {
            supportFragmentManager
                    .beginTransaction()
                    .add(android.R.id.content, PhotoFragment.newInstance(path), PhotoFragment.javaClass.name)
                    .commit()
        }
    }

    private fun onAttachmentClick(link: String) {
        if (needToDecryptAttachment(link)) {
            presenter.openFile(link)
        } else {
            val customTabsIntent = CustomTabsIntent.Builder().build()
            customTabsIntent.launchUrl(this, Uri.parse(link))
        }
    }

    private fun chooseFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, PICKFILE_REQUEST_CODE)
    }

    private fun scrollToBottom() {
        recyclerView.scrollToPosition(adapter.itemCount - 1)
    }

    private fun Menu.setVisible(@IdRes itemId: Int, visible: Boolean) {
        val item = this.findItem(itemId)
        item?.isVisible = visible
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICKFILE_REQUEST_CODE
                && resultCode == Activity.RESULT_OK && data != null) {
            val path = getPath(this, data.data) ?: return
            presenter.sendFile(path)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE &&
                grantResults.filter { it != PackageManager.PERMISSION_GRANTED }.isEmpty()) {
            chooseFile()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        presenter.destroy()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(android.R.id.content)
        if (fragment != null) {
            supportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit()
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val USER = "user"
        const val PICKFILE_REQUEST_CODE = 17 + 53
        const val PERMISSIONS_REQUEST_CODE = 17 * 53

        fun launch(context: Context, user: User) {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(USER, user)
            context.startActivity(intent)
        }
    }
}