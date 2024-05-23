# Tinder AI Chatbot

Hello! This bot combines the convenience of Telegram, the power of ChatGPT, and the fun of Tinder.

## Features

The chatbot can:

1. Generate a Tinder profile based on your description.
2. Write interesting and intriguing messages for dating.
3. Conduct correspondence on your behalf.
4. Allow you to practice chatting with a chatbot.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) installed.
- A Telegram bot token.
- An OpenAI API key.

### Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/EZDAMIR/TinderBot
    cd TinderBot
    ```

2. Create a `config.properties` file in the project root directory with the following content:

    ```properties
    TELEGRAM_BOT_NAME=Your_Telegram_Bot_Name
    TELEGRAM_BOT_TOKEN=Your_Telegram_Bot_Token
    OPEN_AI_TOKEN=Your_OpenAI_Token
    ```

3. Make sure to add `config.properties` to your `.gitignore` file to avoid committing it to version control:

    ```sh
    echo "config.properties" >> .gitignore
    ```

### Usage

1. Build the project:

2. Run the bot:

    ```sh
    java -cp bin com.javarush.telegram.TinderBoltApp
    ```

3. Start interacting with the bot on Telegram.

## Configuration

The bot is configured using a `config.properties` file. Make sure you have the following keys set in your `config.properties` file:

```properties
TELEGRAM_BOT_NAME=Your_Telegram_Bot_Name
TELEGRAM_BOT_TOKEN=Your_Telegram_Bot_Token
OPEN_AI_TOKEN=Your_OpenAI_Token
```

## Contributing
- Fork the repository.
- Create a new branch (git checkout -b feature-branch).
- Make your changes.
- Commit your changes (git commit -m 'Add some feature').
- Push to the branch (git push origin feature-branch).
- Open a pull request.
## License
This project is licensed under the MIT License. See the LICENSE file for details.

## Acknowledgments
Telegram for the Telegram Bot API.
OpenAI for the ChatGPT API.
