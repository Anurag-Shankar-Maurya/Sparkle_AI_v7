package com.babumusai.sparkleai.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.babumusai.sparkleai.model.Message
import com.babumusai.sparkleai.R
import io.noties.markwon.Markwon

class ChatAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]

        if (message.isUser) {
            setupUserMessage(holder, message)
        } else {
            setupBotMessage(holder, message)
        }

        holder.buttonCopyBot.setOnClickListener {
            copyText(holder.itemView.context, holder.botMessageTextView.text.toString(), "Bot Text")
        }
        holder.buttonCopyUser.setOnClickListener {
            copyText(
                holder.itemView.context,
                holder.userMessageTextView.text.toString(),
                "User Text"
            )
        }
    }

    private fun setupUserMessage(holder: ChatViewHolder, message: Message) {
        holder.apply {
            userMessageTextView.text = message.content
            timestampUser.text = message.timestamp

            // Show user message elements, hide bot message elements
            userMessageTextView.visibility = View.VISIBLE
            timestampUser.visibility = View.VISIBLE
            buttonCopyUser.visibility = View.VISIBLE

            botMessageTextView.visibility = View.GONE
            timestampBot.visibility = View.GONE
            buttonCopyBot.visibility = View.GONE
        }
    }

    private fun setupBotMessage(holder: ChatViewHolder, message: Message) {
        val markwon = Markwon.create(holder.itemView.context)

        holder.apply {
            // Format bot's message with Markwon
            markwon.setMarkdown(botMessageTextView, message.content)
            timestampBot.text = message.timestamp

            // Show bot message elements, hide user message elements
            botMessageTextView.visibility = View.VISIBLE
            timestampBot.visibility = View.VISIBLE
            buttonCopyBot.visibility = View.VISIBLE

            userMessageTextView.visibility = View.GONE
            timestampUser.visibility = View.GONE
            buttonCopyUser.visibility = View.GONE
        }
    }

    private fun copyText(context: Context, text: String, label: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(label, text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(context, "$label copied", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int = messages.size

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userMessageTextView: TextView = itemView.findViewById(R.id.userMessageTextView)
        val botMessageTextView: TextView = itemView.findViewById(R.id.botMessageTextView)
        val timestampUser: TextView = itemView.findViewById(R.id.timestampUser)
        val timestampBot: TextView = itemView.findViewById(R.id.timestampBot)
        val buttonCopyBot: Button = itemView.findViewById(R.id.buttonCopyBot)
        val buttonCopyUser: Button = itemView.findViewById(R.id.buttonCopyUser)
    }
}

