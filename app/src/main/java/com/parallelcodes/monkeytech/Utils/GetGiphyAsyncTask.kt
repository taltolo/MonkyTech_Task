package com.parallelcodes.monkeytech.Utils

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.parallelcodes.monkeytech.api.ApiInterface
import com.parallelcodes.monkeytech.models.GiphySearchImagePoko
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetGiphyAsyncTask(request : ApiInterface, context: Context , asyncResponseGif : AsyncResponseGif) : AsyncTask<String, Void, Void?>() {
    var request= request
    var context = context
    var asyncResponseGif = asyncResponseGif
    override fun onPreExecute() {

    }

    override fun doInBackground(vararg query: String?): Void? {
        val call = request?.getData(query[0].toString())
        call.enqueue(object: Callback<GiphySearchImagePoko> {
            override fun onResponse(call: Call<GiphySearchImagePoko>, response: Response<GiphySearchImagePoko>) {
                asyncResponseGif.processGifFinish(response.body()!!.data)

            }

            override fun onFailure(call: Call<GiphySearchImagePoko>, t: Throwable) {
                Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        return null
    }

    override fun onProgressUpdate(vararg values: Void?) {

    }

    override fun onPostExecute(result: Void?) {
        // Done
    }

}