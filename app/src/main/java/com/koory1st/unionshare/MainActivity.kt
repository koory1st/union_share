package com.koory1st.unionshare

import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.jd.open.api.sdk.DefaultJdClient
import com.jd.open.api.sdk.domain.kplunion.promotioncommon.PromotionService.request.get.PromotionCodeReq
import com.jd.open.api.sdk.domain.promotion.Promotion
import com.jd.open.api.sdk.request.kplunion.UnionOpenPromotionBysubunionidGetRequest
import com.jd.open.api.sdk.request.kplunion.UnionOpenPromotionCommonGetRequest
import com.koory1st.unionshare.handler.JdHandler
import com.koory1st.unionshare.ui.theme.UnionShareTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture.AsynchronousCompletionTask


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnionShareTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) {
            return;
        }

        val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        // get text from clipboard
        clipboard.primaryClip?.getItemAt(0)?.text?.let { it ->
            Log.d("MainActivity", it.toString())

            CoroutineScope(Dispatchers.Default).launch {
                val task1 = async(Dispatchers.IO) {
                    JdHandler().request(it.toString())
                }
                val result = task1.await()
                Log.d("MainActivity", result.toString())

                if (result.isFailure) {
                    return@launch
                }

                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result.getOrNull()))
                startActivity(intent)
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UnionShareTheme {
        Greeting("Android")
    }
}