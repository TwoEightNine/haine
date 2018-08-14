package global.msnthrp.haine

import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.SearchView
import android.widget.TextView
import global.msnthrp.haine.extensions.view
import global.msnthrp.haine.login.LoginActivity
import global.msnthrp.haine.search.SearchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @Rule
    @JvmField
    val rule = ActivityTestRule<SearchActivity>(SearchActivity::class.java)

    @Test
    @UiThreadTest
    fun activity_search() {
        val activity = rule.activity
        val searchView = activity.view<SearchView>(R.id.searchView).value

    }

}