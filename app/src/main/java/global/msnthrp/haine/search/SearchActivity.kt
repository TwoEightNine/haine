package global.msnthrp.haine.search

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.miguelcatalan.materialsearchview.MaterialSearchView
import global.msnthrp.haine.App
import global.msnthrp.haine.R
import global.msnthrp.haine.base.BaseActivity
import global.msnthrp.haine.chat.ChatActivity
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.network.ApiService
import global.msnthrp.haine.model.User
import global.msnthrp.haine.utils.showToast
import javax.inject.Inject

/**
 * Created by msnthrp on 22/01/18.
 */

class SearchActivity : BaseActivity(), SearchView {

    @Inject
    lateinit var api: ApiService

    private val searchView: MaterialSearchView by view(R.id.searchView)
    private val recyclerView: RecyclerView by view(R.id.recyclerView)
    private val progressBar: ProgressBar by view(R.id.progressBar)

    private val presenter: SearchPresenter by lazy { SearchPresenter(this, api) }
    private val adapter: SearchAdapter by lazy { SearchAdapter(this, ::onUserClicked) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        App.appComponent.inject(this)
        initSearchView()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun initSearchView() {
        searchView.setBackgroundColor(resources.getColor(R.color.textDark))
        searchView.setTextColor(resources.getColor(R.color.textLight))
        searchView.setHintTextColor(resources.getColor(R.color.textLightSecondary))
        searchView.setOnQueryTextListener(SearchTextListener { presenter.search(it) })
        searchView.setOnSearchViewListener(SearchCloseListener())
        searchView.post {
            searchView.showSearch()
            searchView.showKeyboard(searchView)
        }
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

    override fun onUsersLoaded(users: ArrayList<User>) {
        adapter.clear()
        adapter.addAll(users)
    }

    private fun onUserClicked(user: User) {
        ChatActivity.launch(this, user)
    }

    private inner class SearchTextListener(private val queryCallback: (String) -> Unit) : MaterialSearchView.OnQueryTextListener {

        private val timer = SearchTimer { queryCallback.invoke(lastQuery) }
        private var lastQuery = ""

        override fun onQueryTextSubmit(query: String): Boolean {
            queryCallback.invoke(query)
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            timer.cancel()
            timer.start()
            lastQuery = newText
            return true
        }
    }

    private inner class SearchTimer(private val finish: () -> Unit = {}) : CountDownTimer(300L, 300L) {

        override fun onFinish() {
            finish.invoke()
        }

        override fun onTick(p0: Long) {}
    }

    private inner class SearchCloseListener : MaterialSearchView.SearchViewListener {

        override fun onSearchViewClosed() {
            onBackPressed()
        }

        override fun onSearchViewShown() {}
    }
}