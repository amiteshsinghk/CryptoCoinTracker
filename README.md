A modern Android application demonstrating the Model-View-Intent (MVI) design pattern, developed to track cryptocurrency data with clean architecture and scalable practices.

🚀 **Features**

1. Real-time Cryptocurrency Data: Fetch and display live cryptocurrency prices and details.
2. Unidirectional Data Flow: Ensures predictable state management with MVI architecture.
3. Clean Architecture: Separation of concerns into distinct layers: Data, Domain, and Presentation.
4. Scalable Design: Future API or UI updates can be handled without affecting other layers.
5. Kotlin Coroutines & Flow: For asynchronous programming and reactive streams.

🛠️ **Technologies Used**
1. Kotlin: Primary programming language.
2. Ktor: For API calls.
3. Jetpack Components: ViewModel, LiveData, and Navigation.
4. Jetpack Compose: Ui
5. Koin: For dependency injection.
6. Kotlin Flow: To manage streams of data.
7. Material Design 3: For a modern and intuitive UI.

📐 **Architecture**
This project follows the MVI (Model-View-Intent) architecture:

Model: Represents the data and interacts with APIs or databases.

ViewModel: Processes user intents and updates the state.

Intent: Captures user actions or events as sealed classes.

State: Represents the current UI state.

View: Renders the state and sends user actions to the ViewModel.

Data Flow: User Action (Intent) → ViewModel → State → View

Design Principal: Presentation -> Domain <- Data

🔄 **How MVI Works Here**

User Intent: A user taps a button or interacts with the UI.

ViewModel Processing: The intent triggers an action in the ViewModel.

State Update: The ViewModel updates the state based on the action.

View Rendering: The View observes the state and re-renders accordingly.

🏁 **Getting Started**
Prerequisites:
Android Studio (latest version)
Minimum SDK 24
Installation
Clone the repository:
git clone https://github.com/amiteshsinghk/CryptoCoinTracker.git
Open the project in Android Studio.
Sync Gradle and run the project.
