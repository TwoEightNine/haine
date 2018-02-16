package global.msnthrp.messenger.dialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import global.msnthrp.messenger.App
import global.msnthrp.messenger.R
import global.msnthrp.messenger.base.BaseActivity
import global.msnthrp.messenger.chat.ChatActivity
import global.msnthrp.messenger.extensions.view
import global.msnthrp.messenger.model.Message
import global.msnthrp.messenger.network.ApiService
import global.msnthrp.messenger.search.SearchActivity
import global.msnthrp.messenger.settings.SettingsActivity
import global.msnthrp.messenger.storage.Lg
import global.msnthrp.messenger.storage.Session
import global.msnthrp.messenger.utils.*
import javax.inject.Inject
import android.support.customtabs.CustomTabsIntent



/**
 * Created by msnthrp on 22/01/18.
 */

class DialogsActivity : BaseActivity(), DialogsView {

    @Inject
    lateinit var api: ApiService
    @Inject
    lateinit var session: Session
    @Inject
    lateinit var apiUtils: ApiUtils

    private val toolbar: Toolbar by view(R.id.toolbar)
    private val recyclerView: RecyclerView by view(R.id.recyclerView)
    private val progressBar: ProgressBar by view(R.id.progressBar)
    private val ivToolbarLogo: ImageView by view(R.id.ivToolbarLogo)

    private val presenter: DialogsPresenter by lazy { DialogsPresenter(api, this) }
    private val adapter: DialogsAdapter by lazy {
        DialogsAdapter(this, session) { _, message ->
            if (message.user != null) {
                ChatActivity.launch(this, message.user!!)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialogs)
        ivToolbarLogo.visibility = View.VISIBLE
        initToolbar(toolbar) {
            it.setDisplayHomeAsUpEnabled(false)
        }
        App.appComponent.inject(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

//        apiUtils.updateStickers()
        apiUtils.updatePrime()
        startService(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadDialogs()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_dialogs, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?)
        = when (item?.itemId) {
            R.id.menu_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                true
            }
            R.id.menu_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
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

    override fun onDialogLoaded(dialogs: ArrayList<Message>) {
        adapter.clear()
        adapter.addAll(dialogs)
    }
}