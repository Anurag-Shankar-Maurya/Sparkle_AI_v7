# Sparkle AI ✨

A personalized AI chat application for Android, developed during my summer internship. This app
allows you to chat with
an AI that simulates various personalities, ranging from historical figures to fictional characters,
each with unique
communication styles.

- **Download `Sparkle_AI_v7.apk` file:
  ** [Here](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7/blob/master/Sparkle_AI_v7.apk)

## Description 📝

Sparkle AI lets you explore different conversational styles and tones by simulating diverse
personalities in a chatbot.
You can choose to chat with Albert Einstein, Sherlock Holmes, a motivational speaker, a poet, and
many more! Each
personality offers a unique conversational experience, allowing you to experiment with different
communication styles
and tones.

---

## Table of Contents

- [Usage](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7?tab=readme-ov-file#usage-)
- [Project Structure](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7?tab=readme-ov-file#project-structure-)
- [How It Works](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7?tab=readme-ov-file#how-it-works-%EF%B8%8F)
- [Installation](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7?tab=readme-ov-file#installation-%EF%B8%8F)
- [API Key Setup](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7?tab=readme-ov-file#api-key-setup-)
- [File Details](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7?tab=readme-ov-file#file-details-)
- [Dependencies](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7?tab=readme-ov-file#dependencies-)
- [Author](https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7?tab=readme-ov-file#author-)

## Usage 🚀

1. **Launch the app.**
2. **Choose a personality** from the list provided.
3. **Type your messages** in the input field at the bottom of the chat screen.
4. **Tap the send button (➡️ icon)** to send your message.
5. **Read the AI's response.**
6. **Long press any message** to copy its content.

---

## Project Structure 📂

```
SparkleAI/
├── app/
│   ├── src/main/java/com/babumusai/sparkleai/
│   │   ├── activity/        # Activities (Splash, Behaviour, Main)
│   │   ├── adapter/         # RecyclerView Adapter
│   │   ├── model/          # Data Model for Messages
│   │   └── ...              # Other packages
│   ├── src/main/res/
│   │   ├── drawable/        # Images and icons
│   │   ├── layout/         # XML layout files
│   │   └── values/         # Strings, styles, etc.
│   ├── build.gradle.kts  # App-level Gradle build file
│   └── ...
├── build.gradle.kts      # Project-level Gradle build file
├── settings.gradle.kts   # Project settings
└── ...
```

---

## How It Works ⚙️

1. **Persona Selection:**  The app presents a screen where you can choose from a diverse list of
   personalities.
2. **Chat Interface:** After selecting a persona, you're taken to the chat screen.
3. **AI Interaction:**  Type your messages and send them to the AI. The AI, using the Gemini API,
   generates responses
   reflecting the chosen persona's characteristics and communication style.
4. **Contextual Awareness (Limited):** The AI retains some context from the recent conversation
   history to make the
   interaction more engaging.
5. **Copy Functionality:** You can easily copy both user and AI messages from the chat history.

---

## Installation ⬇️

1. **Clone the Repository:** `git clone https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7`
2. **Open in Android Studio:** Open the project in Android Studio.
3. **Build and Run:** Build the app and run it on an emulator or a physical Android device.
4. **Gemini API Key:** You will need to obtain your own Gemini API key (refer to the Gemini API
   documentation for instructions) and place it in your `local.properties` file as described in the
   next section.

---

## API Key Setup 🔑

Create a file named `local.properties` in the root directory of your project and add the following
line, replacing
`YOUR_GEMINI_API_KEY` with your actual API key:

```
API_KEY="YOUR_GEMINI_API_KEY"
```

**Important:** Do not commit `local.properties` to version control, as it contains sensitive
information.

___

## File Details 📄

* **`SplashActivity.kt`:**  Displays the splash screen on app launch.
* **`BehaviourActivity.kt`:** Presents the persona selection screen.
* **`MainActivity.kt`:**  Handles the chat interface, API calls to Gemini, and message display.
* **`ChatAdapter.kt`:**  Manages the display of messages in the `RecyclerView`.
* **`Message.kt`:**  Data class representing a chat message.
* **`activity_splash.xml`, `activity_behaviour.xml`, `activity_main.xml`, `item_message.xml`:**
  Layout files for the UI.

___

## Sample Images 📸

<img src="https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7/blob/master/Sample%20Images/splash.jpeg" alt="Splash Screen" width="400">
<img src="https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7/blob/master/Sample%20Images/main1.jpeg" alt="Main Screen 1" width="400">
<img src="https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7/blob/master/Sample%20Images/main2.jpeg" alt="Main Screen 2" width="400">
<img src="https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7/blob/master/Sample%20Images/angry.jpeg" alt="Angry Behavior" width="400">
<img src="https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7/blob/master/Sample%20Images/thor.jpeg" alt="Thor" width="400">
<img src="https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7/blob/master/Sample%20Images/chef.jpeg" alt="Chef" width="400">
<img src="https://github.com/Anurag-Shankar-Maurya/Sparkle_AI_v7/blob/master/Sample%20Images/romanticGirl.jpeg" alt="Romantic Girl" width="400">

---

## Dependencies 📦

* **Gemini API:** `com.google.ai.client.generativeai:generativeai:0.7.0`
* **Markdown:** `io.noties.markwon:core:4.6.2`
* **AndroidX Libraries:** `implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)`

---

## Author 🧑‍💻

**Anurag Shankar Maurya**

- GitHub: [@Anurag-Shankar-Maurya](https://github.com/Anurag-Shankar-Maurya)
- Email: anuragshankarmaurya@gmail.com

---

Thank you for reading. Suggestions are welcome via email.


[//]: # (**Key Improvements for GitHub README:**)

[//]: # ()

[//]: # (* **Clearer Structure:**  Uses headings and sections to organize the information.)

[//]: # (* **Emojis:** Adds visual appeal and clarifies sections at a glance.)

[//]: # (* **Installation and Usage:**  Provides step-by-step instructions.)

[//]: # (* **File Details:**  Explains the purpose of each major file.)

[//]: # (* **Sample Images:**  Crucial for showcasing the app's look and feel.)

[//]: # (* **API Key Instructions:** Shows how to configure the API key securely.)

[//]: # (* **Call to Action:** Encourages feedback and contributions.)

[//]: # ()

[//]: # ()

[//]: # (Remember to replace placeholders like `YOUR_USERNAME`, your email, and image links with your actual information. Add a license &#40;e.g., MIT License&#41; if you want others to be able to use or contribute to your code. Consider adding a "Contributing" section if you're open to contributions.  A well-written README is essential for making your project easy to understand and use on GitHub.)
