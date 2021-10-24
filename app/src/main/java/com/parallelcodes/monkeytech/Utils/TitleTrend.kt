package com.parallelcodes.monkeytech.Utils

import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.parallelcodes.monkeytech.api.ApiInterface
import com.parallelcodes.monkeytech.models.GiphySearchImagePoko
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TitleTrendAsyncTask(request : ApiInterface , context : Context,asyncResponse :AsyncResponseTitle ) : AsyncTask<Int, Void, List<String>?>() {
    var request= request
    var context = context
    var result= listOf<String>()
    var asyncResponse = asyncResponse

    override fun onPreExecute() {

    }

    override fun doInBackground(vararg limit: Int?): List<String>? {
        val call = request?.getTitle(limit[0])

        call.enqueue(object: Callback<GiphySearchImagePoko> {
            override fun onResponse(call: Call<GiphySearchImagePoko>, response: Response<GiphySearchImagePoko>) {

                var titleList =response.body()!!.data
                var array= ArrayList<String>()
                for (i in titleList.indices)
                    array.add(titleList[i].title)
                 result=array.toList()
                asyncResponse.processTitleFinish(result)
            }

            override fun onFailure(call: Call<GiphySearchImagePoko>, t: Throwable) {
                Toast.makeText(context,t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    return result
    }

    override fun onProgressUpdate(vararg values: Void?) {

    }

    override fun onPostExecute(result: List<String>?) {

        // Done
    }

 }



