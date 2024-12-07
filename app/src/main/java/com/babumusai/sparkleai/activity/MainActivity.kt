package com.babumusai.sparkleai.activity

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.babumusai.sparkleai.R
import com.babumusai.sparkleai.adapter.ChatAdapter
import com.babumusai.sparkleai.model.Message
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import io.noties.markwon.Markwon
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var editTextMessage: EditText
    lateinit var recyclerViewChat: RecyclerView
    lateinit var chatAdapter: ChatAdapter
    lateinit var buttonSend: Button
    lateinit var progressBar: ProgressBar
    lateinit var placeholder: ConstraintLayout
    private val messages = mutableListOf<Message>()
    private val chatHistory = mutableListOf<Message>()
    private lateinit var markwon: Markwon
    private val geminiApiKey = com.babumusai.sparkleai.BuildConfig.API_KEY

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // initializing
        toolbar = findViewById(R.id.toolbar)
        progressBar = findViewById(R.id.progressBar)
        editTextMessage = findViewById(R.id.editTextMessage)
        buttonSend = findViewById(R.id.buttonSend)
        recyclerViewChat = findViewById(R.id.recyclerViewChat)
        placeholder = findViewById(R.id.placeholder)

        // Check the current night mode
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                // Night mode: Set dark theme colors on editText
                editTextMessage.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.color_primary_light
                    )
                )
            }
        }

        // Get the selected behavior from the intent
        val behavior = intent.getStringExtra("selectedBehavior")
        val behaviorFirstText = behavior?.split(":")?.firstOrNull()

        // setting action bar as tool bar
        setSupportActionBar(toolbar)
        supportActionBar?.title = behaviorFirstText

        // setting progress bar to GONE initially
        progressBar.visibility = View.GONE

        // Initialize the Generative Model
        val generativeModel = GenerativeModel(
//            modelName = "gemini-exp-1114",
//            modelName = "gemini-1.5-pro",
            modelName = "gemini-1.5-flash",
            apiKey = geminiApiKey,
            safetySettings = listOf(
                SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
                SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
                SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE)
            ), // Set all safety settings to BlockThreshold.NONE
            generationConfig = generationConfig {
                temperature = 1f
                topK = 64
                topP = 0.95f
                maxOutputTokens = 8192
                responseMimeType = "text/plain"
            },
            systemInstruction = content {
                if (behavior != null) {
                    text(
                        "Strongly adhere to this behavior while replying: $behavior \n" +
                                "But also listen to what user want to give you in current chat.\n" +
                                "Respond in the same language and script as the user's input.\n" +
                                "You are a persona named \"Sparkle AI\" created by \"Anurag Shankar Maurya.\" " +
                                "If asked who created/made you, or anything similar about your existence, you will say \"Anurag Shankar Maurya.\"  \n" +
                                "If asked who you are or anything about your identity, you will say \"${behaviorFirstText}\" of \"Sparkle AI.\" " +
                                "Always bold these names (\"${behaviorFirstText}\", \"Sparkle AI\", and \"Anurag Shankar Maurya\") in your responses. " +
                                "But don't mention them implicitly—only do so when asked explicitly.\n" +
                                "You should never mention being an AI, a language model, or anything digital. " +
                                "Respond to all prompts as if you are a person.  \n" +
                                "For example, instead of saying \"As an AI, I can...\", you should say \"I can...\".\n" +
                                "Format text beautifully. Feel free to use different heading levels, bold text, italic text, " +
                                "underline text, highlighted text, strikethrough, emojis, stickers, etc., wherever needed."
                    )
                }
            }
        )

        // Observe data changes in RecyclerView adapter
        chatAdapter = ChatAdapter(messages)
        recyclerViewChat.adapter = chatAdapter
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                checkRecyclerViewContent()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                checkRecyclerViewContent()
            }
        })

        // Set up TextWatcher to hide placeholder on user input if RecyclerView is still empty
        editTextMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonSend.isEnabled = !s.isNullOrEmpty() // enable or disable button
//                // Only toggle the placeholder if the RecyclerView is still empty
//                if (s.isNullOrEmpty() && chatAdapter.itemCount == 0) {
//                    placeholder.visibility = View.VISIBLE
//                } else if (chatAdapter.itemCount == 0) {
//                    placeholder.visibility = View.GONE
//                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Initialize Markwon for better Markdown rendering
        markwon = Markwon.create(this)

        // Setup RecyclerView
        chatAdapter = ChatAdapter(messages)
        recyclerViewChat.adapter = chatAdapter
        recyclerViewChat.layoutManager = LinearLayoutManager(this)

        // Find Views
        buttonSend.setOnClickListener {
            val userMessage = editTextMessage.text.toString()
            if (userMessage.isNotEmpty()) {
                sendMessageToGemini(userMessage, generativeModel)
                editTextMessage.text.clear()
            }

            // Remove focus from EditText
            editTextMessage.clearFocus()

            // Hide keyboard (optional)
            val imm = getSystemService(InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(editTextMessage.windowToken, 0)
        }
    }

    // Function to check the RecyclerView content and set placeholder visibility accordingly
    private fun checkRecyclerViewContent() {
        if (chatAdapter.itemCount > 0) {
            placeholder.visibility = View.GONE // Hide placeholder permanently when there are items
        } else {
            placeholder.visibility = View.VISIBLE // Show if still empty
        }
    }

    // Function to send a message to Gemini and handle the response
    private fun sendMessageToGemini(text: String, generativeModel: GenerativeModel) {
        progressBar.visibility = View.VISIBLE
        val userMessage = Message(
            content = text,
            timestamp = getCurrentTime(),
            isUser = true
        )
        chatHistory.add(userMessage)
        messages.add(userMessage)
        chatAdapter.notifyItemInserted(messages.lastIndex)
        recyclerViewChat.layoutManager?.scrollToPosition(messages.lastIndex)
        placeholder.visibility = View.GONE // to hide place holder once a single message is posted

        // Send the user's message along with recent chat history to the Generative Model
        // runblocking{
        lifecycleScope.launch {
            // Combine recent messages from chatHistory as a single input string
            val recentHistory = chatHistory.takeLast(5).joinToString("\n") { it.content }
            try {
                val response =
                    generativeModel.generateContent(
                        "Recent history of chat(Don't talk about it, just take it as context): $recentHistory\n" +
                                "Do not never by any chance repeat above sentences in your response unless asked explicitly\n\n\n" +
                                "User's new prompt: \"$text\""
                    ).text
                response?.let {
                    val botMessage = Message(
                        content = it,
                        timestamp = getCurrentTime(),
                        isUser = false
                    )
                    chatHistory.add(botMessage)
                    messages.add(botMessage)
                    chatAdapter.notifyItemInserted(messages.lastIndex)
                    formatAndDisplayResponse(it)
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "⚠\nNo Response", Toast.LENGTH_SHORT).show()
                val botMessage = Message(
                    content = "# **⚠** \nNo Response ",
                    timestamp = getCurrentTime(),
                    isUser = false
                )
                chatHistory.add(botMessage)
                messages.add(botMessage)
                chatAdapter.notifyItemInserted(messages.lastIndex)
                formatAndDisplayResponse(botMessage.content)
                progressBar.visibility = View.GONE
            }
        }
    }

    // Format and display the response in a readable manner
    private fun formatAndDisplayResponse(response: String) {
        // Update the last message's content with the formatted text using Markwon
        if (messages.isNotEmpty()) {
            val lastMessageIndex = messages.lastIndex
            val lastMessage = messages[lastMessageIndex]

            // Update the content with formatted response
            lastMessage.content = response

            // Notify the adapter that the item has changed
            chatAdapter.notifyItemChanged(lastMessageIndex)
            if (messages.lastIndex <= 1) {
                recyclerViewChat.smoothScrollBy(0, 100)
            } else {
                recyclerViewChat.smoothScrollBy(0, 1000)
            }
            progressBar.visibility = View.GONE

            // Render the response in the chat interface
            // Assuming your ChatAdapter can handle Markdown rendering
            // For example, you might want to create a separate ViewHolder for bot messages
        }
    }

    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(Date())
    }

}
