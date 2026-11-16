package com.example.aivideofinder

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {
    private val categories = listOf(
        "Image to video",
        "Text to video",
        "AI video editing",
        "Free / unlimited options",
        "No-signup tools",
        "Free AI video models"
    )
    private val filters = listOf(
        "Free to use",
        "No signup",
        "No watermark",
        "Commercial-use information",
        "Content-policy compliant"
    )
    private val selected = linkedSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        render()
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun render() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(22), dp(18), dp(18))
            setBackgroundColor(0xFFF5F7FB.toInt())
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        val title = TextView(this).apply {
            text = "🎬  AI Video Finder"
            textSize = 27f
            setTypeface(null, Typeface.BOLD)
            setTextColor(0xFF14243A.toInt())
        }
        content.addView(title)
        content.addView(label("Find free AI video generators and editors", 14f))
        content.addView(space(14))

        content.addView(label("CHOOSE WHAT YOU NEED", 13f, true))
        categories.forEach { category ->
            val b = Button(this).apply {
                text = category
                isAllCaps = false
                textSize = 15f
                setOnClickListener {
                    if (selected.contains(category)) selected.remove(category)
                    else selected.add(category)
                    render()
                }
                if (selected.contains(category)) {
                    setBackgroundColor(0xFFD8E8FF.toInt())
                }
            }
            content.addView(b, matchWrap())
        }

        content.addView(space(8))
        content.addView(label("FILTERS", 13f, true))
        filters.forEach { filter ->
            val cb = CheckBox(this).apply {
                text = filter
                textSize = 14f
                isChecked = selected.contains(filter)
                setOnCheckedChangeListener { _, checked ->
                    if (checked) selected.add(filter) else selected.remove(filter)
                }
            }
            content.addView(cb)
        }

        content.addView(space(8))
        val search = Button(this).apply {
            text = "Search the web for matching tools"
            isAllCaps = false
            textSize = 16f
            setOnClickListener { openSearch(buildQuery()) }
        }
        content.addView(search, matchWrap())

        val tips = TextView(this).apply {
            text = "\\nHOW TO CHECK A RESULT\\n" +
                "1. Open the official provider website.\\n" +
                "2. Check its current free-credit or usage policy.\\n" +
                "3. Check whether signup, watermark or queue limits apply.\\n" +
                "4. Read its content rules before uploading images.\\n\\n" +
                "REALITY CHECK\\n" +
                "“Unlimited free” generation is uncommon. Search results may include free tiers, trials or limited credits. This app does not bypass paywalls or provider restrictions."
            textSize = 14f
            setTextColor(0xFF34445B.toInt())
            setPadding(0, dp(18), 0, dp(12))
        }
        content.addView(tips)
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
    }

    private fun buildQuery(): String {
        val chosen = selected.toList()
        val topic = chosen.filter { it in categories }.ifEmpty { listOf("free AI video generator") }
        val filtersText = chosen.filter { it in filters }.joinToString(" ")
        val safety = "official website terms content policy safe legal use"
        return (topic.joinToString(" ") + " " + filtersText + " " + safety +
                " current free plan limits").trim()
    }

    private fun openSearch(query: String) {
        val url = "https://www.google.com/search?q=" + Uri.encode(query)
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: Exception) {
            Toast.makeText(this, "No browser found. Install or enable a browser.", Toast.LENGTH_LONG).show()
        }
    }

    private fun label(text: String, size: Float, bold: Boolean = false) =
        TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(0xFF26364D.toInt())
            if (bold) setTypeface(null, Typeface.BOLD)
            setPadding(0, dp(8), 0, dp(5))
        }

    private fun space(h: Int) = TextView(this).apply { height = dp(h) }
    private fun matchWrap() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply { bottomMargin = dp(5) }
}
