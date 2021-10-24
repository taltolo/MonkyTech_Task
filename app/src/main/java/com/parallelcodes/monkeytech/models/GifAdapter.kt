package com.parallelcodes.monkeytech.models

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.parallelcodes.monkeytech.R



class GifAdapter (var context: Context , var data: List<GiphySearchImagePoko.GiphySearchDatum>) :
    RecyclerView.Adapter<GifAdapter.ItemHolder>() {
    var color_array= context.resources.obtainTypedArray(R.array.rainbow)
    val colors = IntArray(color_array.length())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.gif_item,parent,false)
        setColorArray()
        return ItemHolder(itemHolder)
    }

    fun setColorArray(){
        for (i in 0 until color_array.length()) {
            colors[i] = color_array.getColor(i, 0)
        }
    }

    fun setDataList(newDataList : List<GiphySearchImagePoko.GiphySearchDatum>){
        data=newDataList
    }

    override fun getItemCount(): Int {
        return data.size
    }


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        var gif : GiphySearchImagePoko.GiphySearchDatum = data.get(position)
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f

        circularProgressDrawable.setColorSchemeColors(0)
        circularProgressDrawable.start()
        Glide.with(context).load(gif.images.original.url).placeholder(circularProgressDrawable).into(holder.gif)
        holder.title.text = gif.title
        var index = pickColor(colors.size-1)
        holder.relativeLayout.setBackgroundColor(colors[index])

        holder.gif.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                    setDialog(gif,circularProgressDrawable)

            }
        })
    }



    fun pickColor (range : Int ) : Int {
        val randomIndex = (0..range).random()
        return randomIndex
    }

    fun setDialog(gif : GiphySearchImagePoko.GiphySearchDatum, circularProgressDrawable : CircularProgressDrawable){

        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.preview_screen)

        val btnClose=dialog.findViewById<Button>(R.id.btnClose)
        val share_bnt=dialog.findViewById<Button>(R.id.share_button)
        val title_text=dialog.findViewById<TextView>(R.id.gif_full_title)

        val gifDisplay = dialog.findViewById<ImageView>(R.id.gifDisplay)

        title_text.text= gif.title
        circularProgressDrawable.setColorSchemeColors(255)
        Glide.with(context.applicationContext).load(gif.images.original.url).placeholder(circularProgressDrawable).into(gifDisplay)

        btnClose.setOnClickListener { dialog.dismiss() }
        share_bnt.setOnClickListener (object : View.OnClickListener {
            override fun onClick(view: View?) {
                onShareClick(gif)
            }})
        dialog.show()
    }

    fun onShareClick(gif : GiphySearchImagePoko.GiphySearchDatum){
        val shareIntent: Intent =Intent(Intent.ACTION_SEND)
        shareIntent . setType ("text/plain")
        shareIntent . putExtra( Intent.EXTRA_TEXT ,gif.images.original.url)
        context.startActivity(Intent.createChooser(shareIntent, "Share GIF"))
    }


    class ItemHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById<TextView>(R.id.gif_title)
        var gif: ImageView = itemView.findViewById<ImageView>(R.id.gif_image)
        var relativeLayout: RelativeLayout =
            itemView.findViewById<RelativeLayout>(R.id.relativeLayout)

    }


    }








