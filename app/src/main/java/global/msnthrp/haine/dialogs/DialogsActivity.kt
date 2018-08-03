package global.msnthrp.haine.dialogs

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import global.msnthrp.haine.App
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseActivity
import global.msnthrp.haine.chat.ChatActivity
import global.msnthrp.haine.extensions.setVisible
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.model.Message
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.search.SearchActivity
import global.msnthrp.haine.settings.SettingsActivity
import global.msnthrp.haine.storage.Session
import global.msnthrp.haine.utils.*
import javax.inject.Inject


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

        apiUtils.updatePrime()
        startService(this)
    }

    override fun onResume() {
        super.onResume()
        apiUtils.updateStickers()
        presenter.loadDialogs()
        closeNotification(this)
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
        progressBar.setVisible(true)
    }

    override fun onHideLoading() {
        progressBar.setVisible(false)
    }

    override fun onShowError(error: String) {
        showToast(this, error)
    }

    override fun onDialogLoaded(dialogs: ArrayList<Message>) {
        adapter.clear()
        adapter.addAll(dialogs)
    }
}