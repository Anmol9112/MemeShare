package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.load.DataSource
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var currentMemeUrl: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }
    private fun loadMeme(){
        nextButton.isEnabled=false
        shareButton.isEnabled=false
        progressBar.visibility=View.VISIBLE
    val url = "https://meme-api.herokuapp.com/gimme"

    // Request a string response from the provided URL.
    val JsonObjectRequest= JsonObjectRequest(
        Request.Method.GET, url,null,
        Response.Listener { response ->
            currentMemeUrl=response.getString("url")
            Glide.with(this).load(currentMemeUrl).listener(object:RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility=View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility=View.GONE
                    nextButton.isEnabled = true
                    shareButton.isEnabled = true
                    return false
                }
            }).into(memeImageView)
        },
        Response.ErrorListener {
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
        })

     // Add the request to the RequestQueue.
    MySingleton.getInstance(this).addToRequestQueue(JsonObjectRequest)
}
    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this cool meme I got from Reddit $currentMemeUrl")
        val chooser=Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}